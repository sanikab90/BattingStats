/*
 * Sanika Buche
 * 
 * A program that takes a file of names and characters and outputs a file
 * of all the names baseball stats including the leaders in each category
 */

import java.util.Scanner;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
public class Main {

	public static void main(String[] args) throws IOException {
		
		//variables
		String[] name = new String [30];
		int[] hits = new int [30];
		int[] out = new int[30];
		int[] strike = new int[30];
		int[] walk = new int [30];
		int[] hbp = new int[30];
		int[] sacFice = new int[30];
		double[] battingAvg = new double[30];
		double[] onBasePer = new double[30];
		
		
		Scanner read = new Scanner(System.in); //create Scanner obj
		String fileName = read.nextLine(); //read user input
		
		//user file
		File inFile = new File(fileName);
		File outFile = new File("leaders.txt");
		PrintWriter write = new PrintWriter(outFile);
		
		//count total number of players
		int totalPlayers = 0;
		
		//scanner object to read inFile
		Scanner in = new Scanner(inFile);
		
		//loop through file and parse variables
		if(inFile.length() > 0) {
			while(in.hasNext()) {
			
				//get character name and save it to the array
				name[totalPlayers] = in.next();
				
				//save character line to a string
				String stats = in.next();
				
				//call parsing function
				parse(stats, hits, out, strike, walk, hbp, sacFice, totalPlayers);
				
				//calculate batting average
				double divByZ = (double)(hits[totalPlayers] + out[totalPlayers] + strike[totalPlayers]);
				
				if(divByZ > 0)
					battingAvg[totalPlayers] = (double)hits[totalPlayers] / divByZ;
				else {
					battingAvg[totalPlayers] = 0;
				}	
				
				//calculate on base percentage
				onBasePer[totalPlayers] = (double)(hits[totalPlayers] + walk[totalPlayers] + hbp[totalPlayers]) / (double)(hits[totalPlayers] + walk[totalPlayers] + hbp[totalPlayers] + out[totalPlayers] + strike[totalPlayers] + sacFice[totalPlayers]);
				
				//increment players
				totalPlayers++;
			}
		}
		
		//once all input is parsed close file
		in.close();
		
		//call display methods and loop for however many times there are players
		for(int i = 0; i < totalPlayers; i++) {
			displayStats(write, i, name, hits, out, strike, walk, hbp, sacFice, battingAvg, onBasePer);
		}
		
		//display the leaders
		displayLeaders(write, totalPlayers, name, hits, out, strike, walk, hbp, sacFice, battingAvg, onBasePer);
		read.close();
		write.close();
	}
	
	/*
	 * A method that takes the string of variables and parses it out to hits, out, strike, walk, hbp, sacrifice, playercount
	 * by using a switch statement 
	 */
	public static void parse(String stats, int[] hits, int[] out, int[] strike, int[] walk, int[] hbp, int[]sacFice, int playerCount) {
		int hitCount = 0;
		int outCount = 0;
		int strikeCount = 0;
		int walkCount = 0;
		int hbpCount = 0;
		int sacrifice = 0;
		
		//loop until string is finished
		for(int i = 0; i < stats.length(); i++) {
			switch(stats.charAt(i)) {
			case 'H':
				hitCount++;
				break;
			case 'O':
				outCount++;
				break;
			case 'K':
				strikeCount++;
				break;
			case 'W':
				walkCount++;
				break;
			case 'P':
				hbpCount++;
				break;
			case 'S':
				sacrifice++;
				break;
			default:
					break;
			}
		}
		
		//update arrays
		hits[playerCount] = hitCount;
		out[playerCount] = outCount;
		strike[playerCount] = strikeCount;
		walk[playerCount] = walkCount;
		hbp[playerCount] = hbpCount;
		sacFice[playerCount] = sacrifice;
	}
	
	/*
	 * A method that finds the leaders in each category and returns the index it is in the array
	 */
	public static ArrayList<Integer> findLeaderIndex(int arr[], int playerTotal, boolean strike){
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		//set highest value to first element
		int highestVal = arr[0];
		index.add(0);
		
		if(!strike) {
			for(int i = 1; i < playerTotal; i++) {
				if(arr[i] > highestVal) {
					index.clear();
					index.add(i);
					highestVal = arr[i];
				}
				else if(arr[i] < highestVal) {
					continue;
				}
				else if(arr[i] == highestVal) {
					index.add(i);
				}
			}
		}
			else {
				for(int i = 1; i < playerTotal; i++) {
					if(arr[i] < highestVal) {
						index.clear();
						index.add(i);
						highestVal = arr[i];
					}
					else if(arr[i] > highestVal) {
						continue;
					}
					else if(arr[i] == highestVal) {
						index.add(i);
					}
			}
		}
		 
		return index;
	}
	
	/*
	 * A method that finds the leaders in the onbasePercentage and the batting average and returns the index it is in the array
	 */
	public static ArrayList<Integer> findLeaderIndex2(double arr[], int playerTotal){
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		//set highest value to first element
		double highestVal = arr[0];
		index.add(0);
		
		for(int i = 1; i < playerTotal; i++) {
			if(arr[i] > highestVal) {
				index.clear();
				index.add(i);
				highestVal = arr[i];
			}
			else if(arr[i] < highestVal) {
				continue;
			}
			else if(arr[i] == highestVal) {
				index.add(i);
			}
		}
		 
		return index;
	}
	
	/*
	 * A method that displays the stats of the player 
	 */
	public static void displayStats(PrintWriter pw, int i, String[] name, int[] hits, int[] out, int[] strike, int[] walk, int[] hbp, int[] sacFice, double[] batAvg, double[] obp) {
	
		//format the file to 3 decimal places rounded up
		DecimalFormat df = new DecimalFormat("#0.000");
		df.setRoundingMode(RoundingMode.HALF_UP);
	
		//write print statement
		pw.println(name[i]);
		pw.println("BA: " + df.format(batAvg[i]));
		pw.println("OB%: " + df.format(obp[i]));
		pw.println("H: " + hits[i]);
		pw.println("BB: " + walk[i]);
		pw.println("K: " + strike[i]);
		pw.println("HBP: " + hbp[i]);
		pw.print("\n");
	}
	
	/*
	 * A method that displays the leaders from each category and 
	 * if a category has multiple leaders it displays that with a comma and space
	 * as well
	 */
	public static void displayLeaders(PrintWriter pw, int i, String[] name, int[] hits, int[] out, int[] strike, int[] walk, int[] hbp, int[] sacFice, double[] batAvg, double[] obp) {
		//format the file to 3 decimal places rounded up
		DecimalFormat df = new DecimalFormat("#0.000");
		df.setRoundingMode(RoundingMode.HALF_UP);

		pw.println("LEAGUE LEADERS");
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		//display batAvg
		index = findLeaderIndex2(batAvg, i);
		pw.print("BA: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + df.format(batAvg[index.get(0)]));
		
		//display OB%
		index = findLeaderIndex2(obp, i);
		pw.print("OB%: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + df.format(obp[index.get(0)]));
		
		//display hits
		index = findLeaderIndex(hits, i, false);
		pw.print("H: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + hits[index.get(0)]);
		
		//display walks
		index = findLeaderIndex(walk, i, false);
		pw.print("BB: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + walk[index.get(0)]);
		
		//display strikes
		index = findLeaderIndex(strike, i, true);
		pw.print("K: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + strike[index.get(0)]);
		
		//display hbp
		index = findLeaderIndex(hbp, i, false);
		pw.print("HBP: ");
		//if there are multiple leaders 
		for(int j = 0; j < index.size(); j++) {
			if(j > 0)
				pw.print(", ");
			pw.print(name[index.get(j)]);
		}
		pw.println(" " + hbp[index.get(0)]);
	}
	
	
	
	
}

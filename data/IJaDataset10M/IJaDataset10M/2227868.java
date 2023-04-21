package org.myjerry.maer.page2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Problem 1 on Project Euler, http://projecteuler.net/index.php?section=problems&id=1
 *
 * @author Sandeep Gupta
 * @since Jan 8, 2011
 */
public class Problem67 {

    private static final int[][] matrix = new int[100][];

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String path = "c:/triangle.txt";
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int lineCount = 0;
        while ((line = reader.readLine()) != null) {
            String tokens[] = line.split(" ");
            if (tokens != null) {
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i];
                    int num = Integer.parseInt(token);
                    row[i] = num;
                }
                matrix[lineCount++] = row;
            }
        }
        for (int row = matrix.length - 1; row > 0; row--) {
            for (int column = 0; column < matrix[row].length - 1; column++) {
                int max = Math.max(matrix[row][column], matrix[row][column + 1]);
                matrix[row - 1][column] += max;
            }
        }
        System.out.println("Max sum = " + matrix[0][0]);
    }
}

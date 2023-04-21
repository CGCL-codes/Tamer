package edu.centenary.centenaryController;

import java.io.*;

/**
 * This class holds static methods to talk to the user.  All exception
 * handling is encapsulated in these methods.  Also, this class
 * has an encapsulated sleep() method which will interrupt a thread
 * properly.
 */
class Utilities {

    public static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    public static int getInteger(String prompt) {
        int number = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                number = Integer.parseInt(stdin.readLine());
                valid = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid Input.  Try again.");
            } catch (IOException ioe) {
                System.out.println("Error in Input.  Terminating.");
                System.exit(0);
            }
        }
        return number;
    }

    public static double getDouble(String prompt) {
        double number = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                number = Double.parseDouble(stdin.readLine());
                valid = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid Input.  Try again.");
            } catch (IOException ioe) {
                System.out.println("Error in Input.  Terminating.");
                System.exit(0);
            }
        }
        return number;
    }

    public static String getString(String prompt) {
        String s = "";
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                s = stdin.readLine();
                valid = true;
            } catch (IOException ioe) {
                System.out.println("Error in Input.  Terminating.");
                System.exit(0);
            }
        }
        return s;
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

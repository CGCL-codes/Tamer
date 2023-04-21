package IOSTREAM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleReader {

    public static String Read() {
        InputStreamReader potok = new InputStreamReader(System.in);
        BufferedReader klaviatura = new BufferedReader(potok);
        try {
            return klaviatura.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static double ReadDouble() {
        Scanner myScanner = new Scanner(System.in);
        return myScanner.nextDouble();
    }
}

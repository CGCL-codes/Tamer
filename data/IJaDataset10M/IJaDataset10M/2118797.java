package edu.upmc.opi.caBIG.caTIES.server.dispatcher.temporal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CaTIES_HashingTester {

    private BufferedReader br = null;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter uuid: ");
            String uuid = readLine(br);
            if (uuid == null || uuid.length() == 0) {
                break;
            }
            System.out.print("Enter collection date and time: ");
            String collectionDateTime = readLine(br);
            System.out.println(new String(uuid + collectionDateTime).hashCode());
        }
        System.out.println("All finished.");
    }

    private static String readLine(BufferedReader br) {
        String result = null;
        try {
            result = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read your name!");
            System.exit(1);
        }
        return result;
    }
}

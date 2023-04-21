package edu.indiana.cs.webmining.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * @author Michel Salim <msalim@cs.indiana.edu>
 * @since Feb 10, 2007
 */
public class DBInitializer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BufferedReader reader;
        if (args.length < 1) {
            System.err.println("Usage: java DBInitializer filename");
            return;
        }
        try {
            reader = new BufferedReader(new FileReader(args[0]));
            try {
                DBManager dbman = new DBManager();
                int count = 0;
                String[] tokens;
                String line = reader.readLine();
                while (line != null) {
                    ++count;
                    if ((count % 1000) == 0) {
                        System.out.println("Added " + count + " links");
                    }
                    tokens = line.split("[\\s,]+");
                    try {
                        dbman.addLink(tokens[1], tokens[0]);
                    } catch (MalformedURLException e) {
                        System.err.println("Malformed URL: " + e.getMessage());
                    }
                    line = reader.readLine();
                }
                reader.readLine();
                dbman.closeConnection();
            } catch (SQLException e) {
                System.err.println("Failed to open database connection");
                e.printStackTrace();
                return;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return;
        } catch (IOException e) {
            System.err.println("Cannot perform IO operation");
            return;
        }
        System.out.println("Database populated");
    }
}

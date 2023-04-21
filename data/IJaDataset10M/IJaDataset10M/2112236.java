package org.icehockeymanager.ihm.clients.matchtest;

/**
 * Main class for the data collector client.
 * 
 * @author Bernhard von Gunten
 * @created September, 2006
 */
public class MatchTest {

    /**
   * Construct the application.
   * 
   * @param args The command line arguments
   */
    public MatchTest(String[] args) {
        org.icehockeymanager.ihm.clients.matchtest.controller.ClientController.getInstance().init(args);
    }

    /**
   * Main method.
   * 
   * @param args The command line arguments
   */
    public static void main(String[] args) {
        new MatchTest(args);
    }
}

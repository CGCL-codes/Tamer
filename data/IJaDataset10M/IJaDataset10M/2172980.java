package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import models.Deck;

public class FixedDeckGenerator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Improper usage : call with 2 paramteres : filename to store deck in and number of decks to generate");
            System.exit(-1);
        }
        List<Deck> decks = new LinkedList<Deck>();
        String file = args[0];
        try {
            FileWriter fileStream = new FileWriter(file);
            BufferedWriter fileOutput = new BufferedWriter(fileStream);
            int n = Integer.parseInt(args[1]);
            for (int i = 0; i < n; i++) {
                Deck deck = new Deck();
                String deckString = deck.toString();
                fileOutput.write(deckString + "\n");
            }
            fileOutput.close();
        } catch (Exception e) {
            System.out.println("Error occured in generating file : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

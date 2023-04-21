package edu.umd.cs.skolli.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Utility to Reduce the size of CSV file by randomly removing lines
 * 
 * @author Nathaniel Ayewah
 *
 */
public class CSVThinner {

    String filenameIn = "data/rawMySQLResultsFull.csv";

    String filenameOut = "data/rawMySQLResults.csv";

    /** the percentage of lines to keep [0,1] */
    double percent = 100.0 / 18490.0;

    public void setParms(String[] args) {
        if (args.length > 0) filenameIn = args[0];
        if (args.length > 1) filenameOut = args[1];
        if (args.length > 2) percent = Double.parseDouble(args[2]);
    }

    public void execute() throws IOException {
        CSVReader in = new CSVReader(new FileReader(filenameIn));
        CSVWriter out = new CSVWriter(new FileWriter(filenameOut), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        String[] line;
        if ((line = in.readNext()) == null) {
            return;
        }
        out.writeNext(line);
        while ((line = in.readNext()) != null) {
            if (Math.random() < percent) out.writeNext(line);
        }
        in.close();
        out.close();
    }

    /**
	 * @param args [filenameIn, filenameOut, percent]
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        CSVThinner thinner = new CSVThinner();
        thinner.setParms(args);
        thinner.execute();
    }
}

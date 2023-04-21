package de.karlsruhe.rz.unicore.plugins.fluent;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *  Parser for Fluent case files. Currently reads out only the dimensions field.
 *  Also allows zipped and gzipped input files.
 *
 *@author     Ralf Ratering
 *@created    6. November 2001
 *@version    $Id: CaseFileParser.java,v 1.1.1.1 2005/11/08 13:00:34 bschuller Exp $
 */
public class CaseFileParser {

    private String parseString;

    private int dimension;

    /**
	 *  Gets the Dimension attribute of the CaseFileParser object
	 *
	 *@return    The Dimension value
	 */
    public int getDimension() {
        return dimension;
    }

    /**
	 *  Parse an ASCII case file for the dimensions. Assumes that key word is "Dimension" and that
	 *  dimensions follow in the next line in the form "(index dimensions)"
	 *
	 *@param  filename                   name of case file
	 *@exception  FileNotFoundException  could not find case file
	 *@exception  IOException            could not load case file
	 */
    public void parseFile(String filename) throws FileNotFoundException, IOException {
        InputStream inputStream = null;
        if (filename.endsWith(".zip")) {
            ZipFile file = new ZipFile(filename);
            Enumeration entries = file.entries();
            inputStream = file.getInputStream((ZipEntry) entries.nextElement());
        } else if (filename.endsWith(".gz")) {
            File file = new File(filename);
            FileInputStream tmpStream = new FileInputStream(file);
            inputStream = new GZIPInputStream(tmpStream);
        } else {
            File file = new File(filename);
            inputStream = new FileInputStream(file);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.indexOf("Dimension") > 0) {
                parseString = reader.readLine();
                String token = getNumber();
                token = getNumber();
                try {
                    dimension = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    dimension = 0;
                }
                break;
            }
        }
        reader.close();
    }

    /**
	 *  Cuts the first number from the parseString field
	 *
	 *@return    A string containing the number
	 */
    private String getNumber() {
        while (!startsWithNumber(parseString)) {
            parseString = parseString.substring(1);
        }
        String result = "";
        while (startsWithNumber(parseString)) {
            result += parseString.substring(0, 1);
            parseString = parseString.substring(1);
        }
        return result;
    }

    /**
	 *  Check if a String starts with a number
	 *
	 *@param  string  to be tested
	 *@return         true if first char is a number
	 */
    private boolean startsWithNumber(String string) {
        return string.startsWith("0") || string.startsWith("1") || string.startsWith("2") || string.startsWith("3") || string.startsWith("4") || string.startsWith("5") || string.startsWith("6") || string.startsWith("7") || string.startsWith("8") || string.startsWith("9");
    }

    /**
	 *  The main program for the CaseFileParser class
	 *
	 *@param  args  The command line arguments
	 */
    public static void main(String[] args) {
        CaseFileParser caseFileParser = new CaseFileParser();
        try {
            caseFileParser.parseFile("c:/tmp/cavity.cas.gz");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Dimensions: " + caseFileParser.getDimension());
    }
}

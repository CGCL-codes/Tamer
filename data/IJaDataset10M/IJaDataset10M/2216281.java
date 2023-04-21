package org.csvtools.parsers;

import java.util.*;
import java.io.*;
import org.csvtools.base.CsvParser;

/**
 * CSVFileParser is a class derived from CSVFile used to parse an existing CSV file.
 * <p>
 * Adapted from a C++ original that is Copyright (C) 1999 Lucent Technologies.<br>
 * Excerpted from 'The Practice of Programming' by Brian Kernighan and Rob Pike.
 * <p>
 * Included by permission of the <a href="http://tpop.awl.com/">Addison-Wesley</a> web site, which says:
 * <cite>"You may use this code for any purpose, as long as you leave the copyright notice and book citation attached"</cite>.
 *
 * @author  Brian Kernighan and Rob Pike (C++ original)
 * @author  Ian F. Darwin (translation into Java and removal of I/O)
 * @author  Ben Ballard (rewrote handleQuotedField to handle double quotes and for readability)
 * @author  Fabrizio Fazzino (added integration with CSVFile, handling of variable textQualifier and Vector with explicit String type)
 * @version %I%, %G%
 */
public class CsvFileParser extends CsvParser {

    /**
	 * The buffered reader linked to the CSV file to be read.
	 */
    protected BufferedReader in;

    /**
	 * CSVFileReader constructor just need the name of the existing CSV file that will be read.
	 *
	 * @param  inputFileName         The name of the CSV file to be opened for reading
	 * @throws FileNotFoundException If the file to be read does not exist
	 */
    public CsvFileParser(String inputFileName) throws FileNotFoundException {
        super();
        in = new BufferedReader(new FileReader(inputFileName));
    }

    /**
	 * CSVFileReader constructor with a given field separator.
	 *
	 * @param  inputFileName          The name of the CSV file to be opened for reading
	 * @param  sep                    The field separator to be used; overwrites the default one
	 * @throws FileNotFoundException  If the file to be read does not exist
	 */
    public CsvFileParser(String inputFileName, char sep) throws FileNotFoundException {
        super(sep);
        in = new BufferedReader(new FileReader(inputFileName));
    }

    /**
	 * CSVFileReader constructor with given field separator and text qualifier.
	 *
	 * @param  inputFileName          The name of the CSV file to be opened for reading
	 * @param  sep                    The field separator to be used; overwrites the default one
	 * @param  qual                   The text qualifier to be used; overwrites the default one
	 * @throws FileNotFoundException  If the file to be read does not exist
	 */
    public CsvFileParser(String inputFileName, char sep, char qual) throws FileNotFoundException {
        super(sep, qual);
        in = new BufferedReader(new FileReader(inputFileName));
    }

    /**
	 * Split the next line of the input CSV file into fields.
	 * <p>
	 * This is currently the most important function of the package.
	 *
	 * @return             Vector of strings containing each field from the next line of the file
	 * @throws IOException If an error occurs while reading the new line from the file
	 */
    public Vector<String> readVectorLine() throws IOException {
        Vector<String> fields = new Vector<String>();
        String line = in.readLine();
        super.parseLine(line, fields);
        return fields;
    }

    /**
	 * Split the next line of the input CSV file into fields.
	 * <p>
	 * This is currently the most important function of the package.
	 *
	 * @return             ArrayList of strings containing each field from the next line of the file
	 * @throws IOException If an error occurs while reading the new line from the file
	 */
    public ArrayList<String> readArrayListLine() throws IOException {
        ArrayList<String> fields = new ArrayList<String>();
        String line = in.readLine();
        super.parseLine(line, fields);
        return fields;
    }

    /**
	 * Close the input CSV file.
	 *
	 * @throws IOException If an error occurs while closing the file
	 */
    public void close() throws IOException {
        in.close();
    }
}

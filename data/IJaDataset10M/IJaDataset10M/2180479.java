package net.sourceforge.javacavemaps.components.parsers.flatfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;
import net.sourceforge.javacavemaps.components.parsers.FixedWidthParsing;
import net.sourceforge.javacavemaps.core.model.FlatFile;

/**
 * Parses a fixed width flat text data file.
 * 
 * @author Larry Gray
 * 
 */
public class FixedWidthParser extends AbstractFixedWidthParser {

    /** A flat file model. */
    private FlatFile aFlatFileModel;

    /**
	 * @return the aFlatFileModel
	 */
    public FlatFile getFlatFileModel() {
        return aFlatFileModel;
    }

    /** A scanner. */
    private Scanner theScanner;

    /** A line of the file. */
    private String aLine;

    /** A file to be parsed. */
    private File aFile;

    /** The data pasted into probably a text area which will be parsed. */
    private String pasteData;

    /** A file read in as a resource from within jared applets or webstart apps. */
    private InputStream resource;

    /** Can't create a parser without giving it something to parse. */
    private FixedWidthParser() {
    }

    private void scan(Scanner aScanner) {
        LinkedList fields = new LinkedList();
        String fieldString = null;
        String line = null;
        int last = 0;
        while (aScanner.hasNext()) {
            line = aScanner.next();
            for (int a = 0; a < widths.length; a++) {
                fieldString = "" + line.substring(last, widths[a]);
                fields.add(fieldString);
                last = widths[a];
            }
            fieldString = "" + line.substring(last, line.length());
            last = 0;
            fields.add(fieldString);
        }
        String[] stringFields = FlatFile.toStringArray(fields.toArray());
        aFlatFileModel = new FlatFile(stringFields);
    }

    /**
	 * Parses a fixed width data text file.
	 * @param aFile
	 */
    public FixedWidthParser(File aFile, int[] widths) {
        this.widths = widths;
        try {
            this.aFile = aFile;
            theScanner = new Scanner(aFile).useDelimiter(END_LINE_DELIMITER);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        scan(theScanner);
    }

    /**
     * Parses a fixed width data text file as a resource form within an applet or webstart jar.
     * @param resource
     */
    public FixedWidthParser(InputStream resource, int[] widths) {
        this.widths = widths;
        theScanner = new Scanner(resource).useDelimiter(END_LINE_DELIMITER);
        scan(theScanner);
    }

    /**
     * 
     * Parses a fixed width data text file pasted as a string into a Java GUI.
     * @param pasteString
     */
    public FixedWidthParser(String pasteString, int[] widths) {
        this.widths = widths;
        theScanner = new Scanner(pasteString).useDelimiter(END_LINE_DELIMITER);
        scan(theScanner);
    }

    /** 
     * Return the string describing the object state  
     */
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        final String NEW_LINE = System.getProperty("line.separator");
        strBuilder.append(super.toString());
        strBuilder.append("aFile:" + (this.aFile == null ? "null" : this.aFile.toString()) + NEW_LINE);
        strBuilder.append("aFlatFileModel:" + (this.aFlatFileModel == null ? "null" : this.aFlatFileModel.toString()) + NEW_LINE);
        strBuilder.append("aLine:" + aLine + NEW_LINE);
        strBuilder.append("pasteData:" + pasteData + NEW_LINE);
        strBuilder.append("resource:" + (this.resource == null ? "null" : this.resource.toString()) + NEW_LINE);
        strBuilder.append("theScanner:" + (this.theScanner == null ? "null" : this.theScanner.toString()) + NEW_LINE);
        return strBuilder.toString();
    }
}

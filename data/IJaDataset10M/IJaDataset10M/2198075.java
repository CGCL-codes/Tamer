package dti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import common.Constants;
import common.InvalidOptionException;

/**
 * Options.java
 * 
 * This classes parses the command line arguments passed to the program and
 * sets up run time variables appropriately. In particular, Options sets up
 * custom break characters, input files, and the stop word file.
 * 
 * Modified and reused from Project 2 Team 1 (WMI) code
 * @author Andrew Bernard, Zachary M. Allen
 */
public class Options {

    /** Input file object vector */
    private List filesToIndex = null;

    /** Stop word file object */
    private File stopWordFile = null;

    /** Break characters array */
    private char[] breakChars = " :/.,+*()~&%$#!`\"{}|^[]\\@?<>=;".toCharArray();

    ;

    /** True if stop words are set, false otherwise */
    private boolean stopWordsSet = false;

    /** True if break characters are set, false otherwise */
    private boolean breakCharsSet = false;

    /**
   * Constructor
   * @param    args    Command line arguments to be processed
   */
    public Options(String[] args) throws FileNotFoundException, InvalidOptionException, IOException {
        parseOptions(args);
    }

    /**
   * This method checks all the arguments passed into the program. It assumes the last
   * argument is a file which contains a list of all the input files that will be indexed.
   * 
   * @param args list of arguments passed into the program
   * @throws FileNotFoundException throws this exception if any referenced file cannot be found
   * @throws InvalidOptionException thrown if an option is invalid
   */
    private void parseOptions(String[] args) throws FileNotFoundException, InvalidOptionException, IOException {
        if (args.length > 0 && args.length % 2 != 0) {
            int fileIndex = args.length - 1;
            for (int i = 0; i < fileIndex - 1; i += 2) {
                setOption(args[i], args[i + 1]);
            }
            File file = new File(args[fileIndex]);
            validateFile(file);
            processFilesToIndex(file);
        } else throw new IOException(Constants.ERR_SYNTAX);
    }

    private void processFilesToIndex(File file) throws IOException {
        filesToIndex = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            File fileToIndex = new File(line);
            validateFile(fileToIndex);
            filesToIndex.add(fileToIndex);
        }
    }

    /**
   * Sets an option to a particular value
   * @param    option    Option to set
   * @param    setting    Value to set it to
   */
    private void setOption(String option, String setting) throws FileNotFoundException, InvalidOptionException {
        if (option.equals("-b") && !breakCharsSet) {
            breakChars = setting.toCharArray();
            breakCharsSet = true;
        } else if (option.equals("-s") && !stopWordsSet) {
            stopWordFile = new File(setting);
            validateFile(stopWordFile);
            stopWordsSet = true;
        } else {
            throw new InvalidOptionException(option);
        }
    }

    /**
   * Checks if <code>file</code> exists and is readable; an error will be
   * thrown if either status is false.
   * 
   * @param file the file to validate
   */
    private void validateFile(File file) throws FileNotFoundException {
        if (!file.exists()) throw new FileNotFoundException(file.getAbsolutePath() + " does not exist."); else if (!file.canRead()) throw new FileNotFoundException(file.getAbsolutePath() + " could not be read.");
    }

    public char[] getBreakChars() {
        return breakChars;
    }

    public List getFilesToIndex() {
        return filesToIndex;
    }

    public File getStopWordFile() {
        return stopWordFile;
    }
}

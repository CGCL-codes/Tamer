package model;

import java.io.*;
import controller.enums.ListFilesEnum;

public class Parser {

    static String languagesListStartLine1 = "LANGUAGE LIST";

    static String languagesListStartLine2 = "=============";

    static String genresListStartLine1 = "8: THE GENRES LIST";

    static String genresListStartLine2 = "==================";

    static String countriesListStartLine1 = "COUNTRIES LIST";

    static String countriesListStartLine2 = "==============";

    static String moviesListStartLine1 = "MOVIES LIST";

    static String moviesListStartLine2 = "===========";

    static String persons1ListStartLine1 = "Name			Titles ";

    static String persons1ListStartLine2 = "----			------";

    static String persons2ListStartLine1 = "Name			Titles";

    static String persons2ListStartLine2 = "----			------";

    static String persons3ListStartLine1 = "Name                    Titles ";

    static String persons3ListStartLine2 = "----                    ------";

    static String persons1ListEndLine = "SUBMITTING UPDATES";

    static String persons2ListEndLine = "Updates can most conveniently be submitted via the movie mail server; send";

    private BufferedReader listFile;

    private ListFilesEnum listType;

    private String filename;

    private String listStartLine1;

    private String listStartLine2;

    private boolean hasEndLine;

    private String listEndLine;

    private int currentLine;

    private boolean isEOF;

    public Parser() {
    }

    /**
	 * Opens the file and resets the line counter
	 * @param filename the file to be loaded
	 * @return boolean if the loading succeeded
	 */
    public boolean loadFile(String filename, ListFilesEnum listType) {
        try {
            FileInputStream fstream = new FileInputStream(filename);
            InputStreamReader insr = new InputStreamReader(fstream);
            listFile = new BufferedReader(insr);
            this.listType = listType;
            this.filename = filename;
            currentLine = 1;
            isEOF = false;
            listEndLine = "";
            switch(listType) {
                case LANGUAGES:
                    listStartLine1 = languagesListStartLine1;
                    listStartLine2 = languagesListStartLine2;
                    hasEndLine = false;
                    break;
                case GENRES:
                    listStartLine1 = genresListStartLine1;
                    listStartLine2 = genresListStartLine2;
                    hasEndLine = false;
                    break;
                case COUNTRIES:
                    listStartLine1 = countriesListStartLine1;
                    listStartLine2 = countriesListStartLine2;
                    hasEndLine = false;
                    break;
                case MOVIES:
                    listStartLine1 = moviesListStartLine1;
                    listStartLine2 = moviesListStartLine2;
                    hasEndLine = false;
                    break;
                case ACTORS:
                case ACTRESSES:
                    listStartLine1 = persons1ListStartLine1;
                    listStartLine2 = persons1ListStartLine2;
                    hasEndLine = true;
                    listEndLine = persons1ListEndLine;
                    break;
                case DIRECTORS:
                    listStartLine1 = persons2ListStartLine1;
                    listStartLine2 = persons2ListStartLine2;
                    hasEndLine = true;
                    listEndLine = persons1ListEndLine;
                    break;
                case PRODUCERS:
                    listStartLine1 = persons3ListStartLine1;
                    listStartLine2 = persons3ListStartLine2;
                    hasEndLine = true;
                    listEndLine = persons1ListEndLine;
                    break;
                case WRITERS:
                    listStartLine1 = persons2ListStartLine1;
                    listStartLine2 = persons2ListStartLine2;
                    hasEndLine = true;
                    listEndLine = persons2ListEndLine;
                    break;
            }
        } catch (Exception e) {
            System.err.println("File input error");
            return false;
        }
        return true;
    }

    /**
	 * Parser constructor
	 * Calls the loadFile method
	 * @param filename the file to be loaded
	 */
    public Parser(String filename, ListFilesEnum listType) {
        loadFile(filename, listType);
    }

    /**
	 * opens the file again, to start from the top of the list
	 * and then runs to the start of the list in the file (after all the comments in the beginning)
	 * @return boolean if the start of the list was found
	 */
    public boolean findStartOfList() {
        closeFile();
        loadFile(filename, listType);
        if (findLine(listStartLine1) && findLine(listStartLine2)) {
            checkIfEOF();
            return true;
        } else return false;
    }

    /**
	 * reading the next line from the file if possible
	 * @return String the line that was read
	 */
    public String readLine() {
        String line;
        try {
            if (listFile.ready()) {
                ++currentLine;
                line = listFile.readLine();
                if (hasEndLine && line.equals(listEndLine)) {
                    isEOF = true;
                    return "";
                }
                checkIfEOF();
                return line;
            }
        } catch (Exception e) {
            System.err.println("File input error");
        }
        return "";
    }

    private boolean checkIfEOF() {
        try {
            if (!(listFile.ready())) isEOF = true;
        } catch (Exception e) {
            System.err.println("File input error");
        }
        return isEOF;
    }

    /**
	 * Gets the current line
	 * @return int the line number, or -1 if the file isn't open
	 */
    public int getLineNumber() {
        return currentLine;
    }

    /**
	 * Resets the counter of the line numbers
	 * @return boolean whether the method succeeded
	 */
    public boolean resetLineCount() {
        currentLine = 1;
        return true;
    }

    /**
	 * Runs over the file and searches the given string
	 * @param searchString
	 * @return boolean whether the line was found
	 */
    public boolean findLine(String searchString) {
        String line;
        try {
            while (!isEOF()) {
                line = readLine();
                if (line.equals(searchString)) return true;
            }
        } catch (Exception e) {
            System.err.println("File input error");
        }
        return false;
    }

    /**
	 * Runs over the file until reaching the line number
	 * @param lineNumber the line to get to
	 * @return int 1 if the line was found, 0 if the line wasn't found (EOF before the line number), -1 if the line requested is smaller than the current line
	 * 				-2 if there was an error
	 */
    public int findLine(int lineNumber) {
        try {
            if (lineNumber < getLineNumber()) return -1; else if (lineNumber == getLineNumber()) return 1; else {
                while (!isEOF()) {
                    readLine();
                    if (getLineNumber() == lineNumber) return 1;
                }
                return 0;
            }
        } catch (Exception e) {
            System.err.println("File input error");
        }
        return -2;
    }

    /**
	 * Checks whether the EOF was reached
	 * Will also return true if the file wasn't open at all 
	 * @return boolean representing whether the EOF has been reached or the file isn't open
	 */
    public boolean isEOF() {
        return isEOF;
    }

    /**
	 * Closes the file stream
	 */
    public void closeFile() {
        try {
            listFile.close();
            currentLine = -1;
        } catch (Exception e) {
            System.err.println("File input error");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (listFile != null) closeFile();
        super.finalize();
    }
}

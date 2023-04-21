package org.chessworks.chessclub;

import java.util.Vector;

/**
 * Parser util for ICC datagrams. Generally defines the interface between an ICCDatagram and the application that needs to use it.
 **/
public class ICCDatagram {

    public static final char MARK = '\031';

    public static final char OPEN = '(';

    public static final char CLOSE = ')';

    public static final char OPEN_STRING = '{';

    public static final char CLOSE_STRING = '}';

    private int commandNumber;

    private Vector<String> arguments;

    private int numArguments;

    private String comString;

    public ICCDatagram(String s) {
        comString = s;
        commandNumber = Integer.parseInt(comString.substring(2, comString.indexOf(' ')));
        arguments = retrieveArguments(comString);
    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public String getArgument(int i) {
        if (i < numArguments) return (String) arguments.elementAt(i); else return null;
    }

    public String getTextArgument(int i) {
        String arg = getArgument(i);
        String text = getText(arg);
        return text;
    }

    public int getNumArguments() {
        return numArguments;
    }

    private Vector<String> retrieveArguments(String comString) {
        Vector<String> args = new Vector<String>();
        int i;
        i = comString.indexOf(' ') + 1;
        numArguments = 0;
        int blankLoc = i - 1;
        boolean ignoreBlanksInText = false;
        boolean ignoreBlanksInTitles = false;
        while (i < comString.length()) {
            if ((!ignoreBlanksInText) && (comString.charAt(i) == OPEN_STRING)) ignoreBlanksInTitles = true;
            if ((!ignoreBlanksInText) && (comString.charAt(i) == CLOSE_STRING)) ignoreBlanksInTitles = false;
            if (ignoreBlanksInText) {
                if (comString.charAt(i) == MARK) {
                    ignoreBlanksInText = false;
                    i++;
                }
            } else if (!ignoreBlanksInTitles) {
                if ((comString.charAt(i) == ' ') || ((comString.charAt(i) == MARK) && (comString.charAt(i + 1) == CLOSE))) {
                    numArguments++;
                    args.addElement(comString.substring(blankLoc + 1, i));
                    blankLoc = i;
                }
            }
            if (comString.charAt(i) == MARK) ignoreBlanksInText = true;
            i++;
        }
        return args;
    }

    /**
	 * Receives a title in the "{"+<titles>+"}" format and returns it in a "("+<titles>+")" format or am empty String if it receives "{}". ASSUMES
	 * THAT THE ARGUMENT IS A REAL TITLE.
	 */
    public static String getTitle(String title) {
        if (title.length() == 2) return ""; else return "(" + title.substring(1, title.length() - 1) + ")";
    }

    /**
	 * Receives a text in the "^Y("+<text>+"^Y)" format and returns only the text. ASSUMES THAT THE ARGUMENT IS A REAL TEXT (i.e. starts with "^Y("
	 * and ends with "^Y)").
	 */
    public static String getText(String text) {
        return text.substring(2, text.length() - 2);
    }

    /**
	 * Converts a String into an integer. ASSUMES THAT THE ARGUMENT IS A REAL INTEGER.
	 */
    public static int getNumber(String text) {
        return Integer.parseInt(text);
    }

    /**
	 * Converts a String ("0" or "1") into a boolean value ("0" to false). if invalid value given, returns false;
	 */
    public static boolean getBoolean(String text) {
        if (text.equals("1")) return true;
        return false;
    }
}

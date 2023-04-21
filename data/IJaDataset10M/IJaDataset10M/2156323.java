package net.sourceforge.jlatin.constants;

import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * This class holds static methods which are of
 * use to a diverse set of classes
 *
 */
public class utilities {

    /**
     * Creates a popup window with an ok button
     * and displays text with a vertical scrollbar (if needed)
     *
     * there is an "OK" button at the bottom to close the window
     *
     */
    public static void textWindow(String theText) {
        JOptionPane pane = new JOptionPane();
        JTextArea jta = new JTextArea(theText);
        JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JOptionPane.showMessageDialog(new JFrame(), jsp);
    }

    public static void StringToFile(String fileToWrite, String filename) {
        File outFile = new File(filename);
        FileOutputStream outFileStream;
        try {
            outFileStream = new FileOutputStream(outFile);
            PrintWriter outPrintWriter = new PrintWriter(outFileStream);
            System.out.println("Writing to file: " + outFile.getName());
            outPrintWriter.println(fileToWrite);
            outPrintWriter.flush();
        } catch (Exception fnf) {
            System.out.println("File not found");
        }
    }

    public static StringBuffer fileToStringBuffer(String whichFile) {
        StringBuffer output = new StringBuffer();
        String currentLine;
        try {
            BufferedReader br = new BufferedReader(new FileReader(whichFile));
            while ((currentLine = br.readLine()) != null) {
                output.append(currentLine);
            }
        } catch (IOException e) {
            System.out.println("\nSomething bad happened while reading " + whichFile);
            System.out.println("Error: " + e);
        }
        return output;
    }

    public static String readFileAsString(String fileName) {
        String theInputTEMP;
        String theInput = new String();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while ((theInputTEMP = (in.readLine())) != null) {
                theInput += (theInputTEMP);
                theInput += "\n";
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } catch (IOException ioe) {
            System.out.println("IOE!!");
        }
        return theInput;
    }

    public static String replaceSubstring(String oldsentence, String newpart, int start, int end) {
        return (oldsentence.substring(0, start) + newpart + oldsentence.substring(end, oldsentence.length()));
    }

    public static boolean notAVowel(char input) {
        if ((input == 'a') || (input == 'e') || (input == 'i') || (input == 'o') || (input == 'u')) {
            return false;
        }
        return true;
    }

    public static ArrayList stringToArrayList(String theString) {
        int loc = 0;
        int oldLoc = 0;
        ArrayList returnedList = new ArrayList();
        while (theString.indexOf(constants.stringSeparator, oldLoc) > -1) {
            loc = theString.indexOf(constants.stringSeparator, oldLoc);
            returnedList.add(theString.substring(oldLoc, loc));
            oldLoc = loc + constants.stringSeparator.length();
        }
        returnedList.add(theString.substring(oldLoc, theString.length()));
        return returnedList;
    }

    public static String arrayListToString(ArrayList theArrayList) {
        String returnedString = new String();
        for (int i = 0; i < theArrayList.size(); i++) {
            returnedString += (String) theArrayList.get(i);
            returnedString += constants.stringSeparator;
        }
        if (returnedString.endsWith(constants.stringSeparator)) {
            returnedString = returnedString.substring(0, returnedString.length() - 1);
        }
        return returnedString;
    }
}

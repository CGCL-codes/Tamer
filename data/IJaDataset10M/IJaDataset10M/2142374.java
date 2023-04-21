package ise.library.ascii;

import java.io.*;
import java.text.*;
import java.util.*;
import ise.library.Log;

/**
 * An ascii text message box.
 *
 * @version   $Revision: 10 $
 */
public class MessageBox {

    private static String LS = System.getProperty("line.separator");

    private static int MAX_WIDTH = 60;

    public static void setMaxWidth(int width) {
        MAX_WIDTH = width;
    }

    public static int getMaxWidth() {
        return MAX_WIDTH;
    }

    /**
     * Create a box with the given text.
     *
     * @param text the message
     * @return a nicely formatted message box
     */
    public static String box(CharSequence text) {
        return box(null, text);
    }

    /**
     * Create a box with the given text and title.
     *
     * @param title title to be placed above the message box
     * @param text the message
     * @return a nicely formatted message box
     */
    public static String box(CharSequence title, CharSequence text) {
        if (title == null && text == null) return "";
        if (title != null && text == null) {
            text = title;
            title = null;
        }
        List title_lines = getLines(title);
        List text_lines = getLines(text);
        int width = Math.max(getWidth(title_lines), getWidth(text_lines));
        StringBuffer sb = new StringBuffer();
        sb.append(LS);
        if (title_lines != null) sb.append(boxTitle(title_lines, width));
        sb.append(boxText(text_lines, width));
        return sb.toString();
    }

    /**
     * Gets the width of the widest line in the given list
     *
     * @param text
     * @return      The width value
     */
    private static int getWidth(List lines) {
        if (lines == null) return 0;
        int width = 0;
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            width = Math.max(width, length((String) it.next()));
        }
        return width;
    }

    /**
     * Breaks a single long line into several shorter lines of no more than
     * MAX_WIDTH characters. 
     *
     * @param line the line to wrap
     * @return      a list of lines, each of which will be no more than 
     * MAX_WIDTH characters long.
     */
    private static List wrapLine(String line) {
        List list = new ArrayList();
        if (line == null || line.length() == 0) {
            return list;
        }
        if (length(line) <= MAX_WIDTH) {
            list.add(line);
            return list;
        }
        StringBuffer first_piece = new StringBuffer();
        BreakIterator words = BreakIterator.getWordInstance();
        words.setText(line);
        int start = words.first();
        int first = start;
        int end = start;
        for (end = words.next(); end != BreakIterator.DONE; start = end, end = words.next()) {
            String word = line.substring(start, end);
            if (length(first_piece) + length(word) > MAX_WIDTH) break; else first_piece.append(word);
        }
        if (length(first_piece) == 0) {
            list.add(line);
            return list;
        }
        String rest = line.substring(first_piece.length());
        if (first_piece.length() > 1 && first_piece.charAt(0) == ' ' && first_piece.charAt(1) != ' ') {
            first_piece.deleteCharAt(0);
        }
        if (first_piece.length() > 0) {
            list.add(first_piece.toString());
        }
        if (end != BreakIterator.DONE && rest.length() > 0) {
            list.addAll(wrapLine(rest));
        }
        return list;
    }

    /**
     * Reads the given text and separates each line into an item in the returned
     * list. Lines longer than MAX_WIDTH will be broken into shorter lines.
     *
     * @param text
     * @return      The lines value
     */
    private static List getLines(CharSequence text) {
        if (text == null) return null;
        ArrayList lines = new ArrayList();
        BufferedReader br = new BufferedReader(new StringReader(text.toString()));
        String line = null;
        try {
            while (true) {
                line = br.readLine();
                if (line == null) break;
                line = line.replaceAll("[\t]", "   ");
                lines.addAll(wrapLine(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Get the top of the box.  Title lines will automatically be colorized.
     *
     * @param lines  plain strings, not yet colorized
     * @param width  max width of the line
     * @return       Description of the Returned Value
     */
    private static String boxTitle(List lines, int width) {
        List title_lines = new ArrayList();
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            title_lines.add(new ColoredString((CharSequence) it.next(), AnsiColor.CYAN));
        }
        return boxText(title_lines, width, false);
    }

    /**
     * Get the body of the box.  No auto-colorization here, pass in ColoredStrings
     * if you want colored text.
     *
     * @param lines
     * @param width
     * @return       Description of the Returned Value
     */
    private static String boxText(List lines, int width) {
        return boxText(lines, width, true);
    }

    /**
     * Get a box, with or without a bottom.  No auto-colorization here, pass in 
     * ColoredStrings if you want colored text.
     *
     * @param lines lines of text to put in box
     * @param width pad each line to be this wide
     * @param withBottom maybe add a bottom line
     * @return            Description of the Returned Value
     */
    private static String boxText(List lines, int width, boolean withBottom) {
        String hline = getHR(width + 2);
        StringBuffer sb = new StringBuffer();
        sb.append(hline);
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            CharSequence line = (CharSequence) it.next();
            sb.append("| ");
            sb.append(line.toString());
            for (int i = length(line); i < width; i++) {
                sb.append(" ");
            }
            sb.append(" |").append(LS);
        }
        if (withBottom) sb.append(hline);
        return sb.toString();
    }

    /**
     * Gets a horizontal rule. A horizontal rule looks like this:<br>
     * +-------------------------+<br>
     * The actual width of the rule will be 
     * <code>width + 2</code>
     * 
     *
     * @param width the number of dashes in the rule
     * @return       a line with a leading + followed by <code>width</code> 
     * dashes, followed by another +.
     */
    private static String getHR(int width) {
        StringBuffer sb = new StringBuffer();
        sb.append("+");
        for (int i = 0; i < width; i++) {
            sb.append("-");
        }
        sb.append("+");
        sb.append(LS);
        return sb.toString();
    }

    /**
	 * Convenience method to get the length of a possibly colorized string.    
	 */
    private static int length(CharSequence s) {
        return ColoredString.length(s);
    }

    /**
     * The main program for the StringBox class
     *
     * @param args  The command line arguments
     */
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb = new StringBuffer();
        sb.append("1) select cdp_transaction_id ");
        sb.append("from parts.cdp_transactions ");
        sb.append("where reminder_timer <= sysdate ");
        sb.append("and expiration_timer > sysdate ");
        sb.append("and cdp_transaction_state_id in ");
        sb.append(" (select cdp_transaction_state_id ");
        sb.append("  from parts.cdp_transaction_states ");
        sb.append("  where ordinal_value <= 4) ");
        sb.append("and reminder_message_id is null ;");
        System.out.println(MessageBox.box("Select command:", sb.toString()));
    }
}

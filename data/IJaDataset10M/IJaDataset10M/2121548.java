package org.psepr.PsEPRServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SimpleTimeZone;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author radams1
 *
 */
public class Utilities {

    private static SimpleDateFormat dateFormatter;

    static {
        dateFormatter = new SimpleDateFormat("yyyyMMdd.HHmmss");
        dateFormatter.setTimeZone(new SimpleTimeZone(0, "Robert"));
    }

    /**
	 * 
	 */
    public Utilities() {
        super();
    }

    /**
	 * Given and element name, skip to the end block of that type
	 * Returns with the parser pointing just after the end element.
	 * 
	 * @param parser pointing into the XML stream
	 * @param elem the element to look for the end
	 */
    public static void XmlSkipBlock(XmlPullParser parser, String elem) throws ParseException {
        XmlBlockit(parser, elem, false);
        return;
    }

    /**
	 * Given an element name, copy from the current element to (and
	 * including) the end element of the passed name.  This body is
	 * returned as a string.
	 * 
	 * @param parser pointing into the XML stream
	 * @param elem the name of the element at the end
	 * @return string of the XML from the current element to the end element
	 */
    public static String XmlCopyBlock(XmlPullParser parser, String elem) throws ParseException {
        return XmlBlockit(parser, elem, true);
    }

    /**
	 * Given an element name, skip through the XML until the terminating
	 * element of that name is found.  If the copy flag is 'true', the
	 * XML that is skipped over is copies and returned as a String.
	 * 
	 * @param parser pointing into the XML stream
	 * @param elem the name of the element at the end
	 * @param copyFlag <code>true</code> is copy return the skipped XML
	 * @return string of the XML from the current element to the end element
	 * 	or null if copyFlag is false
	 */
    private static String XmlBlockit(XmlPullParser parser, String elem, boolean copyFlag) throws ParseException {
        StringBuffer buff = null;
        int level = 0;
        if (copyFlag) {
            buff = new StringBuffer();
        }
        try {
            boolean done = false;
            int pI;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT && !done) {
                if (copyFlag) buff.append(parser.getText());
                pI = parser.next();
                if (pI == XmlPullParser.START_TAG) {
                    level++;
                }
                if (pI == XmlPullParser.END_TAG) {
                    level--;
                    if (level <= 0) {
                        if (parser.getName().equalsIgnoreCase(elem)) {
                            if (copyFlag) buff.append(parser.getText());
                            done = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ParseException("XmlBlockit exception:elem=" + elem + ":" + e.toString(), 0);
        }
        if (copyFlag) {
            return buff.toString();
        } else {
            return null;
        }
    }

    /**
	 * Clean the text returned from XmlPullParser.getText().
	 * By the definition of XML, the text between the elements contains
	 * all the white space and line separators.  Any sane programmer doesn't
	 * want them.  This routine removes all line separator characters and
	 * then any white space at the beginning and the end of the string.
	 * This leaves only the juicy middle.
	 * @param txt The string to clean up.
	 * @return cleaned string.  Could be zero length.
	 */
    public static String cleanXMLText(String txt) {
        StringBuffer t = new StringBuffer(txt);
        for (int ii = t.length() - 1; ii >= 0; ii--) {
            if (Character.getType(t.charAt(ii)) == Character.LINE_SEPARATOR) {
                t.deleteCharAt(ii);
            }
        }
        while (t.length() > 0 && Character.isWhitespace(t.charAt(0))) {
            t.deleteCharAt(0);
        }
        while (t.length() > 0 && Character.isWhitespace(t.charAt(t.length() - 1))) {
            t.deleteCharAt(t.length() - 1);
        }
        return t.toString();
    }

    /**
     * Pseudo-random number generator object for use with randomString().
     * The Random class is not considered to be cryptographically secure, so
     * only use these random Strings for low to medium security applications.
     */
    private static Random randGen = new Random();

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    /**
     * Returns a random String of numbers and letters (lower and upper case)
     * of the specified length. The method uses the Random class that is
     * built-in to Java which is suitable for low to medium grade security uses.
     * This means that the output is only pseudo random, i.e., each number is
     * mathematically generated so is not truly random.<p>
     *
     * The specified length must be at least one. If not, the method will return
     * null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * Returns a random integer that's around zero plus or minus the passed
     * parameter.  So, plusMinus(2) will return either -2, -1, 0, 1 or 2.
     * @param plusminus range to be plus or minus zero
     * @return plus or minus passed parameter around zero
     */
    public static final int plusMinus(int plusminus) {
        int pm = Math.abs(plusminus);
        Long ll = new Long(Math.round(Math.random() * (pm * 2 + 1) - (pm + 0.5)));
        return ll.intValue();
    }

    /**
     * Returns a string which is the XML represenation of the time.
     * String is '<element readable="20051011.134521">1244356723</element>'
     */
    public static final String xmlDate(String element, long dat) {
        return "<" + element + " readable=\"" + dateFormatter.format(new Date(dat)) + "\">" + dat + "</" + element + ">";
    }

    public static final String xmlDate(String element) {
        return xmlDate(element, System.currentTimeMillis());
    }

    /**
     * Return the current call stack in a String.
     */
    public static final String stackToString(StackTraceElement[] ste) {
        StringBuffer buff = new StringBuffer();
        for (int ee = 0; ee < ste.length; ee++) {
            buff.append(ste[ee].toString() + " / ");
        }
        return buff.toString();
    }
}

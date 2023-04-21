package preprocessing.automatic.weka.core;

import java.io.*;
import java.util.*;

/** 
 * Class representing a range of cardinal numbers. The range is set by a 
 * string representation such as: <P>
 *
 * <code>
 *   all
 *   first-last
 *   1,2,3,4
 * </code> <P>
 * or combinations thereof. The range is internally converted from
 * 1-based to 0-based (so methods that set or get numbers not in string
 * format should use 0-based numbers).
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision: 1.14 $
 */
public class Range implements Serializable {

    Vector m_RangeStrings = new Vector();

    boolean m_Invert;

    boolean[] m_SelectFlags;

    int m_Upper = -1;

    public Range() {
    }

    /**
   * Constructor to set initial range.
   *
   * @param rangeList the initial range
   * @exception IllegalArgumentException if the range list is invalid
   */
    public Range(String rangeList) {
        setRanges(rangeList);
    }

    /**
   * Sets the value of "last".
   *
   * @param newUpper the value of "last"
   */
    public void setUpper(int newUpper) {
        if (newUpper >= 0) {
            m_Upper = newUpper;
            setFlags();
        }
    }

    public boolean getInvert() {
        return m_Invert;
    }

    /**
   * Sets whether the range sense is inverted, i.e. all <i>except</i>
   * the values included by the range string are selected.
   * 
   * @param newSetting true if the matching sense is inverted
   */
    public void setInvert(boolean newSetting) {
        m_Invert = newSetting;
    }

    /**
   * Gets the string representing the selected range of values
   *
   * @return the range selection string
   */
    public String getRanges() {
        String result = null;
        Enumeration enu = m_RangeStrings.elements();
        while (enu.hasMoreElements()) {
            if (result == null) {
                result = (String) enu.nextElement();
            } else {
                result += ',' + (String) enu.nextElement();
            }
        }
        return (result == null) ? "" : result;
    }

    public void setRanges(String rangeList) {
        Vector ranges = new Vector(10);
        while (!rangeList.equals("")) {
            String range = rangeList.trim();
            int commaLoc = rangeList.indexOf(',');
            if (commaLoc != -1) {
                range = rangeList.substring(0, commaLoc).trim();
                rangeList = rangeList.substring(commaLoc + 1).trim();
            } else {
                rangeList = "";
            }
            if (!range.equals("")) {
                ranges.addElement(range);
            }
        }
        m_RangeStrings = ranges;
        m_SelectFlags = null;
    }

    public boolean isInRange(int index) {
        if (m_Upper == -1) {
            throw new RuntimeException("No upper limit has been specified for range");
        }
        if (m_Invert) {
            return !m_SelectFlags[index];
        } else {
            return m_SelectFlags[index];
        }
    }

    /**
   * Constructs a representation of the current range. Being a string
   * representation, the numbers are based from 1.
   * 
   * @return the string representation of the current range
   */
    public String toString() {
        if (m_RangeStrings.size() == 0) {
            return "Empty";
        }
        String result = "Strings: ";
        Enumeration enu = m_RangeStrings.elements();
        while (enu.hasMoreElements()) {
            result += (String) enu.nextElement() + " ";
        }
        result += "\n";
        result += "Invert: " + m_Invert + "\n";
        try {
            if (m_Upper == -1) {
                throw new RuntimeException("Upper limit has not been specified");
            }
            String cols = null;
            for (int i = 0; i < m_SelectFlags.length; i++) {
                if (isInRange(i)) {
                    if (cols == null) {
                        cols = "Cols: " + (i + 1);
                    } else {
                        cols += "," + (i + 1);
                    }
                }
            }
            if (cols != null) {
                result += cols + "\n";
            }
        } catch (Exception ex) {
            result += ex.getMessage();
        }
        return result;
    }

    public int[] getSelection() {
        if (m_Upper == -1) {
            throw new RuntimeException("No upper limit has been specified for range");
        }
        int[] selectIndices = new int[m_Upper + 1];
        int numSelected = 0;
        if (m_Invert) {
            for (int i = 0; i <= m_Upper; i++) {
                if (!m_SelectFlags[i]) {
                    selectIndices[numSelected++] = i;
                }
            }
        } else {
            Enumeration enu = m_RangeStrings.elements();
            while (enu.hasMoreElements()) {
                String currentRange = (String) enu.nextElement();
                int start = rangeLower(currentRange);
                int end = rangeUpper(currentRange);
                for (int i = start; (i <= m_Upper) && (i <= end); i++) {
                    if (m_SelectFlags[i]) {
                        selectIndices[numSelected++] = i;
                    }
                }
            }
        }
        int[] result = new int[numSelected];
        System.arraycopy(selectIndices, 0, result, 0, numSelected);
        return result;
    }

    /**
   * Creates a string representation of the indices in the supplied array.
   *
   * @param indices an array containing indices to select.
   * Since the array will typically come from a program, indices are assumed
   * from 0, and thus will have 1 added in the String representation.
   */
    public static String indicesToRangeList(int[] indices) {
        StringBuffer rl = new StringBuffer();
        int last = -2;
        boolean range = false;
        for (int i = 0; i < indices.length; i++) {
            if (i == 0) {
                rl.append(indices[i] + 1);
            } else if (indices[i] == last) {
                range = true;
            } else {
                if (range) {
                    rl.append('-').append(last);
                    range = false;
                }
                rl.append(',').append(indices[i] + 1);
            }
            last = indices[i] + 1;
        }
        if (range) {
            rl.append('-').append(last);
        }
        return rl.toString();
    }

    /** Sets the flags array. */
    protected void setFlags() {
        m_SelectFlags = new boolean[m_Upper + 1];
        Enumeration enu = m_RangeStrings.elements();
        while (enu.hasMoreElements()) {
            String currentRange = (String) enu.nextElement();
            if (!isValidRange(currentRange)) {
                throw new IllegalArgumentException("Invalid range list at " + currentRange);
            }
            int start = rangeLower(currentRange);
            int end = rangeUpper(currentRange);
            for (int i = start; (i <= m_Upper) && (i <= end); i++) {
                m_SelectFlags[i] = true;
            }
        }
    }

    /**
   * Translates a single string selection into it's internal 0-based equivalent
   *
   * @param single the string representing the selection (eg: 1 first last)
   * @return the number corresponding to the selected value
   */
    protected int rangeSingle(String single) {
        if (single.toLowerCase().equals("first")) {
            return 0;
        }
        if (single.toLowerCase().equals("last")) {
            return m_Upper;
        }
        int index = Integer.parseInt(single) - 1;
        if (index < 0) {
            index = 0;
        }
        if (index > m_Upper) {
            index = m_Upper;
        }
        return index;
    }

    /**
   * Translates a range into it's lower index.
   *
   * @param range the string representation of the range
   * @return the lower index of the range
   */
    protected int rangeLower(String range) {
        int hyphenIndex;
        if ((hyphenIndex = range.indexOf('-')) >= 0) {
            return Math.min(rangeLower(range.substring(0, hyphenIndex)), rangeLower(range.substring(hyphenIndex + 1)));
        }
        return rangeSingle(range);
    }

    /**
   * Translates a range into it's upper index. Must only be called once
   * setUpper has been called.
   *
   * @param range the string representation of the range
   * @return the upper index of the range
   */
    protected int rangeUpper(String range) {
        int hyphenIndex;
        if ((hyphenIndex = range.indexOf('-')) >= 0) {
            return Math.max(rangeUpper(range.substring(0, hyphenIndex)), rangeUpper(range.substring(hyphenIndex + 1)));
        }
        return rangeSingle(range);
    }

    /**
   * Determines if a string represents a valid index or simple range.
   * Examples: <code>first  last   2   first-last  first-4  4-last</code>
   * Doesn't check that a < b for a-b
   *
   * @param range
   * @return true if the range is valid
   */
    protected boolean isValidRange(String range) {
        if (range == null) {
            return false;
        }
        int hyphenIndex;
        if ((hyphenIndex = range.indexOf('-')) >= 0) {
            if (isValidRange(range.substring(0, hyphenIndex)) && isValidRange(range.substring(hyphenIndex + 1))) {
                return true;
            }
            return false;
        }
        if (range.toLowerCase().equals("first")) {
            return true;
        }
        if (range.toLowerCase().equals("last")) {
            return true;
        }
        try {
            int index = Integer.parseInt(range);
            if ((index > 0) && (index <= m_Upper + 1)) {
                return true;
            }
            return false;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
   * Main method for testing this class.
   *
   * @param argv one parameter: a test range specification
   */
    public static void main(String[] argv) {
        try {
            if (argv.length == 0) {
                throw new Exception("Usage: Range <rangespec>");
            }
            Range range = new Range();
            range.setRanges(argv[0]);
            range.setUpper(9);
            range.setInvert(false);
            System.out.println("Input: " + argv[0] + "\n" + range.toString());
            int[] rangeIndices = range.getSelection();
            for (int i = 0; i < rangeIndices.length; i++) System.out.print(" " + (rangeIndices[i] + 1));
            System.out.println("");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

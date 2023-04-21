package org.pojosoft.lms.content.scorm2004.sequencer;

import org.apache.log4j.Logger;
import java.io.Serializable;

/**
 * <br><br>
 * <p/>
 * <strong>Filename:</strong> ADLDuration.java<br><br>
 * <p/>
 * <strong>Description:</strong>none<br><br>
 * <p/>
 * <strong>Design Issues:</strong>none<br><br>
 * <p/>
 * <strong>Implementation Issues:</strong>none<br><br>
 * <p/>
 * <p/>
 * <strong>Known Problems:</strong>none<br><br>
 * <p/>
 * <strong>Side Effects:</strong>none<br><br>
 * <p/>
 * <strong>References:</strong>none<br>
 * <ul>
 * <li>SCORM 1.3
 * </ul>
 *
 * @author ADL Technical Team
 */
public class ADLDuration implements Serializable {

    static final long serialVersionUID = 1L;

    static transient Logger logger = Logger.getLogger(ADLDuration.class);

    /**
   * Enumeration of possible relations between two <code>ADLDuration</code>
   * objects.
   * <br>Unknown
   * <br><b>-999</b>
   */
    public static final int UNKNOWN = -999;

    /**
   * Enumeration of possible relations between two <code>ADLDuration</code>
   * objects.
   * <br>Less Than
   * <br><b>-1</b>
   */
    public static final int LT = -1;

    /**
   * Enumeration of possible relations between two <code>ADLDuration</code>
   * objects.
   * <br>Less Than
   * <br><b>0</b>
   */
    public static final int EQ = 0;

    /**
   * Enumeration of possible relations between two <code>ADLDuration</code>
   * objects.
   * <br>Greater Than
   * <br><b>1</b>
   */
    public static final int GT = 1;

    /**
   * Enumeration of possible formats for duration information.
   * <br>Seconds /w one tenth second accuracy
   * <br><b>0</b>
   */
    public static final int FORMAT_SECONDS = 0;

    /**
   * Enumeration of possible formats for duration information.
   * <br>XML Schema -- Duration Type
   * <br><b>1</b>
   */
    public static final int FORMAT_SCHEMA = 1;

    /**
   * The duration being tracked in milliseconds
   */
    public long mDuration = 0;

    public ADLDuration() {
        mDuration = 0;
    }

    /**
   * @param durationInSeconds
   */
    public ADLDuration(double durationInSeconds) {
        mDuration = (long) (durationInSeconds * 1000.0);
    }

    /**
   * @param iFormat FORMAT_SECONDS   or FORMAT_SCHEMA
   * @param iValue
   */
    public ADLDuration(int iFormat, String iValue) {
        String hours = null;
        String min = null;
        String sec = null;
        switch(iFormat) {
            case FORMAT_SECONDS:
                double secs = 0.0;
                try {
                    secs = (new Double(iValue)).doubleValue();
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        System.out.print("  Invalid Format ::  " + iFormat + " // " + iValue);
                    }
                }
                mDuration = (long) (secs * 1000.0);
                break;
            case FORMAT_SCHEMA:
                int locStart = iValue.indexOf('T');
                int loc = 0;
                if (locStart != -1) {
                    locStart++;
                    loc = iValue.indexOf("H", locStart);
                    if (loc != -1) {
                        hours = iValue.substring(locStart, loc);
                        mDuration = (new Long(hours)).longValue() * 3600;
                        locStart = loc + 1;
                    }
                    loc = iValue.indexOf("M", locStart);
                    if (loc != -1) {
                        min = iValue.substring(locStart, loc);
                        mDuration += (new Long(min)).longValue() * 60;
                        locStart = loc + 1;
                    }
                    loc = iValue.indexOf("S", locStart);
                    if (loc != -1) {
                        sec = iValue.substring(locStart, loc);
                        mDuration += (new Long(sec)).longValue();
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(" ERROR : Invalid format  --> " + iValue);
                    }
                }
                break;
            default:
        }
    }

    public String format(int iFormat) {
        String out = null;
        long countHours = 0;
        long countMin = 0;
        long countSec = 0;
        long temp = 0;
        switch(iFormat) {
            case FORMAT_SECONDS:
                double sec = mDuration / 1000.0;
                out = (new Double(sec)).toString();
                break;
            case FORMAT_SCHEMA:
                out = "";
                countHours = 0;
                countMin = 0;
                countSec = 0;
                temp = mDuration / 1000;
                if (temp >= 1000) {
                    if (temp >= 3600) {
                        countHours = temp / 3600;
                        temp %= 3600;
                    }
                    if (temp > 60) {
                        countMin = temp / 60;
                        temp %= 60;
                    }
                    countSec = temp;
                }
                out = "PT";
                if (countHours > 0) {
                    out += Long.toString(countHours, 10);
                    out += "H";
                }
                if (countMin > 0) {
                    out += Long.toString(countMin, 10);
                    out += "M";
                }
                if (countSec > 0) {
                    out += Long.toString(countSec, 10);
                    out += "S";
                }
                break;
            default:
        }
        return out;
    }

    /**
   * This method adds the duration value passed in (<code>iDur</code>) to the
   * duration value being held by <code>mDuration</code>.
   *
   * @param iDur The duration value to add.
   */
    public void add(ADLDuration iDur) {
        mDuration += iDur.mDuration;
    }

    /**
   * This method compares to duration values.  The input duration value
   * (<code>iDur</code> is compared against the <code>mDuration</code> value.
   *
   * @param iDur The duration value to compare.
   * @return Returns an integer value that represents the following:
   *         <ul>
   *         <li> -1 if <code>mDuration</code> is less than <code>iDur</code></li>
   *         <li> 0 if <code>mDuration</code> is equal to <code>iDur</code></li>
   *         <li> 1 if <code>mDuration</code> is greater than <code>iDur</code></li>
   *         <li> -999 if unknown</li>
   *         </ul>
   */
    public int compare(ADLDuration iDur) {
        int relation = ADLDuration.UNKNOWN;
        if (mDuration < iDur.mDuration) {
            relation = ADLDuration.LT;
        } else if (mDuration == iDur.mDuration) {
            relation = ADLDuration.EQ;
        } else if (mDuration > iDur.mDuration) {
            relation = ADLDuration.GT;
        }
        return relation;
    }
}

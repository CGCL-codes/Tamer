package org.pojosoft.lms.content.scorm2004.datatypes;

/**
 * Defines run-time data model utilities.
 * <br><br>
 * <p/>
 * <strong>Filename:</strong> DMTimeUtility.java<br><br>
 * <p/>
 * <strong>Description:</strong>This file contains utility methods used
 * by the run-time datamodel.<br><br>
 * <p/>
 * <strong>Design Issues:</strong><br><br>
 * <p/>
 * <strong>Implementation Issues:</strong><br><br>
 * <p/>
 * <strong>Known Problems:</strong><br><br>
 * <p/>
 * <strong>Side Effects:<strong><br><br>
 * <p/>
 * <strong>References:</strong><br>
 * <ul>
 * <li>SCORM 2004
 * </ul>
 *
 * @author ADL Technical Team
 */
public class ADLDurationUtils {

    /**
   * This method calls the parseTimeIntervalString method to convert a time interval
   * string to integers for the year, month, day, hour, minute, second and
   * decimal portion of second.  The returned integers are added and then
   * converted back to a string which is returned.
   *
   * @param iTimeOne The String representation of a datamodel time interval.
   * @param iTimeTwo The String representation of a datamodel time interval.
   * @return A String representing the addition of the two input parameters.
   */
    public static String add(String iTimeOne, String iTimeTwo) {
        String mTimeString = null;
        int mYear1 = 0;
        int mMonth1 = 0;
        int mDay1 = 0;
        int mHour1 = 0;
        int mMinute1 = 0;
        float mSecond1 = 0;
        int mYear2 = 0;
        int mMonth2 = 0;
        int mDay2 = 0;
        int mHour2 = 0;
        int mMinute2 = 0;
        float mSecond2 = 0;
        int multiple = 1;
        int[] mFirstTime = new int[7];
        int[] mSecondTime = new int[7];
        for (int i = 0; i < 7; i++) {
            mFirstTime[i] = 0;
            mSecondTime[i] = 0;
        }
        parseTimeIntervalString(iTimeOne, mFirstTime);
        parseTimeIntervalString(iTimeTwo, mSecondTime);
        for (int i = 0; i < 7; i++) {
            mFirstTime[i] += mSecondTime[i];
        }
        if (mFirstTime[6] > 99) {
            multiple = mFirstTime[6] / 100;
            mFirstTime[6] = mFirstTime[6] % 100;
            mFirstTime[5] += multiple;
        }
        if (mFirstTime[5] > 59) {
            multiple = mFirstTime[5] / 60;
            mFirstTime[5] = mFirstTime[5] % 60;
            mFirstTime[4] += multiple;
        }
        if (mFirstTime[4] > 59) {
            multiple = mFirstTime[4] / 60;
            mFirstTime[4] = mFirstTime[4] % 60;
            mFirstTime[3] += multiple;
        }
        if (mFirstTime[3] > 23) {
            multiple = mFirstTime[3] / 24;
            mFirstTime[3] = mFirstTime[3] % 24;
            mFirstTime[2] += multiple;
        }
        mTimeString = "P";
        if (mFirstTime[0] != 0) {
            Integer tempInt = new Integer(mFirstTime[0]);
            mTimeString += tempInt.toString();
            mTimeString += "Y";
        }
        if (mFirstTime[1] != 0) {
            Integer tempInt = new Integer(mFirstTime[1]);
            mTimeString += tempInt.toString();
            mTimeString += "M";
        }
        if (mFirstTime[2] != 0) {
            Integer tempInt = new Integer(mFirstTime[2]);
            mTimeString += tempInt.toString();
            mTimeString += "D";
        }
        if ((mFirstTime[3] != 0) || (mFirstTime[4] != 0) || (mFirstTime[5] != 0)) {
            mTimeString += "T";
        }
        if (mFirstTime[3] != 0) {
            Integer tempInt = new Integer(mFirstTime[3]);
            mTimeString += tempInt.toString();
            mTimeString += "H";
        }
        if (mFirstTime[4] != 0) {
            Integer tempInt = new Integer(mFirstTime[4]);
            mTimeString += tempInt.toString();
            mTimeString += "M";
        }
        if (mFirstTime[5] != 0) {
            Integer tempInt = new Integer(mFirstTime[5]);
            mTimeString += tempInt.toString();
        }
        if (mFirstTime[6] != 0) {
            if (mFirstTime[5] == 0) {
                mTimeString += "0";
            }
            mTimeString += ".";
            if (mFirstTime[6] < 10) {
                mTimeString += "0";
            }
            Integer tempInt2 = new Integer(mFirstTime[6]);
            mTimeString += tempInt2.toString();
        }
        if ((mFirstTime[5] != 0) || (mFirstTime[6] != 0)) {
            mTimeString += "S";
        }
        return mTimeString;
    }

    /**
   * This method takes the input String parameter which represents
   * a datamodel time interval string and converts it to an array of integers.
   * The array integers represent the years, months, days, hours, minutes, seco
   * and decimal portions of seconds of the input time interval string.  Any on
   * of the time interval sections may be missing
   *
   * @param iTime   The String representation of a datamodel time interval.
   * @param ioArray An array of integers.
   */
    public static void parseTimeIntervalString(String iTime, int[] ioArray) {
        String mInitArray[];
        String mTempArray2[] = { "0", "0", "0" };
        String mDate = "0";
        String mTime = "0";
        if (iTime == null) {
            return;
        }
        if ((iTime.length() == 1) || (iTime.indexOf("P") == -1)) {
            return;
        }
        try {
            mInitArray = iTime.split("P");
            if (mInitArray[1].indexOf("T") != -1) {
                mTempArray2 = mInitArray[1].split("T");
                mDate = mTempArray2[0];
                mTime = mTempArray2[1];
            } else {
                mDate = mInitArray[1];
            }
            if (mDate.indexOf("Y") != -1) {
                mInitArray = mDate.split("Y");
                Integer tempInt = new Integer(mInitArray[0]);
                ioArray[0] = tempInt.intValue();
            } else {
                mInitArray[1] = mDate;
            }
            if (mDate.indexOf("M") != -1) {
                mTempArray2 = mInitArray[1].split("M");
                Integer tempInt = new Integer(mTempArray2[0]);
                ioArray[1] = tempInt.intValue();
            } else {
                if (mInitArray.length != 2) {
                    mTempArray2[1] = "";
                } else {
                    mTempArray2[1] = mInitArray[1];
                }
            }
            if (mDate.indexOf("D") != -1) {
                mInitArray = mTempArray2[1].split("D");
                Integer tempInt = new Integer(mInitArray[0]);
                ioArray[2] = tempInt.intValue();
            } else {
                mInitArray = new String[2];
            }
            if (!mTime.equals("0")) {
                if (mTime.indexOf("H") != -1) {
                    mInitArray = mTime.split("H");
                    Integer tempInt = new Integer(mInitArray[0]);
                    ioArray[3] = tempInt.intValue();
                } else {
                    mInitArray[1] = mTime;
                }
                if (mTime.indexOf("M") != -1) {
                    mTempArray2 = mInitArray[1].split("M");
                    Integer tempInt = new Integer(mTempArray2[0]);
                    ioArray[4] = tempInt.intValue();
                } else {
                    if (mInitArray.length != 2) {
                        mTempArray2[1] = "";
                    } else {
                        mTempArray2[1] = mInitArray[1];
                    }
                }
                if (mTime.indexOf("S") != -1) {
                    mInitArray = mTempArray2[1].split("S");
                    if (mTime.indexOf(".") != -1) {
                        mTempArray2 = mInitArray[0].split("[.]");
                        if (mTempArray2[1].length() == 1) {
                            mTempArray2[1] = mTempArray2[1] + "0";
                        }
                        Integer tempInt2 = new Integer(mTempArray2[1]);
                        ioArray[6] = tempInt2.intValue();
                        Integer tempInt = new Integer(mTempArray2[0]);
                        ioArray[5] = tempInt.intValue();
                    } else {
                        Integer tempInt = new Integer(mInitArray[0]);
                        ioArray[5] = tempInt.intValue();
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            ;
        }
        return;
    }

    /**
   * Provides the number of seconds represented by the given duration.
   * Includs the two digits fraction <br>
   * The number of seconds does not include leap years.
   *
   * @param iValue ADl timeinterval string
   * @return The number of seconds including the two digits fraction represented by the duration.
   */
    public static Double getSeconds(String iValue) {
        long secs = 0;
        Double total = null;
        int curIdx = 1;
        int tIdx = iValue.indexOf('T');
        int nextIdx = -1;
        String val = null;
        boolean done = false;
        nextIdx = iValue.indexOf('Y');
        if (nextIdx != -1) {
            val = iValue.substring(curIdx, nextIdx);
            curIdx = nextIdx + 1;
            try {
                secs = secs + Long.parseLong(val, 10) * 31536000L;
            } catch (Exception e) {
                done = true;
            }
        }
        nextIdx = iValue.indexOf('M');
        if (!done && nextIdx != -1 && nextIdx < tIdx) {
            val = iValue.substring(curIdx, nextIdx);
            curIdx = nextIdx + 1;
            try {
                secs = secs + Long.parseLong(val, 10) * 2628029L;
            } catch (Exception e) {
                done = true;
            }
        }
        nextIdx = iValue.indexOf('D');
        if (!done && nextIdx != -1) {
            val = iValue.substring(curIdx, nextIdx);
            curIdx = nextIdx + 1;
            try {
                secs = secs + Long.parseLong(val, 10) * 86400L;
            } catch (Exception e) {
                done = true;
            }
        }
        nextIdx = iValue.indexOf('H');
        if (!done && nextIdx != -1) {
            val = iValue.substring(curIdx, nextIdx);
            if (val.startsWith("T")) {
                val = val.substring(1);
            }
            curIdx = nextIdx + 1;
            try {
                secs = secs + Long.parseLong(val, 10) * 3600L;
            } catch (Exception e) {
                done = true;
            }
        }
        if (tIdx != -1) {
            nextIdx = iValue.indexOf('M', tIdx);
        } else {
            nextIdx = iValue.indexOf('M');
        }
        if (!done && nextIdx != -1) {
            val = iValue.substring(curIdx, nextIdx);
            if (val.startsWith("T")) {
                val = val.substring(1);
            }
            curIdx = nextIdx + 1;
            try {
                secs = secs + Long.parseLong(val, 10) * 60L;
            } catch (Exception e) {
                done = true;
            }
        }
        double subSec = Double.NaN;
        nextIdx = iValue.indexOf('S');
        if (!done && nextIdx != -1) {
            val = iValue.substring(curIdx, nextIdx);
            if (val.startsWith("T")) {
                val = val.substring(1);
            }
            try {
                subSec = Double.parseDouble(val);
            } catch (NumberFormatException nfe) {
                subSec = Double.NaN;
                done = true;
            }
        }
        if (!done && secs >= 0) {
            double sec = Double.parseDouble(Long.toString(secs, 10));
            if (Double.compare(Double.NaN, subSec) != 0) {
                subSec = Math.floor(subSec * 100.0) / 100.0;
                total = new Double(subSec + sec);
            } else {
                total = new Double(sec);
            }
        }
        return total;
    }

    public static String getADLDuration(Double timeInSeconds) {
        long lTimeinSeconds = timeInSeconds.longValue();
        double dTimeinSeconds = timeInSeconds.doubleValue();
        double fractionalSeconds = dTimeinSeconds - lTimeinSeconds;
        int[] mFirstTime = new int[7];
        mFirstTime[0] = (int) (lTimeinSeconds / 31536000L);
        long remainingTime = lTimeinSeconds - (mFirstTime[0] * 31536000L);
        mFirstTime[1] = (int) (remainingTime / 2628029L);
        remainingTime = remainingTime - (mFirstTime[1] * 2628029L);
        mFirstTime[2] = (int) (remainingTime / 86400L);
        remainingTime = remainingTime - (mFirstTime[2] * 86400L);
        mFirstTime[3] = (int) (remainingTime / 3600L);
        remainingTime = remainingTime - (mFirstTime[3] * 3600L);
        mFirstTime[4] = (int) (remainingTime / 60L);
        remainingTime = remainingTime - (mFirstTime[4] * 60L);
        mFirstTime[5] = (int) remainingTime;
        mFirstTime[6] = (int) (fractionalSeconds * 100);
        String mTimeString = "P";
        if (mFirstTime[0] != 0) {
            Integer tempInt = new Integer(mFirstTime[0]);
            mTimeString += tempInt.toString();
            mTimeString += "Y";
        }
        if (mFirstTime[1] != 0) {
            Integer tempInt = new Integer(mFirstTime[1]);
            mTimeString += tempInt.toString();
            mTimeString += "M";
        }
        if (mFirstTime[2] != 0) {
            Integer tempInt = new Integer(mFirstTime[2]);
            mTimeString += tempInt.toString();
            mTimeString += "D";
        }
        if ((mFirstTime[3] != 0) || (mFirstTime[4] != 0) || (mFirstTime[5] != 0) || (mFirstTime[6] != 0)) {
            mTimeString += "T";
        }
        if (mFirstTime[3] != 0) {
            Integer tempInt = new Integer(mFirstTime[3]);
            mTimeString += tempInt.toString();
            mTimeString += "H";
        }
        if (mFirstTime[4] != 0) {
            Integer tempInt = new Integer(mFirstTime[4]);
            mTimeString += tempInt.toString();
            mTimeString += "M";
        }
        if (mFirstTime[5] != 0) {
            Integer tempInt = new Integer(mFirstTime[5]);
            mTimeString += tempInt.toString();
        }
        if (mFirstTime[6] != 0) {
            if (mFirstTime[5] == 0) {
                mTimeString += "0";
            }
            mTimeString += ".";
            if (mFirstTime[6] < 10) {
                mTimeString += "0";
            }
            Integer tempInt2 = new Integer(mFirstTime[6]);
            mTimeString += tempInt2.toString();
        }
        if ((mFirstTime[5] != 0) || (mFirstTime[6] != 0)) {
            mTimeString += "S";
        }
        return mTimeString;
    }
}

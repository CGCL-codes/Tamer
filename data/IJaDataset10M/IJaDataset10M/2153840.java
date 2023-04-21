package org.geoforge.worldwind.util;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class ConvertDms {

    private static final String _STR_SUFFIX_LAT_ = "N";

    private static final String _STR_SUFFIX_LON_ = "E";

    public static String s_get(boolean blnIsLat, int intDegrees, int intMinutes, float fltSeconds) {
        String str = "";
        str += Integer.toString(intDegrees);
        str += " ";
        str += Integer.toString(intMinutes);
        str += " ";
        int intSecondsLat = Math.round(fltSeconds);
        str += Integer.toString(intSecondsLat);
        str += " ";
        if (blnIsLat) str += ConvertDms._STR_SUFFIX_LAT_; else str += ConvertDms._STR_SUFFIX_LON_;
        return str;
    }

    private ConvertDms() {
    }
}

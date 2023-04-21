package com.ivis.xprocess.web.client.util;

/**
 * Utilities for string<->number conversion based on user preferences.
 * 
 * We have two preferences - one for display of effort and one for display of
 * daily records. This class depends on com.ivis.xprocess.util.NumberUtils for
 * locale format.
 * 
 */
public class TimescaleUtils {

    /**
	 * 
	 * For converting minutes from core into minutes for display on the UI.
	 * 
	 * The user's locale is injected within this operation, using
	 * NumberUtils.UI_FORMAT
	 * 
	 * @param minutes
	 *            - the minutes from the model that need to be displayed
	 * @param preference
	 *            - the user's preference for format. There is a preference for
	 *            displaying daily records, and for effort.
	 * @return - locale-specific formatted string to display.
	 */
    public static String getMinutesAsString(int minutes, String preference) {
        String timeString = "";
        int decimalPlaces = 1;
        decimalPlaces = 1;
        timeString = NumberUtils.format((minutes / 60d), decimalPlaces);
        return timeString;
    }

    /**
	 * 
	 * For converting string entered by user into minutes for saving in the
	 * model.
	 * 
	 * The user's locale is used to parse the string within this operation,
	 * using NumberUtils.UI_FORMAT
	 * 
	 * @param text
	 *            - the text from the user that needs to be converted for saving
	 *            in the model
	 * @param preference
	 *            - the user's preference for format. There is one preference
	 *            for displaying daily records, and another for effort.
	 * @return - the minutes that need to be saved.
	 */
    public static int getStringAsMinutes(String text, String preference) {
        if (text.length() == 0) {
            return 0;
        }
        double minutes = 0;
        double hours = NumberUtils.parseDouble(text);
        minutes = hours * 60d;
        return (int) minutes;
    }

    /**
	 * @param text
	 * @param preference
	 * @return the integer value from the text based on the preference
	 */
    public static Integer getInteger(String text, String preference) {
        return (int) getStringAsMinutes(text, preference);
    }

    /**
	 * @param text
	 * @param preference
	 * @return true if the text represents 0
	 */
    public static boolean isZero(String text, String preference) {
        return getStringAsMinutes(text, preference) == 0;
    }
}

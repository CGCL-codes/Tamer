package hr.chus.cchat.client.smartgwt.client.utils;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class JSONUtils {

    public static DateTimeFormat frmt = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
	 * 
	 * @param date
	 * @return
	 */
    public static Date getDate(String date) {
        if (date != null && date.endsWith("Z") && date.length() > 2) {
            date = date.substring(0, date.length() - 2) + "+00:00";
        }
        return frmt.parse(date);
    }

    /**
	 * 
	 * @param json
	 * @param field
	 * @return
	 */
    public static Date getDate(JSONObject json, String field) {
        return getDate(getString(json, field));
    }

    /**
	 * 
	 * @param json
	 * @param field
	 * @return
	 */
    public static String getString(JSONObject json, String field) {
        JSONValue jsonValue = null;
        JSONString jsonString = null;
        if ((jsonValue = json.get(field)) == null) {
            return null;
        }
        if ((jsonString = jsonValue.isString()) == null) {
            JSONNumber jsonNumber = jsonValue.isNumber();
            return jsonNumber != null ? jsonNumber.toString() : null;
        }
        return jsonString.stringValue();
    }
}

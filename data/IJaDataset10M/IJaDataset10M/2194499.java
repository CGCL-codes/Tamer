package org.jaimz.telex.util;

import java.io.*;
import java.util.*;
import java.text.ParseException;
import org.json.*;

/**
 * 	The strings class allows us to configure different string resources outside
 * 	of the program code, for example messages that may need to be I18n'ised or
 * 	the protocol strings in SUTP.
 * 
 * 	The Strings class parses a specified ".strings" file which is a JSON (www.json.org)
 *  encoded map from string key to string value. The class then provides methods
 *  to retrieve the string mapped to a key and to format it using printf style
 *  formatting options.
 *  
 *  To allow the same .strings file to be used across different classes we cache
 *  different instances of Strings according to the filename of the .strings file
 *  they correspond to. This avoids the need to re-parse the file in each consumer
 *  class.
 *
 * @author james
 */
public class Strings {

    private JSONObject _strings;

    private static HashMap<String, Strings> _cache = new HashMap<String, Strings>();

    protected Strings(String jsonText) throws ParseException {
        _strings = new JSONObject(jsonText);
    }

    /**
	 * 	Parse the specified ".strings" file and return the corresponding strings object.
	 * 	The Strings object created will be cached against the filename to avoid prohibitive
	 * 	re-parsing subsequently.
	 * 
	 * @param filename The name of a .strins faile to parse.
	 * @return The parsed Strings object.
	 * 
	 * @throws FileNotFoundException 	If 'filename' does not exist
	 * @throws IOException 	If there was a problem reading 'filename'
	 * @throws ParseException		If there was a problem parsing 'filename'. E.g. it was not in 
	 * 							valid JSON format.
	 */
    public static Strings StringsFromFile(String filename) throws FileNotFoundException, IOException, ParseException {
        Strings result = Strings._cache.get(filename);
        if (result != null) return result;
        File f = new File(filename);
        long len = f.length();
        if (len > Integer.MAX_VALUE) {
        } else {
            int ilen = (int) len;
            char[] text = new char[ilen];
            FileReader fr = new FileReader(f);
            int chRead = fr.read(text, 0, ilen);
            if (chRead == ilen) {
                String jsonText = new String(text);
                result = new Strings(jsonText);
            } else {
                throw new IOException("Could not read entire file.");
            }
        }
        if (result != null) Strings._cache.put(filename, result);
        return result;
    }

    /**
	 * Create a Strings object directly from some JSON text. The Strings object
	 * created will <i>not</i> be cached.
	 * 
	 * @param jsonText The JSON text to parse.
	 * @return The parsed strings object.
	 * 
	 * @throws ParseException If 'jsonText' is not in valid JSON format.
	 */
    public static Strings StringsfromText(String jsonText) throws ParseException {
        return new Strings(jsonText);
    }

    /**
	 * Return the string corresponding to 'key' formatted according to the
	 * arguments. This performs printf style formatting. For example given the
	 * .strings file:
	 * <pre>
	 * 	{	numbers : "one two %s four" 	}
	 * </pre>
	 * The call <code>myStrings.format("numbers", "3")</code> would return:
	 * <pre>
	 * one two 3 four
	 * </pre>
	 * 
	 * @param key The string to retrieve.
	 * @param args Formatting arguments.
	 * 
	 * 
	 * @return The formatted string, or null if 'key' is not in the Strings map.
	 */
    public String format(String key, Object... args) {
        String result = null;
        String formatStr = this._strings.optString(key);
        if (formatStr != null) result = String.format(formatStr, args);
        return result;
    }

    /**
	 * Get the raw stirng stored against the key "key". This method
	 * does not apply any formatting.
	 * 
	 * @param key The name of the string to retrieve.
	 * @return The string against that name, or null if 'key' is not in the Strings map.
	 */
    public String getString(String key) {
        return this._strings.optString(key);
    }
}

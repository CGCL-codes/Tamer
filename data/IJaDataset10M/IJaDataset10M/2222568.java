package com.tb.fb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <p>The Facebook Java SDK provides an interpreter for the Facebook API. The are currently methods to parse all objects returned by the Facebook API.</p>
 * <p>A couple object properties have not been included:</p>
 * <ul>
 * 		<li>Video.length				- I could not see this properties. In all of the videos I tested this property never got returned so I could not see the value to know how to parse it.</li>
 * 		<li>User.about 					- Again, on all the users I tested this value never got returned so I could not see the value to know how to parse it.</li>
 * 		<li>User.platformrequests		- Again, on all the users I tested this value never got returned so I could not see the value to know how to parse it.</li>
 * 		<li>Checkin.coordinates			- This value seems to have been put into the Checkin.place value. To get the latitude use Checkin.place..getLatitude().</li>
 * 		<li>Application.insights		- I could not see this so I did not know how to parse it just like Video.length and User.about.</li>
 * 		<li>Application.subscription	- I could not see this so I did not know how to parse it just like Video.length and User.about.</li>
 * </ul>
 * <p>I also have not implemented calls to an objects connections such as "me/photos". If you want to get that data now just make a call to "me" with "photos" as a field and then access by me.photos.data[x].</p>
 * <p>Metadata had to be placed into its own class because of conflicts with property names.</p>
 * <p>The <a href="http://developers.facebook.com/docs/api#selection" target="_blank">ids</a> parameter for Facebook requests <strongHAS NOT</strong> been implemented yet.</p>
 * @author Travis Beauvais - <a href="http://travisbeauvais.com" target="_blank">http://travisbeauvais.com</a>
 * @version 0.1
 *
 */
public class Facebook {

    private String access_token;

    private int ids = 0, fields = 0, metadata = 0;

    /**
	 * Sets the access token.
	 * 
	 * @param	accessToken		The OAuth access token.
	 * 
	 */
    public Facebook(String accessToken) {
        access_token = accessToken;
    }

    /**
	 * Process an API call on a single object (User, Photo, Album, Group, etc.)
	 * 
	 * @param	id				The ID of the object to return.
	 * @return	A FacebookObject with the values returned from Facebook.
	 * @see FacebookObject
	 * 
	 */
    public FacebookObject api(String id) throws IOException {
        return new FacebookObject(process(URL(id)));
    }

    /**
	 * Process an API call on a single object (User, Photo, Album, Group, etc.) and returning specific fields.
	 * 
	 * @param	id				The ID of the object to return.
	 * @param 	useFields		The specific field you want return comma separated.
	 * @return	A FacebookObject with the values returned from Facebook.
	 * @see FacebookObject
	 * 
	 */
    public FacebookObject api(String id, String useFields) throws IOException {
        return new FacebookObject(process(URL(id, useFields)));
    }

    /**
	 * Process an API call on a single object (User, Photo, Album, Group, etc.) and returning specific fields and metadata for the objet.
	 * 
	 * @param	id				The ID of the object to return.
	 * @param 	useFields		The specific field you want return comma separated.
	 * @param	getMetadata		Set to 1 to return the object metadata.
	 * @return	A FacebookObject with the values returned from Facebook.
	 * @see FacebookObject
	 * 
	 */
    public FacebookObject api(String id, String useFields, int getMetadata) throws IOException {
        return new FacebookObject(process(URL(id, useFields, getMetadata)));
    }

    /**
	 * Process an API call on a single object (User, Photo, Album, Group, etc.) and returning metadata for the objet.
	 * 
	 * @param	id				The ID of the object to return.
	 * @param	getMetadata		Set to 1 to return the object metadata.
	 * @return	A FacebookObject with the values returned from Facebook.
	 * @see FacebookObject
	 * 
	 */
    public FacebookObject api(String id, int getMetadata) throws IOException {
        return new FacebookObject(process(URL(id, getMetadata)));
    }

    /**
	 * Make a request to the Facebook API and return the JSON results.
	 * 
	 * @param	url The URL to make a request to.
	 * @return	A JSON string returned from Facebook.
	 * 
	 */
    public String process(URL url) throws IOException {
        String line, results = "";
        InputStream is = url.openStream();
        BufferedReader dis = new BufferedReader(new InputStreamReader(is));
        while ((line = dis.readLine()) != null) {
            results += line + "\n";
        }
        System.out.println(results);
        return results;
    }

    /**
	 * Trim double quotes from the beginning and of a string.
	 * 
	 * @param	s				A string that may or may not have double quotes at the beginning and/or end.
	 * @return	A string  without the quotes on the beginning and end.
	 * 
	 */
    public static String removeQuotes(String s) {
        if (s.length() == 0 || (s.length() == 2 && s.substring(0, 1).equals("\"") && s.substring(1).equals("\""))) return null;
        if (s.substring(0, 1).equals("\"")) s = s.substring(1);
        if (s.substring(s.length() - 1).equals("\"")) s = s.substring(0, s.length() - 1);
        return s;
    }

    /**
	 * Trim curly braces from the beginning and end of a string.
	 * 
	 * @param	s				A string that may or may not have curly braces at the beginning and/or end.
	 * @return	A string without curly braces on the ends.
	 * 
	 */
    public static String removeBraces(String s) {
        s = s.replace("{", "");
        s = s.replace("}", "");
        return s;
    }

    /**
	 * Process a JSON and return the objects contained in it.
	 * 
	 * @param	json			The JSON to be parsed.
	 * @param	multiple		Set to 1 if the string contains multiple objects. (i.e. [{"name":"John Doe", "id":"12345"},{"name":"Jane Doe","id":"67890"}] would return the 2 objects inside the [].)
	 * @return	An array of object parsed from the JSON string.
	 * 
	 */
    public static Object[] objs(String json, int multiple) {
        Pattern breaks = Pattern.compile("\n|\r|[\n\r]|[\r\n]");
        Matcher m = breaks.matcher(json);
        json = m.replaceAll("");
        if (multiple == 0) {
            if ((json.substring(0, 1).equals("[") || json.substring(0, 1).equals("{")) && objEnd(json) == json.length() - 1) json = json.substring(1);
        }
        return getObjs(json);
    }

    /**
	 * Process a JSON and return the objects contained in it.
	 * 
	 * @param	json			The JSON to be parsed.
	 * @return	An array of object parsed from the JSON string.
	 * 
	 */
    public static Object[] objs(String json) {
        return objs(json, 0);
    }

    /**
	 * Get the first object in a JSOn string based on the first character. If the first character is "{" the object is everything contained within that character and the closing "}" (no necessarily the next "}"). The same is true for brackets ([]) as well.
	 * 
	 * @param	s			The JSON to be parsed.
	 * @return	The end position of the object in the string.
	 * 
	 */
    public static int objEnd(String s) {
        String cont = s.substring(0, 1), close = null, prev = null;
        s = s.substring(1);
        int count = 1, stop = 0;
        if (cont.equals("{")) close = "}"; else if (cont.equals("[")) close = "]"; else if (cont.equals("\"")) close = "\"";
        while (count > 0) {
            if (cont.equals("\"") && s.substring(0, 1).equals(cont)) {
                if (!prev.equals("\\")) {
                    count--;
                }
            } else {
                if (s.substring(0, 1).equals(cont)) {
                    count++;
                } else if (s.substring(0, 1).equals(close)) {
                    count--;
                }
            }
            prev = s.substring(0, 1);
            s = s.substring(1);
            stop++;
        }
        return stop;
    }

    /**
	 * Unescape any escaped forward slashes (/) or double quotes (") in a string.
	 * 
	 * @param	str				String to be unescaped.
	 * @return	The unescaped string.
	 * 
	 */
    public static String unescape(String str) {
        Pattern p = Pattern.compile("\\\\/");
        Matcher matches = p.matcher(str);
        str = matches.replaceAll("/");
        p = Pattern.compile("\\\"");
        matches = p.matcher(str);
        str = matches.replaceAll("\"");
        return str;
    }

    private String URL(String id) throws IOException {
        return URL(id, 0);
    }

    private String URL(String id, String useFields) throws IOException {
        return URL(id, useFields, 0);
    }

    private String URL(String id, String useFields, int getMetadata) throws IOException {
        String url = firstParam(id);
        url += secondParam(useFields);
        return url + addMetadata(getMetadata);
    }

    private String URL(String id, int getMetadata) throws IOException {
        String url = firstParam(id);
        return url + addMetadata(getMetadata);
    }

    private String firstParam(String str) {
        String temp = "https://graph.facebook.com/";
        if (str.length() >= 3) {
            if (str.substring(0, 3).equals("ids")) {
                temp += "?ids=";
                str = str.substring(4);
                ids = 1;
            }
        }
        if (str.substring(0, 1).equals("/")) str = str.substring(1);
        return temp + str;
    }

    private String secondParam(String str) {
        if (str.length() < 7 || !str.substring(0, 7).equals("fields=")) {
            str = "fields=" + str;
        }
        if (ids == 1) {
            str = "&" + str;
        } else {
            str = "?" + str;
        }
        fields = 1;
        return str;
    }

    private String addMetadata(int md) {
        String temp = "";
        if (md == 1) metadata = 1;
        if (ids == 1 || fields == 1) temp += "&"; else temp += "?";
        if (metadata == 1) temp += "metadata=1";
        return temp + "access_token=" + access_token;
    }

    private String process(String url) throws IOException {
        System.out.println(url);
        return process(new URL(url));
    }

    private static Object[] getObjs(String json) {
        List<String> objs = new ArrayList<String>();
        String t = null;
        int next;
        if (!json.substring(0, 1).equals("\"") && !json.substring(0, 1).equals("{") && !json.substring(0, 1).equals("[")) return objs.toArray();
        while (json != null) {
            t = "";
            if (json.length() == 1 || json.length() == 0) {
                json = null;
                continue;
            }
            if (json.substring(0, 1).equals(",") || json.substring(0, 1).equals("]")) {
                json = json.substring(1);
                continue;
            }
            if (json.substring(0, 1).equals("{") || json.substring(0, 1).equals("[")) {
                next = Facebook.objEnd(json);
                t = json.substring(0, next + 1);
                if (t.length() == 2) t = "";
                json = json.substring(next + 1);
            } else if (json.substring(0, 1).equals("\"")) {
                next = Facebook.objEnd(json);
                t = json.substring(0, next + 1);
                json = json.substring(next + 1);
                if (json.substring(0, 1).equals(":")) {
                    if (!json.substring(1, 2).equals("\"") && !json.substring(1, 2).equals("{") && !json.substring(1, 2).equals("[")) {
                        next = json.indexOf(",");
                        if (next < 0) next = json.indexOf("}");
                        t += json.substring(0, next);
                        json = json.substring(next);
                    } else {
                        if (json.substring(1, 3).equals("\"\"")) {
                            next = 1;
                            t += ":";
                        } else {
                            next = Facebook.objEnd(json.substring(1));
                            t += json.substring(0, next + 2);
                        }
                        json = json.substring(next + 2);
                    }
                }
            }
            if (!t.equals("")) objs.add(t);
        }
        return objs.toArray();
    }
}

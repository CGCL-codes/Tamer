package net.sourceforge.fluxion.ajax.util;

import net.sf.json.JSONObject;
import java.lang.Deprecated;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A utils class for working with JSON objects in an AJAX context
 *
 * @author Tony Burdett
 * @author Rob Davey
 * @date 06-May-2010
 */
public class JSONUtils {

    @Deprecated
    public static JSONObject JSONObjectResponse(String json) {
        return JSONObject.fromObject(json);
    }

    public static JSONObject JSONObjectResponse(String key, String value) {
        JSONObject response = new JSONObject();
        try {
            response.put(key, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    public static JSONObject JSONObjectResponse(Map<String, String> jsonMap) {
        JSONObject response = new JSONObject();
        for (String key : jsonMap.keySet()) {
            String value = jsonMap.get(key);
            if (value != null) {
                try {
                    response.put(key, URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("JSON response:: " + response.toString());
        return response;
    }

    public static JSONObject JSONObjectResponse(JSONObject... jsons) {
        JSONObject response = new JSONObject();
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        int responseCount = 0;
        for (JSONObject json : jsons) {
            for (Object key : json.keySet()) {
                if (map.get(key) != null) {
                    Object o = json.get(key);
                    if (o != null) {
                        if (o instanceof String) {
                            try {
                                map.put((key.toString() + responseCount++), URLEncoder.encode((String) o, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            map.put((key.toString() + responseCount++), o);
                        }
                    } else {
                        map.put((key.toString() + responseCount++), o);
                    }
                } else {
                    map.put(key.toString(), json.get(key));
                }
            }
        }
        response.putAll(map);
        System.out.println("Compound JSON response:: " + response.toString());
        return response;
    }

    public static JSONObject SimpleJSONResponse(String content) {
        try {
            return JSONObject.fromObject("{" + "\"response\":\"" + URLEncoder.encode(content, "UTF-8") + "\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SimpleJSONError("Encoding failure on JSON response");
        }
    }

    public static JSONObject LoggedJSONResponse(String content, String log) {
        try {
            return JSONObject.fromObject("{" + "\"response\":\"" + URLEncoder.encode(content, "UTF-8") + "\", \"log\":\"" + URLEncoder.encode(log, "UTF-8") + "\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SimpleJSONError("Encoding failure on JSON response");
        }
    }

    public static JSONObject SimpleJSONError(String error) {
        try {
            return JSONObject.fromObject("{" + "\"error\":\"" + URLEncoder.encode(error, "UTF-8") + "\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SimpleJSONError("Encoding failure on JSON response");
        }
    }
}

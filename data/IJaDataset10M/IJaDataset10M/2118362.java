package com.google.resting.component.impl.json;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.http.message.BasicNameValuePair;
import com.google.resting.component.RequestParams;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

/**
 * Implementation of the collection of JSON request parameters in the REST request.
 * 
 * @author sujata.de
 * @since resting 0.1
 * 
 */
public class JSONRequestParams extends RequestParams {

    public JSONRequestParams() {
        super();
    }

    /**
	 * To add input params in the format
	 * &valueArrayKey={valueArray[0],valueArray[1]...}
	 * 
	 * @param valueArrayKey
	 * @param valueArray
	 */
    public void add(String valueArrayKey, String[] valueArray) {
        JSONArray jsonArray = new JSONArray();
        for (String value : valueArray) {
            jsonArray.put(value);
        }
        queryParams.add(new BasicNameValuePair(valueArrayKey, jsonArray.toString()));
    }

    /**
	 * To add input params in the format
	 * &key=[valueArrayKey={valueArray[0],valueArray[1]...}]
	 * 
	 * @param key
	 * @param valueArrayKey
	 * @param valueArray
	 */
    public void add(String key, String valueArrayKey, String[] valueArray) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        if (valueArray.length == 1) {
            jsonObject = new JSONObject();
            try {
                jsonObject.put(valueArrayKey, valueArray[0]);
            } catch (JSONException e) {
            }
        } else {
            for (String value : valueArray) {
                jsonArray.put(value);
            }
            try {
                jsonObject = new JSONObject();
                jsonObject.put(valueArrayKey, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        queryParams.add(new BasicNameValuePair(key, jsonObject.toString()));
    }

    /**
	 * To add multidimensional arrays of input params in the format:
	 * &key={values(1).key:[values(1).value[0],values(1).value[1],
	 * values(2).key:[values(2).value[0],values(2).value[1]} Ex.
	 * &filters={"colorFacet":["Blue", "Black"],"size":["13", "12"]}
	 * 
	 * @param key
	 * @param values
	 */
    public void add(String key, Map<String, String[]> values) {
        String[] valueElements;
        JSONArray jsonArray;
        JSONObject jsonObject = null;
        int size = values.size();
        StringBuilder result = new StringBuilder("");
        int i = 1;
        Set<Entry<String, String[]>> entrySet = values.entrySet();
        for (Map.Entry<String, String[]> value : entrySet) {
            valueElements = value.getValue();
            jsonArray = new JSONArray();
            for (String valueElement : valueElements) {
                jsonArray.put(valueElement);
            }
            try {
                jsonObject = new JSONObject().put(value.getKey(), jsonArray.toString());
            } catch (JSONException e) {
            }
            String str = jsonObject.toString();
            str = str.substring(1, str.length() - 1).replaceAll("\"\\[", "[").replaceAll("\\]\"", "]").replaceAll("\\\\", "");
            if (i < size) result.append(str).append(","); else result.append(str).append("}");
            i++;
        }
        queryParams.add(new BasicNameValuePair(key, result.toString()));
    }

    @Override
    public void add(String key, String value) {
        queryParams.add(new BasicNameValuePair(key, value));
    }
}

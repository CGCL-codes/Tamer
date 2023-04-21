package org.jdesktop.http.async;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jdesktop.http.Method;
import org.jdesktop.http.async.AsyncHttpRequest.ReadyState;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rbair
 */
public final class JsonHttpRequest extends AsyncHttpRequest {

    private JSONObject responseJSON;

    private Map<String, Object> responseMap;

    /** Creates a new instance of JsonHttpRequest */
    public JsonHttpRequest() {
    }

    public Map<String, Object> getResponseMap() {
        if (getReadyState() == ReadyState.LOADED) {
            return responseMap;
        } else {
            return null;
        }
    }

    protected void reset() {
        setResponseJSON(null);
        setResponseMap(null);
        super.reset();
    }

    protected void handleResponse(String responseText) throws Exception {
        if (responseText == null) {
            setResponseJSON(null);
            setResponseMap(null);
        } else {
            try {
                setResponseJSON(new JSONObject(responseText));
                Map<String, Object> map = new HashMap<String, Object>();
                stuffIntoMap(responseJSON, map);
                setResponseMap(map);
            } catch (Exception e) {
                setResponseJSON(null);
                setResponseMap(null);
                throw e;
            }
        }
    }

    private void stuffIntoMap(JSONObject obj, Map<String, Object> map) {
        if (obj == null) {
            return;
        }
        Iterator itr = obj.keys();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            Object value = obj.opt(key);
            if (value instanceof JSONArray) {
                JSONArray a = (JSONArray) value;
                Object[] array = new Object[a.length()];
                stuffIntoArray(a, array);
                map.put(key, array);
            } else if (value instanceof JSONObject) {
                Map<String, Object> submap = new HashMap<String, Object>();
                stuffIntoMap((JSONObject) value, submap);
                map.put(key, submap);
            } else {
                map.put(key, value);
            }
        }
    }

    private void stuffIntoArray(JSONArray a, Object[] array) {
        if (a == null) {
            return;
        }
        for (int i = 0; i < array.length; i++) {
            Object value = a.opt(i);
            if (value instanceof JSONObject) {
                JSONObject obj = (JSONObject) value;
                Map<String, Object> map = new HashMap<String, Object>();
                stuffIntoMap(obj, map);
                array[i] = map;
            } else if (value instanceof JSONArray) {
                JSONArray ja = (JSONArray) value;
                Object[] o = new Object[ja.length()];
                stuffIntoArray(ja, o);
                array[i] = o;
            } else {
                array[i] = value;
            }
        }
    }

    private void setResponseJSON(JSONObject obj) {
        JSONObject old = this.responseJSON;
        this.responseJSON = obj;
        firePropertyChange("responseJSON", old, this.responseJSON);
    }

    private void setResponseMap(Map<String, Object> obj) {
        Map<String, Object> old = this.responseMap;
        this.responseMap = obj;
        firePropertyChange("responseMap", old, this.responseMap);
    }

    public static void main(String[] args) {
        final JsonHttpRequest req = new JsonHttpRequest();
        req.addReadyStateChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() == ReadyState.LOADED) {
                    Map map = (Map) req.getResponseMap().get("ResultSet");
                    System.out.println("Total Results Avail.: " + map.get("totalResultsAvailable"));
                    System.out.println("Results: ");
                    Object[] results2 = (Object[]) map.get("Result");
                    for (int i = 0; i < results2.length; i++) {
                        Map m = (Map) results2[i];
                        System.out.println("\tTitle: " + m.get("Title"));
                    }
                }
            }
        });
        try {
            req.open(Method.GET, "http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&query=JavaOne&output=json");
            req.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

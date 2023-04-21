package com.mallorcamaus.productlist.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

/**
 * This Class parse an url with JSON code and add this to an ArrayList<HashMap<String, Object>>
 * 
 * @author mario
 *
 */
public class JsonDataParser {

    private ArrayList<HashMap<String, Object>> entryList;

    private HashMap<String, Object> entry;

    private final String domainPathForImage = "http://www.mallorcamaus.com/";

    String[] keyNames;

    public JsonDataParser() {
        entryList = new ArrayList<HashMap<String, Object>>();
        keyNames = new String[] { "image", "title", "desc" };
    }

    public void setJSONKeyNames(String[] names) {
        this.keyNames = names;
    }

    /**
	 * Parse the url and this content to an array list
	 * @param String url The Url to be parse
	 */
    public void parse(String url) {
        try {
            JSONObject completeData = new JSONObject(parseJSONContent(url));
            JSONArray JSONdata = completeData.getJSONArray("data");
            Log.i(JsonDataParser.class.toString(), "lenght: " + JSONdata.length());
            for (int i = 0; i < JSONdata.length(); i++) {
                String imageUrl = domainPathForImage + JSONdata.getJSONObject(i).getString(keyNames[0]);
                addDataOnMap(JSONdata.getJSONObject(i).getString(keyNames[1]), JSONdata.getJSONObject(i).getString(keyNames[2]), imageUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Put some data as HashMap on array lis
	 * 
	 * @param String title The title 
	 * @param String desc The description
	 * @param String imageUrl The Url
	 */
    private void addDataOnMap(String title, String desc, String imageUrl) {
        entry = new HashMap<String, Object>();
        entry.put(keyNames[0], imageUrl);
        entry.put(keyNames[1], title);
        entry.put(keyNames[2], removeBadHtmlCode(desc));
        entryList.add(entry);
    }

    /**
	 * Parse the JSON content by given url
	 * 
	 * @param String url The Url to be parsed 
	 * @return The content parsed by url
	 */
    private String parseJSONContent(String url) {
        URL jsonContent = null;
        String line = null;
        StringBuffer content = new StringBuffer();
        BufferedReader in;
        try {
            jsonContent = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection mc;
        try {
            mc = jsonContent.openConnection();
            in = new BufferedReader(new InputStreamReader(mc.getInputStream()));
            while ((line = in.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                content.append(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
	 * Get the parsed data 
	 * @return
	 */
    public ArrayList<HashMap<String, Object>> getData() {
        return entryList;
    }

    /**
	 * Strips tags and replace &nsbp; with empty chars
	 * 
	 * @param code String The bad html code
	 * 
	 * @return String The clean html code
	 */
    protected String removeBadHtmlCode(String code) {
        return code.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", " ");
    }
}

package com.foobnix.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.foobnix.model.VkAudio;

public class JSONHelper {

    public static List<VkAudio> parseVKSongs(String jsonString) throws JSONException {
        List<VkAudio> results = new ArrayList<VkAudio>();
        JSONObject jObject = new JSONObject(jsonString);
        JSONArray jResponse = jObject.getJSONArray("response");
        for (int i = 1; i < jResponse.length(); i++) {
            JSONObject jItem = jResponse.getJSONObject(i);
            String aid = jItem.getString("aid");
            String owner_id = jItem.getString("owner_id");
            String artist = jItem.getString("artist");
            String title = jItem.getString("title");
            String duration = jItem.getString("duration");
            String url = jItem.getString("url");
            results.add(new VkAudio(aid, owner_id, artist, title, duration, url));
        }
        return results;
    }
}

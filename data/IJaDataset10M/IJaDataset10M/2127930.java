package com.google.code.sagetvaddons.swl.server;

import gkusnick.sagetv.api.ShowAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonShow {

    public static JSONObject toJSON(ShowAPI.Show show) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", show.GetShowExternalID());
            obj.put("desc", show.GetShowDescription());
            obj.put("title", show.GetShowTitle());
            obj.put("cat", show.GetShowCategory());
            obj.put("subcat", show.GetShowSubCategory());
            obj.put("credits", getPeopleInShow(show));
            obj.put("rated", show.GetShowRated());
            obj.put("year", show.GetShowYear());
            obj.put("parentalRating", show.GetShowParentalRating());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    private static JSONObject getPeopleInShow(ShowAPI.Show s) {
        JSONObject jobj = new JSONObject();
        for (String role : s.GetRolesInShow()) {
            JSONArray people = new JSONArray();
            for (String person : s.GetPeopleListInShowInRole(role)) people.put(person);
            try {
                jobj.put(role, people);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jobj;
    }

    private JsonShow() {
    }
}

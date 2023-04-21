package com.google.code.sagetvaddons.swl.server;

import org.json.JSONException;
import org.json.JSONObject;

class SageCommandSaveFavourite implements SageCommand {

    private String rawData;

    public SageCommandSaveFavourite(String rawData) {
        this.rawData = rawData;
    }

    public String execute() {
        try {
            JSONObject jobj = new JSONObject(rawData);
            JsonFavourite.fromJSON(jobj);
            return "OK";
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

package org.jnav.location;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 *
 * @author Matthias Lohr <mlohr@jnav.org>
 */
public class GeoAddress extends JSONObject {

    public GeoAddress(String jsonString) throws JSONException {
        super(jsonString);
    }

    public GeoAddress(String country, String name, GeoPosition geoPosition) {
        super();
        try {
            super.put("country", country);
            super.put("name", name);
            super.put("label", name);
            super.put("geoPosition", geoPosition);
            super.put("fullName", "");
            super.put("lastUse", System.currentTimeMillis());
        } catch (JSONException e) {
        }
    }

    public String getFullName() {
        try {
            return super.getString("fullName");
        } catch (JSONException e) {
            return "";
        }
    }

    public GeoPosition getGeoPosition() {
        try {
            return (GeoPosition) super.getJSONObject("geoPosition");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getLabel() {
        try {
            return super.getString("label");
        } catch (JSONException ex) {
            return this.getName();
        }
    }

    public long getLastUse() {
        try {
            return Long.parseLong(super.getString("lastUse"));
        } catch (JSONException ex) {
            return 0;
        }
    }

    public String getName() {
        try {
            return super.getString("name");
        } catch (JSONException e) {
            return "";
        }
    }

    public void setFullName(String fullName) {
        try {
            super.put("fullName", fullName);
        } catch (JSONException ex) {
        }
    }

    public void setLabel(String label) {
        try {
            super.put("label", label);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}

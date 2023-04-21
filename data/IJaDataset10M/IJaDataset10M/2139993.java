package com.rednels.ofcgwt.client.model.elements.dot;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * OFC Star
 */
public class Star extends BaseDot {

    private Integer rotation;

    private Boolean hollow;

    public Star() {
        this(null);
    }

    public Star(Number value) {
        super("star", value);
    }

    public JSONValue buildJSON() {
        JSONObject json = (JSONObject) super.buildJSON();
        if (rotation != null) json.put("rotation", new JSONNumber(rotation.intValue()));
        if (hollow != null) json.put("hollow", JSONBoolean.getInstance(hollow));
        return json;
    }

    /**
	 * Gets the rotation.
	 * 
	 * @return the rotation
	 */
    public Integer getRotation() {
        return rotation;
    }

    /**
	 * @return true if hollow
	 */
    public boolean isHollow() {
        return hollow;
    }

    /**
	 * Sets the hollow.
	 * 
	 * @param hollow
	 *            the hollow
	 */
    public void setHollow(Boolean hollow) {
        this.hollow = hollow;
    }

    /**
	 * Sets the rotation.
	 * 
	 * @param rotation
	 *            the rotation
	 */
    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}

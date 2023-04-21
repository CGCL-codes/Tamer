package com.google.gjofc.axis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gjofc.JSONizable;
import com.google.gjson.JSONArray;
import com.google.gjson.JSONBoolean;
import com.google.gjson.JSONNumber;
import com.google.gjson.JSONObject;
import com.google.gjson.JSONString;
import com.google.gjson.JSONValue;

/**
 * OFC y-axis
 */
public class YAxis extends AbstractAxis implements JSONizable {

    private Integer tickLength;

    private Boolean logscale;

    private List<String> labels;

    /**
	 * Adds the labels.
	 * 
	 * @param labels
	 *            the labels
	 */
    public void addLabels(List<String> labels) {
        checkLabelsNotNull();
        this.labels.addAll(labels);
    }

    /**
	 * Adds the labels.
	 * 
	 * @param labels
	 *            the labels
	 */
    public void addLabels(String... labels) {
        checkLabelsNotNull();
        this.labels.addAll(Arrays.asList(labels));
    }

    public JSONValue buildJSON() {
        JSONObject json = (JSONObject) super.buildJSON();
        if (tickLength != null) json.put("tick-length", new JSONNumber(tickLength));
        if (logscale != null) json.put("log-scale", JSONBoolean.getInstance(logscale));
        if (labels == null) return json;
        JSONArray ary = new JSONArray();
        int index = 0;
        for (String o : labels) {
            ary.set(index++, new JSONString(o));
        }
        if (index != 0) json.put("labels", ary);
        return json;
    }

    /**
	 * Gets the labels.
	 * 
	 * @return the labels
	 */
    public List<String> getLabels() {
        return labels;
    }

    /**
	 * Gets the log scale.
	 * 
	 * @return the logscale
	 */
    public Boolean getLogScale() {
        return logscale;
    }

    /**
	 * Gets the tick length.
	 * 
	 * @return the tick length
	 */
    public Integer getTickLength() {
        return tickLength;
    }

    /**
	 * Sets the labels.
	 * 
	 * @param labels
	 *            the new labels
	 */
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    /**
	 * Sets the labels.
	 * 
	 * @param labels
	 *            the new labels
	 */
    public void setLabels(String... labels) {
        setLabels(Arrays.asList(labels));
    }

    /**
	 * Sets the tick length.
	 * 
	 * @param tick_length
	 *            the new tick length
	 */
    public void setTickLength(Integer tick_length) {
        this.tickLength = tick_length;
    }

    /**
	 * Sets the log scale.
	 * 
	 * @param logscale
	 *            the logscale
	 */
    public void setVisible(Boolean logscale) {
        this.logscale = logscale;
    }

    /**
	 * Check labels not null.
	 */
    private synchronized void checkLabelsNotNull() {
        if (labels == null) labels = new ArrayList<String>();
    }
}

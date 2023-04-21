package com.showdown.web.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a JSON Array
 * @author Mat DeLong
 */
public class JSONArray extends JSONValue {

    private List<JSONValue> values;

    /**
    * Array which initializes the array of values
    */
    public JSONArray() {
        this.values = new ArrayList<JSONValue>();
    }

    /**
    * {@inheritDoc}
    */
    public JSONValueType getType() {
        return JSONValueType.ARRAY;
    }

    /**
    * Returns the list of values
    * @return the list of values
    */
    public List<JSONValue> getValues() {
        return values;
    }

    /**
    * Adds the value to the list
    * @param value the value to add
    */
    public void addValue(JSONValue value) {
        this.values.add(value);
    }

    /**
    * {@inheritDoc}
    */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        for (JSONValue v : values) {
            if (!isFirst) {
                sb.append(",");
            }
            isFirst = false;
            sb.append(v.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}

package spidr.datamodel;

import java.io.*;
import java.util.*;

public class DataDescription implements Serializable {

    private static final float MISSING_VALUE = 1.0e+33f;

    public Hashtable attributes;

    public DataDescription() {
        this("", "", "", "", "", "");
    }

    public DataDescription(String table, String element) {
        this(table, element, "", "", "", "");
    }

    public DataDescription(String table, String element, String origin, String elemDescr, String units, String label) {
        attributes = new Hashtable();
        if (table != null) attributes.put("table", table);
        if (element != null) attributes.put("element", element);
        if (origin != null) attributes.put("origin", origin);
        if (elemDescr != null) attributes.put("elemDescr", elemDescr);
        if (units != null) attributes.put("units", units);
        if (label != null) attributes.put("label", label);
        attributes.put("missingValue", new Float(MISSING_VALUE));
        attributes.put("multiplier", new Float(1f));
        attributes.put("outputFormat", "");
        attributes.put("type", "");
    }

    public DataDescription(DataDescription dd) {
        attributes = new Hashtable();
        Enumeration keys = dd.attributes.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = dd.attributes.get(key);
            attributes.put(key, value);
        }
    }

    public String getTable() {
        return (String) attributes.get("table");
    }

    public void setTable(String table) {
        if (table != null) attributes.put("table", table);
    }

    public String getElement() {
        return (String) attributes.get("element");
    }

    public void setElement(String element) {
        if (element != null) attributes.put("element", element);
    }

    public String getOrigin() {
        return (String) attributes.get("origin");
    }

    public void setOrigin(String origin) {
        if (origin != null) attributes.put("origin", origin);
    }

    public String getElemDescr() {
        return (String) attributes.get("elemDescr");
    }

    public void setElemDescr(String elemDescr) {
        if (elemDescr != null) attributes.put("elemDescr", elemDescr);
    }

    public String getLabel() {
        return (String) attributes.get("label");
    }

    public void setLabel(String label) {
        if (label != null) attributes.put("label", label);
    }

    public String getUnits() {
        return (String) attributes.get("units");
    }

    public void setUnits(String units) {
        if (units != null) attributes.put("units", units);
    }

    public float getMissingValue() {
        return ((Float) attributes.get("missingValue")).floatValue();
    }

    public void setMissingValue(float missingValue) {
        attributes.put("missingValue", new Float(missingValue));
    }

    public float getMultiplier() {
        return ((Float) attributes.get("multiplier")).floatValue();
    }

    public void setMultiplier(float multiplier) {
        attributes.put("multiplier", new Float(multiplier));
    }

    public String getOutputFormat() {
        return (String) attributes.get("outputFormat");
    }

    public void setOutputFormat(String outputFormat) {
        if (outputFormat != null) attributes.put("outputFormat", outputFormat);
    }

    public String getType() {
        return (String) attributes.get("type");
    }

    public void setType(String type) {
        if (type != null) {
            attributes.put("type", type);
        }
    }

    public boolean equals(Object descr) {
        String table = getTable();
        String element = getElement();
        if (descr == null) {
            return false;
        } else {
            return (table.equals(((DataDescription) descr).getTable()) && element.equals(((DataDescription) descr).getElement()));
        }
    }

    public String toString() {
        String table = getTable();
        String element = getElement();
        String label = getLabel();
        String elemDescr = getElemDescr();
        String units = getUnits();
        float missingValue = getMissingValue();
        float multiplier = getMultiplier();
        return "table=" + table + "; element=" + element + "; label=" + label + "\n" + "elemDescr=" + elemDescr + "\n" + "units=" + units + "\n" + "missingValue=" + missingValue + "; multiplier=" + multiplier + "\n";
    }

    public void print() {
        System.out.println(this.toString());
    }
}

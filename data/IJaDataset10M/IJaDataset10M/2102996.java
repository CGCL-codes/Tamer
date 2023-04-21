package de.pangaea.metadataportal.search;

import de.pangaea.metadataportal.utils.*;

public class SearchResponseItem implements java.io.Serializable {

    public float getScore() {
        return score;
    }

    public String getXml() {
        return xml;
    }

    public String getIdentifier() {
        return identifier;
    }

    public java.util.Map<String, Object[]> getFields() {
        return fields;
    }

    protected float score = 0.0f;

    protected String xml = null, identifier = null;

    protected java.util.Map<String, Object[]> fields = new java.util.HashMap<String, Object[]>();
}

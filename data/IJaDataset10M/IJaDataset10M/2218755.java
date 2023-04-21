package org.apache.solr.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultDoc {

    private String _id;

    private Integer _internalDocID;

    private Float _score = null;

    private String _explain;

    private Map<String, Object> _field = new LinkedHashMap<String, Object>();

    private Map<String, String[]> _highlight = new LinkedHashMap<String, String[]>();

    public ResultDoc() {
    }

    public ResultDoc(String id, Float score) {
        _id = id;
        _score = score;
    }

    public String toString() {
        return "Doc[" + _id + ":" + _field + "]";
    }

    public String getId() {
        return _id;
    }

    public String getExplain() {
        return _explain;
    }

    public Float getScore() {
        return _score;
    }

    public Integer getInternalDocID() {
        return _internalDocID;
    }

    public Map<String, Object> getField() {
        return _field;
    }

    public Object getField(String name) {
        return _field.get(name);
    }

    public void setExplain(String explain) {
        _explain = explain;
    }

    public void setField(Map<String, Object> fields) {
        _field = fields;
    }

    public void setHighlight(String name, String[] arr) {
        _highlight.put(name, arr);
    }

    public void setId(String id) {
        _id = id;
    }

    public void setInternalDocID(Integer internalDocID) {
        _internalDocID = internalDocID;
    }

    public void setScore(Float score) {
        _score = score;
    }
}

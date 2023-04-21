package com.vircon.myajax.web;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import com.vircon.util.JSONConvertible;
import com.vircon.util.JSONUtils;

public class ArrayListModel extends JSONConvertible implements Serializable, ListModel {

    @Override
    protected void fillJSONObject(JSONObject aJsObject) throws JSONException {
        aJsObject.put("list", JSONUtils.toJSONArray(aryList));
    }

    public void write(XMLWriter aWriter) {
        aWriter.startElement("listModel");
        aWriter.startElement("list");
        for (int item = 0; item < aryList.length; item++) {
            aWriter.startElement("item");
            aWriter.addAttribute("index", item);
            aWriter.setBody(aryList[item]);
            aWriter.endElement();
        }
        aWriter.endElement();
        aWriter.endElement();
    }

    public ArrayListModel(Object[] aryList) {
        this.aryList = aryList;
    }

    public Object get(int i) {
        return aryList[i];
    }

    public int getLength() {
        return aryList.length;
    }

    public String[] getAsString() {
        String result[] = new String[aryList.length];
        for (int i = 0; i < aryList.length; i++) {
            result[i] = aryList[i].toString();
        }
        return result;
    }

    public void clear() {
        aryList = new Object[0];
    }

    public void add(Object object) {
        throw new UnsupportedOperationException("add not supported for ArrayListModel");
    }

    private Object[] aryList;

    private static final long serialVersionUID = 1L;
}

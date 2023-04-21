package com.guanghua.brick.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigContent implements IContent {

    private String id = null;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    public List<Map<String, String>> buildContent(HttpServletRequest request, HttpServletResponse response) {
        return this.list;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addRow(Map<String, String> map) {
        this.list.add(map);
    }

    public void clearRow() {
        this.list.clear();
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }
}

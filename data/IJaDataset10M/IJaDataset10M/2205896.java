package com.yilan.java.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCommandParser {

    private String source;

    private String command;

    private Map<String, String> values = new HashMap<String, String>();

    private List<String> params = new ArrayList<String>();

    public String getCommand() {
        return command;
    }

    public String getValue(int i) {
        return params.get(i);
    }

    public String getParameter(String param) {
        if (values.get(param) == null) {
            return "";
        } else {
            return values.get(param);
        }
    }

    public void loadResource(String source) {
        this.source = source;
        String[] params = source.split(" ");
        command = params[0];
        String key = "";
        StringBuffer value = new StringBuffer("");
        for (int i = 1; i < params.length; i++) {
            if (params[i].equals("")) continue;
            this.params.add(params[i]);
            if (params[i].startsWith("-")) {
                if (!key.equals("")) {
                    this.values.put(key, value.toString().trim());
                    value = new StringBuffer("");
                }
                key = params[i];
            } else {
                value.append(params[i] + " ");
            }
        }
        if (!key.equals("")) {
            this.values.put(key, value.toString().trim());
        }
    }
}

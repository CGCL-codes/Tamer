package com.googlecode.gwt_control.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Olafur Gauti Gudmundsson
 */
public class PageLayout implements Serializable {

    protected final Map<String, List<Widget>> widgets;

    public PageLayout() {
        widgets = new HashMap<String, List<Widget>>();
    }

    public void clear() {
        Set<String> keys = new HashSet<String>(widgets.keySet());
        for (String key : keys) {
            clear(key);
        }
    }

    public void clear(String key) {
        RootPanel.get(key).clear();
        widgets.remove(key);
    }

    public void addWidget(String key, Widget w) {
        if (!widgets.containsKey(key)) {
            widgets.put(key, new ArrayList<Widget>());
        }
        widgets.get(key).add(w);
    }

    public List<Widget> getWidgets(String key) {
        return widgets.get(key);
    }

    public void render() {
        for (Map.Entry<String, List<Widget>> entry : widgets.entrySet()) {
            for (Widget w : entry.getValue()) {
                RootPanel.get(entry.getKey()).add(w);
            }
        }
    }
}

package com.google.gwt.maps.sample.hellomaps.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.maps.sample.hellomaps.client.MapsDemo.MapsDemoInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import java.util.ArrayList;

/**
 * The left panel that contains all of the sinks, along with a short description
 * of each.
 */
public class DemoList extends Composite {

    private ListBox list = new ListBox();

    private ArrayList<MapsDemoInfo> sinks = new ArrayList<MapsDemoInfo>();

    public DemoList() {
        initWidget(list);
        list.setVisibleItemCount(1);
        list.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                History.newItem(list.getItemText(list.getSelectedIndex()));
            }
        });
    }

    public void addMapsDemo(final MapsDemoInfo info) {
        String name = info.getName();
        list.addItem(name);
        sinks.add(info);
    }

    public MapsDemoInfo find(String sinkName) {
        for (int i = 0; i < sinks.size(); ++i) {
            MapsDemoInfo info = sinks.get(i);
            if (info.getName().equals(sinkName)) {
                return info;
            }
        }
        return null;
    }

    public MapsDemoInfo getNext() {
        int nextIndex = list.getSelectedIndex() + 1;
        if (nextIndex >= sinks.size()) {
            nextIndex = 0;
        }
        return sinks.get(nextIndex);
    }

    public void setMapsDemoSelection(String name) {
        for (int i = 0; i < list.getItemCount(); i++) {
            if (name.equals(list.getItemText(i))) {
                list.setSelectedIndex(i);
                break;
            }
        }
    }
}

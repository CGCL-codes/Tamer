package com.totsp.sample.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The main data for the model of an example GWT app. 
 * 
 * The other part of the model, the actions, are all
 * captured in the service servlets. 
 * 
 * @author ccollins
 *
 */
public class MyProjectData implements DataChangeNotifier {

    private List<DataChangeListener> listeners;

    private List<Entry> entries;

    public MyProjectData() {
        this.listeners = new ArrayList<DataChangeListener>();
        this.entries = new ArrayList<Entry>();
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
        this.updateListeners();
    }

    public void addChangeListener(final DataChangeListener listener) {
        this.listeners.add(listener);
    }

    private void updateListeners() {
        for (DataChangeListener l : this.listeners) {
            l.onChange(this);
        }
    }
}

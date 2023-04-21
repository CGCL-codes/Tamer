package com.pcmsolutions.system.preferences;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.util.Vector;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: paulmeehan
 * Date: 01-Dec-2003
 * Time: 05:29:44
 * To change this template use Options | File Templates.
 */
public abstract class Impl_ZPref implements ZPref, PreferenceChangeListener {

    private final String defString;

    private volatile String valueString;

    private final Preferences prefs;

    private final String key;

    private final String presentationName;

    private final String description;

    private final String category;

    private final Vector listeners = new Vector();

    private final ChangeEvent ce = new ChangeEvent(this);

    protected Impl_ZPref(Preferences prefs, String key, String defString, String presentationName, String description) {
        this(prefs, key, defString, presentationName, description, "Uncategorized");
    }

    public Impl_ZPref(Preferences prefs, String key, String def) {
        this(prefs, key, String.valueOf(def), "Unnamed", "No description");
    }

    protected Impl_ZPref(Preferences prefs, String key, String defString, String presentationName, String description, String category) {
        this.defString = defString;
        this.prefs = prefs;
        this.key = key;
        this.category = category;
        this.valueString = prefs.get(key, defString);
        this.presentationName = presentationName;
        if (description == null) this.description = presentationName; else this.description = description;
        prefs.addPreferenceChangeListener(this);
    }

    public String getPresentationName() {
        return presentationName;
    }

    public String getDescription() {
        return description;
    }

    public void zDispose() {
        prefs.removePreferenceChangeListener(this);
        clearListeners();
    }

    public String getValueString() {
        return valueString;
    }

    public String getDefaultString() {
        return defString;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public String getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void addChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }

    public void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
    }

    private Runnable r = new Runnable() {

        public void run() {
            synchronized (listeners) {
                for (int i = 0; i < listeners.size(); i++) {
                    try {
                        ((ChangeListener) listeners.get(i)).stateChanged(ce);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    protected void fireStateChanged(boolean onUIThread) {
        EventListenerList l;
        EventListener el;
        if (onUIThread) {
            SwingUtilities.invokeLater(r);
        } else {
            r.run();
        }
    }

    public String toString() {
        return valueString;
    }

    public synchronized void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals(key)) {
            valueString = evt.getNewValue();
            fireStateChanged(true);
        }
    }

    public abstract void putDefault();

    public abstract void putValueString(String strVal);

    public abstract Object getValueObject();
}

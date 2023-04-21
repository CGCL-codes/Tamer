package edu.ucsd.ncmir.visualizer.viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author spl
 */
class DisplaySizeModel implements ComboBoxModel {

    private ArrayList<DisplaySize> _sizes = new ArrayList<DisplaySize>();

    private int _selected = 0;

    public DisplaySizeModel(Preferences preferences) {
        int[][] choices = { { 640, 480 }, { 1024, 960 }, { 1280, 1024 } };
        for (int[] choice : choices) this._sizes.add(new DisplaySize(choice));
        this._sizes.add(new DisplaySize("0", "0"));
        DisplaySize selected = new DisplaySize(preferences.get("GameWidth", "640"), preferences.get("GameHeight", "480"));
        this.setSelectedItem(selected);
    }

    public void setSelectedItem(Object o) {
        if (o instanceof DisplaySize) {
            DisplaySize ds = (DisplaySize) o;
            if (ds.isOther()) SizeInputBox.getDimensions(this, this._sizes.get(this._selected)); else {
                if (!this._sizes.contains(ds)) {
                    this._sizes.add(ds);
                    DisplaySize[] sizes = this._sizes.toArray(new DisplaySize[0]);
                    Arrays.sort(sizes);
                    this._sizes.clear();
                    for (DisplaySize size : sizes) this._sizes.add(size);
                    ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, this._sizes.indexOf(ds), this._sizes.indexOf(ds));
                    for (ListDataListener ldn : this._listeners) ldn.contentsChanged(lde);
                }
                this._selected = this._sizes.indexOf(ds);
            }
        }
    }

    public Object getSelectedItem() {
        return this._sizes.get(this._selected);
    }

    public int getSize() {
        return this._sizes.size();
    }

    public Object getElementAt(int index) {
        return this._sizes.get(index);
    }

    private ArrayList<ListDataListener> _listeners = new ArrayList<ListDataListener>();

    public void addListDataListener(ListDataListener l) {
        this._listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        this._listeners.remove(l);
    }
}

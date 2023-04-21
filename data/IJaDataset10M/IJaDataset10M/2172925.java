package net.sf.accolorhelper.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import net.sf.accolorhelper.ColorWithText;

public class ColorListModel implements ListModel {

    public ColorListModel(ColorWithText[] pColors) {
        super();
        aColors.addAll(Arrays.<ColorWithText>asList(pColors));
    }

    public int getSize() {
        return aColors.size();
    }

    public Object getElementAt(int pIndex) {
        return aColors.get(pIndex);
    }

    public List<ColorWithText> getColors() {
        return aColors;
    }

    public void add(Color pColor, String pText) {
        aColors.add(new ColorWithText(pColor, pText));
        for (ListDataListener lListener : aListeners) {
            int lEventIndex;
            lEventIndex = aColors.size() - 1;
            lListener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, lEventIndex, lEventIndex));
        }
    }

    public void set(int pIndex, Color pColor, String pText) {
        aColors.set(pIndex, new ColorWithText(pColor, pText));
        for (ListDataListener lListener : aListeners) {
            lListener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, pIndex, pIndex));
            lListener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pIndex, pIndex));
        }
    }

    public void remove(int pIndex) {
        aColors.remove(pIndex);
        for (ListDataListener lListener : aListeners) {
            lListener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, pIndex, pIndex));
        }
    }

    public void swap(int pFirst, int pSecond) {
        ColorWithText lTemporaryColorForSwapping;
        lTemporaryColorForSwapping = aColors.get(pFirst);
        aColors.set(pFirst, aColors.get(pSecond));
        aColors.set(pSecond, lTemporaryColorForSwapping);
        for (ListDataListener lListener : aListeners) {
            lListener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, pFirst, pSecond));
        }
    }

    public void addListDataListener(ListDataListener pListener) {
        aListeners.add(pListener);
    }

    public void removeListDataListener(ListDataListener pListener) {
        aListeners.remove(pListener);
    }

    private List<ColorWithText> aColors = new ArrayList<ColorWithText>();

    private Set<ListDataListener> aListeners = new HashSet<ListDataListener>();
}

package org.netbeans.api.visual.vmd;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 * This class represents a node anchor. The anchor could be assign by multiple connection widgets.
 * For each usage the anchor resolves a different possition.
 * The position are resolved at the top and the bottom of the widget where the anchor is attached to.
 *
 * @author David Kaspar
 */
public class VMDNodeAnchor extends Anchor {

    private static final int PIN_GAP = 8;

    private boolean requiresRecalculation = true;

    private HashMap<Entry, Result> results = new HashMap<Entry, Result>();

    /**
     * Creates a node anchor.
     * @param widget the node widget where the anchor is attached to
     */
    public VMDNodeAnchor(Widget widget) {
        super(widget);
        assert widget != null;
    }

    /**
     * Notifies when an entry is registered
     * @param entry the registered entry
     */
    protected void notifyEntryAdded(Entry entry) {
        requiresRecalculation = true;
    }

    /**
     * Notifies when an entry is unregistered
     * @param entry the unregistered entry
     */
    protected void notifyEntryRemoved(Entry entry) {
        results.remove(entry);
        requiresRecalculation = true;
    }

    private void recalculate() {
        if (!requiresRecalculation) return;
        Widget widget = getRelatedWidget();
        Point relatedLocation = getRelatedSceneLocation();
        HashMap<Entry, Float> topmap = new HashMap<Entry, Float>();
        HashMap<Entry, Float> bottommap = new HashMap<Entry, Float>();
        for (Entry entry : getEntries()) {
            Point oppositeLocation = getOppositeSceneLocation(entry);
            int dy = oppositeLocation.y - relatedLocation.y;
            int dx = oppositeLocation.x - relatedLocation.x;
            if (dy > 0) bottommap.put(entry, (float) dx / (float) dy); else if (dy < 0) topmap.put(entry, (float) -dx / (float) dy); else topmap.put(entry, dx < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
        }
        Entry[] topList = toArray(topmap);
        Entry[] bottomList = toArray(bottommap);
        Rectangle bounds = widget.convertLocalToScene(widget.getBounds());
        int y = bounds.y;
        int len = topList.length;
        for (int a = 0; a < len; a++) {
            Entry entry = topList[a];
            int x = bounds.x + (a + 1) * bounds.width / (len + 1);
            results.put(entry, new Result(new Point(x, y - PIN_GAP), Direction.TOP));
        }
        y = bounds.y + bounds.height;
        len = bottomList.length;
        for (int a = 0; a < len; a++) {
            Entry entry = bottomList[a];
            int x = bounds.x + (a + 1) * bounds.width / (len + 1);
            results.put(entry, new Result(new Point(x, y + PIN_GAP), Direction.BOTTOM));
        }
    }

    private Entry[] toArray(final HashMap<Entry, Float> map) {
        Set<Entry> keys = map.keySet();
        Entry[] entries = keys.toArray(new Entry[keys.size()]);
        Arrays.sort(entries, new Comparator<Entry>() {

            public int compare(Entry o1, Entry o2) {
                float f = map.get(o1) - map.get(o2);
                if (f > 0.0f) return 1; else if (f < 0.0f) return -1; else return 0;
            }
        });
        return entries;
    }

    /**
     * Computes a result (position and direction) for a specific entry.
     * @param entry the entry
     * @return the calculated result
     */
    public Result compute(Entry entry) {
        recalculate();
        return results.get(entry);
    }
}

package org.dhs.echobase.jcomics.gui;

import org.dhs.echobase.jcomics.data.*;
import java.util.*;
import javax.swing.tree.*;

/**
 * Wrapper class around DefaultMutableTreeNode for holding a Series.
 */
public class SeriesTreeNode extends DefaultMutableTreeNode implements Comparable {

    /**
     * Constructor
     */
    public SeriesTreeNode(Series ser) {
        super(ser);
    }

    /**
     * Retrieves the key for the contained Series.
     *
     * @return The key for this Series, its ID.
     */
    public String getSeriesKey() {
        return ((Series) this.getUserObject()).getKey();
    }

    /**
     * Retrieves the Series in this node.
     *
     * @return The Series stored in this node.
     */
    public Series getSeries() {
        return (Series) this.getUserObject();
    }

    /**
     * Whether this node is a leaf or not.
     *
     * @return false, as we want all Series to appear the same in the list,
     * even if it has no Volumes attached.
     */
    public boolean isLeaf() {
        return false;
    }

    public String toString() {
        return ((Series) this.getUserObject()).toString();
    }

    public void sort() {
        if (!(children == null)) {
            Collections.sort(children);
        }
    }

    public int compareTo(Object obj) {
        if (obj instanceof SeriesTreeNode) {
            return this.toString().compareTo(obj.toString());
        }
        return 0;
    }
}

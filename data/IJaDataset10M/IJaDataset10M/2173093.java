package de.peathal.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class is a helper for classes that wish to fire changes and want to add
 * listener to these changes.
 * 
 * @author Peter Karich
 */
public class FireTo {

    transient Set hs;

    private Set getColl() {
        if (hs == null) hs = new HashSet();
        return hs;
    }

    public void addListener(PropertyChangeListener pcl) {
        getColl().add(pcl);
    }

    public void removeListener(PropertyChangeListener pcl) {
        getColl().remove(pcl);
    }

    protected void fireChangeEvent(PropertyChangeEvent ce) {
        Iterator iter = getColl().iterator();
        while (iter.hasNext()) {
            ((PropertyChangeListener) iter.next()).propertyChange(ce);
        }
    }
}

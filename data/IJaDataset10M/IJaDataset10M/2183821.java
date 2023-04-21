package org.sgmiller.formicidae;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Defines the abstracted interface contract which concrete formicidae models should 
 * implement / represent, as regards available listener services and minimum API set. 
 * 
 * @author scgmille
 * @since 1.3, Jul-1-2004
 */
public abstract class Model {

    /**
   * Our models work via the Listener design pattern, propigating pre-designated
   * events of interest to registered callers.
   */
    protected List changeListeners = new ArrayList();

    /**
   * Simple setter to add a listener.
   * 
   * @param listener
   */
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    /**
   * Removes all previously registered listeners.
   */
    public void removeAllChangeListeners() {
        changeListeners.clear();
    }

    public void fireChangeEvent() {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (Iterator iterator = changeListeners.iterator(); iterator.hasNext(); ) {
            ((ChangeListener) iterator.next()).stateChanged(changeEvent);
        }
    }
}

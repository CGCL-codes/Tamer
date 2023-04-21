package com.nepxion.swing.listener;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public class DisplayAbilityListener implements HierarchyListener {

    /**
	 * Invoked when the hierarchy state is changed.
	 * @param e the instance of HierarchyEvent
	 */
    public void hierarchyChanged(HierarchyEvent e) {
        long changeFlags = e.getChangeFlags();
        if ((changeFlags & HierarchyEvent.DISPLAYABILITY_CHANGED) == HierarchyEvent.DISPLAYABILITY_CHANGED) {
            displayAbilityChanged(e);
        }
    }

    /**
	 * Invoked when the display ability is changed.
	 * @param e the instance of HierarchyEvent
	 */
    public void displayAbilityChanged(HierarchyEvent e) {
    }
}

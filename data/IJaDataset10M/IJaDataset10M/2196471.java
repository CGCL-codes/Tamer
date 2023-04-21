package com.loribel.commons.abstraction;

import javax.swing.JComponent;

/**
 * Interface to manage focus on a specific component in a container.
 */
public interface GB_FocusComponentOwner {

    /**
     * Returns the component to set focus.
     */
    JComponent getFocusComponent();
}

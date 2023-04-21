package com.akivasoftware.misc.domain;

import com.akivasoftware.misc.gui.widgets.IAWidget;

/**
 * Superclass of everything that implements <code>IAWidget</code>
 */
public class AComponent implements IAWidget {

    public void setWidgetIcon(String _iName) throws Exception {
    }

    /**
     * Adds a child reference to a parent
     */
    public void addChild(IAWidget _widget) throws Exception {
    }

    public void setWidgetText(String _text) throws Exception {
    }

    /**
     * Sets the parent reference of a child
     */
    public void setParent(IAWidget _widget) throws Exception {
    }

    public IAWidget endSetup() {
        return (this);
    }
}

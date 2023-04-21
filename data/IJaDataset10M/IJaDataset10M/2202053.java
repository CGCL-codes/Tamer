package com.vircon.myajax.web;

import java.io.Serializable;

public abstract class Region extends XMLOutputHelper implements Serializable {

    /**
	 * @param aChild
	 */
    public Region(Component aChild) {
        super();
        child = aChild;
    }

    public Component getChild() {
        return child;
    }

    public void setChild(Component aChild) {
        child = aChild;
    }

    protected Component child;
}

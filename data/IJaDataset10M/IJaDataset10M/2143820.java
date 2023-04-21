package com.novocode.naf.gui.event;

import java.util.EventObject;

/**
 * Tells a listener that the state of a model has changed.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */
public class ChangeEvent extends EventObject {

    private static final long serialVersionUID = -3177189114307253301L;

    public ChangeEvent(Object source) {
        super(source);
    }
}

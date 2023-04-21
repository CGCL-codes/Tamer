package org.wsmostudio.bpmo.model;

import org.eclipse.draw2d.geometry.Dimension;

public class EndEvent extends Event {

    private static final long serialVersionUID = 1L;

    public EndEvent() {
        setName("End");
    }

    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }
}

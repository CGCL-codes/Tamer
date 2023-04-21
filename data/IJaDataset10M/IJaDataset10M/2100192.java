package com.comarretechnologies.midp.widgets.event;

import com.comarretechnologies.midp.widgets.Component;

/**
 * @author Comarre Technologies
 */
public class ActionEvent {

    private Component source;

    public ActionEvent(Component source) {
        this.source = source;
    }

    public Component getSource() {
        return source;
    }
}

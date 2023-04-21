package org.gridbus.broker.jobdescription.commands;

import java.io.Serializable;

/**
 * @author Xingchen Chu
 *
 */
public class WSInput extends WSParameter {

    private Serializable value = null;

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }
}

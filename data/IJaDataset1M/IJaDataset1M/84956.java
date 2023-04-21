package org.coos.javaframe.gui;

import javax.microedition.lcdui.Form;

/**
 * This class is necessary to be able to simulate beans outside the container.
 * CHANGE: Add other actors to be included in the simulation, and eventual
 * sending of messages to actors.
 */
public class MidletFormWindow extends Form {

    public MidletFormWindow(String s) {
        super(s);
    }
}

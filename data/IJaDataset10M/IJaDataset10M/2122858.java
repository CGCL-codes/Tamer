package org.swingerproject.actions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QuitAction extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent event) {
        event.getWindow().dispose();
    }
}

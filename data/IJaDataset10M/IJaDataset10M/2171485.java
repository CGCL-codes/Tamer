package com.tetrasix.majix.uis;

import java.awt.Component;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class LFlist {

    private static Vector _components = new Vector();

    public static void add(Component component) {
        _components.addElement(component);
    }

    public static void remove(Component component) {
        _components.removeElement(component);
    }

    public static void updateUI() {
        int ii;
        for (ii = 0; ii < _components.size(); ii++) {
            Frame frame = (Frame) _components.elementAt(ii);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        }
    }
}

package com.googlecode.progobots.io;

import java.util.ResourceBundle;
import com.googlecode.progobots.Beepers;
import com.googlecode.progobots.WorldObject;

public class BeepersWriter implements WorldObjectWriter {

    private String label;

    public BeepersWriter() {
        label = ResourceBundle.getBundle("progobots/Progobots").getString("Beepers.label");
    }

    public String getLabel() {
        return label;
    }

    public String write(WorldObject object) {
        Beepers beepers = (Beepers) object;
        if (beepers.getBeepers() == 0) {
            return null;
        }
        return "" + beepers.getLocation() + " " + beepers.getBeepers();
    }
}

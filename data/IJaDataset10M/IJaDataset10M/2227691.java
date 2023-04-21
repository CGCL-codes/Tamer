package com.g3d.display;

import java.awt.GraphicsConfiguration;
import java.awt.Panel;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.VirtualUniverse;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Canvas extends Panel {

    private SimpleUniverse universe = null;

    public Canvas() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        universe = new SimpleUniverse(canvas);
        this.add(canvas);
    }

    public VirtualUniverse getUniverse() {
        return universe;
    }
}

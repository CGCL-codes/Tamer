package net.sourceforge.plantuml.applet;

import java.applet.Applet;
import java.awt.Graphics;
import net.sourceforge.plantuml.version.Version;

public class VersionApplet extends Applet {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("" + Version.version(), 0, 10);
    }
}

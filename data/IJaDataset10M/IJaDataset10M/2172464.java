package net.aviv.javacowboys;

import java.awt.*;
import java.net.*;
import java.applet.*;

public class AppletImageGetter implements ImageGetter {

    URL codeBase;

    Applet applet;

    MediaTracker mediaTracker;

    int id;

    AppletImageGetter(Applet applet, MediaTracker mediaTracker) {
        this.applet = applet;
        codeBase = applet.getCodeBase();
        this.mediaTracker = mediaTracker;
        id = 0;
    }

    public Image getImage(String string) {
        Image out = applet.getImage(codeBase, string);
        mediaTracker.addImage(out, id++);
        return out;
    }
}

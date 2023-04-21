package net.aviv.javacowboys;

import java.awt.*;

public class AppImageGetter implements ImageGetter {

    Toolkit toolkit;

    MediaTracker mediaTracker;

    int id;

    public AppImageGetter(MediaTracker mediaTracker) {
        toolkit = Toolkit.getDefaultToolkit();
        this.mediaTracker = mediaTracker;
        id = 0;
    }

    public Image getImage(String string) {
        Image out = toolkit.getImage("objects\\" + string);
        mediaTracker.addImage(out, id++);
        return out;
    }
}

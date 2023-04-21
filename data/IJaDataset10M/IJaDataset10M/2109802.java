package game.graphics;

import java.awt.Image;

public class Frame {

    private Image image;

    private long delay;

    public Frame(Image image, long delay) {
        this.image = image;
        this.delay = delay;
    }

    public Image getImage() {
        return image;
    }

    public long getDelay() {
        return delay;
    }
}

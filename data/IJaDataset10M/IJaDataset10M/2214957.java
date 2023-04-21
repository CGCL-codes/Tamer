package org.nakedobjects.viewer.lightweight;

import java.awt.Image;

public class Icon {

    Image iconImage;

    public Icon(Image iconImage) {
        if (iconImage == null) {
            throw new NullPointerException();
        }
        this.iconImage = iconImage;
    }

    public int getHeight() {
        return iconImage.getHeight(null);
    }

    public int getWidth() {
        return iconImage.getWidth(null);
    }

    Image getAwtImage() {
        return iconImage;
    }
}

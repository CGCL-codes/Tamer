package org.nakedobjects.application.value;

import org.nakedobjects.application.ApplicationException;

public class Image implements SimpleBusinessValue {

    private int[][] image;

    public Image(final int[][] image) {
        this.image = image;
    }

    public Image() {
        this.image = new int[0][0];
    }

    public Object getValue() {
        return image;
    }

    public void parseUserEntry(final String text) {
    }

    public void restoreFromEncodedString(final String data) {
        throw new ApplicationException();
    }

    public String asEncodedString() {
        throw new ApplicationException();
    }

    public String toString() {
        return "Image [size=" + image.length + "x" + (image.length == 0 || image[0] == null ? 0 : image[0].length) + "]";
    }

    public boolean userChangeable() {
        return false;
    }

    public int[][] getImage() {
        return image;
    }

    public void setImage(final int[][] image) {
        this.image = image;
    }
}

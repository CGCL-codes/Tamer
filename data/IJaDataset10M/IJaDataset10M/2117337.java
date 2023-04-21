package name.huzhenbo.java.serialization;

import java.io.Serializable;

/**
 *
 *
 */
class PersistedBox implements Serializable {

    private static final long serialVersionUID = 41341324132421L;

    private int height;

    private int width;

    private transient int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

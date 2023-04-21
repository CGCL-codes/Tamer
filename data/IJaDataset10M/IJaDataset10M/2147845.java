package com.tinywebgears.tuatara.framework.common;

import java.io.Serializable;

public class Dimension implements Serializable {

    private final int width;

    private final int height;

    public Dimension(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public Dimension(final Dimension dimension) {
        super();
        this.width = dimension.width;
        this.height = dimension.height;
    }

    public Dimension() {
        super();
        this.width = 0;
        this.height = 0;
    }

    /**
     * Creates a Dimension with identical width and height.
     * 
     * @param size
     */
    public Dimension(final int size) {
        super();
        this.width = size;
        this.height = size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Dimension other = (Dimension) obj;
        if (height != other.height) return false;
        if (width != other.width) return false;
        return true;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("width=").append(width);
        sb.append(",").append("height=").append(height);
        sb.append("]");
        return sb.toString();
    }
}

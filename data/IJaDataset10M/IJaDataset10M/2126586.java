package org.saintandreas.serket.impl.av;

import org.saintandreas.serket.device.Icon;

public class SerketIcon implements Icon {

    private final int depth;

    private final int height;

    private final int width;

    private final String mimeType;

    private final String url;

    public SerketIcon(String mimeType, String url, int width, int height, int depth) {
        this.mimeType = mimeType;
        this.url = url;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getURL() {
        return url;
    }
}

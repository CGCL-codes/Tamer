package com.lti.civil.impl.common;

import com.lti.civil.VideoFormat;
import com.lti.civil.utility.VideoFormatNames;

/**
 * Default implementation of {@link VideoFormat}.
 * @author Ken Larson
 *
 */
public class VideoFormatImpl implements VideoFormat {

    private final int formatType;

    private final int width;

    private final int height;

    private final float fps;

    private final int dataType;

    public VideoFormatImpl(final int formatType, final int width, final int height, final float fps, final int dataType) {
        super();
        this.formatType = formatType;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.dataType = dataType;
    }

    public int getFormatType() {
        return formatType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getFPS() {
        return fps;
    }

    public int getDataType() {
        return dataType;
    }

    public boolean equals(Object obj) {
        if (obj instanceof VideoFormatImpl) {
            VideoFormatImpl vf = (VideoFormatImpl) obj;
            return (vf.formatType == formatType) && (vf.width == width) && (vf.height == height) && (vf.dataType == dataType);
        }
        return false;
    }

    public String toString() {
        return width + " x " + height + " (" + VideoFormatNames.formatTypeToString(formatType) + ")" + " (" + dataType + ")";
    }
}

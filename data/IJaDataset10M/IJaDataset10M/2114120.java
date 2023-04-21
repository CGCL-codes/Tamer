package org.skunk.swing.text.syntax;

import org.skunk.util.GappedIntArray;

public final class StyleBufferUtilities {

    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StyleBufferUtilities.class);

    public static final int STYLE_BUFFER_STYLE_MASK = 31;

    public static final int STYLE_BUFFER_STATE_MASK = 992;

    public static final int getState(int styleBufferEntry) {
        return (styleBufferEntry & STYLE_BUFFER_STATE_MASK) >> 5;
    }

    public static final int getStyle(int styleBufferEntry) {
        return (styleBufferEntry & STYLE_BUFFER_STYLE_MASK);
    }

    public static final int applyStyle(GappedIntArray styleBuffer, int style, int state, int offset, int length) {
        int type = style | (state << 5);
        if (styleBuffer != null) {
            int[] tmpBuff = new int[length];
            for (int i = 0; i < length; i++) {
                tmpBuff[i] = type;
            }
            if (styleBuffer.length() > offset + length) {
                styleBuffer.set(offset, tmpBuff);
            } else {
                log.trace("applyStyle tried to set style past end of style buffer");
            }
        }
        return type;
    }

    private StyleBufferUtilities() {
    }
}

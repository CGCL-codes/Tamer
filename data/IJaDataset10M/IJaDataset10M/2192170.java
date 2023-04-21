package com.jswiff.swfrecords.tags;

import com.jswiff.io.OutputBitStream;

/**
 * This tag instructs Flash Player to display all characters added  (either
 * with <code>PlaceObject</code> or <code>PlaceObject2</code>) to the display
 * list. The display list is cleared, and the movie is paused for the duration
 * of a single frame (which is the reciprocal of the SWF frame rate).
 *
 * @since SWF 1
 */
public final class ShowFrame extends Tag {

    /**
   * Creates a new ShowFrame tag.
   */
    public ShowFrame() {
        code = TagConstants.SHOW_FRAME;
    }

    protected void writeData(OutputBitStream outStream) {
    }

    void setData(byte[] data) {
    }
}

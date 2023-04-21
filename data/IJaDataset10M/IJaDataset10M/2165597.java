package org.axsl.areaW;

import org.axsl.text.line.LineOutput;

/**
 * Area containing a line.
 */
public interface LineArea extends BlockArea, LineContentFactory {

    /**
     * Return <code>this</code> cast as a LineOutput implementation.
     * @return This cast as a LineOuput implementation.
     */
    LineOutput asLineOutput();

    /**
     * Indicates whether this line is empty or has content.
     * @return True iff this line has no content.
     */
    boolean isEmpty();
}

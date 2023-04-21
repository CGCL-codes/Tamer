package org.jd3lib;

import java.nio.ByteBuffer;

/**
 * @author Andreas Grunewald
 * 
 * TIT3 The 'Subtitle/Description refinement' frame is used for information
 * directly related to the contents title (e.g. "Op. 16" or "Performed live at
 * Wembley").
 */
public class Id3FrameTIT3 extends Id3FrameTBase {

    /**
   * @param frameData
   * @param header
   */
    public Id3FrameTIT3(ByteBuffer frameData, Id3v2FrameHeader header) {
        super(frameData, header);
    }
}

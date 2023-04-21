package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

/**
 * Conductor Text information frame.
 * <p>The 'Conductor' frame is used for the name of the conductor.
 * 
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 * 
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id: FrameBodyTPE3.java,v 1.9 2006/08/25 15:35:26 paultaylor Exp $
 */
public class FrameBodyTPE3 extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTPE3 datatype.
     */
    public FrameBodyTPE3() {
    }

    public FrameBodyTPE3(FrameBodyTPE3 body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTPE3 datatype.
     *
     * @param textEncoding 
     * @param text         
     */
    public FrameBodyTPE3(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTPE3 datatype.
     *
      * @throws java.io.IOException 
     * @throws InvalidTagException 
     */
    public FrameBodyTPE3(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_CONDUCTOR;
    }
}

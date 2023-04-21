package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

public class FrameBodyTDRL extends AbstractFrameBodyTextInfo implements ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTDRL datatype.
     */
    public FrameBodyTDRL() {
    }

    public FrameBodyTDRL(FrameBodyTDRL body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTDRL datatype.
     *
     * @param textEncoding 
     * @param text         
     */
    public FrameBodyTDRL(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTDRL datatype.
     *    
     * @throws InvalidTagException 
     */
    public FrameBodyTDRL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_RELEASE_TIME;
    }
}

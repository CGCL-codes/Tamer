package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

public class FrameBodyTSST extends AbstractFrameBodyTextInfo implements ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTSST datatype.
     */
    public FrameBodyTSST() {
    }

    public FrameBodyTSST(FrameBodyTSST body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTSST datatype.
     *
     * @param textEncoding 
     * @param text         
     */
    public FrameBodyTSST(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTSST datatype.
     *
     * @throws java.io.IOException 
     * @throws InvalidTagException 
     */
    public FrameBodyTSST(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_SET_SUBTITLE;
    }
}

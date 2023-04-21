package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

public class FrameBodyTMCL extends AbstractFrameBodyTextInfo implements ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTMCL datatype.
     */
    public FrameBodyTMCL() {
    }

    public FrameBodyTMCL(FrameBodyTMCL body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTMCL datatype.
     *
     * @param textEncoding
     * @param text
     */
    public FrameBodyTMCL(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTMCL datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    public FrameBodyTMCL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_MUSICIAN_CREDITS;
    }
}

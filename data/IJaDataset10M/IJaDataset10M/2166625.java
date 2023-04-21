package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

public class FrameBodyTSRC extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTSRC datatype.
     */
    public FrameBodyTSRC() {
    }

    public FrameBodyTSRC(FrameBodyTSRC body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTSRC datatype.
     *
     * @param textEncoding
     * @param text
     */
    public FrameBodyTSRC(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTSRC datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    public FrameBodyTSRC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_ISRC;
    }
}

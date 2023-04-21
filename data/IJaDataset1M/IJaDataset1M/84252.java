package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

/**
 * Track number/Position in set Text information frame.
 * <p>The 'Track number/Position in set' frame is a numeric string containing the order number of the audio-file on its original recording. This may be extended with a "/" character and a numeric string containing the total numer of tracks/elements on the original recording. E.g. "4/9".
 * 
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 * 
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id: FrameBodyTRCK.java,v 1.9 2006/08/25 15:35:26 paultaylor Exp $
 */
public class FrameBodyTRCK extends AbstractFrameBodyTextInfo implements ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTRCK datatype.
     */
    public FrameBodyTRCK() {
    }

    public FrameBodyTRCK(FrameBodyTRCK body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param textEncoding 
     * @param text         
     */
    public FrameBodyTRCK(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @throws java.io.IOException 
     * @throws InvalidTagException 
     */
    public FrameBodyTRCK(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_TRACK;
    }
}

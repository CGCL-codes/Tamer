package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

/**
 * Internet radio station owner Text information frame.
 * <p>The 'Internet radio station owner' frame contains the name of the owner of the internet radio station from which the audio is streamed.
 * 
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 * 
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id: FrameBodyTRSO.java,v 1.9 2006/08/25 15:35:27 paultaylor Exp $
 */
public class FrameBodyTRSO extends AbstractFrameBodyTextInfo implements ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTRSO datatype.
     */
    public FrameBodyTRSO() {
    }

    public FrameBodyTRSO(FrameBodyTRSO body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTRSO datatype.
     *
     * @param textEncoding 
     * @param text         
     */
    public FrameBodyTRSO(byte textEncoding, String text) {
        super(textEncoding, text);
    }

    /**
     * Creates a new FrameBodyTRSO datatype.
     *
     * @throws java.io.IOException 
     * @throws InvalidTagException 
     */
    public FrameBodyTRSO(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_RADIO_OWNER;
    }
}

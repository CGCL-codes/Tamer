package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

/**
 * Official audio file webpage URL link frames.
 * <p>The 'Official audio file webpage' frame is a URL pointing at a file specific webpage.
 * 
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 * 
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id: FrameBodyWOAF.java,v 1.10 2007/06/27 11:49:23 paultaylor Exp $
 */
public class FrameBodyWOAF extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyWOAF datatype.
     */
    public FrameBodyWOAF() {
    }

    /**
     * Creates a new FrameBodyWOAF datatype.
     *
     * @param urlLink 
     */
    public FrameBodyWOAF(String urlLink) {
        super(urlLink);
    }

    public FrameBodyWOAF(FrameBodyWOAF body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyWOAF datatype.
     *   
     * @throws InvalidTagException 
     */
    public FrameBodyWOAF(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_URL_FILE_WEB;
    }
}

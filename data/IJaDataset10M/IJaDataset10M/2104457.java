package com.hadeslee.audiotag.tag.id3.framebody;

import com.hadeslee.audiotag.tag.InvalidTagException;
import com.hadeslee.audiotag.tag.datatype.ByteArraySizeTerminated;
import com.hadeslee.audiotag.tag.datatype.DataTypes;
import com.hadeslee.audiotag.tag.id3.ID3v24Frames;
import java.nio.ByteBuffer;

public class FrameBodyRVA2 extends AbstractID3v2FrameBody implements ID3v24FrameBody {

    /**
     * Creates a new FrameBodyRVA2 datatype.
     */
    public FrameBodyRVA2() {
    }

    public FrameBodyRVA2(FrameBodyRVA2 body) {
        super(body);
    }

    /**
     * Convert from V3 to V4 Frame
     */
    public FrameBodyRVA2(FrameBodyRVAD body) {
        setObjectValue(DataTypes.OBJ_DATA, body.getObjectValue(DataTypes.OBJ_DATA));
    }

    /**
     * Creates a new FrameBodyRVAD datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    public FrameBodyRVA2(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2;
    }

    /**
     * Setup the Object List. A byte Array which will be read upto frame size
     * bytes.
     */
    protected void setupObjectList() {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}

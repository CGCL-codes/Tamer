package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Represents a user defined url
 */
public class FrameBodyWXXX extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody {

    public static final String URL_DISCOGS_RELEASE_SITE = "DISCOGS_RELEASE";

    public static final String URL_WIKIPEDIA_RELEASE_SITE = "WIKIPEDIA_RELEASE";

    public static final String URL_OFFICIAL_RELEASE_SITE = "OFFICIAL_RELEASE";

    public static final String URL_DISCOGS_ARTIST_SITE = "DISCOGS_ARTIST";

    public static final String URL_WIKIPEDIA_ARTIST_SITE = "WIKIPEDIA_ARTIST";

    public static final String URL_LYRICS_SITE = "LYRICS_SITE";

    /**
     * Creates a new FrameBodyWXXX datatype.
     */
    public FrameBodyWXXX() {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_URLLINK, "");
    }

    public FrameBodyWXXX(FrameBodyWXXX body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyWXXX datatype.
     *
     * @param textEncoding
     * @param description
     * @param urlLink
     */
    public FrameBodyWXXX(byte textEncoding, String description, String urlLink) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_URLLINK, urlLink);
    }

    /**
     * Creates a new FrameBodyWXXX datatype by reading from file.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    public FrameBodyWXXX(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    /**
     * Set a description of the hyperlink
     *
     * @param description
     */
    public void setDescription(String description) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * @return a description of the hyperlink
     */
    public String getDescription() {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier() {
        return ID3v24Frames.FRAME_ID_USER_DEFINED_URL;
    }

    /**
     * If the description cannot be encoded using the current encoding change the encoder
     */
    public void write(ByteArrayOutputStream tagBuffer) {
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded()) {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }

    /**
     * This is different ot other URL Links
     */
    protected void setupObjectList() {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_URLLINK, this));
    }
}

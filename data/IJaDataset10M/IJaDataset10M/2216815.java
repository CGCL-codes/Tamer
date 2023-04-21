package de.vdheide.mp3;

/**
 * Used for text ID3v2 frames which support encoding.
 * Supports get and set operations
 */
public class TextFrameEncoding extends TextFrame {

    /**
     * Creates a new TextFrameEncoding with a given content
     * 
     * @param id3v2 ID3v2 tag
     * @param type Type of frame
     * @param content TagContent to write
     * @param use_compression Use compression?
     * @exception TagFormatException If text content field is null
     */
    public TextFrameEncoding(ID3v2 id3v2, String type, TagContent content, boolean use_compression) throws TagFormatException {
        super(true, id3v2, type, content, use_compression);
    }

    /**
     * Read content from ID3v2 tag.
     *
     * @param encoding Use encoding?
     * @param id3v2 ID3v2 tag to read from
     * @param type Type of frame to read
     * @exception FrameDamagedException If frame is damaged (e.g. too short)
     */
    public static TagContent read(ID3v2 id3v2, String type) throws FrameDamagedException {
        return read(true, id3v2, type);
    }
}

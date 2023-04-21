package com.jswiff.swfrecords.tags;

import java.io.IOException;
import com.jswiff.io.InputBitStream;

public interface TagReader {

    /**
	 * Reads a tag from a data buffer. The tag header must be parsed before
	 * invoking this method.
	 *
	 * @param header tag header
	 * @param tagData data buffer containing the tag to be read
	 * @param swfVersion flash version (from the SWF file header)
	 * @param japanese specifies whether japanese encoding is to be used for strings
	 *
	 * @return the read tag
	 *
	 * @throws IOException if I/O problems occur
	 */
    public Tag readTag(TagHeader header, byte[] tagData, short swfVersion, boolean japanese) throws IOException;

    /**
	 * Reads a tag from a bit stream as raw data. The tag header must be read
	 * before invoking this method.
	 *
	 * @param stream source bit stream
	 * @param header tag header
	 *
	 * @return tag as data buffer
	 *
	 * @throws IOException if an I/O error occured
	 */
    public byte[] readTagData(InputBitStream stream, TagHeader header) throws IOException;

    /**
	 * Reads a tag header from a bit stream.
	 *
	 * @param stream source bit stream
	 *
	 * @return the parsed tag header
	 *
	 * @throws IOException if an I/O error occured
	 */
    public TagHeader readTagHeader(InputBitStream stream) throws IOException;

    /**
	 * Reads a tag from a bit stream.
	 */
    public Tag readTag(InputBitStream stream, short swfVersion, boolean shiftJIS) throws IOException;
}

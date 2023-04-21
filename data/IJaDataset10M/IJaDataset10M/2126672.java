package net.jxta.endpoint;

import java.nio.ByteBuffer;
import net.jxta.document.Document;
import net.jxta.document.DocumentByteBufferIO;
import net.jxta.document.MimeMediaType;

/**
 *  A wire serialization of an abstract message.
 *
 *  @see net.jxta.endpoint.Message
 *  @see net.jxta.endpoint.WireFormatMessageFactory
 */
public interface WireFormatMessage extends Document, DocumentByteBufferIO {

    /**
     * Returns the encoding used for this content. May be {@code null} for 
     * unencoded (raw) content.
     *
     *  @return The encoding used for this message.
     */
    MimeMediaType getContentEncoding();

    /**
     * Returns the size of the serialized and encoded form of the message in bytes.
     *
     * @return The size of the serialized and encoded message in bytes.
     */
    long getByteLength();
}

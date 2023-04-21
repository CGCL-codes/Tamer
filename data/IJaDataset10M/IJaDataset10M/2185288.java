package com.threerings.s3.client;

import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

/**
 * A representation of a single object stored in S3.
 */
public abstract class S3Object extends S3Metadata {

    /**
     * Instantiate an S3 object with the given key.
     * @param key S3 object key.
     */
    public S3Object(String key) {
        this(key, DEFAULT_MEDIA_TYPE);
    }

    /**
     * Instantiate an S3 Object with the given key and media type.
     * @param key S3 object key.
     * @param mediaType Object's media type.
     */
    public S3Object(String key, MediaType mediaType) {
        this(key, mediaType, new HashMap<String, String>());
    }

    /**
     * Instantiate an S3 Object with the given key, media type, and metadata.
     * @param key S3 object key.
     * @param mediaType Object's media type.
     * @param metadata Object's metadata. Metadata keys must be a single, ASCII
     *     string, and may not contain spaces. Metadata values must also be ASCII,
     *     and any leading or trailing spaces may be stripped.
     */
    public S3Object(String key, MediaType mediaType, Map<String, String> metadata) {
        super(key, mediaType, metadata);
    }

    /**
     * Get the object's input stream, used to read object contents, potentially
     * from the remote S3 server. The caller is responsible for closing the
     * stream.
     */
    public abstract InputStream getInputStream() throws S3ClientException;
}

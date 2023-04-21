package com.curl.orb.type;

import java.util.Map;
import java.util.HashMap;

/**
 * A serializable file class to download and upload.
 * 
 * @author Hitoshi Okada
 * @since 0.5
 */
public abstract class AbstractSerializableFile implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public final transient String RESERVED_KEY_NAME = "name";

    public final transient String RESERVED_KEY_SIZE = "size";

    protected final Map<Object, Object> fileDescriptor;

    public AbstractSerializableFile() {
        fileDescriptor = new HashMap<Object, Object>();
    }

    public Map<Object, Object> getFileDescriptor() {
        return fileDescriptor;
    }

    public abstract Object getContent();

    public abstract void setContent(Object content);

    public abstract void write(String url) throws DataTypeException;
}

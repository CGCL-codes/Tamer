package org.gecko.jee.community.mobidick.resource;

import org.springframework.core.io.ByteArrayResource;

public class ResourceByteArray extends ByteArrayResource implements Resource {

    public ResourceByteArray(final byte[] byteArray) {
        super(byteArray);
    }

    public ResourceByteArray(final byte[] byteArray, final String description) {
        super(byteArray, description);
    }
}

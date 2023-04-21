package org.tagbox.engine.resource;

import org.tagbox.engine.TagBoxException;

public class ResourceException extends TagBoxException {

    private Resource resource;

    public ResourceException(Resource resource, String msg) {
        super(msg);
        this.resource = resource;
    }

    public ResourceException(Resource resource, Throwable cause) {
        super(cause);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}

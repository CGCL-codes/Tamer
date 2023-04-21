package com.esri.gpt.framework.resource.common;

import com.esri.gpt.framework.resource.api.SourceUri;
import com.esri.gpt.framework.util.UuidUtil;

/**
 * UUID-based source URI.
 */
public class UuidUri implements SourceUri {

    /** uuid */
    private String uuid;

    /**
 * Creates instance of the source URI.
 * @param uuid base uuid
 */
    public UuidUri(String uuid) {
        this.uuid = UuidUtil.isUuid(uuid) ? UuidUtil.addCurlies(uuid) : "";
    }

    @Override
    public boolean equals(Object sourceUri) {
        if (UuidUtil.isUuid(uuid) && sourceUri instanceof UuidUri) {
            return uuid.equalsIgnoreCase(((UuidUri) sourceUri).uuid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String asString() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }
}

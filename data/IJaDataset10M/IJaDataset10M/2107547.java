package org.xblackcat.rojac.data;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ASUS
 */
public final class Version {

    private final byte[] base64Version;

    public Version(byte[] base64Version) {
        this.base64Version = base64Version;
    }

    public Version() {
        base64Version = ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    public byte[] getBytes() {
        return base64Version.clone();
    }

    private String getAsString() {
        if (ArrayUtils.isEmpty(base64Version)) {
            return "empty";
        }
        StringBuilder res = new StringBuilder();
        for (byte b : base64Version) {
            res.append(String.format(",%02x", b & 0xff));
        }
        return res.substring(1);
    }

    public String toString() {
        return "Version[id=" + getAsString() + ']';
    }

    public boolean isEmpty() {
        if (ArrayUtils.isEmpty(base64Version)) {
            return true;
        } else {
            for (byte b : base64Version) {
                if (b != 0) {
                    return false;
                }
            }
            return true;
        }
    }
}

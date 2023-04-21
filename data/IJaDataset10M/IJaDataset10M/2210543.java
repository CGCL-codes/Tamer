package de.schlund.pfixxml;

/**
 * RequestParamType.java
 *
 *
 * Created: Tue May 14 20:47:08 2002
 *
 * @author <a href="mailto:jtl@schlund.de">Jens Lautenbacher</a>
 *
 *
 */
public class RequestParamType {

    private String tag;

    public static final RequestParamType SIMPLE = new RequestParamType("SIMPLE");

    public static final RequestParamType FIELDDATA = new RequestParamType("MULTIPART/FIELD");

    public static final RequestParamType FILEDATA = new RequestParamType("MULTIPART/FILE");

    private RequestParamType() {
    }

    private RequestParamType(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "[RequestParamType " + tag + "]";
    }
}

package mr3.data;

import java.io.*;

public class URIType implements Serializable {

    private String type;

    private URIType(String t) {
        type = t;
    }

    public static final URIType URI = new URIType("URI");

    public static final URIType ID = new URIType("ID");

    public static final URIType ANONYMOUS = new URIType("ANONYMOUS");

    public static URIType getURIType(String t) {
        if (t.equals("URI")) {
            return URI;
        } else if (t.equals("ID")) {
            return ID;
        } else if (t.equals("ANONYMOUS")) {
            return ANONYMOUS;
        }
        assert false : "invalid type";
        return null;
    }

    public String toString() {
        return type;
    }
}

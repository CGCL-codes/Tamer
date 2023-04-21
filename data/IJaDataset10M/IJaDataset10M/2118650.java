package up5.mi.visio.B2B;

import java.util.Vector;

public class Constant {

    public static Vector<String> RESERVED_HEADERS = new Vector<String>();

    static {
        Constant.RESERVED_HEADERS.add("Route");
        Constant.RESERVED_HEADERS.add("Max-Forwards");
        Constant.RESERVED_HEADERS.add("Record-Route");
        Constant.RESERVED_HEADERS.add("Content-Length");
        Constant.RESERVED_HEADERS.add("Contact");
        Constant.RESERVED_HEADERS.add("From");
        Constant.RESERVED_HEADERS.add("To");
        Constant.RESERVED_HEADERS.add("Cseq");
        Constant.RESERVED_HEADERS.add("Via");
        Constant.RESERVED_HEADERS.add("Call-Id");
    }
}

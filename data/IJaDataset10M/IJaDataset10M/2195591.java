package tristero.util;

import java.net.*;

public class MyURLEncoder {

    public String encode(Object o) {
        return URLEncoder.encode(o.toString());
    }
}

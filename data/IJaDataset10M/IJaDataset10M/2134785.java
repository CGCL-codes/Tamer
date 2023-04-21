package org.yajul.net.http;

import java.io.IOException;

/**
 * TODO: Add class javadoc
 * User: josh
 * Date: Jan 25, 2004
 * Time: 6:19:54 PM
 */
public class ResponseHeader extends MessageHeader {

    public void read(HTTPInputStream in) throws IOException {
        read(in, true);
    }
}

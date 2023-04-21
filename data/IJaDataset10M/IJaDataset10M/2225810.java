package org.xsocket.connection;

import org.junit.Assert;
import org.junit.Test;
import org.xsocket.connection.BlockingConnection;
import org.xsocket.connection.IBlockingConnection;

/**
*
* @author grro@xsocket.org
*/
public final class FlushTest {

    @Test
    public void testLiveFlush() throws Exception {
        IBlockingConnection con = new BlockingConnection("www.web.de", 80);
        con.setAutoflush(false);
        con.write("GET / HTTP/1.1\r\n");
        con.write("Host: www.web.de\r\n");
        con.write("\r\n");
        con.flush();
        String rawHeader = con.readStringByDelimiter("\r\n");
        Assert.assertTrue(rawHeader.indexOf("200") != -1);
        con.close();
    }
}

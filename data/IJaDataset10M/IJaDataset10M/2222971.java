package com.softaspects.jsf.component.base.support;

import javax.servlet.ServletInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Igor
 * Date: 31.08.2008
 * Time: 23:26:08
 * To change this template use File | Settings | File Templates.
 */
class MultipartInputStreamHandler {

    ServletInputStream in;

    int totalExpected;

    int totalRead = 0;

    byte[] buf = new byte[8 * 1024];

    public MultipartInputStreamHandler(ServletInputStream in, int totalExpected) {
        this.in = in;
        this.totalExpected = totalExpected;
    }

    public String readLine() throws IOException {
        StringBuffer sbuf = new StringBuffer();
        int result;
        String line;
        do {
            result = this.readLine(buf, 0, buf.length);
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, "ISO-8859-1"));
            }
        } while (result == buf.length);
        if (sbuf.length() == 0) {
            return null;
        }
        sbuf.setLength(sbuf.length() - 2);
        return sbuf.toString();
    }

    public int readLine(byte b[], int off, int len) throws IOException {
        if (totalRead >= totalExpected) {
            return -1;
        } else {
            int result = in.readLine(b, off, len);
            if (result > 0) {
                totalRead += result;
            }
            return result;
        }
    }
}

package com.googlecode.httl.support.util;

import java.io.IOException;
import java.io.Reader;

/**
 * IOUtils. (Tool, Static, ThreadSafe)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class IOUtils {

    public static String readToString(Reader reader) throws IOException {
        StringBuffer buffer = new StringBuffer();
        char[] buf = new char[8192];
        int len = 0;
        while ((len = reader.read(buf)) != -1) {
            buffer.append(buf, 0, len);
        }
        return buffer.toString();
    }

    private IOUtils() {
    }
}

package com.jformdesigner.runtime;

import java.io.IOException;
import java.io.InputStream;

/**
 * A input stream that replaces JFormDesigner 1.0 class names on the fly to
 * 2.0 class names.
 * 
 * @author Karl Tauber
 */
class V1FilterInputStream extends InputStream {

    private static final byte[][] SEARCH_REPLACE = { "class=\"com.ktauber.form.model.".getBytes(), "class=\"com.jformdesigner.model.".getBytes(), "<class>com.ktauber.form.runtime.".getBytes(), "<class>com.jformdesigner.runtime.".getBytes(), "string>com.ktauber.form.wrapper.JGoodiesForms".getBytes(), "string>com.jformdesigner.designer.wrapper.JGoodiesForms".getBytes() };

    private InputStream in;

    private int[] firstSearchBytes;

    private byte[] buf;

    private int bufCount;

    private int bufPos = -1;

    private byte[] searchBuf;

    private byte[] replaceBuf;

    private int replacePos = -1;

    V1FilterInputStream(InputStream in) {
        this.in = in;
        firstSearchBytes = new int[SEARCH_REPLACE.length / 2];
        int maxLen = 0;
        for (int i = 0; i < SEARCH_REPLACE.length; i += 2) {
            firstSearchBytes[i / 2] = SEARCH_REPLACE[i][0];
            maxLen = Math.max(maxLen, SEARCH_REPLACE[i].length);
        }
        buf = new byte[maxLen];
    }

    public int read() throws IOException {
        if (replacePos >= 0) {
            int ch = replaceBuf[replacePos++];
            if (replacePos >= replaceBuf.length) replacePos = -1;
            return ch;
        }
        int ch;
        if (bufPos < 0) {
            ch = in.read();
            if (ch == -1) return -1;
        } else {
            ch = buf[bufPos++];
            if (bufPos >= bufCount) {
                bufPos = -1;
                bufCount = 0;
            }
        }
        int index = -1;
        for (int i = 0; i < firstSearchBytes.length; i++) {
            if (ch == firstSearchBytes[i]) {
                index = i * 2;
                break;
            }
        }
        if (index < 0) return ch;
        searchBuf = SEARCH_REPLACE[index];
        replaceBuf = SEARCH_REPLACE[index + 1];
        int searchPos = 1;
        if (bufCount <= 0) buf[bufCount++] = (byte) ch; else {
            buf[0] = (byte) ch;
            System.arraycopy(buf, bufPos, buf, 1, bufCount - bufPos);
            bufCount = bufCount - bufPos + 1;
            bufPos = 1;
            for (int i = 1; i < searchBuf.length && i < bufCount; i++) {
                if (buf[i] != searchBuf[i]) break;
                searchPos++;
            }
        }
        for (int i = searchPos; i < searchBuf.length; i++) {
            int ch2 = in.read();
            if (ch2 == -1) break;
            buf[bufCount++] = (byte) ch2;
            if (ch2 != searchBuf[i]) break;
            searchPos++;
        }
        if (searchPos == searchBuf.length) {
            if (bufCount == searchBuf.length) {
                bufCount = 0;
                bufPos = -1;
            } else {
                bufPos = searchBuf.length;
            }
            replacePos = 0;
            return replaceBuf[replacePos++];
        }
        if (bufCount == 1) {
            bufCount = 0;
            bufPos = -1;
            return ch;
        }
        bufPos = 0;
        return buf[bufPos++];
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        in.close();
    }
}

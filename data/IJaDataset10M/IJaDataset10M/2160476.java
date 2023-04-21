package com.substanceofcode.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * InputStream wrapper so that we can get the total amount of bytes transferred
 * count.
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class CustomInputStream {

    private InputStream stream;

    private InputStreamReader reader;

    private StringBuffer inputBuffer;

    public CustomInputStream(InputStream stream) {
        inputBuffer = new StringBuffer();
        this.stream = stream;
        try {
            reader = new InputStreamReader(stream, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            reader = new InputStreamReader(stream);
        }
    }

    public int read() throws IOException {
        HttpTransferStatus.addReceivedBytes(1);
        int next = reader.read();
        inputBuffer.append((char) next);
        return next;
    }

    public String getText() {
        return inputBuffer.toString();
    }

    public void close() throws IOException {
        stream.close();
    }
}

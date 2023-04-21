package htmlCanvas;

import context.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HtmlTokenizer implements Debug.Constants {

    private int pos = 0;

    private byte[] bytes;

    public HtmlTokenizer(String input) {
        this.bytes = input.getBytes();
    }

    public HtmlTokenizer(byte[] bytes) {
        this.bytes = bytes;
    }

    public String next() {
        StringBuffer buf = new StringBuffer();
        try {
            while (isWhitespace(bytes[pos])) pos++;
            while (!isWhitespace(bytes[pos])) buf.append((char) bytes[pos++]);
        } catch (Exception e) {
        }
        return (buf.toString());
    }

    private boolean isWhitespace(byte b) {
        switch(b) {
            case ' ':
            case '\r':
            case '\n':
            case '\t':
                return (true);
        }
        return (false);
    }
}

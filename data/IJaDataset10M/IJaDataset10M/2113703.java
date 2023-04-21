package com.sun.mail.util;

import java.io.*;
import javax.mail.MessagingException;

/**
 * This class is to support writing out Strings as a sequence of bytes
 * terminated by a CRLF sequence. The String must contain only US-ASCII
 * characters.<p>
 *
 * The expected use is to write out RFC822 style headers to an output
 * stream. <p>
 *
 * @author John Mani
 */
public class LineOutputStream extends FilterOutputStream {

    private static byte[] newline;

    static {
        newline = new byte[2];
        newline[0] = (byte) '\r';
        newline[1] = (byte) '\n';
    }

    public LineOutputStream(OutputStream out) {
        super(out);
    }

    public void writeln(String s) throws MessagingException {
        try {
            byte[] bytes = ASCIIUtility.getBytes(s);
            out.write(bytes);
            out.write(newline);
        } catch (Exception ex) {
            throw new MessagingException("IOException", ex);
        }
    }

    public void writeln() throws MessagingException {
        try {
            out.write(newline);
        } catch (Exception ex) {
            throw new MessagingException("IOException", ex);
        }
    }
}

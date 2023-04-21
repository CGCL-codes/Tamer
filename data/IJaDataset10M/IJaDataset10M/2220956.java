package org.tolven.security.auth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.util.Arrays;

/**
 * A utility class for reading passwords
 *
 */
public class Password {

    /** Reads user password from given input stream. */
    public static char[] readPassword(InputStream in) {
        char[] consoleEntered = null;
        byte[] consoleBytes = null;
        try {
            Console con = null;
            if (in == System.in && ((con = System.console()) != null)) {
                consoleEntered = con.readPassword();
                if (consoleEntered != null && consoleEntered.length == 0) {
                    return null;
                }
                consoleBytes = convertToBytes(consoleEntered);
                in = new ByteArrayInputStream(consoleBytes);
            }
            char[] lineBuffer;
            char[] buf;
            buf = lineBuffer = new char[128];
            int room = buf.length;
            int offset = 0;
            int c;
            boolean done = false;
            while (!done) {
                switch(c = in.read()) {
                    case -1:
                    case '\n':
                        done = true;
                        break;
                    case '\r':
                        int c2 = in.read();
                        if ((c2 != '\n') && (c2 != -1)) {
                            if (!(in instanceof PushbackInputStream)) {
                                in = new PushbackInputStream(in);
                            }
                            ((PushbackInputStream) in).unread(c2);
                        } else {
                            done = true;
                            break;
                        }
                    default:
                        if (--room < 0) {
                            buf = new char[offset + 128];
                            room = buf.length - offset - 1;
                            System.arraycopy(lineBuffer, 0, buf, 0, offset);
                            Arrays.fill(lineBuffer, ' ');
                            lineBuffer = buf;
                        }
                        buf[offset++] = (char) c;
                        break;
                }
            }
            if (offset == 0) {
                return null;
            }
            char[] ret = new char[offset];
            System.arraycopy(buf, 0, ret, 0, offset);
            Arrays.fill(buf, ' ');
            return ret;
        } catch (IOException ex) {
            throw new RuntimeException("Could not read password from InputStream", ex);
        } finally {
            if (consoleEntered != null) {
                Arrays.fill(consoleEntered, ' ');
            }
            if (consoleBytes != null) {
                Arrays.fill(consoleBytes, (byte) 0);
            }
        }
    }

    /**
     * Change a password read from Console.readPassword() into
     * its original bytes.
     *
     * @param pass a char[]
     * @return its byte[] format, similar to new String(pass).getBytes()
     */
    private static byte[] convertToBytes(char[] pass) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = null;
            try {
                outputStreamWriter = new OutputStreamWriter(baos);
                outputStreamWriter.write(pass, 0, pass.length);
            } finally {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Could not convert char[] to byte[]", ex);
        }
    }
}

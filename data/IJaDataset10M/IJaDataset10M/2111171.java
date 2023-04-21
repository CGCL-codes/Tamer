package org.garret.ptl.startup;

import java.io.*;

/**
 * Redirects System.out or System.err to logger stream.
 *
 * @author Andrey Subbotin
 */
public class RedirectingPrintStream extends PrintStream {

    public RedirectingPrintStream(boolean isErr) throws UnsupportedEncodingException {
        super(new RedirectingOutputStream(isErr), true, "UTF-8");
    }

    private static class RedirectingOutputStreamUtf16 extends OutputStream {

        private final StringBuffer lineBuffer = new StringBuffer();

        private final boolean isErr;

        private int count = 0;

        private int unicodeStart;

        RedirectingOutputStreamUtf16(boolean isErr) {
            this.isErr = isErr;
        }

        public void write(int b) throws IOException {
            if (count++ % 2 == 0) {
                unicodeStart = b;
                return;
            } else {
                b = (unicodeStart << 8) | b;
            }
            if (count < 2) {
            } else if (b == 10) {
                if (isErr) {
                    Main.s_logger.error(lineBuffer.toString());
                } else {
                    Main.s_logger.info(lineBuffer.toString());
                }
                lineBuffer.setLength(0);
            } else if (b < ' ') {
            } else {
                lineBuffer.append((char) b);
            }
        }
    }

    private static class RedirectingOutputStream extends OutputStream {

        private final StringBuffer lineBuffer = new StringBuffer();

        private final boolean isErr;

        RedirectingOutputStream(boolean isErr) {
            this.isErr = isErr;
        }

        public void write(int b) throws IOException {
            if (b == 10) {
                if (isErr) {
                    Main.s_logger.error(lineBuffer.toString());
                } else {
                    Main.s_logger.info(lineBuffer.toString());
                }
                lineBuffer.setLength(0);
            } else {
                lineBuffer.append((char) b);
            }
        }
    }
}

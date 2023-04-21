package mail.util;

import java.io.IOException;
import java.io.OutputStream;

public class LineOutputStream extends OutputStream {

    private static byte[] newline;

    static {
        newline = new byte[2];
        newline[0] = (byte) '\r';
        newline[1] = (byte) '\n';
    }

    protected OutputStream out;

    public LineOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) {
        try {
            out.write(b);
        } catch (Exception ex) {
            System.err.println("Writing to os problem " + ex.toString());
        }
    }

    public void writeln(String s) {
        try {
            if (s != null) {
                byte[] bytes = StringUtils.getBytes(s);
                out.write(bytes);
                out.write(newline);
            }
        } catch (Exception ex) {
            System.err.println("Writing to os problem " + ex.toString());
        }
    }
}

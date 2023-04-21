package main;

import java.io.PrintStream;
import javax.swing.JOptionPane;
import estructuras.Fecha;

public class TeeStream extends PrintStream {

    PrintStream out;

    window_main main;

    public TeeStream(PrintStream out1, PrintStream out2, window_main main) {
        super(out1, true);
        this.out = out2;
        this.main = main;
    }

    public void write(byte buf[], int off, int len) {
        try {
            super.write(buf, off, len);
            out.write(buf, off, len);
        } catch (Exception e) {
        }
    }

    public void flush() {
        super.flush();
        out.flush();
    }
}

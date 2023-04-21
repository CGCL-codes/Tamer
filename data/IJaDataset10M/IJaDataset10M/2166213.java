package net.sf.buildbox.bbx;

import org.junit.Test;
import java.io.PrintStream;

public class ZcdTest {

    @Test
    public void testZcdDot() {
        zcd(System.err, ".");
    }

    @Test
    public void testZcdDotDot() {
        zcd(System.err, "..");
    }

    @Test
    public void testZcdDotDotDot() {
        zcd(System.err, "...");
    }

    private static void zcd(PrintStream out, String... args) {
        out.print("# zcd");
        for (String arg : args) {
            out.print(' ');
            out.print(arg);
        }
        out.println();
        Zcd.main(args);
    }
}

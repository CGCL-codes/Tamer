package vavi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import vavix.util.grep.FileDigger;
import vavix.util.grep.RegexFileDigger;

/**
 * BoyerMooreSearchTest. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2011/02/09 umjammer initial version <br>
 */
public class BoyerMooreSearchTest {

    /**
     * @param args 0: top dir, 1: patern
     */
    public static void main(String[] args) throws Exception {
        new RegexFileDigger(new FileDigger.FileDredger() {

            public void dredge(File file) throws IOException {
                System.out.println("---- " + file + " ----");
                exec(file);
                System.out.println();
                System.exit(0);
            }
        }, Pattern.compile(args[1])).dig(new File(args[0]));
    }

    static void exec(File file) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        byte[] pat = new byte[256];
        byte[] buf = new byte[8192];
        while (is.available() > 0) {
            int r = is.read(buf, 0, buf.length);
            if (r < 0) {
                break;
            }
            byte[] b = new byte[r];
            System.arraycopy(buf, 0, b, 0, r);
            System.out.println(StringUtil.getDump(b, 128));
            BoyerMooreSearch bm = new BoyerMooreSearch(b);
            int p = bm.indexOf(pat, 0);
            if (p > -1) {
                System.out.printf("found at: %08x %08x\n", is.available(), p);
            }
        }
    }
}

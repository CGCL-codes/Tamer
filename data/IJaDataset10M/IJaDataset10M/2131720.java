package huf.io.test;

import huf.io.DevNullOutputStream;
import huf.io.FileUtils;
import huf.io.MD5OutputStream;
import huf.misc.To;
import huf.misc.tester.Tester;
import java.io.File;
import java.io.IOException;

/**
 * MD5OutputStream class test suite.
 */
public class MD5OutputStreamTest {

    /**
	 * Run test suite.
	 *
	 * @param t tester
	 * @throws IOException when something bad happens to I/O
	 */
    public MD5OutputStreamTest(Tester t) throws IOException {
        t.testClass(new MD5OutputStream(null));
        write(t);
    }

    /**
	 * Run test.
	 *
	 * @param t tester
	 * @throws IOException when something bad happens to I/O
	 */
    private void write(Tester t) throws IOException {
        MD5OutputStream out = new MD5OutputStream(new DevNullOutputStream());
        FileUtils.copy(new File("COPYING"), out);
        t.test("write01", "d32239bcb673463ab874e80d47fae504", To.hexString(out.getMD5()));
    }

    /**
	 * Run tests from command line
	 *
	 * @param args ignored
	 * @throws IOException when something bad happens to I/O
	 */
    public static void main(String[] args) throws IOException {
        Tester t = new Tester();
        new MD5OutputStreamTest(t);
        t.totals();
    }
}

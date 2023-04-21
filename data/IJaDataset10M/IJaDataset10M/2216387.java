package net.sf.performancepacks.io.perf;

import net.sf.performancepacks.PerfTestBase;
import java.io.ByteArrayInputStream;
import net.sf.performancepacks.io.UnsyncByteArrayInputStream;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A simple performance test to compare UnsyncByteArrayInputStream and
 * ByteArrayInputStream's performance difference.
 * <p>
 * This test only compares reading performance, since it is the most important
 * one.
 * <p>
 * The test measures the time used to finish the same job by different ways, so
 * the shorter the faster.
 * 
 * @author Shuyang Zhou
 */
public class UnsyncByteArrayInputStreamPerfTest extends PerfTestBase {

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Performance test for " + UnsyncByteArrayInputStream.class.getName());
    }

    @Test
    public void testByteArrayInputStreamBlockRead() throws Exception {
        runPerfTest(new ByteArrayInputStreamBlockReadOnceTest());
    }

    @Test
    public void testUnsyncByteArrayInputStreamBlockRead() throws Exception {
        runPerfTest(new UnsyncByteArrayInputStreamBlockReadOnceTest());
    }

    @Test
    public void testByteArrayInputStreamRead() throws Exception {
        runPerfTest(new ByteArrayInputStreamReadOnceTest());
    }

    @Test
    public void testUnsyncByteArrayInputStreamRead() throws Exception {
        runPerfTest(new UnsyncByteArrayInputStreamReadOnceTest());
    }

    private class ByteArrayInputStreamBlockReadOnceTest implements OnceTest {

        public String getName() {
            return "ByteArrayInputStream block read";
        }

        public void run() throws Exception {
            for (int i = 0; i < runTimes; i++) {
                bais.reset();
                while (bais.read(readBuffer) != -1) ;
            }
        }

        private ByteArrayInputStream bais = new ByteArrayInputStream(blockReadData);

        private byte[] readBuffer = new byte[blockReadBufferSize];
    }

    private class UnsyncByteArrayInputStreamBlockReadOnceTest implements OnceTest {

        public String getName() {
            return "UnsyncByteArrayInputStream block read";
        }

        public void run() throws Exception {
            for (int i = 0; i < runTimes; i++) {
                bais.reset();
                while (bais.read(readBuffer) != -1) ;
            }
        }

        private UnsyncByteArrayInputStream bais = new UnsyncByteArrayInputStream(blockReadData);

        private byte[] readBuffer = new byte[blockReadBufferSize];
    }

    private class ByteArrayInputStreamReadOnceTest implements OnceTest {

        public String getName() {
            return "ByteArrayInputStream read";
        }

        public void run() throws Exception {
            for (int i = 0; i < runTimes; i++) {
                bais.reset();
                while (bais.read() != -1) ;
            }
        }

        private ByteArrayInputStream bais = new ByteArrayInputStream(readData);
    }

    private class UnsyncByteArrayInputStreamReadOnceTest implements OnceTest {

        public String getName() {
            return "UnsyncByteArrayInputStream read";
        }

        public void run() throws Exception {
            for (int i = 0; i < runTimes; i++) {
                bais.reset();
                while (bais.read() != -1) ;
            }
        }

        private UnsyncByteArrayInputStream bais = new UnsyncByteArrayInputStream(readData);
    }

    private static final int blockReadBufferSize = 128;

    private static final int blockReadDataSize = 1024 * 1024;

    private static final int readDataSize = 512 * 1024;

    private static byte[] blockReadData = new byte[blockReadDataSize];

    private static byte[] readData = new byte[readDataSize];
}

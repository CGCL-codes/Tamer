package sdloader.util.databuffer;

import java.io.InputStream;
import junit.framework.TestCase;
import org.junit.Assert;

public class CompositeBufferTest extends TestCase {

    public void test1() throws Exception {
        CompositeDataBuffer buffer = new CompositeDataBuffer(14) {

            @Override
            protected DataBuffer createFirstDataBuffer() {
                return new ByteDataBuffer(10);
            }

            @Override
            protected DataBuffer createNextDataBuffer() {
                return new TempFileDataBuffer();
            }
        };
        for (int i = 0; i < 20; i++) {
            buffer.write(i);
        }
        InputStream is = buffer.getInputStream();
        for (int i = 0; i < 22; i++) {
            if (i < 20) {
                assertEquals(i, is.read());
            } else {
                assertEquals(-1, is.read());
            }
        }
    }

    public void test2() throws Exception {
        CompositeDataBuffer buffer = new CompositeDataBuffer(14) {

            @Override
            protected DataBuffer createFirstDataBuffer() {
                return new ByteDataBuffer(10);
            }

            @Override
            protected DataBuffer createNextDataBuffer() {
                return new TempFileDataBuffer();
            }
        };
        byte[] data = new byte[30];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        buffer.write(data, 0, 10);
        buffer.write(data, 10, 10);
        buffer.write(data, 20, 10);
        InputStream is = buffer.getInputStream();
        byte[] result = new byte[30];
        byte[] buf = new byte[10];
        int len = -1;
        int pos = 0;
        while ((len = is.read(buf)) != -1) {
            System.arraycopy(buf, 0, result, pos, len);
            pos += len;
        }
        Assert.assertArrayEquals(data, result);
    }
}

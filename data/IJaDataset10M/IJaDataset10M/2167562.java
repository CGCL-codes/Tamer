package au.com.lastweekend.ws2300;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SafeDeviceTest extends RawDeviceTest {

    public int errors = 0;

    public class ErrorInjectingStream extends FilterInputStream {

        private static final int RESULT_ERROR = 0xFF;

        protected ErrorInjectingStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int realValue = super.read();
            if (errors > 0) {
                errors = errors - 1;
                return RESULT_ERROR;
            } else {
                return realValue;
            }
        }
    }

    @Override
    protected Device createDevice(PipedInputStream deviceIn, PipedOutputStream deviceOut) {
        return new SafeDevice(new ErrorInjectingStream(deviceIn), deviceOut);
    }

    @After
    public void clearErrors() {
        errors = 0;
    }

    @Before
    public void setupProxyToGenerateErrors() {
        errors = 1;
    }

    @Test
    public void testErrors() throws IOException {
        Device d = getDevice();
        errors = 3;
        d.read(10, 3);
    }

    @Test
    public void testMaxRetriesThrowsException() throws IOException {
        SafeDevice d = (SafeDevice) getDevice();
        errors = d.getMaxRetries();
        try {
            d.read(10, 3);
            fail("Expected IO Exception due to max retries");
        } catch (IOException e) {
        }
    }

    @Test
    public void testBigRead() throws IOException {
        Device d = getDevice();
        errors = 0;
        int address = 10;
        d.write(address, new byte[] { 0x01 });
        d.write(address + 10, new byte[] { 0x02 });
        d.write(address + 28, new byte[] { 0x03, 0x04, 0x05, 0x06 });
        d.write(address + 38, new byte[] { 0x07, 0x08, 0x09, 0x0A });
        d.write(address + 99, new byte[] { 0x0B, 0x0C, 0X0D, 0x0E });
        byte[] results = d.read(address, 15);
        assertThat(results.length, is(30));
        assertThat(results[10], is((byte) 0x02));
        assertThat(results[28], is((byte) 0x03));
        assertThat(results[29], is((byte) 0x04));
        results = d.read(address, 20);
        assertThat(results.length, is(40));
        assertThat(results[10], is((byte) 0x02));
        assertThat(results[28], is((byte) 0x03));
        assertThat(results[29], is((byte) 0x04));
        assertThat(results[31], is((byte) 0x06));
        assertThat(results[38], is((byte) 0x07));
        assertThat(results[39], is((byte) 0x08));
        results = d.read(address, 60);
        assertThat(results.length, is(120));
        assertThat(results[10], is((byte) 0x02));
        assertThat(results[28], is((byte) 0x03));
        assertThat(results[29], is((byte) 0x04));
        assertThat(results[31], is((byte) 0x06));
        assertThat(results[38], is((byte) 0x07));
        assertThat(results[39], is((byte) 0x08));
        assertThat(results[99], is((byte) 0x0B));
        assertThat(results[100], is((byte) 0x0C));
        assertThat(results[102], is((byte) 0x0E));
    }
}

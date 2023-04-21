package onepoint.service.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import onepoint.service.XSizeInputStream;

/**
 * This class contains the test used to validate the integrity of the XSizeInputStream class.
 *
 * @author lucian.furtos
 */
public class XStreamTest extends TestCase {

    private String string_1 = "This is the first data stream!";

    private byte[] data_1 = string_1.getBytes();

    private String string_2 = "The second data stream!";

    private byte[] data_2 = string_2.getBytes();

    private String string_3 = "Finaly the last data stream!";

    private byte[] data_3 = string_3.getBytes();

    private XSizeInputStream streamAll;

    private XSizeInputStream stream_1;

    private XSizeInputStream stream_2;

    private XSizeInputStream stream_3;

    @Override
    protected void setUp() throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byteOut.write(data_1);
        byteOut.write(data_2);
        byteOut.write(data_3);
        byte[] inputData = byteOut.toByteArray();
        byteOut.close();
        assertEquals(inputData.length, data_1.length + data_2.length + data_3.length);
        InputStream sharedIS = new ByteArrayInputStream(inputData);
        streamAll = new XSizeInputStream(sharedIS, inputData.length);
        stream_1 = new XSizeInputStream(sharedIS, data_1.length, true);
        stream_2 = new XSizeInputStream(sharedIS, data_2.length, true);
        stream_3 = new XSizeInputStream(sharedIS, data_3.length, true);
    }

    protected void tearDown() throws IOException {
        stream_3.close();
        stream_2.close();
        stream_1.close();
        streamAll.close();
    }

    public void testReadBuff() throws Exception {
        byte[] buff = new byte[data_2.length];
        assertEquals(data_1.length, streamAll.skip(data_1.length));
        assertEquals(data_2.length, streamAll.read(buff, 0, data_2.length));
        String actual = new String(buff);
        assertEquals(string_2, actual);
    }

    /**
    * Test share reading from the stream
    *
    * @throws Exception if errors occur during test
    */
    public void testSharedRead() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream_1));
        String actual = reader.readLine();
        assertEquals(string_1, actual);
        reader = new BufferedReader(new InputStreamReader(stream_2));
        actual = reader.readLine();
        assertEquals(string_2, actual);
        reader = new BufferedReader(new InputStreamReader(stream_3));
        actual = reader.readLine();
        assertEquals(string_3, actual);
    }

    /**
    * Test available and skip from stream
    *
    * @throws Exception if errors occur during test
    */
    public void testAvailable() throws Exception {
        assertEquals(data_1.length, stream_1.available());
        assertEquals(5, stream_1.skip(5));
        assertEquals(data_1.length - 5, stream_1.available());
        assertEquals(data_1.length - 5, stream_1.skip(1000));
        assertEquals(0, stream_1.available());
        assertEquals(data_2.length, stream_2.available());
    }
}

package org.microemu.app.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * @author vlads
 * 
 */
public class MIDletResourceInputStreamTest extends TestCase {

    private void setData(byte data[]) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
    }

    public void testReadPart() throws IOException {
        int max = 10;
        byte data[] = new byte[max];
        setData(data);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        MIDletResourceInputStream mis = new MIDletResourceInputStream(is);
        byte data2[] = new byte[max + 5];
        int rc = mis.read(data2);
        assertEquals("read part", max, rc);
    }
}

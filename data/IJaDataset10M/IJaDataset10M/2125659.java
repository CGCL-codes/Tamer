package jaxlib.io;

import java.io.*;
import java.util.*;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ZippedTest.java 2267 2007-03-16 08:33:33Z joerg_wassmer $
 */
public final class ZippedTest extends junit.framework.TestCase {

    public ZippedTest(String name) {
        super(name);
    }

    public void testReadWrite() throws Exception {
        StringBuilder sb = new StringBuilder(((int) Character.MAX_VALUE) * 2);
        for (int c = 0; c <= Character.MAX_VALUE; c++) {
            sb.append((char) c);
            sb.append((char) c);
        }
        final Properties value = System.getProperties();
        value.put("test", sb.toString());
        sb = null;
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(new Zipped<Object>(value));
        out.close();
        final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        final ObjectInputStream in = new ObjectInputStream(bin);
        Zipped<Object> zipped = (Zipped) in.readObject();
        in.close();
        assertEquals(value, zipped.get());
    }

    public void testSingleBlock() throws Exception {
        StringBuilder sb = new StringBuilder(((int) Character.MAX_VALUE) * 2);
        for (int c = 0; c <= Character.MAX_VALUE; c++) {
            sb.append((char) c);
            sb.append((char) c);
        }
        final Properties value = System.getProperties();
        value.put("test", sb.toString());
        sb = null;
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(new Zipped<Object>(value, Zipped.SINGLE_BLOCK));
        out.close();
        final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        final ObjectInputStream in = new ObjectInputStream(bin);
        Zipped<Object> zipped = (Zipped) in.readObject();
        in.close();
        assertEquals(value, zipped.get());
    }
}

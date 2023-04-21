package net.sf.xml2cb.cobol.engine.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.xml.stream.XMLStreamException;
import junit.framework.TestCase;
import net.sf.xml2cb.core.io.DefaultBufferContext;
import net.sf.xml2cb.core.io.FastByteArrayInputStream;
import net.sf.xml2cb.core.io.FastByteArrayOutputStream;
import net.sf.xml2cb.core.io.XmlReader;
import net.sf.xml2cb.core.io.XmlReaderInputStream;
import net.sf.xml2cb.core.io.XmlWriter;
import net.sf.xml2cb.core.io.XmlWriterOutputStream;

public class CobolAreaImplTest extends TestCase {

    /**
	 * Copy cobol test�e :<br/>
	 * <pre>
	 * 01 HELLO         PIC X(010).
	 * </pre>
	 * @throws IOException
	 */
    public void testInitialisation() throws IOException {
        CobolAreaImpl area = new CobolAreaImpl("HELLO", 10, new CobolTrimedStringConverter());
        byte[] expectedInitBytes = "          ".getBytes("cp1147");
        int length = 10;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        area.initialize(buffer);
        buffer.flip();
        assertTrue(Arrays.equals(expectedInitBytes, buffer.array()));
    }

    /**
	 * Copy cobol test�e :<br/>
	 * <pre>
	 * 01 A.
	 *    02 B         PIC X(010).
	 * </pre>
	 * XML :<br/>
	 * &lt;A&gt;&lt;B&gt;hello&lt;/B&gt;&lt/A&gt;
	 */
    public void testMarshall() throws XMLStreamException, IOException {
        byte[] xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><A><B>hello</B></A>".getBytes();
        CobolGroupAreaImpl group = new CobolGroupAreaImpl("A");
        group.addArea(new CobolAreaImpl("B", 10, new CobolTrimedStringConverter()));
        int length = 10;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        group.initialize(buffer);
        buffer.flip();
        XmlReader reader = new XmlReaderInputStream(new FastByteArrayInputStream(xml));
        group.marshall(reader, new DefaultBufferContext(buffer, 10));
        assertTrue(Arrays.equals("hello     ".getBytes("cp1147"), buffer.array()));
    }

    /**
	 * Copy cobol test�e :<br/>
	 * <pre>
	 * 01 A.
	 *    02 B         PIC X(010).
	 * </pre>
	 * XML :<br/>
	 * &lt;A&gt;&lt;B&gt;hello&lt;/B&gt;&lt/A&gt;
	 */
    public void testUnmarshall() throws XMLStreamException, IOException {
        CobolGroupAreaImpl group = new CobolGroupAreaImpl("A");
        group.addArea(new CobolAreaImpl("B", 10, new CobolTrimedStringConverter()));
        int length = 10;
        String expectedXml = "<?xml version=\'1.0\' encoding=\'UTF-8\'?><A><B>hello</B></A>";
        byte[] initBytes = "hello     ".getBytes("cp1147");
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(initBytes);
        buffer.flip();
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        XmlWriter xsw = new XmlWriterOutputStream(out, "UTF-8");
        xsw.startDocument();
        group.unmarshall(new DefaultBufferContext(buffer, 10), xsw);
        xsw.close();
        assertEquals(expectedXml, out.toString("UTF-8"));
    }
}

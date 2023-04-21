package flattree.xstream;

import java.io.EOFException;
import java.io.ObjectInputStream;
import com.thoughtworks.xstream.XStream;

public class FlatHierarchicalStreamReaderTest extends AbstractStreamTest {

    public void testSingle() {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver(getSingleRoot()));
        Foo foo = (Foo) xstream.fromXML(getSingleFlat());
        assertEquals("a foo's string", foo.string);
        assertNotNull(foo.bar);
        assertEquals("a bar's string", foo.bar.string);
    }

    public void testMultiple() throws Exception {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver(getMultipleRoot()));
        ObjectInputStream stream = xstream.createObjectInputStream(getMultipleFlat());
        Foo foo = (Foo) stream.readObject();
        assertEquals("a foo's string", foo.string);
        assertNotNull(foo.bar);
        assertEquals("a bar's string", foo.bar.string);
        foo = (Foo) stream.readObject();
        assertEquals("a foo's string", foo.string);
        assertNotNull(foo.bar);
        assertEquals("a bar's string", foo.bar.string);
        try {
            stream.readObject();
            fail();
        } catch (EOFException expected) {
        }
    }
}

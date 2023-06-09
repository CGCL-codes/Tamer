package simple.xml.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Set;
import org.w3c.dom.Document;
import junit.framework.TestCase;
import simple.xml.load.Persister;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementList;
import simple.xml.Root;

public class DictionaryTest extends TestCase {

    private static final String LIST = "<?xml version=\"1.0\"?>\n" + "<test name='example'>\n" + "   <list>\n" + "      <property name='1'>\n" + "         <text>one</text>  \n\r" + "      </property>\n\r" + "      <property name='2'>\n" + "         <text>two</text>  \n\r" + "      </property>\n" + "      <property name='3'>\n" + "         <text>three</text>  \n\r" + "      </property>\n" + "   </list>\n" + "</test>";

    @Root(name = "property")
    private static class Property extends Entry {

        @Element(name = "text")
        private String text;
    }

    @Root(name = "test")
    private static class PropertySet implements Iterable<Property> {

        @ElementList(name = "list", type = Property.class)
        private Dictionary<Property> list;

        @Attribute(name = "name")
        private String name;

        public Iterator<Property> iterator() {
            return list.iterator();
        }

        public Property get(String name) {
            return list.get(name);
        }

        public int size() {
            return list.size();
        }
    }

    private Persister serializer;

    public void setUp() {
        serializer = new Persister();
    }

    public void testDictionary() throws Exception {
        PropertySet set = (PropertySet) serializer.read(PropertySet.class, LIST);
        assertEquals(3, set.size());
        assertEquals("one", set.get("1").text);
        assertEquals("two", set.get("2").text);
        assertEquals("three", set.get("3").text);
        serializer.write(set, System.err);
    }
}

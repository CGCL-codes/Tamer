package jaxlib.prefs;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import jaxlib.junit.ObjectTestCase;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: IniFileTest.java 3027 2011-12-20 13:20:37Z joerg_wassmer $
 */
public final class IniFileTest extends ObjectTestCase {

    public IniFileTest(String name) {
        super(name);
    }

    @Override
    protected Object createObject() {
        IniMap f = new IniMap();
        f.add("test").put("key", "value");
        return f;
    }

    private IniMap read(Reader in) throws IOException {
        IniMap ini = new IniMap();
        ini.read(in);
        in.close();
        System.err.println(ini);
        assertEquals("HeaderComment", ini.getHeaderComment());
        IniMap.Section section0 = ini.get("section-0");
        assertNotNull(section0);
        assertEquals("value0", section0.get("key0"));
        assertEquals("value1", section0.get("key1"));
        assertEquals("value2", section0.get("key2"));
        IniMap.Section section1 = ini.get("section-1");
        assertNotNull(section1);
        assertEquals("value3", section1.get("key3"));
        assertEquals("value4", section1.get("key4"));
        assertEquals("value5", section1.get("key5"));
        IniMap.Section section2 = ini.get("section-2");
        assertNotNull(section2);
        assertTrue(section2.map().isEmpty());
        IniMap.Section section3 = ini.get("section-3");
        assertNotNull(section3);
        assertEquals("value", section3.get("key"));
        assertEquals(null, section0.getComment());
        assertEquals("section1Comment", section1.getComment());
        assertEquals(null, section2.getComment());
        assertEquals("section3Comment", section3.getComment());
        return ini;
    }

    public void testRead() throws IOException {
        read(new InputStreamReader(getClass().getResourceAsStream("TestIniFile.ini")));
    }

    public void testWrite() throws IOException {
        IniMap a = read(new InputStreamReader(getClass().getResourceAsStream("TestIniFile.ini")));
        StringWriter out = new StringWriter();
        a.write(out);
        IniMap b = read(new StringReader(out.toString()));
        System.err.println(b);
        assertEquals(a, b);
    }
}

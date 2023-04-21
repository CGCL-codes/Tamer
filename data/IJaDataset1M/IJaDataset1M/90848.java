package net.sf.doolin.oxml;

import java.util.List;
import junit.framework.TestCase;
import net.sf.doolin.oxml.config.XMLOXMLConfig;
import net.sf.doolin.oxml.source.ResourceOXMLSource;
import net.sf.doolin.util.Utils;

/**
 * Tests the reading of an XML source to a Java object with a tree structure
 * 
 * @author Damien Coraboeuf
 */
public class TestTree extends TestCase {

    /**
	 * Tests the reading.
	 * 
	 * @throws Exception
	 *             In case there is an error
	 */
    public void testRead() throws Exception {
        OXMLConfig config = new XMLOXMLConfig(Utils.getResource(getClass(), "ConfigurationTree.xml"));
        OXMLReader<Node> reader = new OXMLReader<Node>(config);
        ResourceOXMLSource source = new ResourceOXMLSource(getClass(), "SourceDocumentTree.xml");
        Node a = reader.read(source);
        assertNotNull(a);
        assertEquals("a", a.getId());
        List<Node> aList = a.getNodes();
        assertNotNull(aList);
        assertEquals(2, aList.size());
        Node aa = aList.get(0);
        assertEquals("aa", aa.getId());
        List<Node> aaList = aa.getNodes();
        assertNotNull(aaList);
        assertEquals(2, aaList.size());
        Node aaa = aaList.get(0);
        assertEquals("aaa", aaa.getId());
        List<Node> aaaList = aaa.getNodes();
        assertNotNull(aaaList);
        assertEquals(0, aaaList.size());
        Node aab = aaList.get(1);
        assertEquals("aab", aab.getId());
        List<Node> aabList = aab.getNodes();
        assertNotNull(aabList);
        assertEquals(0, aabList.size());
        Node ab = aList.get(1);
        assertEquals("ab", ab.getId());
        List<Node> abList = ab.getNodes();
        assertNotNull(abList);
        assertEquals(2, abList.size());
        Node aba = abList.get(0);
        assertEquals("aba", aba.getId());
        List<Node> abaList = aba.getNodes();
        assertNotNull(abaList);
        assertEquals(0, abaList.size());
        Node abb = abList.get(1);
        assertEquals("abb", abb.getId());
        List<Node> abbList = abb.getNodes();
        assertNotNull(abbList);
        assertEquals(0, abbList.size());
    }
}

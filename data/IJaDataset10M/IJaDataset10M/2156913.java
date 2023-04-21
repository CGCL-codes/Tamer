package com.yubarta.docman.impl;

import com.yubarta.utils.XmlExplorer;
import junit.framework.TestCase;

/**
 * @author César
 */
public class ConfigReaderTest extends TestCase {

    public final void testGetElementContentStringArray() throws Exception {
        XmlExplorer cr = new XmlExplorer("test/config/xmlTest.xml", new String[0]);
        assertEquals("http://juntadeandazulia.es/docu/a>b<c'd\"e", cr.getValue(new String[] { "params", "baseUrl" }));
        assertEquals("test/config/schema1.rdf", cr.getValue(new String[] { "params", "schemaFile" }));
        String s1 = cr.getElementContent(new String[] { "params", "sesame" });
        String s2 = "<local>false</local><config><url>ñáhttp://a&gt;b&lt;c&#39;d&quot;e</url>/teki</config>";
        assertEquals(s2, s1);
    }
}

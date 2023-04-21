package org.openexi.proc.io;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.openexi.proc.common.XmlUriConst;
import org.openexi.scomp.EXISchemaFactoryErrorMonitor;

public class PrefixPartitionTest extends TestCase {

    public PrefixPartitionTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m_compilerErrors = new EXISchemaFactoryErrorMonitor();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        m_compilerErrors.clear();
    }

    private EXISchemaFactoryErrorMonitor m_compilerErrors;

    /**
   */
    public void testDefaultPrefixes() throws Exception {
        StringTable stringTable;
        StringTable.PrefixPartition prefixPartition;
        stringTable = new StringTable(null);
        prefixPartition = stringTable.getPrefixPartition("");
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId(""));
        prefixPartition.addString("a");
        Assert.assertEquals(2, prefixPartition.n_strings);
        Assert.assertEquals(1, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("b");
        Assert.assertEquals(3, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("c");
        Assert.assertEquals(4, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("d");
        Assert.assertEquals(5, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("e");
        Assert.assertEquals(6, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("f");
        Assert.assertEquals(7, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("g");
        Assert.assertEquals(8, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(4, prefixPartition.forwardedWidth);
        prefixPartition.clear();
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId(""));
    }

    /**
   */
    public void testXmlPrefixes() throws Exception {
        StringTable stringTable;
        StringTable.PrefixPartition prefixPartition;
        stringTable = new StringTable(null);
        prefixPartition = stringTable.getPrefixPartition(XmlUriConst.W3C_XML_1998_URI);
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId("xml"));
        prefixPartition.addString("a");
        Assert.assertEquals(2, prefixPartition.n_strings);
        Assert.assertEquals(1, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("b");
        Assert.assertEquals(3, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("c");
        Assert.assertEquals(4, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("d");
        Assert.assertEquals(5, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("e");
        Assert.assertEquals(6, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("f");
        Assert.assertEquals(7, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("g");
        Assert.assertEquals(8, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(4, prefixPartition.forwardedWidth);
        prefixPartition.clear();
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId("xml"));
    }

    /**
   */
    public void testXsiPrefixes() throws Exception {
        StringTable stringTable;
        StringTable.PrefixPartition prefixPartition;
        stringTable = new StringTable(null);
        prefixPartition = stringTable.getPrefixPartition(XmlUriConst.W3C_2001_XMLSCHEMA_INSTANCE_URI);
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId("xsi"));
        prefixPartition.addString("a");
        Assert.assertEquals(2, prefixPartition.n_strings);
        Assert.assertEquals(1, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("b");
        Assert.assertEquals(3, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("c");
        Assert.assertEquals(4, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("d");
        Assert.assertEquals(5, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("e");
        Assert.assertEquals(6, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("f");
        Assert.assertEquals(7, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("g");
        Assert.assertEquals(8, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(4, prefixPartition.forwardedWidth);
        prefixPartition.clear();
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.getCompactId("xsi"));
    }

    /**
   */
    public void testXsdPrefixes() throws Exception {
        StringTable stringTable;
        StringTable.PrefixPartition prefixPartition;
        stringTable = new StringTable(null);
        prefixPartition = stringTable.getPrefixPartition(XmlUriConst.W3C_2001_XMLSCHEMA_URI);
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(0, prefixPartition.forwardedWidth);
        Assert.assertEquals(0, prefixPartition.n_strings);
        prefixPartition.addString("a");
        Assert.assertEquals(1, prefixPartition.n_strings);
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(1, prefixPartition.forwardedWidth);
        prefixPartition.addString("b");
        Assert.assertEquals(2, prefixPartition.n_strings);
        Assert.assertEquals(1, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("c");
        Assert.assertEquals(3, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(2, prefixPartition.forwardedWidth);
        prefixPartition.addString("d");
        Assert.assertEquals(4, prefixPartition.n_strings);
        Assert.assertEquals(2, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("e");
        Assert.assertEquals(5, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("f");
        Assert.assertEquals(6, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("g");
        Assert.assertEquals(7, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(3, prefixPartition.forwardedWidth);
        prefixPartition.addString("h");
        Assert.assertEquals(8, prefixPartition.n_strings);
        Assert.assertEquals(3, prefixPartition.width);
        Assert.assertEquals(4, prefixPartition.forwardedWidth);
        prefixPartition.clear();
        Assert.assertEquals(0, prefixPartition.width);
        Assert.assertEquals(0, prefixPartition.forwardedWidth);
        Assert.assertEquals(0, prefixPartition.n_strings);
    }
}

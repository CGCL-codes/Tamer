package net.sf.xsdutils.xml;

import junit.framework.TestCase;

public class DOMComparatorTest extends TestCase {

    private DOMComparator comparator;

    protected void setUp() throws Exception {
        comparator = new DOMComparator();
    }

    public void testCompareOK() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc2.xml"));
        assertTrue(String.valueOf(rc), rc == null);
    }

    public void testCompareWrongAttrValue() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc3.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("attr1"));
    }

    public void testCompareExtraElement() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc5.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("el3"));
    }

    public void testCompareBadValue() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc6.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("true"));
    }

    public void testCompareMissingValue() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc7.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("true"));
    }

    public void testCompareMissingAttr() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc8.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("attr2"));
    }

    public void testCompareExtraAttr() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("doc9.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("attrC"));
    }

    public void testCompareNSAttr() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("doc1.xml"), getClass().getResource("docA.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("ns1"));
    }

    public void testCompareNSElement() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docB.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("ns1:build"));
    }

    public void testCompareNS() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docC.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("qwer"));
    }

    public void testCompareWrongEntity() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docD.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains(">"));
    }

    public void testCompareXmlDecl() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docE.xml"));
        assertTrue(rc == null);
    }

    public void testCompareXmlDeclDiff() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docF.xml"));
        assertTrue(rc == null);
    }

    public void testCompareXmlDeclDiffEnc() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docF.xml"), getClass().getResource("docG.xml"));
        assertTrue(rc == null);
    }

    public void testCompareComment() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docA.xml"), getClass().getResource("docH.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("asdfg"));
    }

    public void testCompareDoctype() throws Exception {
        DOMCompareResult rc = comparator.compare(getClass().getResource("docI.xml"), getClass().getResource("docJ.xml"));
        assertTrue(rc != null);
        assertTrue(rc.getMessage(), rc.getMessage().contains("doctype"));
    }
}

package org.dcm4che.srom;

import org.dcm4che.srom.*;
import org.dcm4che.data.*;
import junit.framework.*;
import java.io.*;
import javax.imageio.stream.*;

/**
 *
 * @author  gunter zeilinger
 * @version 1.0
 */
public class SRDocumentFactoryTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(SRDocumentFactoryTest.class);
    }

    /** 
     * Creates new ExtractorTest 
     */
    public SRDocumentFactoryTest(String name) {
        super(name);
    }

    private DcmObjectFactory dsf = null;

    private SRDocumentFactory srf = null;

    protected void setUp() throws Exception {
        dsf = DcmObjectFactory.getInstance();
        srf = SRDocumentFactory.getInstance();
    }

    private static final String SR_DCM = "data/sr_601cr.dcm";

    private static final String KO_DCM = "data/sr_511_cr.dcm";

    public void testSR() throws Exception {
        Dataset ds = dsf.newDataset();
        InputStream in = new BufferedInputStream(new FileInputStream(new File(SR_DCM)));
        try {
            ds.readFile(in, null, -1);
        } finally {
            in.close();
        }
        SRDocument sr = srf.newSRDocument(ds);
        assertEquals("CRTHREE^PAUL", sr.getPatient().getPatientName());
        assertEquals("CR3", sr.getPatient().getPatientID());
        assertEquals("1.113654.1.2001.30", sr.getStudy().getStudyInstanceUID());
        assertEquals("2001C30", sr.getStudy().getAccessionNumber());
        assertEquals("SR", sr.getSeries().getModality());
        assertEquals("1.113654.1.2001.30.2.601", sr.getSeries().getSeriesInstanceUID());
        assertEquals(1, sr.getSeries().getSeriesNumber());
        assertEquals("MIR", sr.getEquipment().getManufacturer());
        assertTrue(sr.isComplete());
        assertTrue(!sr.isVerified());
        assertTrue(sr.isSeparate());
        assertEquals("DCMR", sr.getTemplate().getMappingResource());
        assertEquals("2000", sr.getTemplate().getTemplateIdentifier());
        assertEquals(Content.ValueType.CONTAINER, sr.getValueType());
        checkCode("11528-7", "LN", "Radiology Report", sr.getName());
        assertTrue(sr.hasChildren());
        Content child = sr.getFirstChild();
        assertNotNull(child);
        checkCode("121049", "DCM", "Language of Content Item and Descendents", child.getName());
        assertEquals(Content.ValueType.CODE, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121005", "DCM", "Observer Type", child.getName());
        assertEquals(Content.ValueType.CODE, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121008", "DCM", "Person Observer Name", child.getName());
        assertEquals(Content.ValueType.PNAME, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121060", "DCM", "History", child.getName());
        assertEquals(Content.ValueType.CONTAINER, child.getValueType());
        assertTrue(sr.hasChildren());
        Content subchild = child.getFirstChild();
        assertNotNull(subchild);
        checkCode("121060", "DCM", "History", subchild.getName());
        assertEquals(Content.ValueType.TEXT, subchild.getValueType());
        assertNull(subchild.getNextSibling());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121070", "DCM", "Findings", child.getName());
        assertEquals(Content.ValueType.CONTAINER, child.getValueType());
        assertTrue(sr.hasChildren());
        subchild = child.getFirstChild();
        assertNotNull(subchild);
        checkCode("121071", "DCM", "Finding", subchild.getName());
        assertEquals(Content.ValueType.TEXT, subchild.getValueType());
        assertNull(subchild.getNextSibling());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121076", "DCM", "Conclusions", child.getName());
        assertEquals(Content.ValueType.CONTAINER, child.getValueType());
        assertTrue(sr.hasChildren());
        subchild = child.getFirstChild();
        assertNotNull(subchild);
        checkCode("121077", "DCM", "Conclusion", subchild.getName());
        assertEquals(Content.ValueType.TEXT, subchild.getValueType());
        assertNull(subchild.getNextSibling());
        assertNull(child.getNextSibling());
    }

    private void checkCode(String value, String scheme, String meaning, Code code) {
        assertEquals(value, code.getCodeValue());
        assertEquals(scheme, code.getCodingSchemeDesignator());
        assertEquals(meaning, code.getCodeMeaning());
    }

    public void testKO() throws Exception {
        Dataset ds = dsf.newDataset();
        InputStream in = new BufferedInputStream(new FileInputStream(new File(KO_DCM)));
        try {
            ds.readFile(in, null, -1);
        } finally {
            in.close();
        }
        KeyObject ko = srf.newKeyObject(ds);
        assertEquals("CRTHREE^PAUL", ko.getPatient().getPatientName());
        assertEquals("CR3", ko.getPatient().getPatientID());
        assertEquals("1.113654.1.2001.30", ko.getStudy().getStudyInstanceUID());
        assertEquals("2001B20", ko.getStudy().getAccessionNumber());
        assertEquals("KO", ko.getSeries().getModality());
        assertEquals("1.113654.1.2001.30.511", ko.getSeries().getSeriesInstanceUID());
        assertEquals(511, ko.getSeries().getSeriesNumber());
        assertEquals("MIR", ko.getEquipment().getManufacturer());
        assertEquals(Content.ValueType.CONTAINER, ko.getValueType());
        assertEquals("DCMR", ko.getTemplate().getMappingResource());
        assertEquals("2010", ko.getTemplate().getTemplateIdentifier());
        checkCode("113000", "DCM", "Of Interest", ko.getName());
        assertTrue(ko.hasChildren());
        Content child = ko.getFirstChild();
        assertNotNull(child);
        checkCode("121049", "DCM", "Language of Content Item and Descendents", child.getName());
        assertEquals(Content.ValueType.CODE, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121005", "DCM", "Observer Type", child.getName());
        assertEquals(Content.ValueType.CODE, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("121008", "DCM", "Person Observer Name", child.getName());
        assertEquals(Content.ValueType.PNAME, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        checkCode("113012", "DCM", "Key Object Description", child.getName());
        assertEquals(Content.ValueType.TEXT, child.getValueType());
        child = child.getNextSibling();
        assertNotNull(child);
        assertEquals(Content.ValueType.IMAGE, child.getValueType());
        assertNull(child.getNextSibling());
    }
}

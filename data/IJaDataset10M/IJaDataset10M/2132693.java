package test.de.unibi.techfak.bibiserv.biodom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import de.unibi.techfak.bibiserv.biodom.AbstractBioDOM;
import de.unibi.techfak.bibiserv.biodom.SequenceML;
import de.unibi.techfak.bibiserv.biodom.exception.BioDOMException;

/**
 * This is a test for the SequenceML class that implements the
 * SequenceMLInterface. Current version need JUNIT 4.0 or above for running the
 * test
 * 
 * @author Jan Kr�ger <jkrueger@techfak.uni-bielefeld.de
 * 
 */
public class SequenceMLTest {

    private static Document refdom;

    @BeforeClass
    public static void init() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            refdom = dbf.newDocumentBuilder().parse(new File("resources/xml/SequenceML_20060201.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void fromFasta() {
        try {
            SequenceML sml = new SequenceML();
            sml.appendFasta(new FileReader("resources/classic/sequences.fasta"));
            Document dom = sml.getDom();
            Assert.assertTrue(DOMEqual.equal(refdom.getDocumentElement(), dom.getDocumentElement()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void toFasta() {
        try {
            StringBuffer fasta = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader("resources/classic/sequences.fasta"));
            String line = null;
            while ((line = br.readLine()) != null) {
                fasta.append(line);
            }
            br.close();
            SequenceML aml = new SequenceML(refdom);
            Assert.assertEquals(aml.toFasta().replaceAll(System.getProperty("line.terminator") + "|\\s", ""), fasta.toString().replaceAll(System.getProperty("line.terminator") + "|\\s", ""));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void brokenXML() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document dom = dbf.newDocumentBuilder().parse(new File("resources/xml/SequenceML_20060201_broken.xml"));
            SequenceML sml = new SequenceML();
            try {
                sml.setDom(dom);
                Assert.fail("DOM is invalid, so this point should never reached!");
            } catch (BioDOMException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testHuge() {
        try {
            SequenceML sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(1000, CreateSequence.DNA));
            long start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            long time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 1 KB --> " + time + "ms for validation");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(5000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 5 KB --> " + time + "ms for validation");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(10000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 10 KB --> " + time + "ms for validation");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(100000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 100 KB --> " + time + "ms for validation");
            sml = new SequenceML();
            start = System.currentTimeMillis();
            sml.appendFasta(CreateSequence.createFastaSequence(1000000, CreateSequence.DNA));
            time = System.currentTimeMillis() - start;
            Assert.assertTrue(sml.validate());
            System.out.println("testHuge : 1 MB --> " + time + "ms for validation ");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(5000000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 5 MB --> " + time + "ms for validation");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(10000000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 10 MB --> " + time + "ms for validation");
            sml = new SequenceML();
            sml.appendFasta(CreateSequence.createFastaSequence(20000000, CreateSequence.DNA));
            start = System.currentTimeMillis();
            Assert.assertTrue(sml.validate());
            time = System.currentTimeMillis() - start;
            System.out.println("testHuge : 20 MB --> " + time + "ms for validation");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getSequencesByIDs() {
        try {
            String TESTSEQUENCE_1 = CreateSequence.createFastaSequence(1000, CreateSequence.DNA, "ID_1");
            String TESTSEQUENCE_2 = CreateSequence.createFastaSequence(1000, CreateSequence.DNA, "ID_2", "ThisisSeqencewithID_2");
            SequenceML sml = new SequenceML();
            sml.appendFasta(TESTSEQUENCE_1, AbstractBioDOM.NUCLEICACID);
            sml.appendFasta(TESTSEQUENCE_2, AbstractBioDOM.NUCLEICACID);
            List<String> idl = sml.getIDlist();
            Assert.assertNotNull(idl);
            Assert.assertTrue(idl.contains("ID_1"));
            Assert.assertTrue(idl.contains("ID_2"));
            Map s = sml.getSequence("ID_2");
            Assert.assertEquals(s.get("id"), "ID_2");
            Assert.assertEquals(s.get("sequence"), TESTSEQUENCE_2.split(System.getProperty("line.separator"), 2)[1].replaceAll(System.getProperty("line.separator"), ""));
            Assert.assertEquals(s.get("description"), "ThisisSeqencewithID_2");
            Assert.assertNull(s.get("synonyms"));
            try {
                Map no = sml.getSequence("isnichtda");
                Assert.fail("Nicht vorhandene ID sollte Exception werfen!");
            } catch (BioDOMException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SequenceMLTest.class);
    }
}

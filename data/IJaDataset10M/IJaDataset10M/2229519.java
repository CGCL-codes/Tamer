package test.de.unibi.techfak.bibiserv.biodom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.JUnit4TestAdapter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import de.unibi.techfak.bibiserv.biodom.AlignmentML;

/**
 * This is a test for the AlignmentML class that implements
 * the AlignmentMLInterface. Current version need JUNIT 4.0
 * or above for running the test
 * 
 * @author Kai Loewenthal <kloewent@techfak.uni-bielefeld.de>
 *         Jan Kr�ger <jkrueger@techfak.uni-bielefeld.de>
 *         
 * @version 0.2
 *
 */
public class AlignmentMLTest {

    static Document refdoc = null;

    @BeforeClass
    public static void init() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            refdoc = dbf.newDocumentBuilder().parse(new File("resources/xml/AlignmentML_20060602.xml"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void appendClustal() {
        try {
            AlignmentML aml = new AlignmentML();
            aml.appendClustal(new FileReader("resources/classic/alignment.clustal"));
            Document doc = aml.getDom();
            Assert.assertTrue(DOMEqual.equal(refdoc.getDocumentElement(), doc.getDocumentElement()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void appendFasta() {
        try {
            AlignmentML aml = new AlignmentML();
            aml.appendFastaAsAlignment(new FileReader("resources/classic/alignment.fasta"));
            Document doc = aml.getDom();
            Assert.assertTrue(DOMEqual.equal(refdoc.getDocumentElement(), doc.getDocumentElement()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void toFasta() {
        try {
            StringBuffer fasta = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader("resources/classic/alignment.fasta"));
            String line = null;
            while ((line = br.readLine()) != null) {
                fasta.append(line);
            }
            br.close();
            AlignmentML aml = new AlignmentML(refdoc);
            Assert.assertEquals(aml.toFasta().replaceAll(System.getProperty("line.terminator") + "|\\s", ""), fasta.toString().replaceAll(System.getProperty("line.terminator") + "|\\s", ""));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AlignmentMLTest.class);
    }
}

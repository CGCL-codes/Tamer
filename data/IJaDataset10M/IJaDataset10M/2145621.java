package eulergui.gui;

import com.hp.gloze.Gloze;
import n3_project.helpers.GuiResourceBundle;
import org.netbeans.jemmy.operators.JMenuBarOperator;
import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.CharArrayWriter;
import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;

/** tests the use cases from the
 * <a href="http://deductions.svn.sourceforge.net/viewvc/deductions/html/GUIgenerator.html">
 * Déductions project Manual</a>:
 * test File... / Export as XML
 */
public class TestEulerGUIOutputs extends TestJemmy {

    public static void main(String[] argv) throws Exception {
        TestEulerGUIOutputs testJemmy = new TestEulerGUIOutputs();
        testJemmy.setUp();
        testJemmy.testXMLOutputFromRDF();
        testJemmy.testXMLOutputFromN3();
    }

    public void testXMLOutputFromRDF() throws Exception {
        String generatedXML = "xml-export/kml2.rdf.xml";
        new File(generatedXML).delete();
        launchEulerGUIAndExportXML("./test/kml.n3p");
        String exported = stringify(generatedXML);
        String original = stringify("test/kml.xml");
        compareAndAssertTrue(exported, original);
    }

    public void testXMLOutputFromN3() throws Exception {
        String generatedXML = "xml-export/kml2.n3.xml";
        new File(generatedXML).delete();
        launchEulerGUIAndExportXML("./test/kml_n3.n3p");
        String exported = stringify(generatedXML);
        String original = stringify("test/kml.xml");
        compareAndAssertTrue(exported, original);
    }

    private void compareAndAssertTrue(String exported, String original) {
        boolean equals = original.equals(exported);
        if (!equals) {
            Logger.getLogger("theDefault").info(MessageFormat.format(GuiResourceBundle.getString("original.n.0"), original));
            Logger.getLogger("theDefault").info(MessageFormat.format(GuiResourceBundle.getString("exported.n.0"), exported));
        }
        assertTrue("kml.xml translated to RDF and back should be identical, ", equals);
    }

    private void launchEulerGUIAndExportXML(String n3Project) throws Exception {
        launchEulerGUI(n3Project);
        JMenuBarOperator menuBar = new JMenuBarOperator(mainFrame);
        JMenuItemOperator menuItem = menuBar.showMenuItem(GuiResourceBundle.getString("file") + "|" + GuiResourceBundle.getString("export.as.xml"));
        menuItem.getTimeouts().setTimeout("JMenuItemOperator.PushMenuTimeout", 30000);
        menuItem.push();
    }

    /** stringify an XML file */
    private String stringify(String pathname) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document domDocument = db.parse(new File(pathname));
            Source domSource = new DOMSource(domDocument);
            CharArrayWriter writer = new CharArrayWriter();
            Result streamResult = new StreamResult(writer);
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new DOMSource(db.parse(new File("test/identquery2.xslt")));
            Transformer transformer = factory.newTransformer(xslt);
            transformer.transform(domSource, streamResult);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Gloze.clearCache();
    }
}

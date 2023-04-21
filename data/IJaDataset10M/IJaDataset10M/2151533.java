package dom;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import junit.framework.*;
import org.infoset.xml.Document;
import org.infoset.xml.DocumentLoader;
import org.infoset.xml.XMLException;
import org.infoset.xml.dom.DOMDocumentProxy;
import org.infoset.xml.sax.SAXDocumentLoader;
import org.infoset.xml.util.ItemDiff;

/**
 *
 * @author alex
 */
public class DOMIdentityTest extends TestCase {

    public DOMIdentityTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testIdentity() {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer id = factory.newTransformer();
            DocumentLoader loader = new SAXDocumentLoader();
            URL location = getClass().getResource("id-1.xml");
            Document doc = loader.load(location.toURI());
            DOMDocumentProxy proxy = new DOMDocumentProxy(doc);
            StringWriter w = new StringWriter();
            id.transform(new DOMSource(proxy), new StreamResult(w));
            System.out.println(w.toString());
            ItemDiff diff = new ItemDiff(doc, new PrintWriter(System.err));
            loader.generate(new StringReader(w.toString()), diff);
            System.out.println("Different: " + diff.isDifferent());
            assertFalse(diff.isDifferent());
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (XMLException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (TransformerException ex) {
            ex.printStackTrace();
            if (ex.getCause() != null) {
                ex.getCause().printStackTrace();
            }
            fail(ex.getMessage());
        } catch (TransformerFactoryConfigurationError ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    public void testIdentityViaXSLT() {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            DocumentLoader loader = new SAXDocumentLoader();
            URL xsltLocation = getClass().getResource("xslt-identity.xsl");
            Document xsltDoc = loader.load(xsltLocation.toURI());
            org.w3c.dom.Document xsltDOM = new DOMDocumentProxy(xsltDoc);
            DOMSource xsltSource = new DOMSource(xsltDOM);
            xsltSource.setSystemId(xsltDOM.getBaseURI());
            Transformer id = factory.newTransformer(xsltSource);
            URL location = getClass().getResource("id-1.xml");
            Document doc = loader.load(location.toURI());
            DOMDocumentProxy proxy = new DOMDocumentProxy(doc);
            StringWriter w = new StringWriter();
            id.transform(new DOMSource(proxy), new StreamResult(w));
            System.out.println(w.toString());
            ItemDiff diff = new ItemDiff(doc, new PrintWriter(System.err));
            loader.generate(new StringReader(w.toString()), diff);
            System.out.println("Different: " + diff.isDifferent());
            assertFalse(diff.isDifferent());
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (XMLException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (TransformerException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (TransformerFactoryConfigurationError ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}

package ishima.sandbox;

import javax.xml.xpath.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.util.XMLCatalogResolver;

public class XPathEvaluator {

    public void evaluateDocument(File xmlDocument) {
        try {
            XMLCatalogResolver resolver = new XMLCatalogResolver();
            resolver.setCatalogList(new String[] { "D:\\mgr\\temp\\xhtml1-catalog.xml" });
            DOMParser parser = new DOMParser();
            parser.setProperty("http://apache.org/xml/properties/internal/entity-resolver", resolver);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            InputSource inputSource = new InputSource(new FileInputStream(xmlDocument));
            XPathExpression xPathExpression = xPath.compile("//td[@class]");
            Object result = xPathExpression.evaluate(inputSource, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("NODE: " + nodes.item(i).getAttributes().getNamedItem("class").getNodeValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) {
        XPathEvaluator evaluator = new XPathEvaluator();
        File xmlDocument = new File("D:\\mgr\\temp\\trove_list.php.xml");
        evaluator.evaluateDocument(xmlDocument);
        System.exit(0);
    }
}

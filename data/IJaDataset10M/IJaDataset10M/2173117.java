package genj.fo;

import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/** 
 * Format for XLSFO - a simple identity transformation 
 */
public class XSLFOFormat extends Format {

    /**
   * Constructor
   */
    public XSLFOFormat() {
        super("XSL-FO", "xml", true);
    }

    /**
   * Formatting logic 
   */
    protected void formatImpl(Document doc, OutputStream out) throws Throwable {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(doc.getDOMSource(), new StreamResult(out));
    }
}

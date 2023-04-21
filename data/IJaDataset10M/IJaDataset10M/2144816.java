package es.caib.zkib.jxpath.xml;

import java.io.InputStream;
import org.jdom.input.SAXBuilder;
import es.caib.zkib.jxpath.JXPathException;

/**
 * An implementation of the XMLParser interface that produces a JDOM Document.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.1 $ $Date: 2009-04-03 08:13:14 $
 */
public class JDOMParser extends XMLParser2 {

    public Object parseXML(InputStream stream) {
        if (!isNamespaceAware()) {
            throw new JXPathException("JDOM parser configuration error. JDOM " + "does not support the namespaceAware=false setting.");
        }
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setExpandEntities(isExpandEntityReferences());
            builder.setIgnoringElementContentWhitespace(isIgnoringElementContentWhitespace());
            builder.setValidation(isValidating());
            return builder.build(stream);
        } catch (Exception ex) {
            throw new JXPathException("JDOM parser error", ex);
        }
    }
}

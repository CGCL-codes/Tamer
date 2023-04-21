package net.sf.cybowmodeller.componenteditor.xml;

import java.util.Map;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import net.sf.cybowmodeller.util.logging.LoggerInstance;
import net.sf.cybowmodeller.util.xml.MathMLConstants;
import net.sf.cybowmodeller.util.xml.TransformerInstance;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 44 $
 */
public final class MMLContent2Presentation extends TransformerInstance {

    private static final String XSL_FILE_PATH = "xslt/mmlc2p.xsl";

    private static final MMLContent2Presentation instance = new MMLContent2Presentation();

    private MMLContent2Presentation() {
        super(ClassLoader.getSystemResource(XSL_FILE_PATH).toString());
    }

    public static void convert(final Element content, final Map<String, Element> symbolMap, final Node target) {
        try {
            translate(replaceVariables(content, symbolMap), target);
        } catch (TransformerException e) {
            LoggerInstance.severe(null, e);
            throw new RuntimeException(e);
        }
    }

    public static void translate(final Node content, final Node target) throws TransformerException {
        instance.transform(new DOMSource(content), new DOMResult(target));
    }

    public static Element replaceVariables(final Element content, final Map<String, Element> symbolMap) {
        final Element math = (Element) content.cloneNode(true);
        final NodeList nodes = math.getElementsByTagNameNS(MathMLConstants.NS_MATHML, MathMLConstants.TAG_CI);
        for (int i = 0; i < nodes.getLength(); ) {
            final Element ci = (Element) nodes.item(i);
            final Element varmath = symbolMap.get(ci.getTextContent());
            if (varmath == null) {
                i++;
            } else {
                final Node mi = math.getOwnerDocument().importNode(getFirstChildElement(varmath), true);
                ci.getParentNode().replaceChild(mi, ci);
            }
        }
        return math;
    }

    private static Element getFirstChildElement(final Element element) {
        final NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) child;
            }
        }
        return null;
    }
}

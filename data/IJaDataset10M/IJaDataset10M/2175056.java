package common.devbot.generator;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import common.devbot.util.xml.XMLException;
import common.devbot.util.xml.XMLUtils;

/**
 * @author T Decoster.
 *
 */
public class XMLCodeTemplate extends CodeTemplate {

    /**
	 * @param file the source file
	 */
    public XMLCodeTemplate(final File file) {
        try {
            Document doc = XMLUtils.parseFile(file);
            this.setPathPattern(XMLUtils.getAttributeValue(doc, "/templateset/template/path@pattern"));
            this.setFileNamePattern(XMLUtils.getAttributeValue(doc, "/templateset/template/name@pattern"));
            this.setContent(XMLUtils.getNodeText(doc, "/templateset/template/content"));
        } catch (XMLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param node the source node
	 */
    public XMLCodeTemplate(final Node node) {
        this.setPathPattern(XMLUtils.getAttributeValue(node, "/path@pattern"));
        this.setFileNamePattern(XMLUtils.getAttributeValue(node, "/name@pattern"));
        this.setContent(XMLUtils.getNodeText(node, "/content"));
    }
}

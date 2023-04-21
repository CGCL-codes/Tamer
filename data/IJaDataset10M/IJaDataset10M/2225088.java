package net.jwiki.engine.exporter.support.transformer;

import org.w3c.dom.*;

/**
 * User: Armond Avanes
 */
public class CloneTransformer implements Transformer {

    public String getName() {
        return "Clone Transformer";
    }

    public String getDescription() {
        return "This transformer does nothing but to clone/duplicate the same node! Useful when there is no transformer defined for a specific node type.";
    }

    public Node doTransform(Node source, Document destDoc) {
        String srcTag = source.getNodeName().toString().toLowerCase();
        Node result = null;
        if (source instanceof Element) {
            NamedNodeMap attrs = source.getAttributes();
            result = destDoc.createElement(srcTag);
            for (int i = 0; i < attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                ((Element) result).setAttribute(attr.getName(), attr.getValue());
            }
        } else if (source instanceof Text) result = destDoc.createTextNode(((Text) source).getNodeValue());
        return result;
    }
}

package org.catapult.web;

import java.util.Map;
import org.catapult.el.Expression;
import org.springframework.util.Assert;

public class DefaultRenderer implements Renderer {

    public void encode(ResponseWriter writer, Node node) throws Exception {
        Assert.notNull(writer);
        if (node instanceof Text) {
            encodeText(writer, (Text) node);
        } else if (node instanceof Element) {
            encodeBegin(writer, (Element) node);
            encodeChildren(writer, (Element) node);
            encodeEnd(writer, (Element) node);
        } else if (node instanceof Document) {
            encodeDocument(writer, (Document) node);
        }
    }

    protected void encodeDocument(ResponseWriter writer, Document node) throws Exception {
        writer.startDocument();
        node.getDocumentElement().encode(writer);
        writer.endDocument();
    }

    protected void encodeBegin(ResponseWriter writer, Element node) throws Exception {
        writer.startElement(node.getName());
        for (Map.Entry<String, Expression> attr : node.getAttributes().entrySet()) {
            if (attr.getValue() != null) {
                Expression expression = attr.getValue();
                Object value = expression.getValue(node.getContext());
                if (value != null) {
                    writer.writeAttribute(attr.getKey(), value.toString());
                }
            }
        }
    }

    protected void encodeChildren(ResponseWriter writer, Element node) throws Exception {
        for (Node child : node.getChildNodes()) {
            child.encode(writer);
        }
    }

    protected void encodeEnd(ResponseWriter writer, Element node) throws Exception {
        writer.endElement(node.getName());
    }

    protected void encodeText(ResponseWriter writer, Text node) throws Exception {
        Object value = node.value().getValue(node.getContext());
        if (value != null) {
            writer.writeText(value.toString());
        }
    }
}

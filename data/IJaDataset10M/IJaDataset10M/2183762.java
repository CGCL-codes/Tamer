package net.toolkit.xml;

import java.util.ArrayList;
import java.util.List;
import net.toolkit.xml.XmlElement.Attribute;

/**
 * @author wilfried
 * 
 */
class ToStringVisitor implements Visitor {

    /**
	 * 
	 */
    private static final String XML_HEADER = "<?xml version=\"%s\" encoding=\"%s\"?>";

    /**
	 * 
	 */
    private static final String CDATA = "<![CDATA[%s]]>";

    /**
	 * 
	 */
    private List<XmlElement> history = new ArrayList<XmlElement>();

    /**
	 * 
	 */
    private StringBuilder builder = new StringBuilder();

    @Override
    public void visit(XmlDocument document) {
        builder.append(String.format(XML_HEADER, document.getVersion(), document.getEncoding()));
    }

    @Override
    public void visit(XmlElement element) {
        if (history.size() > 0 && element.equals(history.get(history.size() - 1))) {
            closeElement(element.getName());
        } else {
            builder.append("<");
            if (!Utils.isEmpty(element.getPrefix())) {
                builder.append(element.getPrefix());
                builder.append(":");
            }
            builder.append(element.getName());
            if (element.isRoot()) {
                appendNamespaces(element);
                appendSchemaLocations(element);
            }
            appendAttributes(element);
            if (element.hasChildren()) {
                builder.append(">");
                history.add(element);
            } else {
                builder.append("/>");
            }
        }
    }

    @Override
    public void visit(Text text) {
        if (text.isCdata()) builder.append(String.format(CDATA, text.getContent())); else builder.append(text.getContent());
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    /**
	 * @param elementName
	 */
    private void closeElement(String elementName) {
        history.remove(history.size() - 1);
        builder.append("</");
        builder.append(elementName);
        builder.append(">");
    }

    /**
	 * @param e
	 */
    private void appendNamespaces(XmlElement e) {
        for (Namespace n : e.getNamespaces()) {
            builder.append(Utils.SPACE_STR);
            builder.append("xmlns:");
            if (n.getPrefix() != null) {
                builder.append(n.getPrefix());
            }
            builder.append("=\"");
            builder.append(n.getURLAsString());
            builder.append("\"");
        }
    }

    /**
	 * @param e
	 */
    private void appendSchemaLocations(XmlElement e) {
        if (e.getSchemaLocations().size() > 0) {
            builder.append(Utils.SPACE_STR);
            builder.append("schemaLocations=\"");
            for (String str : e.getSchemaLocations()) {
                builder.append(str);
                builder.append(Utils.SPACE_STR);
            }
            builder.append("\"");
        }
    }

    /**
	 * @param e
	 */
    private void appendAttributes(XmlElement e) {
        for (Attribute a : e.getAttributes()) {
            builder.append(Utils.SPACE_STR);
            builder.append(a.getName());
            builder.append("=\"");
            builder.append(a.getValue());
            builder.append("\"");
        }
    }
}

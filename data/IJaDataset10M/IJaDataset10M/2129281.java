package com.knowgate.misc;

import org.w3c.dom.*;

/**
 * JavaScriptCleaner.java
 *
 * This class removes hopefully all of the possible malicious code from HTML messages
 * like <SCRIPT> tags, javascript: hrefs and onMouseOver, ...;
 *
 * Furthermore, we should consider removing all IMG tags as they might be used to call CGIs
 *
 * Created: Mon Jan  1 15:20:54 2001
 *
 * @author Sebastian Schaffert
 * @version
 */
public class JavaScriptCleaner {

    Document d;

    public JavaScriptCleaner(Document d) {
        this.d = d;
        walkTree(d.getDocumentElement());
    }

    protected void walkTree(Node node) {
        if (node instanceof Element && ((Element) node).getTagName().toUpperCase().equals("SCRIPT")) {
            ((Element) node).setAttribute("malicious", "Marked malicious because of potential JavaScript abuse");
        }
        if (node instanceof Element && ((Element) node).getTagName().toUpperCase().equals("IMG")) {
            ((Element) node).setAttribute("malicious", "Marked malicious because of potential Image/CGI abuse");
        }
        if (node instanceof Element && ((Element) node).getTagName().toUpperCase().equals("FORM")) {
            ((Element) node).setAttribute("malicious", "Marked malicious because of potential JavaScript abuse");
        }
        String javascript_href = "javascript";
        NamedNodeMap map = node.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Attr a = (Attr) map.item(i);
            if (a.getName().toUpperCase().equals("HREF")) {
                for (int j = 0; j < a.getValue().length() - javascript_href.length(); j++) {
                    if (a.getValue().regionMatches(true, j, javascript_href, 0, javascript_href.length())) {
                        ((Element) node).setAttribute("malicious", "Marked malicious because of potential JavaScript abuse (HREF attribute contains javascript code)");
                        break;
                    }
                }
            } else if (a.getName().toUpperCase().startsWith("ON")) {
                ((Element) node).setAttribute("malicious", "Marked malicious because of potential JavaScript abuse (element contains script events)");
            }
        }
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                walkTree(nl.item(i));
            }
        }
    }
}

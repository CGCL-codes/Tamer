package gov.lanl.IDViewer;

import nu.xom.*;

/**
 * The class <code>XMLToTrait</code> takes a trait that's represented by a DOM
 * tree and converts it to a stringified representation.
 *
 * @author  Torsten Staab
 * @version 09/12/99  $Id: XMLToTrait.java 3352 2006-03-28 01:28:49Z dwforslund $
 *
 */
public class XMLToTrait {

    public XMLToTrait() {
    }

    /**
     * Converts a trait that's represented by a DOM tree to a stringified version
     *
     * @param currNode  the current DOM tree top-level node
     *
     * @return  the stringified DOM tree trait
     */
    public String convertXMLToTrait(Node currNode) {
        String currLeafDelimiter = "";
        String concatenatedLeaves = "";
        if (currNode == null) return null;
        if (currNode instanceof Element) {
            Element el = (Element) currNode;
            if (el.getLocalName().equalsIgnoreCase("node")) {
                int len = el.getAttributeCount();
                for (int i = 0; i < len; i++) if (el.getAttribute(i).getLocalName().equalsIgnoreCase("child_delim")) {
                    currLeafDelimiter = el.getAttribute(i).getValue();
                    break;
                }
            }
            int len = el.getChildCount();
            for (int i = 0; i < len; i++) {
                concatenatedLeaves += convertXMLToTrait(el.getChild(i));
                if (i + 1 < len) concatenatedLeaves += currLeafDelimiter;
            }
            return concatenatedLeaves;
        } else if (currNode instanceof Text) {
            return currNode.getValue();
        }
        return "";
    }
}

package net.sf.cybowmodeller.util.xml;

import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 4 $
 */
public class ElementList implements Iterable<Element> {

    private static class ElementIterator implements Iterator<Element> {

        private final NodeList nodes;

        private int index;

        public ElementIterator(final NodeList nodeList) {
            nodes = nodeList;
        }

        public boolean hasNext() {
            for (; index < nodes.getLength(); index++) {
                if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE) {
                    return true;
                }
            }
            return false;
        }

        public Element next() {
            for (; index < nodes.getLength(); index++) {
                final Node node = nodes.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    index++;
                    return (Element) node;
                }
            }
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final NodeList nodes;

    public ElementList(NodeList nodeList) {
        nodes = nodeList;
    }

    public Iterator<Element> iterator() {
        return new ElementIterator(nodes);
    }
}

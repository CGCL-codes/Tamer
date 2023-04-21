package jlibs.xml.sax.dog.path.tests;

import jlibs.xml.sax.dog.NodeType;
import jlibs.xml.sax.dog.path.Constraint;
import jlibs.xml.sax.dog.sniff.Event;

/**
 * @author Santhosh Kumar T
 */
public final class NamespaceURI extends Constraint {

    public final String namespaceURI;

    public NamespaceURI(int id, String namespaceURI) {
        super(id);
        this.namespaceURI = namespaceURI;
    }

    @Override
    public boolean matches(Event event) {
        switch(event.type()) {
            case NodeType.ELEMENT:
            case NodeType.ATTRIBUTE:
                return namespaceURI.equals(event.namespaceURI());
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s}*", namespaceURI);
    }
}

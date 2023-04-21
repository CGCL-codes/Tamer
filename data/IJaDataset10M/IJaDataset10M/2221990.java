package xmitools.umllinker;

import org.w3c.dom.Element;
import xmitools.xmilinker.XmiLinkerElement;
import xmitools.xmilinker.XmiLinkerFactoryImp;

/**
 * @author rlechner
 * 
 * The implementation of the <code>UML 1.4 XMI 1.1</code> linker factory.
 */
public class UmlLinkerFactory11Imp extends XmiLinkerFactoryImp {

    public XmiLinkerElement createLinkerElement(Element element, XmiLinkerElement parent) {
        return new UmlLinkerElement11Imp(element, parent, this);
    }

    public String getXmiVersion() {
        return "1.1";
    }
}

package net.sf.webwarp.util.xml;

/**
 * 
 * @author bse
 */
public class DomConverter {

    /** Creates a new instance of Converter */
    private DomConverter() {
    }

    public static org.dom4j.Element convertToDom4j(org.jdom.Element element) {
        org.jdom.Document document = new org.jdom.Document();
        document.setRootElement((org.jdom.Element) element.clone());
        return convertToDom4j(document).getRootElement();
    }

    public static org.dom4j.Document convertToDom4j(org.jdom.Document document) {
        try {
            org.jdom.output.DOMOutputter outputter = new org.jdom.output.DOMOutputter();
            org.w3c.dom.Document w3cDomDocument = outputter.output(document);
            org.dom4j.io.DOMReader reader = new org.dom4j.io.DOMReader();
            return reader.read(w3cDomDocument);
        } catch (org.jdom.JDOMException ex) {
            throw new RuntimeException("Unexpected exception while converting document.", ex);
        }
    }
}

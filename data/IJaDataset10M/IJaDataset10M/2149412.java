package manager.plugins;

import manager.gui.widgets.XMLRootItem;
import manager.util.XMLString;

/**
 * Diese Klasse erm�glicht es in Plugin leicht Metadaten zu speichern.
 *
 * @author Soenke Brummerloh
 */
public class Metadata {

    XMLString xml;

    public Metadata() {
        xml = XMLRootItem.createNewMetadata("").toXML();
    }

    /**
     * Setzt einen Wert.
     *
     * @param path  der Pfad innerhalb einer Metadata-XML-Datei. Die Pfadelemente sind durch Punkte voneinander getrennt.
     * @param value der zu setzende Wert
     */
    public void setValue(String path, String value) {
        org.jdom.Element element = xml.addPath(path);
        element.setText(value);
    }

    /**
     * F�gt unter einem bestimmten Pfad ein neues Element ein und setzt f�r dieses einen Wert.
     *
     * @param path        der Pfad
     * @param elementName der Elementname
     * @param value       der zusetzende Wert
     */
    public void addValue(String path, String elementName, String value) {
        org.jdom.Element parentElement = xml.addPath(path);
        org.jdom.Element childElement = new org.jdom.Element(elementName);
        childElement.setText(value);
        parentElement.addContent(childElement);
    }

    /**
     * F�gt ein neues Element-Objekt als XML unter dem angegebenen Pfad ein.
     *
     * @param path    der Pfad
     * @param element das neue Element
     */
    public void addValue(String path, Element element) {
        org.jdom.Element parentElement = xml.addPath(path);
        org.jdom.Element childElement = element.getXml().getXMLElement();
        parentElement.addContent(childElement);
    }

    /**
     * Gibt die Metadaten als XML-String zur�ck.
     *
     * @return der XML-String
     */
    public XMLString getXml() {
        return xml;
    }
}

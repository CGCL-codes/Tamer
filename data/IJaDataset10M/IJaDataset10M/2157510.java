package adnotatio.common.xml.gwt;

import adnotatio.common.xml.XMLDocument;
import adnotatio.common.xml.XMLElement;
import adnotatio.common.xml.XMLName;
import adnotatio.common.xml.XMLText;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;

/**
 * @author kotelnikov
 */
public class GwtXmlDocument extends XMLDocument {

    /**
     * 
     */
    public GwtXmlDocument(Document doc) {
        super(doc);
    }

    /**
     * @see adnotatio.common.xml.XMLDocument#createElement(java.lang.Object,
     *      adnotatio.common.xml.XMLName)
     */
    protected XMLElement createElement(Object node, XMLName name) {
        Document doc = (Document) node;
        Element element = doc.createElement(name.getName());
        return new GwtXmlElement(this, element);
    }

    /**
     * @see adnotatio.common.xml.XMLDocument#createTextNode(java.lang.Object,
     *      java.lang.String)
     */
    protected XMLText createTextNode(Object doc, String content) {
        Text node = ((Document) doc).createTextNode(content);
        return new GwtXmlText(this, node);
    }

    /**
     * @see adnotatio.common.xml.XMLDocument#getRootElement(java.lang.Object)
     */
    protected XMLElement getRootElement(Object doc) {
        Document d = (Document) doc;
        Element element = d.getDocumentElement();
        return new GwtXmlElement(this, element);
    }

    /**
     * @see adnotatio.common.xml.XMLDocument#setRootElement(java.lang.Object,
     *      java.lang.Object)
     */
    protected void setRootElement(Object doc, Object root) {
        Document d = (Document) doc;
        d.appendChild((Element) root);
    }
}

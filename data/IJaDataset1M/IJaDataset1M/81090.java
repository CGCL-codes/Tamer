package nl.fountain.xelem.excel;

import java.util.ArrayList;
import java.util.List;
import nl.fountain.xelem.GIO;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * An abstract implementation of an XLElement.
 */
public abstract class AbstractXLElement implements XLElement {

    private List<String> comments;

    private boolean printComments;

    protected AbstractXLElement() {
    }

    /**
     * Add a comment to this XLElement. Whether the comment will be printed
     * in the xml produced during assembly, depends on the setting of the
     * Workbook's 
     * {@link nl.fountain.xelem.excel.Workbook#setPrintElementComments(boolean)}-method.
     * 
     * @param 	comment the comment to be added.
     */
    public void addElementComment(String comment) {
        if (comments == null) {
            comments = new ArrayList<String>();
        }
        comments.add(comment);
    }

    /**
     * Gets the list of added comments.
     * 
     * @return 	a list of Strings.
     */
    public List<String> getElementComments() {
        return comments;
    }

    /**
     * Does effectively nothing.
     */
    public void setAttributes(Attributes attrs) {
    }

    /**
     * Does effectively nothing.
     */
    public void setChildElement(String localName, String content) {
    }

    /**
     * Creates an {@link org.w3c.dom.Element} with the tag-name, namespace and
     * prefix suitable for the calling XLElement-implementation. 
     * If comments were set on
     * the calling XLElement-subclass and comments should be printed according to
     * the <code>gio</code>, these comments are appended to the returned element.
     * 
     * @param 	doc the enveloping Document
     * @param 	gio	a global information object
     * 
     * @return 	an Element suitable for the calling XLElement-implementation.
     */
    protected Element assemble(Document doc, GIO gio) {
        Element element = doc.createElementNS(getNameSpace(), getTagName());
        element.setPrefix(getPrefix());
        if (gio.isPrintingComments() && getElementComments() != null) {
            for (String s : getElementComments()) {
                element.appendChild(doc.createComment(s));
            }
        }
        return element;
    }

    /**
     * Creates an {@link org.w3c.dom.Element} with the given <code>qName</code>
     * as it's qualified name. Namespace and
     * prefix of the returned element are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document
     * @param 	qName	the qualified name of the newly created element.
     * 
     * @return 	a newly created element.
     */
    protected Element createElementNS(Document doc, String qName) {
        Element n = doc.createElementNS(getNameSpace(), qName);
        n.setPrefix(getPrefix());
        return n;
    }

    /**
     * Creates an {@link org.w3c.dom.Element} with the given <code>qName</code>
     * as it's qualified name and the given <code>value</code> appended to
     * it as a {@link org.w3c.dom.Text}. 
     * Namespace and
     * prefix of the returned element are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document.
     * @param 	qName	the qualified name of the newly created element.
     * @param 	value	the data for the Node.
     * 
     * @return 	a newly created element.
     */
    protected Element createElementNS(Document doc, String qName, String value) {
        Element n = doc.createElementNS(getNameSpace(), qName);
        n.setPrefix(getPrefix());
        n.appendChild(doc.createTextNode(value));
        return n;
    }

    /**
     * Creates an {@link org.w3c.dom.Element} with the given <code>qName</code>
     * as it's qualified name and the given value of <code>i</code> appended to
     * it as a {@link org.w3c.dom.Text}. 
     * Namespace and
     * prefix of the returned element are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document.
     * @param 	qName	the qualified name of the newly created element.
     * @param 	i	the data for the Node.
     * 
     * @return 	a newly created element.
     */
    protected Element createElementNS(Document doc, String qName, int i) {
        Element n = doc.createElementNS(getNameSpace(), qName);
        n.setPrefix(getPrefix());
        n.appendChild(doc.createTextNode("" + i));
        return n;
    }

    /**
     * Creates an {@link org.w3c.dom.Element} with the given <code>qName</code>
     * as it's qualified name and the given value of <code>b</code> appended to
     * it as a {@link org.w3c.dom.Text}, translated as "True" or "False". 
     * Namespace and
     * prefix of the returned element are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document.
     * @param 	qName	the qualified name of the newly created element.
     * @param 	b	the data for the Node.
     * 
     * @return 	a newly created element.
     */
    protected Element createElementNS(Document doc, String qName, boolean b) {
        Element n = doc.createElementNS(getNameSpace(), qName);
        n.setPrefix(getPrefix());
        n.appendChild(doc.createTextNode(b ? "True" : "False"));
        return n;
    }

    /**
     * Creates an {@link org.w3c.dom.Attr} with the given <code>qName</code>
     * as it's qualified name and the given value of <code>i</code> as it's
     * value. 
     * Namespace and
     * prefix of the returned attribute are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document.
     * @param 	qName	the qualified name of the newly created attribute.
     * @param 	i	the value of the attribute.
     * 
     * @return 	a newly created attribute.
     */
    protected Attr createAttributeNS(Document doc, String qName, int i) {
        Attr attr = doc.createAttributeNS(getNameSpace(), qName);
        attr.setPrefix(getPrefix());
        attr.setValue("" + i);
        return attr;
    }

    /**
     * Creates an {@link org.w3c.dom.Attr} with the given <code>qName</code>
     * as it's qualified name and the given <code>value</code> as it's
     * value. 
     * Namespace and
     * prefix of the returned attribute are the same as those of the 
     * calling AbstractXLElement-subclass.
     * 
     * @param 	doc the enveloping Document.
     * @param 	qName	the qualified name of the newly created attribute.
     * @param 	value	the value of the attribute.
     * 
     * @return 	a newly created attribute.
     */
    protected Attr createAttributeNS(Document doc, String qName, String value) {
        Attr attr = doc.createAttributeNS(getNameSpace(), qName);
        attr.setPrefix(getPrefix());
        attr.setValue(value);
        return attr;
    }
}

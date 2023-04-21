package org.mobicents.eclipslee.util.slee.xml.ant;

import org.mobicents.eclipslee.util.slee.xml.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author cath
 */
public class AntPathXML extends XML {

    protected AntPathXML(Document document, Element root) {
        super(document, root);
    }

    public void setID(String id) {
        root.setAttribute("id", id);
    }

    public String getID() {
        return root.getAttribute("id");
    }

    public void setRefID(String id) {
        root.setAttribute("refid", id);
    }

    public String getRefID() {
        return root.getAttribute("refid");
    }

    public void addPathElement(String path) {
        Element child = addElement(root, "pathelement");
        child.setAttribute("location", path);
    }

    public void removePathElement(String path) {
        Element children[] = getNodes("path/pathelement");
        for (int i = 0; i < children.length; i++) {
            if (path.equals(children[i].getAttribute("location"))) {
                children[i].getParentNode().removeChild(children[i]);
                return;
            }
        }
    }

    public boolean containsPathElement(String path) {
        String[] elements = getPathElements();
        for (int i = 0; i < elements.length; i++) if (elements[i].equals(path)) return true;
        return false;
    }

    public String[] getPathElements() {
        Element children[] = getNodes("path/pathelement");
        String output[] = new String[children.length];
        for (int i = 0; i < children.length; i++) output[i] = children[i].getAttribute("location");
        return output;
    }
}

package uk.ac.reload.straker.datamodel.contentpackaging;

import java.util.Iterator;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModel;

/**
 * Resource type for Content Package
 * 
 * @author Phillip Beauvoir
 * @version $Id: CP_Organizations.java,v 1.3 2006/07/10 11:50:39 phillipus Exp $
 */
public class CP_Organizations extends CP_DataComponent {

    public static String XML_ELEMENT_NAME = "organizations";

    public static String IMAGE_NAME = ICON_ORGS;

    /**
     * Fields
     */
    private String _default;

    /**
     * Default constructor
     */
    public CP_Organizations(DataModel dataModel) {
        super(dataModel);
    }

    /**
     * Set the Default
     * @param s
     */
    public void setDefault(String s) {
        _default = s;
    }

    /**
     * @return The Default
     */
    public String getDefault() {
        return _default;
    }

    public String getImageName() {
        return IMAGE_NAME;
    }

    public String toString() {
        return getDescriptionMessage();
    }

    /**
     * Destroy this Component
     */
    public void dispose() {
        super.dispose();
    }

    public Element marshall2XML(Element parentElement) {
        Element element = super.marshall2XML(parentElement);
        if (getDefault() != null && getDefault().length() > 0) {
            element.setAttribute("default", getDefault());
        }
        DataComponent[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].marshall2XML(element);
        }
        return element;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        setDefault(element.getAttributeValue("default"));
        Iterator i = element.getChildren("organization").iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            CP_Organization org = new CP_Organization(getDataModel());
            addChild(org);
            org.unmarshallXML(e);
        }
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}

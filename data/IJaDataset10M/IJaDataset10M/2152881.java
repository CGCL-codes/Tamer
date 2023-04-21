package uk.ac.reload.straker.datamodel.contentpackaging;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModel;

/**
 * Resource type for Content Package
 * 
 * @author Phillip Beauvoir
 * @version $Id: CP_Resource.java,v 1.4 2006/07/10 11:50:40 phillipus Exp $
 */
public class CP_Resource extends CP_DataComponent {

    public static String XML_ELEMENT_NAME = "resource";

    public static String IMAGE_NAME = ICON_RESOURCE;

    /**
     * Fields
     */
    private String _type, _base, _href;

    /**
     * Dependencies
     */
    private ArrayList _dependencies = new ArrayList();

    /**
     * Default constructor
     */
    public CP_Resource(DataModel dataModel) {
        super(dataModel);
        setType("webcontent");
    }

    /**
     * Over-ride to ensure we have one
     * @return The id of the component
     */
    public String getIdentifier() {
        if (super.getIdentifier() == null) {
            setIdentifier(generateID("resource-"));
        }
        return super.getIdentifier();
    }

    /**
     * Set the Type
     * @param s
     */
    public void setType(String s) {
        _type = s;
    }

    /**
     * @return The Type
     */
    public String getType() {
        return _type;
    }

    /**
     * Set the Base
     * @param s
     */
    public void setBase(String s) {
        _base = s;
    }

    /**
     * @return The Base
     */
    public String getBase() {
        return _base;
    }

    /**
     * Set the href
     * @param s
     */
    public void setHREF(String s) {
        _href = s;
    }

    /**
     * @return The href
     */
    public String getHREF() {
        return _href;
    }

    /**
     * Add a Dependency component
     * @param dep
     */
    public void addDependency(CP_Dependency dep) {
        _dependencies.add(dep);
    }

    /**
     * remove a Dependency component
     * @param dep
     */
    public void removeDependency(CP_Dependency dep) {
        _dependencies.remove(dep);
    }

    /**
     * Clear the Dependencies
     */
    public void clearDependencies() {
        _dependencies.clear();
    }

    /**
     * @return Dependencies
     */
    public CP_Dependency[] getDependencies() {
        return (CP_Dependency[]) _dependencies.toArray(new CP_Dependency[_dependencies.size()]);
    }

    public String getImageName() {
        return IMAGE_NAME;
    }

    public String toString() {
        return _href == null ? getDescriptionMessage() : _href;
    }

    /**
     * Destroy this Component
     */
    public void dispose() {
        super.dispose();
        _dependencies.clear();
        _dependencies = null;
    }

    public Element marshall2XML(Element parentElement) {
        Element element = super.marshall2XML(parentElement);
        if (getType() != null && getType().length() > 0) {
            element.setAttribute("type", getType());
        }
        if (getBase() != null && getBase().length() > 0) {
            element.setAttribute("base", getBase());
        }
        if (getHREF() != null && getHREF().length() > 0) {
            element.setAttribute("href", getHREF());
        }
        if (_metadata != null) {
            _metadata.marshall2XML(element);
        }
        DataComponent[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].marshall2XML(element);
        }
        CP_Dependency[] deps = getDependencies();
        for (int i = 0; i < deps.length; i++) {
            deps[i].marshall2XML(element);
        }
        return element;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        setType(element.getAttributeValue("type"));
        setBase(element.getAttributeValue("base"));
        setHREF(element.getAttributeValue("href"));
        Element md = element.getChild("metadata");
        if (md != null) {
            getMetadata().unmarshallXML(md);
        }
        Iterator i = element.getChildren("file").iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            CP_File file = new CP_File(getDataModel());
            addChild(file);
            file.unmarshallXML(e);
        }
        i = element.getChildren("dependency").iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            CP_Dependency dep = new CP_Dependency(getDataModel());
            addDependency(dep);
            dep.unmarshallXML(e);
        }
    }

    public String getXMLElementName() {
        return XML_ELEMENT_NAME;
    }
}

package com.prolix.editor.resourcemanager.model;

import org.jdom.Element;
import com.prolix.editor.GLMImageRegistry;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LearningDesign;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemType;

public class ResourceTreeItemURL extends ResourceTreeItem {

    public static final String XMLTypeURL = "url";

    public static final String XML_URL = "url-ref";

    public static final String IMAGE = GLMImageRegistry.Network;

    private String url;

    public ResourceTreeItemURL(String label, ResourceTreeCategory parent, String url, LearningDesign ld) {
        super(label, IMAGE, parent, ld, ld.generateID("url-"));
        this.url = url;
        getParent().addResourceTreeItem(this);
    }

    protected ResourceTreeItemURL(ResourceTreeCategory parent) {
        super(parent);
    }

    public Object getResource() {
        return url;
    }

    /**
	 * TODO check???
	 */
    public String getResourceIdentifier() {
        return getId();
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getType() {
        return XMLTypeURL;
    }

    /**
	 * ====================================================================
	 * 
	 * added by SZ; modifications after discussion with PP
	 * 
	 * ====================================================================
	 */
    public ItemType createItemType() {
        ItemType type = super.createItemType();
        type.setIdentifierRef(this.getId());
        return type;
    }

    public ResourceTreeItem copyForOtherLD(LearningDesignDataModel lddm) {
        ResourceTreeCategory parent = lddm.getResourceRootCategory().findApplicableCategory(getParent());
        if (parent == null) {
            return null;
        }
        return new ResourceTreeItemURL(this.getLabel(), parent, getURL(), lddm.getLearningDesign());
    }

    /**
	 * ==============================================================================
	 * 
	 * marshall / unmarshall XML methods
	 * 
	 * ==============================================================================
	 */
    public Element marshall2XML(Element parentElement) {
        return marshall2XML(parentElement, false);
    }

    public Element marshall2XML(Element parentElement, boolean export) {
        Element element = super.marshall2XML(parentElement, export);
        element.setAttribute(XML_URL, getURL());
        return element;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        setURL(element.getAttributeValue(XML_URL));
        getParent().addResourceTreeItem(this);
    }
}

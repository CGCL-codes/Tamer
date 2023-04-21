package com.prolix.editor.resourcemanager.model;

import uk.ac.reload.straker.datamodel.IXMLMarshaller;

/**
 * @author zander
 *
 */
public interface IResourceTreeElement extends IXMLMarshaller {

    /**
	 * returns the element's parent
	 */
    public ResourceTreeCategory getParent();

    /**
	 * sets a new parent for the current element; used for moving elements between categories
	 * @param parent
	 */
    public void setParent(ResourceTreeCategory parent);

    public String getLabel();

    public void setLabel(String label);

    public String getImage();

    public void setImage(String image);
}

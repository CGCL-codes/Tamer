package org.springframework.webflow.engine;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.Annotated;

/**
 * A base class for all objects in the web flow system that support annotation using arbitrary properties. Mainly used
 * to ensure consistent configuration of properties for all annotated objects.
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public abstract class AnnotatedObject implements Annotated {

    /**
	 * The caption property name ("caption"). A caption is also known as a "short description" and may be used in a GUI
	 * tooltip.
	 */
    public static final String CAPTION_PROPERTY = "caption";

    /**
	 * The long description property name ("description"). A description provides additional, free-form detail about
	 * this object and might be shown in a GUI text area.
	 */
    public static final String DESCRIPTION_PROPERTY = "description";

    /**
	 * Additional properties further describing this object. The properties set in this map may be arbitrary.
	 */
    private LocalAttributeMap attributes = new LocalAttributeMap();

    public String getCaption() {
        return attributes.getString(CAPTION_PROPERTY);
    }

    public String getDescription() {
        return attributes.getString(DESCRIPTION_PROPERTY);
    }

    public AttributeMap getAttributes() {
        return attributes;
    }

    /**
	 * Sets the short description (suitable for display in a tooltip).
	 * @param caption the caption
	 */
    public void setCaption(String caption) {
        attributes.put(CAPTION_PROPERTY, caption);
    }

    /**
	 * Sets the long description.
	 * @param description the long description
	 */
    public void setDescription(String description) {
        attributes.put(DESCRIPTION_PROPERTY, description);
    }

    /**
	 * Returns the mutable attribute map for this annotated object. May be used to set attributes after construction.
	 */
    public MutableAttributeMap getAttributeMap() {
        return attributes;
    }
}

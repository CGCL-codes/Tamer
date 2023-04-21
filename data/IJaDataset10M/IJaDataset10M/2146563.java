package org.springframework.webflow.definition;

import org.springframework.webflow.core.collection.AttributeMap;

/**
 * An interface to be implemented by objects that are annotated with attributes they wish to expose to clients.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface Annotated {

    /**
	 * Returns a short summary of this object, suitable for display as an icon caption or tool tip.
	 * @return the caption
	 */
    public String getCaption();

    /**
	 * Returns a longer, more detailed description of this object.
	 * @return the description
	 */
    public String getDescription();

    /**
	 * Returns an immutable attribute map containing the attributes annotating this object. These attributes provide
	 * descriptive characteristics or properties that may affect object behavior.
	 * @return the attribute map
	 */
    public AttributeMap getAttributes();
}

package org.blueoxygen.cimande.template;

import org.blueoxygen.cimande.DefaultPersistent;
import org.blueoxygen.cimande.descriptors.Descriptor;
import org.blueoxygen.cimande.descriptors.Collection;

/**
 * @author Ikromy
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * @hibernate.class table="template_object_detail"
 */
public class TemplateObjectDetail extends DefaultPersistent {

    private String description;

    private Collection collection;

    private Descriptor descriptor;

    private TemplateObject templateObject;

    private int type;

    /**
	 * @hibernate.many-to-one column="template_object_id"
	 * @return Returns the templateObject.
	 */
    public TemplateObject getTemplateObject() {
        return templateObject;
    }

    /**
	 * @param descriptor The templateObject to set.
	 */
    public void setTemplateObject(TemplateObject templateObject) {
        this.templateObject = templateObject;
    }

    /**
	 * @hibernate.many-to-one column="descriptor_id"
	 * @return Returns the objDescriptor.
	 */
    public Descriptor getDescriptor() {
        return descriptor;
    }

    /**
	 * @param descriptor The descriptor to set.
	 */
    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
	 * @hibernate.many-to-one column="collection_id"
	 * @return Returns the collection.
	 */
    public Collection getCollection() {
        return collection;
    }

    /**
	 * @param moduleDescriptor The collection to set.
	 */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    /**
	 * @return Returns the description.
	 * @hibernate.property
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the type.
	 * @hibernate.property column="type_id" length="1"
	 */
    public int getType() {
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setType(int type) {
        this.type = type;
    }
}

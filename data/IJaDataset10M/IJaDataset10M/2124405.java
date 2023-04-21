package org.blueoxygen.obat;

import org.blueoxygen.cimande.DefaultPersistent32;

/**
 * @author intercitra
 * @hibernate.class table="customer_type"
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CustomerType extends DefaultPersistent32 {

    private String type;

    private String description;

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
	 * @hibernate.property
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setType(String type) {
        this.type = type;
    }
}

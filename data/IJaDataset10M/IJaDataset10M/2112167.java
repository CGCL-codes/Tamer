package net.jomper.shyfish.model.impl;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class BaseModel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

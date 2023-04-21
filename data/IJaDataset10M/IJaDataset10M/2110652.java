package com.avdheshyadav.p4j.jdbc.model;

import java.io.Serializable;
import com.avdheshyadav.p4j.common.ReflectionToStringBuilder;

/**
 * 
 * @author Avdhesh yadav
 */
public abstract class DTO implements Serializable {

    /**
	* Creates a New Data Transfer Object
	*/
    public DTO() {
    }

    /**
	 * 
	 */
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
	 * 
	 * @return Table Name
	 */
    public abstract String getTableName();
}

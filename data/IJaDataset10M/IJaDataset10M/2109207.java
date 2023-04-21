package com.cube42.util.parameter;

import com.cube42.util.exception.Cube42NullParameterException;

/**
 * Represents a parameter that is required to execute a program
 * 
 * @author		Matt Paulin
 * @version	$Id: Parameter.java,v 1.1 2003/08/27 20:39:23 zer0wing Exp $
 */
public class Parameter {

    /**
	 * The name of the parameter
	 */
    private String name;

    /**
	 * The value of the parameter
	 */
    private String value;

    /**
	 * The description of the parameter
	 */
    private String description;

    /**
	 * Constructs the parameter
	 * 
	 * @param	name			The name of the parameter
	 * @param	description	A description of the parameter
	 */
    public Parameter(String name, String description) {
        super();
        Cube42NullParameterException.checkNullInConstructor(name, "name", "Parameter");
        Cube42NullParameterException.checkNullInConstructor(description, "description", "Parameter");
        this.name = name;
        this.description = description;
    }

    /**
	 * @return
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param string
	 */
    public void setValue(String string) {
        value = string;
    }

    /**
	 * Returns true if the parameter is complete and ready for use
	 * 
	 * @return 	true	if the parameter is filled out properly
	 */
    public boolean isComplete() {
        if ((this.value == null) || (this.value.trim().length() < 1)) return false;
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Parameter)) {
            Parameter tempParameter = (Parameter) obj;
            if (this.getName().equals(tempParameter.getName())) return true;
        }
        return false;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    public String toString() {
        return this.getName();
    }
}

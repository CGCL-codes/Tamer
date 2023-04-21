package de.mse.mogwai.utils.erdesigner.types;

/**
 * A class representing a default value.
 * 
 * @author Mirko Sertic
 */
public class DefaultValue {

    private String m_name;

    private String m_definition;

    /**
	 * Construct a default value.
	 * 
	 * @param name
	 *            The value name name
	 * @param definition
	 *            The domain definition
	 */
    public DefaultValue(String name, String definition) {
        this.m_name = name;
        this.m_definition = definition;
    }

    /**
	 * Get the default value name.
	 * 
	 * @return The domain name
	 */
    public String getName() {
        return this.m_name;
    }

    /**
	 * Set the value name.
	 * 
	 * @param name
	 *            The new domain name
	 */
    public void setName(String name) {
        this.m_name = name;
    }

    /**
	 * Get the default value definition
	 * 
	 * @return The domain definiton
	 */
    public String getDefinition() {
        return this.m_definition;
    }

    /**
	 * Set the value defintion
	 * 
	 * @param definition
	 *            The new domain definition
	 */
    public void setDefinition(String definition) {
        this.m_definition = definition;
    }

    /**
	 * Get the string representation of this default value
	 * 
	 * @return The string representation
	 */
    public String toString() {
        return this.m_name;
    }
}

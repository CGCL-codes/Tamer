package org.tzi.ugt.model.expression;

import org.tzi.ugt.main.Exception;

/**
 * Represents a parameter in the ugt data model.
 * 
 * @author lschaps
 */
public class Parameter {

    private String m_Name;

    private String m_Type;

    private Expression m_Value;

    /**
	 * Constructs the parameter.
	 * 
	 * @param in_Name
	 *            The name of the parameter.
	 * @param in_Type
	 *            The type of the parameter.
	 * @param in_Value
	 *            The values of the parameter.
	 */
    Parameter(String in_Name, String in_Type, Expression in_Value) {
        m_Name = in_Name;
        m_Type = in_Type;
        m_Value = in_Value;
    }

    /**
	 * Returns the name of the parameter.
	 * 
	 * @return The name of the parameter.
	 */
    public String getName() {
        return m_Name;
    }

    /**
	 * Returns the values of the parameter,
	 * 
	 * @return The value od the parameter.
	 */
    public Expression getValue() {
        return m_Value;
    }

    /**
	 * Returns the type of the parameter.
	 * 
	 * @return The type of the parameter.
	 */
    public String getType() {
        return m_Type;
    }

    /**
	 * Sets the type of the parameter.
	 * 
	 * @param in_Type
	 * 
	 * @throws Exception
	 */
    public void setType(String in_Type) throws Exception {
        if (null == m_Type) {
            m_Type = in_Type;
        } else {
            if (0 != m_Type.compareTo(in_Type)) {
                throw new Exception("Parameter has already a different type");
            }
        }
    }
}

package org.gridbus.workflow.common;

import org.gridbus.broker.constants.VariableType;

/**
 * 
 * @author Luyao Wu
 */
public class WfConsVariable {

    private long id;

    private String name;

    private int type;

    private String value;

    private WfConsVariable() {
        this("", VariableType.STRING, null);
    }

    /**
	 * Constructs variable with the given name.
	 * 
	 * @param name
	 */
    public WfConsVariable(String name) {
        this(name, VariableType.STRING, null);
    }

    /**
	 * Constructs variable with the given name and its type.
	 * 
	 * @param name
	 * @param type
	 */
    public WfConsVariable(String name, int type) {
        this(name, type, null);
    }

    /**
	 * @param name
	 * @param type
	 * @param value
	 */
    public WfConsVariable(String name, int type, String value) {
        super();
        setName(name);
        setType(type);
        setValue(value);
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets the name of this variable.
	 * 
	 * @return this variables name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the type.
	 */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
	 * @return Returns the value.
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value
	 *            The value to set.
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return
	 */
    public WfVariable duplicate() {
        WfVariable var = new WfVariable(this.name, this.type, this.value);
        return var;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        String s = "Variable: id =" + id + ", name = " + name + ", value = " + value + ", type = " + type;
        return s;
    }
}

package com.netflexitysolutions.amazonws.ec2;

public class LaunchPermission {

    public enum Type {

        USER, GROUP
    }

    private Type type;

    private String value;

    /**
	 * 
	 */
    public LaunchPermission() {
    }

    /**
	 * @param type
	 * @param value
	 */
    public LaunchPermission(Type type, String value) {
        super();
        this.type = type;
        this.value = value;
    }

    /**
	 * @return the type
	 */
    public Type getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(Type type) {
        this.type = type;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }
}

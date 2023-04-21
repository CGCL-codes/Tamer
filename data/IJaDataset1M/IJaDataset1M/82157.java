package org.ericliu.serializable;

public class SerialObject implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4330077800915813112L;

    String name = "";

    int age = 0;

    String desc = "";

    public SerialObject(String name, int age, String desc) {
        this.name = name;
        this.age = age;
        this.desc = desc;
    }
}

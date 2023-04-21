package org.poimodel.entity;

/**
 * Created by IntelliJ IDEA.
 * User: sedov
 * Date: 05.06.2009
 * Time: 11:20:54
 * To change this template use File | Settings | File Templates.
 */
public class Property {

    public static final String STRING = "String";

    public static final String INTEGER = "Integer";

    public static final String DOUBLE = "Double";

    public static final String FLOAT = "Float";

    public static final String DATE = "Date";

    public static final String BOOLEAN = "Boolean";

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package jdbframework.action.view;

import java.io.Serializable;

/**
 *
 * @author korotindv
 */
public class Parameter implements Serializable {

    public static final String IN = "in";

    public static final String OUT = "out";

    public static final String IN_OUT = "inout";

    private String name;

    private String type;

    private String datatype;

    private String value;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        if (type.equals("")) type = IN;
        if (!type.equals(IN) && !type.equals(OUT) && !type.equals(IN_OUT)) throw new RuntimeException("Incorrect type \"" + type + "\"!");
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDatatype() {
        return this.datatype;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public Parameter() {
    }
}

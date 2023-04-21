package org.iqual.chaplin;

/**
 * A utility class used during the code injection phase which holds information about a field. 
 * <p/>
 * @author Zbynek Slajchrt
 * @since Mar 15, 2009 11:12:42 PM
 */
public class FieldDescriptor {

    final String name;

    final String desc;

    final String signature;

    public FieldDescriptor(String name, String desc, String signature) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSignature() {
        return signature;
    }
}

package it.javalinux.wise.jaxCore;

import java.lang.reflect.Type;

/**
 * Holds single parameter's data required for an invocation
 * 
 * @author stefano.maestri@javalinux.it
 * 
 * @since 23-Aug-2007
 */
public class WebParameter {

    private Type type;

    private String name;

    public WebParameter(Type type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

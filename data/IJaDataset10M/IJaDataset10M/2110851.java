package org.drarch.diagram.DiagramModel.componentModel;

/**
 * 
 * @author nicolasfrontini@gmail.com (Nicolas Frontini)
 * @author maldonadofacundo@gmail.com (Facundo Maldonado)
 */
public class Port {

    private String name;

    private Interface required;

    private Interface provided;

    private Component component;

    public Port(Component c) {
        this.name = "noname";
        this.component = c;
        this.provided = null;
        this.required = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public Interface getProvided() {
        return provided;
    }

    public void setProvided(Interface provideds) {
        this.provided = provideds;
    }

    public Interface getRequired() {
        return required;
    }

    public void setRequired(Interface requireds) {
        this.required = requireds;
    }

    public boolean hasProvidedInterface() {
        if (provided == null) return false;
        return true;
    }

    public boolean hasRequiredInterface() {
        if (required == null) return false;
        return true;
    }
}

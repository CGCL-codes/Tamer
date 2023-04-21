package org.springframework.faces.ui;

/**
 * Component that uses the Dojo implementation of Spring JavaScript to decorate a child input component with client-side
 * text validation behavior.
 * 
 * @author Jeremy Grelle
 * 
 */
public class DojoClientTextValidator extends DojoDecoration {

    private static final String DOJO_COMPONENT_TYPE = "dijit.form.ValidationTextBox";

    protected String[] getDojoAttributes() {
        return DojoDecoration.DOJO_ATTRS;
    }

    public String getDojoComponentType() {
        return DOJO_COMPONENT_TYPE;
    }
}

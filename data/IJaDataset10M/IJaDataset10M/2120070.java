package net.ar.guia.own.implementation;

import net.ar.guia.own.interfaces.*;

public class DefaultButtonComponent extends DefaultVisualComponent implements ButtonComponent {

    public DefaultButtonComponent() {
        setModel(new DefaultButton());
    }

    public DefaultButtonComponent(String aText) {
        this();
        setCaption(aText);
    }

    public ButtonGroupComponent getButtonGroup() {
        return getButton().getButtonGroup();
    }

    public String getCaption() {
        return getButton().getCaption();
    }

    public void setButtonGroup(ButtonGroupComponent aGroup) {
        getButton().setButtonGroup(aGroup);
    }

    public void setCaption(String aString) {
        getButton().setCaption(aString);
    }

    public boolean isSelected() {
        return getButton().isSelected();
    }

    public void setSelected(boolean isSelected) {
        getButton().setSelected(isSelected);
    }

    protected Button getButton() {
        return (Button) getModel();
    }
}

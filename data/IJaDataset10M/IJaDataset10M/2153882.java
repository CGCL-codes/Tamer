package it.eng.bxmodeller.application;

import org.jdom.Attribute;
import org.jdom.Element;

public class JndiName {

    private String jndiName = null;

    public void generateClass(Element elementoAppoggio) {
        this.setJndiName(elementoAppoggio.getText());
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public Element generateXPDL() {
        Element item = new Element("JndiName");
        if (jndiName != null) item.setText(jndiName);
        return item;
    }
}

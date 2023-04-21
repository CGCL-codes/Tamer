package it.eng.bxmodeller;

import org.jdom.Element;

public class Responsible {

    private String responsible = null;

    public void generateClass(Element elementoAppoggio) {
        if (elementoAppoggio.getAttributeValue("Responsible") != null) this.setResponsible(elementoAppoggio.getAttributeValue("Responsible"));
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public Element generateXPDL() {
        Element item = new Element("Responsible");
        item.setText(responsible);
        return item;
    }
}

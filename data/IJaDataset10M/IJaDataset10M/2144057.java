package org.tubs.epoc.SMFF.ModelElements.Timing;

import org.jdom.Element;

public class EventActivation extends AbstractActivationPattern {

    public static String ActivationPatternName = "EventActivation";

    public EventActivation() {
        super();
    }

    public EventActivation(Element e) {
        super();
    }

    @Override
    public Element toXML() {
        Element root = new Element("ActivationPattern");
        root.setAttribute("classname", this.getClass().getName());
        root.setAttribute("name", ActivationPatternName);
        return root;
    }
}

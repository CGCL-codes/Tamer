package org.itsnat.feashow.features.comp.labels;

import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonCustomLabelRenderer implements ItsNatLabelRenderer {

    public PersonCustomLabelRenderer() {
    }

    public void renderLabel(ItsNatLabel label, Object value, Element labelElem, boolean isNew) {
        Person person = (Person) value;
        ItsNatDocument itsNatDoc = label.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element firstNameElem = doc.getElementById("firstNameId");
        ItsNatDOMUtil.setTextContent(firstNameElem, person.getFirstName());
        Element lastNameElem = doc.getElementById("lastNameId");
        ItsNatDOMUtil.setTextContent(lastNameElem, person.getLastName());
    }

    public void unrenderLabel(ItsNatLabel label, Element labelElem) {
    }
}

package it.eng.bxmodeller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;

public class Participant {

    private ParticipantType participantType = null;

    private String id = null;

    private String name = null;

    private Description description = null;

    private ExternalReference externalReference = null;

    private ArrayList extendedAttributes = null;

    private String classFather = null;

    public void generateClass(Element elementoAppoggio) {
        extendedAttributes = new ArrayList();
        extendedAttributes.clear();
        if (elementoAppoggio.getAttributeValue("Id") != null) {
            this.setId(elementoAppoggio.getAttributeValue("Id"));
        }
        if (elementoAppoggio.getAttributeValue("Name") != null) {
            this.setName(elementoAppoggio.getAttributeValue("Name"));
        }
        List elencoFigli = elementoAppoggio.getChildren();
        Iterator iteratore = elencoFigli.iterator();
        Element elementoAppoggioFigli = null;
        String nomeTagAppoggio = "";
        while (iteratore.hasNext()) {
            elementoAppoggioFigli = (Element) iteratore.next();
            nomeTagAppoggio = elementoAppoggioFigli.getName();
            if (nomeTagAppoggio.equals("ParticipantType")) {
                ParticipantType tmp = new ParticipantType();
                tmp.generateClass(elementoAppoggioFigli);
                this.setParticipantType(tmp);
            } else if (nomeTagAppoggio.equals("Description")) {
                Description tmp = new Description();
                tmp.generateClass(elementoAppoggioFigli);
                this.setDescription(tmp);
            } else if (nomeTagAppoggio.equals("ExternalReference")) {
                ExternalReference tmp = new ExternalReference();
                tmp.generateClass(elementoAppoggioFigli);
                this.setExternalReference(tmp);
            } else if (nomeTagAppoggio.equals("ExtendedAttributes")) {
                List elencoAttributi = elementoAppoggioFigli.getChildren();
                Iterator iteratoreAttributi = elencoAttributi.iterator();
                Element elementoAppoggioVendor = null;
                while (iteratoreAttributi.hasNext()) {
                    elementoAppoggioVendor = (Element) iteratoreAttributi.next();
                    ExtendedAttribute tmp = new ExtendedAttribute();
                    tmp.generateClass(elementoAppoggioVendor);
                    extendedAttributes.add(tmp);
                }
            }
        }
    }

    public ExternalReference getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(ExternalReference externalReference) {
        this.externalReference = externalReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(ParticipantType participantType) {
        this.participantType = participantType;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        if (description.getDescription().equals("")) this.description = null; else this.description = description;
    }

    public ArrayList getExtendedAttributes() {
        return extendedAttributes;
    }

    public void setExtendedAttributes(ArrayList extendedAttributes) {
        this.extendedAttributes = extendedAttributes;
    }

    public String getClassFather() {
        return classFather;
    }

    public void setClassFather(String classFather) {
        this.classFather = classFather;
    }

    public Element generateXPDL() {
        Element item = new Element("Participant");
        Attribute x_id = new Attribute("Id", this.id);
        Attribute x_name = new Attribute("Name", this.name);
        item.setAttribute(x_id);
        item.setAttribute(x_name);
        if (description != null) item.addContent(description.generateXPDL());
        if (externalReference != null) item.addContent(externalReference.generateXPDL());
        if (participantType != null) item.addContent(participantType.generateXPDL());
        if (extendedAttributes != null) {
            Element x_extenedeAttributes = new Element("ExtendedAttributes");
            for (int i = 0; i < extendedAttributes.size(); i++) {
                ExtendedAttribute extendedAttribute = (ExtendedAttribute) extendedAttributes.get(i);
                x_extenedeAttributes.addContent(extendedAttribute.generateXPDL());
            }
            item.addContent(x_extenedeAttributes);
        }
        return item;
    }

    public Element generateXPDL_1_0() {
        Element item = new Element("Participant");
        Attribute x_id = new Attribute("Id", this.id);
        Attribute x_name = new Attribute("Name", this.name);
        item.setAttribute(x_id);
        item.setAttribute(x_name);
        if (description != null) item.addContent(description.generateXPDL_1_0());
        if (externalReference != null) item.addContent(externalReference.generateXPDL_1_0());
        if (participantType != null) item.addContent(participantType.generateXPDL_1_0());
        return item;
    }
}

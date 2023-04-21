package org.deft.operation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Ontology2IndividualDataConfiguration extends AbstractOperationConfiguration {

    public static String ID = "org.deft.operation.ro2individualdata";

    private String individualName;

    public Ontology2IndividualDataConfiguration() {
        super("org.deft.requirementsontology");
    }

    @Override
    public String getId() {
        return ID;
    }

    public void setIndividual(String individualName) {
        this.individualName = individualName;
    }

    public String getIndividual() {
        return individualName;
    }

    @Override
    public void loadFromXml(Element element) {
        if (isValid(element)) {
            Element eIndividual = (Element) element.getElementsByTagName("individual").item(0);
            this.individualName = eIndividual.getAttribute("name");
        }
    }

    @Override
    public void addToXml(Element root) {
        Document doc = root.getOwnerDocument();
        Element eIndividual = doc.createElement("individual");
        eIndividual.setAttribute("name", individualName);
        root.appendChild(eIndividual);
    }

    @Override
    protected String getLocalSchemaFileLocation() {
        return "resources/o2dataschema.xsd";
    }
}

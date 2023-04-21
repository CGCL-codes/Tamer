package org.deft.operation.idents2styledrunningtext;

import org.deft.operation.AbstractOperationConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Idents2StyledRunningTextConfiguration extends AbstractOperationConfiguration {

    public static String ID = "org.deft.operation.idents2styledrunningtext";

    private String scopeId;

    public Idents2StyledRunningTextConfiguration() {
    }

    @Override
    public String getId() {
        return ID;
    }

    public void setScopeId(String id) {
        System.out.println("setting id " + id);
        this.scopeId = id;
    }

    public String getScopeId() {
        return scopeId;
    }

    @Override
    public void loadFromXml(Element element) {
        if (isValid(element)) {
            loadScopeId(element);
        }
    }

    private void loadScopeId(Element element) {
        Element eScopeId = (Element) element.getElementsByTagName("scopeid").item(0);
        String scopeId = eScopeId.getTextContent();
        this.scopeId = scopeId;
    }

    @Override
    public void addToXml(Element root) {
        addScopeIdToXml(root);
    }

    private void addScopeIdToXml(Element root) {
        Document doc = root.getOwnerDocument();
        Element eScopeId = doc.createElement("scopeid");
        eScopeId.setTextContent(scopeId);
        root.appendChild(eScopeId);
    }

    @Override
    protected String getLocalSchemaFileLocation() {
        return "resources/schema.xsd";
    }
}

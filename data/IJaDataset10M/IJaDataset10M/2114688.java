package edu.asu.vogon.rdf.xml;

public class NodeProperty {

    private String propertyName;

    private String propertyValue;

    public NodeProperty(String propertyName, String propertyValue) {
        super();
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}

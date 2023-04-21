package org.apache.axis2.rmi.deploy.config;

public class FieldInfo {

    private String javaName;

    private String xmlName;

    private boolean isElement = true;

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public boolean isElement() {
        return isElement;
    }

    public void setElement(boolean element) {
        isElement = element;
    }
}

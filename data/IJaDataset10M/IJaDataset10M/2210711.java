package org.xorm.util.jdoxml;

/**
 * Represents a JDO XML collection declaration.
 */
public class JDOCollection extends JDOExtendable {

    private String elementType;

    private Boolean embeddedElement;

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public Boolean isEmbeddedElement() {
        return embeddedElement;
    }

    public void setEmbeddedElement(Boolean value) {
        this.embeddedElement = value;
    }
}

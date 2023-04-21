package org.jostraca.block;

public class BasicAttribute implements Attribute {

    protected String iName = "";

    protected String iType = "";

    protected String iText = "";

    public BasicAttribute(String pType, String pName) {
        iType = pType;
        iName = pName;
    }

    public BasicAttribute(String pType, String pName, String pText) {
        iType = pType;
        iName = pName;
        iText = pText;
    }

    public String getName() {
        return iName;
    }

    public String getType() {
        return iType;
    }

    public String getText() {
        return iText;
    }

    public String toString() {
        return iType + ":" + iName + ":" + iText;
    }

    public boolean equals(Object pObject) {
        if (null != pObject && pObject instanceof BasicAttribute) {
            BasicAttribute other = (BasicAttribute) pObject;
            return iType.equals(other.iType) && iName.equals(other.iName) && iText.equals(other.iText);
        } else {
            return false;
        }
    }
}

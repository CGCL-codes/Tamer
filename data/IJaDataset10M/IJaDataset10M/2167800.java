package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author reseto
 */
public class XSDDocumentElement {

    private final String trimmedQName;

    private final Map<String, SAXAttributeData> attrs;

    private String associatedCTypeName;

    private boolean associatedWithUnnamedCType;

    public XSDDocumentElement(final String trimmedQName) {
        if (trimmedQName == null || trimmedQName.equals("")) {
            throw new IllegalArgumentException("XDS Document Element: can't have empty or null element name!");
        } else {
            this.trimmedQName = trimmedQName;
        }
        attrs = new HashMap<String, SAXAttributeData>();
        associatedCTypeName = "";
        associatedWithUnnamedCType = false;
    }

    public String getAssociatedCTypeName() {
        return associatedCTypeName;
    }

    public void setAssociatedCTypeName(String associatedCTypeName) {
        this.associatedCTypeName = associatedCTypeName;
    }

    public Map<String, SAXAttributeData> getAttrs() {
        return attrs;
    }

    public String getName() {
        return trimmedQName;
    }

    public boolean isNamedComplexType() {
        SAXAttributeData nameAttr = attrs.get("name");
        return (nameAttr != null && !nameAttr.getQName().equals("") && isComplexType()) ? true : false;
    }

    public boolean isComplexType() {
        return (trimmedQName.equalsIgnoreCase("complextype")) ? true : false;
    }

    public void associateWithUnnamedCType() {
        this.associatedWithUnnamedCType = true;
    }

    public boolean isAssociated() {
        return associatedWithUnnamedCType;
    }
}

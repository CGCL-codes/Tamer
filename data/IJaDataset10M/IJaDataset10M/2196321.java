package org.vikamine.kernel.data;

import java.util.List;

public class MultipleChoiceYesNoComponentAttribute extends DefaultNominalAttribute {

    public static final String CONST_MC_SEP = "-MC-";

    private static final long serialVersionUID = 144654251808369823L;

    public static String extractMCIdFromAttributeName(String attName) {
        int i = attName.indexOf(CONST_MC_SEP);
        if (i == -1) {
            return null;
        }
        return attName.substring(0, i);
    }

    private String parentMCAttributeID;

    private String ynID;

    private NominalAttribute parentMCAttribute;

    protected MultipleChoiceYesNoComponentAttribute(String attributeName, List<String> attributeValues) {
        super(attributeName, attributeValues);
        ynID = attributeName;
    }

    @Override
    public String getId() {
        if (ynID != null) {
            return ynID;
        } else {
            return super.getId();
        }
    }

    public NominalAttribute getParentMCAttribute() {
        return parentMCAttribute;
    }

    public String getParentMCAttributeID() {
        return parentMCAttributeID;
    }

    public void setParentMCAttribute(NominalAttribute parentMCAttribute) {
        this.parentMCAttribute = parentMCAttribute;
    }

    public void setParentMCAttributeID(String mcAttributeID) {
        this.parentMCAttributeID = mcAttributeID;
    }
}

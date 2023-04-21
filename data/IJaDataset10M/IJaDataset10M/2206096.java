package org.dcm4chex.archive.web.maverick.xdsi;

import org.dcm4chee.xds.infoset.v30.ClassificationType;
import org.dcm4chee.xds.infoset.v30.util.InfoSetUtil;

public class XDSClassification {

    private String classificationScheme;

    private String classifiedObject;

    private String displayName;

    private String codeValue;

    private String codingScheme;

    public String getClassificationScheme() {
        return classificationScheme;
    }

    public String getClassifiedObject() {
        return classifiedObject;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public String getCodingScheme() {
        return codingScheme;
    }

    public XDSClassification(ClassificationType classificationType) {
        classificationScheme = classificationType.getClassificationScheme();
        codeValue = classificationType.getNodeRepresentation();
        classifiedObject = classificationType.getClassifiedObject();
        displayName = InfoSetUtil.getString(classificationType.getName(), null);
        codingScheme = InfoSetUtil.getSlotValue(classificationType.getSlot(), "codingScheme", null);
    }
}

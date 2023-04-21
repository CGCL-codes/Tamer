package net.sourceforge.olympos.diagramimageexporter;

public class InfoXmlConnection {

    String type;

    String targetType;

    String targetOid;

    String targetRole;

    String relationType;

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    InfoXmlConnection(String type, String targetType, String targetOid, String targetRole, String relationType) {
        setAll(targetType, type, targetOid, targetRole, relationType);
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetOid() {
        return targetOid;
    }

    public void setTargetOid(String targetOid) {
        this.targetOid = targetOid;
    }

    public void setAll(String targetType, String type, String targetOid, String targetRole, String relationType) {
        this.targetType = targetType;
        this.type = type;
        this.targetOid = targetOid;
        this.targetRole = targetRole;
        this.relationType = relationType;
    }
}

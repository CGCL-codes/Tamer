package com.googlecode.hibernate.audit.model.object;

public class EntityAuditObject extends AuditObject {

    protected String targetEntityId;

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }
}

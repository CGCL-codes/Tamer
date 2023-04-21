package org.personalsmartspace.cm.db.impl.model;

import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssConstants;

public abstract class StubBaseCtxIdentifier implements ICtxIdentifier {

    private static final long serialVersionUID = -6424013968827058509L;

    private String operatorId;

    private String localServiceId;

    private String type;

    private Long objectNumber;

    StubBaseCtxIdentifier(String operatorId, String localServiceId, String type, Long objectNumber) {
        this.operatorId = operatorId;
        this.localServiceId = localServiceId;
        this.type = type;
        this.objectNumber = objectNumber;
    }

    public abstract CtxModelType getModelType();

    @Override
    public String getOperatorId() {
        return this.operatorId;
    }

    @Override
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public String getLocalServiceId() {
        return this.localServiceId;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Long getObjectNumber() {
        return this.objectNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof ICtxIdentifier)) return false;
        ICtxIdentifier other = (ICtxIdentifier) obj;
        if (this.getOperatorId() == null) {
            if (other.getOperatorId() != null) return false;
        } else if (!this.getOperatorId().equals(other.getOperatorId())) return false;
        if (this.getLocalServiceId() == null) {
            if (other.getLocalServiceId() != null) return false;
        } else if (!this.getLocalServiceId().equals(other.getLocalServiceId())) return false;
        if (this.getModelType() == null) {
            if (other.getModelType() != null) return false;
        } else if (!this.getModelType().equals(other.getModelType())) return false;
        if (this.getType() == null) {
            if (other.getType() != null) return false;
        } else if (!this.getType().equals(other.getType())) return false;
        if (this.getObjectNumber() == null) {
            if (other.getObjectNumber() != null) return false;
        } else if (!this.getObjectNumber().equals(other.getObjectNumber())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getOperatorId() == null) ? 0 : this.getOperatorId().hashCode());
        result = prime * result + ((this.getLocalServiceId() == null) ? 0 : this.getLocalServiceId().hashCode());
        result = prime * result + ((this.getModelType() == null) ? 0 : this.getModelType().hashCode());
        result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
        result = prime * result + ((this.getObjectNumber() == null) ? 0 : this.getObjectNumber().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(PssConstants.PSS_URI_PREFIX + "://");
        result.append(this.getOperatorId());
        result.append("@");
        result.append(this.getLocalServiceId());
        result.append("/");
        result.append(this.getModelType());
        result.append("/");
        result.append(this.getType());
        result.append("/");
        result.append(this.getObjectNumber());
        return result.toString();
    }

    @Override
    public String toUriString() {
        return this.toString();
    }
}

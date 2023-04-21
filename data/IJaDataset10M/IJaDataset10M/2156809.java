package org.personalsmartspace.cm.db.impl.model;

import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttributeIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;

/**
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @see ICtxAttributeIdentifier
 * @since 0.1.0
 */
public final class CtxAttributeIdentifier extends BaseCtxIdentifier implements ICtxAttributeIdentifier {

    private static final long serialVersionUID = 8671733499422961083L;

    private ICtxEntity entity;

    /**
     * Empty constructor required by Hibernate
     */
    CtxAttributeIdentifier() {
    }

    /**
     * Creates a context attribute identifier by specifying the containing
     * entity, the attribute type and the unique numeric model object identifier
     * 
     * @param entity
     *            the containing context entity
     * @param type
     *            the attribute type
     * @param objectNumber
     *            the unique numeric model object identifier
     */
    CtxAttributeIdentifier(ICtxEntity entity, String type, Long objectNumber) {
        super(entity.getOperatorId(), entity.getLocalServiceId(), type, objectNumber);
        this.entity = entity;
    }

    public ICtxEntityIdentifier getScope() {
        return this.entity.getCtxIdentifier();
    }

    @Override
    public String getOperatorId() {
        return this.entity.getOperatorId();
    }

    @Override
    public void setOperatorId(String operatorId) {
        this.entity.getCtxIdentifier().setOperatorId(operatorId);
    }

    @Override
    public String getLocalServiceId() {
        return this.entity.getLocalServiceId();
    }

    @Override
    public CtxModelType getModelType() {
        return CtxModelType.ATTRIBUTE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!super.equals(obj)) return false;
        CtxAttributeIdentifier other = (CtxAttributeIdentifier) obj;
        if (this.getScope() == null) {
            if (other.getScope() != null) return false;
        } else if (!this.getScope().equals(other.getScope())) return false;
        if (this.getModelType() == null) {
            if (other.getModelType() != null) return false;
        } else if (!this.getModelType().equals(other.getModelType())) return false;
        if (super.getType() == null) {
            if (other.getType() != null) return false;
        } else if (!super.getType().equals(other.getType())) return false;
        if (super.getObjectNumber() == null) {
            if (other.getObjectNumber() != null) return false;
        } else if (!super.getObjectNumber().equals(other.getObjectNumber())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getScope() == null) ? 0 : this.getScope().hashCode());
        result = prime * result + ((this.getModelType() == null) ? 0 : this.getModelType().hashCode());
        result = prime * result + ((super.getType() == null) ? 0 : super.getType().hashCode());
        result = prime * result + ((super.getObjectNumber() == null) ? 0 : super.getObjectNumber().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getScope());
        result.append("/");
        result.append(this.getModelType());
        result.append("/");
        result.append(super.getType());
        result.append("/");
        result.append(super.getObjectNumber());
        return result.toString();
    }

    @Override
    public String toUriString() {
        return this.toString();
    }
}

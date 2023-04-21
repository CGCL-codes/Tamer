package org.caleigo.core.meta;

import org.caleigo.core.*;
import org.caleigo.toolkit.log.*;

public class MetaEntityRelationEntity extends AbstractMetaEntity {

    /** Default constructor for MetaEntityRelationEntity.
     */
    public MetaEntityRelationEntity() {
    }

    /** Copy constructor for MetaEntityRelationEntity.
     */
    public MetaEntityRelationEntity(MetaEntityRelationEntity entity) {
        this.copyData(entity);
        this.clearAllDirtyFlags();
    }

    /** This constructor creates a new MetaEntityRelationEntity and loads
     * it with data from the data source using the entity's identity data.
     */
    public MetaEntityRelationEntity(String aSourceName) {
        super(Qualifier.create(MetaEntityRelation.SourceName, new String(aSourceName)));
    }

    /** This constructor creates a new MetaEntityRelationEntity and loads
     * it with data from the data source using the provided identity qualifier.
     */
    public MetaEntityRelationEntity(Qualifier identityQualifier) {
        super(identityQualifier);
    }

    public IEntityDescriptor getEntityDescriptor() {
        return MetaEntityRelation.instance;
    }

    protected void doOnDataChange(IFieldDescriptor fieldDescriptor, Object oldValue, Object newValue) {
        if (fieldDescriptor == MetaEntityRelation.ReferenceEntity && oldValue == null && this.isDataNull(MetaEntityRelation.ReverseName)) {
            MetaEntityDescriptorEntity entity = MetaEntityDescriptor.load(newValue.toString());
            if (entity != null) this.setData(MetaEntityRelation.ReverseName, entity.getDisplayName());
        }
        if (fieldDescriptor == MetaEntityRelation.TargetEntity && oldValue == null && this.isDataNull(MetaEntityRelation.ForwardName)) {
            MetaEntityDescriptorEntity entity = MetaEntityDescriptor.load(newValue.toString());
            if (entity != null) this.setData(MetaEntityRelation.ForwardName, entity.getDisplayName());
        }
        if (fieldDescriptor == MetaEntityRelation.ReferenceEntity && oldValue == null && !this.isDataNull(MetaEntityRelation.TargetEntity) || fieldDescriptor == MetaEntityRelation.TargetEntity && oldValue == null && !this.isDataNull(MetaEntityRelation.ReferenceEntity)) {
            try {
                IEntity refEntity = MetaEntityDescriptor.load((String) this.getData(MetaEntityRelation.ReferenceEntity));
                IEntity tgtEntity = MetaEntityDescriptor.load((String) this.getData(MetaEntityRelation.TargetEntity));
                String compositeName = (String) refEntity.getData(MetaEntityDescriptor.CodeName) + (String) tgtEntity.getData(MetaEntityDescriptor.CodeName);
                if (this.isDataNull(MetaEntityRelation.CodeName)) this.setData(MetaEntityRelation.CodeName, compositeName);
                if (this.isDataNull(MetaEntityRelation.SourceName)) this.setData(MetaEntityRelation.SourceName, compositeName);
            } catch (Exception e) {
                Log.printWarning(this, "Failed to calculate entity relation code and source name: " + e.getClass().getName());
            }
        }
    }

    public String getSourceName() {
        return (String) this.getData(MetaEntityRelation.SourceName);
    }

    public String getCodeName() {
        return (String) this.getData(MetaEntityRelation.CodeName);
    }

    public String getForwardName() {
        return (String) this.getData(MetaEntityRelation.ForwardName);
    }

    public String getReverseName() {
        return (String) this.getData(MetaEntityRelation.ReverseName);
    }

    public String getReferenceEntity() {
        return (String) this.getData(MetaEntityRelation.ReferenceEntity);
    }

    public String getTargetEntity() {
        return (String) this.getData(MetaEntityRelation.TargetEntity);
    }

    public void setSourceName(String value) {
        this.setData(MetaEntityRelation.SourceName, value);
    }

    public void setCodeName(String value) {
        this.setData(MetaEntityRelation.CodeName, value);
    }

    public void setForwardName(String value) {
        this.setData(MetaEntityRelation.ForwardName, value);
    }

    public void setReverseName(String value) {
        this.setData(MetaEntityRelation.ReverseName, value);
    }

    public void setReferenceEntity(String value) {
        this.setData(MetaEntityRelation.ReferenceEntity, value);
    }

    public void setTargetEntity(String value) {
        this.setData(MetaEntityRelation.TargetEntity, value);
    }

    public Object getSourceNameData() {
        return this.getData(MetaEntityRelation.SourceName);
    }

    public Object getCodeNameData() {
        return this.getData(MetaEntityRelation.CodeName);
    }

    public Object getForwardNameData() {
        return this.getData(MetaEntityRelation.ForwardName);
    }

    public Object getReverseNameData() {
        return this.getData(MetaEntityRelation.ReverseName);
    }

    public Object getReferenceEntityData() {
        return this.getData(MetaEntityRelation.ReferenceEntity);
    }

    public Object getTargetEntityData() {
        return this.getData(MetaEntityRelation.TargetEntity);
    }

    public void setSourceNameData(Object value) {
        this.setData(MetaEntityRelation.SourceName, value);
    }

    public void setCodeNameData(Object value) {
        this.setData(MetaEntityRelation.CodeName, value);
    }

    public void setForwardNameData(Object value) {
        this.setData(MetaEntityRelation.ForwardName, value);
    }

    public void setReverseNameData(Object value) {
        this.setData(MetaEntityRelation.ReverseName, value);
    }

    public void setReferenceEntityData(Object value) {
        this.setData(MetaEntityRelation.ReferenceEntity, value);
    }

    public void setTargetEntityData(Object value) {
        this.setData(MetaEntityRelation.TargetEntity, value);
    }
}

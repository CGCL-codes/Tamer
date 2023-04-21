package net.sourceforge.iwii.db.dev.bo.project.artifact.phase6;

import net.sourceforge.iwii.db.dev.bo.AbstractConvertableBO;
import net.sourceforge.iwii.db.dev.bo.IBusinessObject;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase5.UseCaseOperationDataPackagePropertyBO;
import net.sourceforge.iwii.db.dev.common.TempIdGenerator;

/**
 * Class represents real world object business object.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class EntityClassPropertyMappingBO extends AbstractConvertableBO<Long> {

    private UseCaseOperationDataPackagePropertyBO packageProperty;

    private EntityClassPropertyBO entityProperty;

    private EntityClassModelArtifactDataBO artifactData;

    public EntityClassPropertyMappingBO() {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
    }

    public EntityClassPropertyMappingBO(UseCaseOperationDataPackagePropertyBO packageProperty, EntityClassPropertyBO entityProperty, EntityClassModelArtifactDataBO artifactData) {
        this.setDatabaseId(TempIdGenerator.getInstance().generateId());
        this.packageProperty = packageProperty;
        this.entityProperty = entityProperty;
        this.artifactData = artifactData;
    }

    public EntityClassModelArtifactDataBO getArtifactData() {
        return artifactData;
    }

    public void setArtifactData(EntityClassModelArtifactDataBO artifactData) {
        this.artifactData = artifactData;
    }

    public EntityClassPropertyBO getEntityProperty() {
        return entityProperty;
    }

    public void setEntityProperty(EntityClassPropertyBO entityProperty) {
        this.entityProperty = entityProperty;
    }

    public UseCaseOperationDataPackagePropertyBO getPackageProperty() {
        return packageProperty;
    }

    public void setPackageProperty(UseCaseOperationDataPackagePropertyBO packageProperty) {
        this.packageProperty = packageProperty;
    }

    @Override
    public String toString() {
        return "business-object://convertable/" + this.getClass().getName() + "[databaseId=" + this.getDatabaseId() + "]";
    }

    @Override
    public void initWithOtherBO(IBusinessObject otherBO) {
        super.initWithOtherBO(otherBO);
        if (otherBO instanceof EntityClassPropertyMappingBO) {
            EntityClassPropertyMappingBO bo = (EntityClassPropertyMappingBO) otherBO;
            this.setArtifactData(bo.getArtifactData());
            this.setEntityProperty(bo.getEntityProperty());
            this.setPackageProperty(bo.getPackageProperty());
        }
    }
}

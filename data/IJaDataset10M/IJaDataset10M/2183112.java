package net.sourceforge.iwii.db.dev.persistence.converters.impl.project.artifact.phase1;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.FunctionalRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.FunctionalRequirementGroupBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.FunctionalRequirementsArtifactDataBO;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverter;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterFactory;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterQueue;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAOFactory;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.FunctionalRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.FunctionalRequirementGroupEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.FunctionalRequirementsArtifactDataEntity;

/**
 * Class is responsible for converting between FunctionalRequirementGroupEntity and FunctionalRequirementGroupBO objects.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class FunctionalRequirementGroupConverter implements IConverter<FunctionalRequirementGroupEntity, FunctionalRequirementGroupBO> {

    private static final Logger logger = Logger.getLogger(FunctionalRequirementGroupConverter.class.getName());

    private IDAO<FunctionalRequirementGroupEntity, Long> dao;

    private IConverter<FunctionalRequirementsArtifactDataEntity, FunctionalRequirementsArtifactDataBO> artifactDataConverter;

    private IConverter<FunctionalRequirementEntity, FunctionalRequirementBO> requirementConverter;

    private IConverterQueue queue;

    private boolean isPostInitCompleted = false;

    public FunctionalRequirementGroupConverter() {
        long start = System.currentTimeMillis();
        FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Initializating...");
        this.dao = ServiceInjector.injectSingletonService(IDAOFactory.class).getDAOForEntity(FunctionalRequirementGroupEntity.class);
        FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Initialization done in " + String.valueOf(start - System.currentTimeMillis()) + " [ms]");
    }

    /**
     * Used to perform post initialization of converter
     */
    protected void postInit() {
        if (!this.isPostInitCompleted) {
            this.isPostInitCompleted = true;
            long start = System.currentTimeMillis();
            FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Post initialization...");
            IConverterFactory converterFactory = ServiceInjector.injectSingletonService(IConverterFactory.class);
            this.artifactDataConverter = converterFactory.createConverter(FunctionalRequirementsArtifactDataEntity.class, FunctionalRequirementsArtifactDataBO.class);
            this.requirementConverter = converterFactory.createConverter(FunctionalRequirementEntity.class, FunctionalRequirementBO.class);
            this.queue = converterFactory.getConverterQueue();
            FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Post initialization done in " + String.valueOf(start - System.currentTimeMillis()) + " [ms]");
        }
    }

    @Override
    public FunctionalRequirementGroupEntity convertToEntity(FunctionalRequirementGroupBO businessObject) {
        this.postInit();
        if (businessObject == null) return null;
        if (this.queue.isBOConverted(businessObject)) {
            return (FunctionalRequirementGroupEntity) this.queue.getConvertedBO(businessObject);
        }
        FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Converting business object: " + businessObject + " to entity...");
        FunctionalRequirementGroupEntity entity = new FunctionalRequirementGroupEntity();
        this.queue.putConvertedBO(businessObject, entity);
        entity.setArtifactData(this.artifactDataConverter.convertToEntity(businessObject.getArtifactData()));
        entity.setCreationDate(businessObject.getCreationDate());
        entity.setId(businessObject.getDatabaseId() < 0 ? null : businessObject.getDatabaseId());
        entity.setModificationDate(businessObject.getModificationDate());
        entity.setName(businessObject.getName());
        entity.setSuperGroup(this.convertToEntity(businessObject.getSuperGroup()));
        for (FunctionalRequirementBO bo : businessObject.getRequirements()) {
            entity.getRequirements().add(this.requirementConverter.convertToEntity(bo));
        }
        for (FunctionalRequirementGroupBO bo : businessObject.getSubGroups()) {
            entity.getSubGroups().add(this.convertToEntity(bo));
        }
        return entity;
    }

    @Override
    public FunctionalRequirementGroupEntity loadCorrespondingEntity(FunctionalRequirementGroupBO businessObject) {
        this.postInit();
        if (businessObject == null || businessObject.getDatabaseId() == null) return null;
        FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Loading corresponding entity to business object: " + businessObject + "...");
        return this.dao.findById(businessObject.getDatabaseId());
    }

    @Override
    public FunctionalRequirementGroupBO convertToBusinessObject(FunctionalRequirementGroupEntity entity) {
        this.postInit();
        if (entity == null) return null;
        if (this.queue.isEntityConverted(entity)) {
            return (FunctionalRequirementGroupBO) this.queue.getConvertedEntity(entity);
        }
        FunctionalRequirementGroupConverter.logger.log(Level.INFO, "Converting entity: " + entity + " to business object...");
        FunctionalRequirementGroupBO businessObject = new FunctionalRequirementGroupBO();
        this.queue.putConvertedEntity(entity, businessObject);
        businessObject.setArtifactData(this.artifactDataConverter.convertToBusinessObject(entity.getArtifactData()));
        businessObject.setCreationDate(entity.getCreationDate());
        businessObject.setDatabaseId(entity.getId());
        businessObject.setModificationDate(entity.getModificationDate());
        businessObject.setName(entity.getName());
        businessObject.setSuperGroup(this.convertToBusinessObject(entity.getSuperGroup()));
        for (FunctionalRequirementEntity en : entity.getRequirements()) {
            businessObject.getRequirements().add(this.requirementConverter.convertToBusinessObject(en));
        }
        for (FunctionalRequirementGroupEntity en : entity.getSubGroups()) {
            businessObject.getSubGroups().add(this.convertToBusinessObject(en));
        }
        return businessObject;
    }
}

package net.sourceforge.iwii.db.dev.persistence.converters.impl.project.artifact.phase4;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.FunctionalRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.RejectedFunctionalRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.VisionArtifactDataBO;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverter;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterFactory;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterQueue;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAOFactory;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.FunctionalRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.RejectedFunctionalRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.VisionArtifactDataEntity;

/**
 * Class is responsible for converting between RejectedFunctionalRequirementEntity and RejectedFunctionalRequirementBO objects.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class RejectedFunctionalRequirementConverter implements IConverter<RejectedFunctionalRequirementEntity, RejectedFunctionalRequirementBO> {

    private static final Logger logger = Logger.getLogger(RejectedFunctionalRequirementConverter.class.getName());

    private IDAO<RejectedFunctionalRequirementEntity, Long> dao;

    private IConverter<VisionArtifactDataEntity, VisionArtifactDataBO> artifactDataConverter;

    private IConverter<FunctionalRequirementEntity, FunctionalRequirementBO> functionalRequirementConverter;

    private IConverterQueue queue;

    private boolean isPostInitCompleted = false;

    public RejectedFunctionalRequirementConverter() {
        long start = System.currentTimeMillis();
        RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Initializating...");
        this.dao = ServiceInjector.injectSingletonService(IDAOFactory.class).getDAOForEntity(RejectedFunctionalRequirementEntity.class);
        RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
    }

    /**
     * Used to perform post initialization of converter
     */
    protected void postInit() {
        if (!this.isPostInitCompleted) {
            this.isPostInitCompleted = true;
            long start = System.currentTimeMillis();
            RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Post initialization...");
            IConverterFactory converterFactory = ServiceInjector.injectSingletonService(IConverterFactory.class);
            this.artifactDataConverter = converterFactory.createConverter(VisionArtifactDataEntity.class, VisionArtifactDataBO.class);
            this.functionalRequirementConverter = converterFactory.createConverter(FunctionalRequirementEntity.class, FunctionalRequirementBO.class);
            this.queue = converterFactory.getConverterQueue();
            RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Post initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
        }
    }

    @Override
    public RejectedFunctionalRequirementEntity convertToEntity(RejectedFunctionalRequirementBO businessObject) {
        this.postInit();
        if (businessObject == null) return null;
        if (this.queue.isBOConverted(businessObject)) {
            return (RejectedFunctionalRequirementEntity) this.queue.getConvertedBO(businessObject);
        }
        RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Converting business object: " + businessObject + " to entity...");
        RejectedFunctionalRequirementEntity entity = new RejectedFunctionalRequirementEntity();
        this.queue.putConvertedBO(businessObject, entity);
        entity.setArtifactData(this.artifactDataConverter.convertToEntity(businessObject.getArtifactData()));
        entity.setCreationDate(businessObject.getCreationDate());
        entity.setId(businessObject.getDatabaseId() < 0 ? null : businessObject.getDatabaseId());
        entity.setModificationDate(businessObject.getModificationDate());
        entity.setRequirement(this.functionalRequirementConverter.convertToEntity(businessObject.getRequirement()));
        entity.setRejectionNode(businessObject.getRejectionNote());
        return entity;
    }

    @Override
    public RejectedFunctionalRequirementEntity loadCorrespondingEntity(RejectedFunctionalRequirementBO businessObject) {
        this.postInit();
        if (businessObject == null || businessObject.getDatabaseId() == null) return null;
        RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Loading corresponding entity to business object: " + businessObject + "...");
        return this.dao.findById(businessObject.getDatabaseId());
    }

    @Override
    public RejectedFunctionalRequirementBO convertToBusinessObject(RejectedFunctionalRequirementEntity entity) {
        this.postInit();
        if (entity == null) return null;
        if (this.queue.isEntityConverted(entity)) {
            return (RejectedFunctionalRequirementBO) this.queue.getConvertedEntity(entity);
        }
        RejectedFunctionalRequirementConverter.logger.log(Level.INFO, "Converting entity: " + entity + " to business object...");
        RejectedFunctionalRequirementBO businessObject = new RejectedFunctionalRequirementBO();
        this.queue.putConvertedEntity(entity, businessObject);
        businessObject.setArtifactData(this.artifactDataConverter.convertToBusinessObject(entity.getArtifactData()));
        businessObject.setCreationDate(entity.getCreationDate());
        businessObject.setDatabaseId(entity.getId());
        businessObject.setModificationDate(entity.getModificationDate());
        businessObject.setRequirement(this.functionalRequirementConverter.convertToBusinessObject(entity.getRequirement()));
        businessObject.setRejectionNote(entity.getRejectionNode());
        return businessObject;
    }
}

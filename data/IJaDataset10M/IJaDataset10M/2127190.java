package net.sourceforge.iwii.db.dev.persistence.converters.impl.project.artifact.phase1;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.FeasibilityStudyArtifactDataBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase1.CustomerImplementationEnvironmentBO;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverter;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterFactory;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterQueue;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAOFactory;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.FeasibilityStudyArtifactDataEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1.CustomerImplementationEnvironmentEntity;

/**
 * Class is responsible for converting between CustomerImplementationEnvironmentEntity and CustomerImplementationEnvironmentBO objects.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class CustomerImplementationEnvironmentConverter implements IConverter<CustomerImplementationEnvironmentEntity, CustomerImplementationEnvironmentBO> {

    private static final Logger logger = Logger.getLogger(CustomerImplementationEnvironmentConverter.class.getName());

    private IDAO<CustomerImplementationEnvironmentEntity, Long> dao;

    private IConverter<FeasibilityStudyArtifactDataEntity, FeasibilityStudyArtifactDataBO> artifactDataConverter;

    private IConverterQueue queue;

    private boolean isPostInitCompleted = false;

    public CustomerImplementationEnvironmentConverter() {
        long start = System.currentTimeMillis();
        CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Initializating...");
        this.dao = ServiceInjector.injectSingletonService(IDAOFactory.class).getDAOForEntity(CustomerImplementationEnvironmentEntity.class);
        CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Initialization done in " + String.valueOf(start - System.currentTimeMillis()) + " [ms]");
    }

    /**
     * Used to perform post initialization of converter
     */
    protected void postInit() {
        if (!this.isPostInitCompleted) {
            this.isPostInitCompleted = true;
            long start = System.currentTimeMillis();
            CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Post initialization...");
            IConverterFactory converterFactory = ServiceInjector.injectSingletonService(IConverterFactory.class);
            this.artifactDataConverter = converterFactory.createConverter(FeasibilityStudyArtifactDataEntity.class, FeasibilityStudyArtifactDataBO.class);
            this.queue = converterFactory.getConverterQueue();
            CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Post initialization done in " + String.valueOf(start - System.currentTimeMillis()) + " [ms]");
        }
    }

    @Override
    public CustomerImplementationEnvironmentEntity convertToEntity(CustomerImplementationEnvironmentBO businessObject) {
        this.postInit();
        if (businessObject == null) return null;
        if (this.queue.isBOConverted(businessObject)) {
            return (CustomerImplementationEnvironmentEntity) this.queue.getConvertedBO(businessObject);
        }
        CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Converting business object: " + businessObject + " to entity...");
        CustomerImplementationEnvironmentEntity entity = new CustomerImplementationEnvironmentEntity();
        this.queue.putConvertedBO(businessObject, entity);
        entity.setClientEnvironment(businessObject.getClientEnvironment());
        entity.setClientOS(businessObject.getClientOS());
        entity.setComment(businessObject.getComment());
        entity.setCreationDate(businessObject.getCreationDate());
        entity.setId(businessObject.getDatabaseId() < 0 ? null : businessObject.getDatabaseId());
        entity.setModificationDate(businessObject.getModificationDate());
        entity.setServerAS(businessObject.getServerAS());
        entity.setServerDBMS(businessObject.getServerDBMS());
        entity.setServerEnvironment(businessObject.getServerEnvironment());
        entity.setServerOS(businessObject.getServerOS());
        entity.setArtifactData(this.artifactDataConverter.convertToEntity(businessObject.getArtifactData()));
        return entity;
    }

    @Override
    public CustomerImplementationEnvironmentEntity loadCorrespondingEntity(CustomerImplementationEnvironmentBO businessObject) {
        this.postInit();
        if (businessObject == null || businessObject.getDatabaseId() == null) return null;
        CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Loading corresponding entity to business object: " + businessObject + "...");
        return this.dao.findById(businessObject.getDatabaseId());
    }

    @Override
    public CustomerImplementationEnvironmentBO convertToBusinessObject(CustomerImplementationEnvironmentEntity entity) {
        this.postInit();
        if (entity == null) return null;
        if (this.queue.isEntityConverted(entity)) {
            return (CustomerImplementationEnvironmentBO) this.queue.getConvertedEntity(entity);
        }
        CustomerImplementationEnvironmentConverter.logger.log(Level.INFO, "Converting entity: " + entity + " to business object...");
        CustomerImplementationEnvironmentBO businessObject = new CustomerImplementationEnvironmentBO();
        this.queue.putConvertedEntity(entity, businessObject);
        businessObject.setClientEnvironment(entity.getClientEnvironment());
        businessObject.setClientOS(entity.getClientOS());
        businessObject.setComment(entity.getComment());
        businessObject.setCreationDate(entity.getCreationDate());
        businessObject.setDatabaseId(entity.getId());
        businessObject.setModificationDate(entity.getModificationDate());
        businessObject.setServerAS(entity.getServerAS());
        businessObject.setServerDBMS(entity.getServerDBMS());
        businessObject.setServerEnvironment(entity.getServerEnvironment());
        businessObject.setServerOS(entity.getServerOS());
        businessObject.setArtifactData(this.artifactDataConverter.convertToBusinessObject(entity.getArtifactData()));
        return businessObject;
    }
}

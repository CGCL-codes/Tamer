package net.sourceforge.iwii.db.dev.persistence.converters.impl.project.artifact.phase5;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase5.UseCaseBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase5.UseCaseOperationBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase5.UseCaseOperationDataPackageBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase5.UseCaseOperationEntryBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase7.DatabaseOperationBO;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverter;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterFactory;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterQueue;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAOFactory;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase5.UseCaseEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase5.UseCaseOperationDataPackageEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase5.UseCaseOperationEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase5.UseCaseOperationEntryEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase7.DatabaseOperationEntity;

/**
 * Class is responsible for converting between UseCaseOperationEntity and UseCaseOperationBO objects.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class UseCaseOperationConverter implements IConverter<UseCaseOperationEntity, UseCaseOperationBO> {

    private static final Logger logger = Logger.getLogger(UseCaseOperationConverter.class.getName());

    private IDAO<UseCaseOperationEntity, Long> dao;

    private IConverter<UseCaseEntity, UseCaseBO> useCaseConverter;

    private IConverter<UseCaseOperationEntryEntity, UseCaseOperationEntryBO> entryConverter;

    private IConverter<UseCaseOperationDataPackageEntity, UseCaseOperationDataPackageBO> packageConverter;

    private IConverter<DatabaseOperationEntity, DatabaseOperationBO> databaseOperationConverter;

    private IConverterQueue queue;

    private boolean isPostInitCompleted = false;

    public UseCaseOperationConverter() {
        long start = System.currentTimeMillis();
        UseCaseOperationConverter.logger.log(Level.INFO, "Initializating...");
        this.dao = ServiceInjector.injectSingletonService(IDAOFactory.class).getDAOForEntity(UseCaseOperationEntity.class);
        UseCaseOperationConverter.logger.log(Level.INFO, "Initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
    }

    /**
     * Used to perform post initialization of converter
     */
    protected void postInit() {
        if (!this.isPostInitCompleted) {
            this.isPostInitCompleted = true;
            long start = System.currentTimeMillis();
            UseCaseOperationConverter.logger.log(Level.INFO, "Post initialization...");
            IConverterFactory converterFactory = ServiceInjector.injectSingletonService(IConverterFactory.class);
            this.useCaseConverter = converterFactory.createConverter(UseCaseEntity.class, UseCaseBO.class);
            this.entryConverter = converterFactory.createConverter(UseCaseOperationEntryEntity.class, UseCaseOperationEntryBO.class);
            this.packageConverter = converterFactory.createConverter(UseCaseOperationDataPackageEntity.class, UseCaseOperationDataPackageBO.class);
            this.databaseOperationConverter = converterFactory.createConverter(DatabaseOperationEntity.class, DatabaseOperationBO.class);
            this.queue = converterFactory.getConverterQueue();
            UseCaseOperationConverter.logger.log(Level.INFO, "Post initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
        }
    }

    @Override
    public UseCaseOperationEntity convertToEntity(UseCaseOperationBO businessObject) {
        this.postInit();
        if (businessObject == null) return null;
        if (this.queue.isBOConverted(businessObject)) {
            return (UseCaseOperationEntity) this.queue.getConvertedBO(businessObject);
        }
        UseCaseOperationConverter.logger.log(Level.INFO, "Converting business object: " + businessObject + " to entity...");
        UseCaseOperationEntity entity = new UseCaseOperationEntity();
        this.queue.putConvertedBO(businessObject, entity);
        entity.setCreationDate(businessObject.getCreationDate());
        entity.setId(businessObject.getDatabaseId() < 0 ? null : businessObject.getDatabaseId());
        entity.setInProjectId(businessObject.getInProjectId());
        entity.setModificationDate(businessObject.getModificationDate());
        entity.setUseCase(this.useCaseConverter.convertToEntity(businessObject.getUseCase()));
        entity.setName(businessObject.getName());
        entity.setState(businessObject.getState());
        for (UseCaseOperationEntryBO bo : businessObject.getEntries()) {
            entity.getEntries().add(this.entryConverter.convertToEntity(bo));
        }
        for (UseCaseOperationDataPackageBO bo : businessObject.getDataPackages()) {
            entity.getDataPackages().add(this.packageConverter.convertToEntity(bo));
        }
        for (DatabaseOperationBO bo : businessObject.getTraceToDatabaseOperations()) {
            entity.getTraceToDatabaseOperations().add(this.databaseOperationConverter.convertToEntity(bo));
        }
        return entity;
    }

    @Override
    public UseCaseOperationEntity loadCorrespondingEntity(UseCaseOperationBO businessObject) {
        this.postInit();
        if (businessObject == null || businessObject.getDatabaseId() == null) return null;
        UseCaseOperationConverter.logger.log(Level.INFO, "Loading corresponding entity to business object: " + businessObject + "...");
        return this.dao.findById(businessObject.getDatabaseId());
    }

    @Override
    public UseCaseOperationBO convertToBusinessObject(UseCaseOperationEntity entity) {
        this.postInit();
        if (entity == null) return null;
        if (this.queue.isEntityConverted(entity)) {
            return (UseCaseOperationBO) this.queue.getConvertedEntity(entity);
        }
        UseCaseOperationConverter.logger.log(Level.INFO, "Converting entity: " + entity + " to business object...");
        UseCaseOperationBO businessObject = new UseCaseOperationBO();
        this.queue.putConvertedEntity(entity, businessObject);
        businessObject.setCreationDate(entity.getCreationDate());
        businessObject.setDatabaseId(entity.getId());
        businessObject.setInProjectId(entity.getInProjectId());
        businessObject.setModificationDate(entity.getModificationDate());
        businessObject.setUseCase(this.useCaseConverter.convertToBusinessObject(entity.getUseCase()));
        businessObject.setName(entity.getName());
        businessObject.setState(entity.getState());
        for (UseCaseOperationEntryEntity en : entity.getEntries()) {
            businessObject.getEntries().add(this.entryConverter.convertToBusinessObject(en));
        }
        for (UseCaseOperationDataPackageEntity en : entity.getDataPackages()) {
            businessObject.getDataPackages().add(this.packageConverter.convertToBusinessObject(en));
        }
        for (DatabaseOperationEntity en : entity.getTraceToDatabaseOperations()) {
            businessObject.getTraceToDatabaseOperations().add(this.databaseOperationConverter.convertToBusinessObject(en));
        }
        return businessObject;
    }
}

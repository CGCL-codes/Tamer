package net.sourceforge.iwii.db.dev.persistence.converters.impl.project.artifact.phase4;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactVersionBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.AcceptedFunctionalRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.KeyProblemBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.KeyRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.ProductCapabilityBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.ProductPropertyBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.RejectedFunctionalRequirementBO;
import net.sourceforge.iwii.db.dev.bo.project.artifact.phase4.VisionArtifactDataBO;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverter;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterFactory;
import net.sourceforge.iwii.db.dev.persistence.converters.api.IConverterQueue;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAO;
import net.sourceforge.iwii.db.dev.persistence.dao.api.IDAOFactory;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.ProjectArtifactVersionEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.AcceptedFunctionalRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.KeyProblemEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.KeyRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.ProductCapabilityEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.ProductPropertyEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.RejectedFunctionalRequirementEntity;
import net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase4.VisionArtifactDataEntity;

/**
 * Class is responsible for converting between VisionArtifactDataEntity and VisionArtifactDataBO objects.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class VisionArtifactDataConverter implements IConverter<VisionArtifactDataEntity, VisionArtifactDataBO> {

    private static final Logger logger = Logger.getLogger(VisionArtifactDataConverter.class.getName());

    private IDAO<VisionArtifactDataEntity, Long> dao;

    private IConverter<AcceptedFunctionalRequirementEntity, AcceptedFunctionalRequirementBO> acceptedRequirementConverter;

    private IConverter<KeyProblemEntity, KeyProblemBO> keyProblemConverter;

    private IConverter<KeyRequirementEntity, KeyRequirementBO> keyRequirementConverter;

    private IConverter<ProductCapabilityEntity, ProductCapabilityBO> productCapabilityConverter;

    private IConverter<ProductPropertyEntity, ProductPropertyBO> productPropertyConverter;

    private IConverter<RejectedFunctionalRequirementEntity, RejectedFunctionalRequirementBO> rejectedRequirementConverter;

    private IConverter<ProjectArtifactVersionEntity, ProjectArtifactVersionBO> artifactVersionConverter;

    private IConverterQueue queue;

    private boolean isPostInitCompleted = false;

    public VisionArtifactDataConverter() {
        long start = System.currentTimeMillis();
        VisionArtifactDataConverter.logger.log(Level.INFO, "Initializating...");
        this.dao = ServiceInjector.injectSingletonService(IDAOFactory.class).getDAOForEntity(VisionArtifactDataEntity.class);
        VisionArtifactDataConverter.logger.log(Level.INFO, "Initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
    }

    /**
     * Used to perform post initialization of converter
     */
    protected void postInit() {
        if (!this.isPostInitCompleted) {
            this.isPostInitCompleted = true;
            long start = System.currentTimeMillis();
            VisionArtifactDataConverter.logger.log(Level.INFO, "Post initialization...");
            IConverterFactory converterFactory = ServiceInjector.injectSingletonService(IConverterFactory.class);
            this.acceptedRequirementConverter = converterFactory.createConverter(AcceptedFunctionalRequirementEntity.class, AcceptedFunctionalRequirementBO.class);
            this.keyProblemConverter = converterFactory.createConverter(KeyProblemEntity.class, KeyProblemBO.class);
            this.keyRequirementConverter = converterFactory.createConverter(KeyRequirementEntity.class, KeyRequirementBO.class);
            this.productCapabilityConverter = converterFactory.createConverter(ProductCapabilityEntity.class, ProductCapabilityBO.class);
            this.productPropertyConverter = converterFactory.createConverter(ProductPropertyEntity.class, ProductPropertyBO.class);
            this.rejectedRequirementConverter = converterFactory.createConverter(RejectedFunctionalRequirementEntity.class, RejectedFunctionalRequirementBO.class);
            this.artifactVersionConverter = converterFactory.createConverter(ProjectArtifactVersionEntity.class, ProjectArtifactVersionBO.class);
            this.queue = converterFactory.getConverterQueue();
            VisionArtifactDataConverter.logger.log(Level.INFO, "Post initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
        }
    }

    @Override
    public VisionArtifactDataEntity convertToEntity(VisionArtifactDataBO businessObject) {
        this.postInit();
        if (businessObject == null) return null;
        if (this.queue.isBOConverted(businessObject)) {
            return (VisionArtifactDataEntity) this.queue.getConvertedBO(businessObject);
        }
        VisionArtifactDataConverter.logger.log(Level.INFO, "Converting business object: " + businessObject + " to entity...");
        VisionArtifactDataEntity entity = new VisionArtifactDataEntity();
        this.queue.putConvertedBO(businessObject, entity);
        entity.setCreationDate(businessObject.getCreationDate());
        entity.setId(businessObject.getDatabaseId() < 0 ? null : businessObject.getDatabaseId());
        entity.setModificationDate(businessObject.getModificationDate());
        for (AcceptedFunctionalRequirementBO bo : businessObject.getAcceptedFunctionalRequirements()) {
            entity.getAcceptedFunctionalRequirements().add(this.acceptedRequirementConverter.convertToEntity(bo));
        }
        for (KeyProblemBO bo : businessObject.getKeyProblems()) {
            entity.getKeyProblems().add(this.keyProblemConverter.convertToEntity(bo));
        }
        for (KeyRequirementBO bo : businessObject.getKeyRequirements()) {
            entity.getKeyRequirements().add(this.keyRequirementConverter.convertToEntity(bo));
        }
        for (ProductCapabilityBO bo : businessObject.getProductCapabilities()) {
            entity.getProductCapabilities().add(this.productCapabilityConverter.convertToEntity(bo));
        }
        for (ProductPropertyBO bo : businessObject.getProductProperties()) {
            entity.getProductProperties().add(this.productPropertyConverter.convertToEntity(bo));
        }
        for (RejectedFunctionalRequirementBO bo : businessObject.getRejectedFunctionalRequirements()) {
            entity.getRejectedFunctionalRequirements().add(this.rejectedRequirementConverter.convertToEntity(bo));
        }
        entity.setArtifactVersion(this.artifactVersionConverter.convertToEntity(businessObject.getArtifactVersion()));
        entity.setStatus(businessObject.getStatus());
        entity.setHelpRequirements(businessObject.getHelpRequirements());
        entity.setManualRequirements(businessObject.getManualRequirements());
        entity.setOnLineRequirements(businessObject.getOnLineRequirements());
        return entity;
    }

    @Override
    public VisionArtifactDataEntity loadCorrespondingEntity(VisionArtifactDataBO businessObject) {
        this.postInit();
        if (businessObject == null || businessObject.getDatabaseId() == null) return null;
        VisionArtifactDataConverter.logger.log(Level.INFO, "Loading corresponding entity to business object: " + businessObject + "...");
        return this.dao.findById(businessObject.getDatabaseId());
    }

    @Override
    public VisionArtifactDataBO convertToBusinessObject(VisionArtifactDataEntity entity) {
        this.postInit();
        if (entity == null) return null;
        if (this.queue.isEntityConverted(entity)) {
            return (VisionArtifactDataBO) this.queue.getConvertedEntity(entity);
        }
        VisionArtifactDataConverter.logger.log(Level.INFO, "Converting entity: " + entity + " to business object...");
        VisionArtifactDataBO businessObject = new VisionArtifactDataBO();
        this.queue.putConvertedEntity(entity, businessObject);
        businessObject.setArtifactVersion(this.artifactVersionConverter.convertToBusinessObject(entity.getArtifactVersion()));
        businessObject.setCreationDate(entity.getCreationDate());
        businessObject.setDatabaseId(entity.getId());
        businessObject.setHelpRequirements(entity.getHelpRequirements());
        businessObject.setManualRequirements(entity.getManualRequirements());
        businessObject.setModificationDate(entity.getModificationDate());
        businessObject.setOnLineRequirements(entity.getOnLineRequirements());
        businessObject.setStatus(entity.getStatus());
        for (AcceptedFunctionalRequirementEntity en : entity.getAcceptedFunctionalRequirements()) {
            businessObject.getAcceptedFunctionalRequirements().add(this.acceptedRequirementConverter.convertToBusinessObject(en));
        }
        for (KeyProblemEntity en : entity.getKeyProblems()) {
            businessObject.getKeyProblems().add(this.keyProblemConverter.convertToBusinessObject(en));
        }
        for (KeyRequirementEntity en : entity.getKeyRequirements()) {
            businessObject.getKeyRequirements().add(this.keyRequirementConverter.convertToBusinessObject(en));
        }
        for (ProductCapabilityEntity en : entity.getProductCapabilities()) {
            businessObject.getProductCapabilities().add(this.productCapabilityConverter.convertToBusinessObject(en));
        }
        for (ProductPropertyEntity en : entity.getProductProperties()) {
            businessObject.getProductProperties().add(this.productPropertyConverter.convertToBusinessObject(en));
        }
        for (RejectedFunctionalRequirementEntity en : entity.getRejectedFunctionalRequirements()) {
            businessObject.getRejectedFunctionalRequirements().add(this.rejectedRequirementConverter.convertToBusinessObject(en));
        }
        return businessObject;
    }
}

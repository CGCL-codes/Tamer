package org.modelibra.persistency.xml;

import java.util.List;
import java.util.Observable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entities;
import org.modelibra.IEntities;
import org.modelibra.IDomainModel;
import org.modelibra.DomainModel;
import org.modelibra.action.EntitiesAction;
import org.modelibra.action.Transaction;
import org.modelibra.config.ModelConfig;
import org.modelibra.persistency.IPersistentEntities;
import org.modelibra.persistency.PersistentModel;

/**
 * XML persistent model of entities.
 * 
 * @author Dzenan Ridjanovic
 * @version 2008-10-16
 */
public class XmlModel extends PersistentModel {

    private static final long serialVersionUID = 4830L;

    private static Log log = LogFactory.getLog(XmlModel.class);

    private String dataDirectoryPath;

    /**
	 * Constructs an XML persistent model from the domain model.
	 * 
	 * @param domainModel
	 *            domain model
	 * @param dataDirectoryPath
	 *            data directory path
	 */
    public XmlModel(IDomainModel domainModel, String dataDirectoryPath) {
        super(domainModel);
        this.dataDirectoryPath = dataDirectoryPath;
        if (domainModel.getModelConfig().isDefaultLoadSave()) {
            load();
        }
    }

    /**
	 * Sets the data directory path.
	 * 
	 * @param dataDirectoryPath
	 *            data directory path
	 */
    public void setDataDirectoryPath(String dataDirectoryPath) {
        this.dataDirectoryPath = dataDirectoryPath;
    }

    /**
	 * Gets the data directory path.
	 * 
	 * @return data directory path
	 */
    public String getDataDirectoryPath() {
        return dataDirectoryPath;
    }

    /**
	 * Updates an XML model data file from the observed domain model.
	 * 
	 * @param o
	 *            observed domain model
	 * @param arg
	 *            action
	 */
    public void update(Observable o, Object arg) {
        if (isLoaded()) {
            ModelConfig modelConfig = getModel().getModelConfig();
            if (modelConfig.isDefaultLoadSave()) {
                DomainModel model = (DomainModel) getModel();
                if (o == model) {
                    if (model.isSession()) {
                        if (arg instanceof Transaction) {
                            Transaction transaction = (Transaction) arg;
                            String transactionStatus = transaction.getStatus();
                            if (transactionStatus.equals("executed") || transactionStatus.equals("undone")) {
                                List<IEntities<?>> entries = transaction.getEntries();
                                for (IEntities<?> entry : entries) {
                                    String entryCode = entry.getConceptConfig().getEntitiesCode();
                                    IPersistentEntities persistentEntities = getPersistentEntry(entryCode);
                                    persistentEntities.save();
                                }
                            }
                        } else if (arg instanceof EntitiesAction) {
                            EntitiesAction action = (EntitiesAction) arg;
                            String actionName = action.getName();
                            Entities<?> entities = (Entities<?>) action.getEntities();
                            if (entities.isPersistent()) {
                                if (actionName.equals("add") || actionName.equals("remove") || actionName.equals("update")) {
                                    IEntities<?> entry = model.getModelMeta().getEntry(entities);
                                    if (entry != null) {
                                        String entryCode = entry.getConceptConfig().getEntitiesCode();
                                        IPersistentEntities persistentEntities = getPersistentEntry(entryCode);
                                        persistentEntities.save();
                                    }
                                }
                            }
                        }
                    } else if (arg instanceof EntitiesAction) {
                        EntitiesAction action = (EntitiesAction) arg;
                        String actionName = action.getName();
                        Entities<?> entities = (Entities<?>) action.getEntities();
                        if (entities.isPersistent()) {
                            if (actionName.equals("add") || actionName.equals("remove") || actionName.equals("update")) {
                                IEntities<?> entry = model.getModelMeta().getEntry(entities);
                                if (entry != null) {
                                    String entryCode = entry.getConceptConfig().getEntitiesCode();
                                    IPersistentEntities persistentEntities = getPersistentEntry(entryCode);
                                    if (persistentEntities != null) {
                                        persistentEntities.save();
                                    } else {
                                        log.info(entryCode + " is not an entry!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Closes the XML model.
	 */
    public void close() {
        super.close();
    }
}

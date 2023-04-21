package com.sri.emo.wizard.creation.persistence;

import com.jcorporate.expresso.core.dataobjects.jdbc.JDBCDataObject;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBConnectionPool;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.db.exception.DBRecordNotFoundException;
import com.jcorporate.expresso.core.registry.RequestRegistry;
import com.sri.common.dbobj.ObjectNotFoundException;
import com.sri.common.dbobj.RepositoryConversionException;
import com.sri.common.dbobj.RepositoryException;
import com.sri.emo.dbobj.WizDefinition;
import com.sri.emo.wizard.creation.CreationRepository;
import com.sri.emo.wizard.creation.model.CreationBeans;
import com.sri.emo.wizard.creation.model.FieldCompletion;
import org.apache.log4j.Logger;

/**
 * Expresso-specific implementation that converts the completion beans to
 * DBObjects.
 *
 * @author Michael Rimov
 */
public class ExpressoTemplateRepository implements CreationRepository {

    /**
     * The bean to dbobject converter bridge.
     */
    private final CreationDBObjConverter completionConverter;

    private Logger mLog = Logger.getLogger(ExpressoTemplateRepository.class);

    /**
     * Completion Repository constructor that takes a converter.
     *
     * @param converter CompletionDBObjConverter
     */
    public ExpressoTemplateRepository(final CreationDBObjConverter converter) {
        assert converter != null;
        completionConverter = converter;
    }

    /**
     * Adds a CompletionBean to the underlying data store.
     *
     * @param newValue CompletionBean the metadata for the wizard to update.
     * @return the new wizard id.
     * @throws RepositoryException           upon underlying error.
     * @throws RepositoryConversionException if there was an error converting
     *                                       the <tt>CompletionBean</tt> object to the underlying WizDefinition DBObject.
     */
    public int add(final CreationBeans newValue) throws RepositoryException, RepositoryConversionException {
        assert newValue != null;
        DBConnection connection = null;
        WizDefinition wizDef = null;
        try {
            wizDef = completionConverter.convertToDBObject(newValue);
            assert wizDef != null;
            connection = DBConnectionPool.getInstance(RequestRegistry.getDataContext()).getConnection("Add Completion Wizard");
            connection.setAutoCommit(false);
            wizDef.setConnection(connection);
            wizDef.add();
            connection.commit();
            newValue.setWizardId(new Integer(wizDef.getId()));
            return Integer.parseInt(wizDef.getId());
        } catch (DBException ex) {
            try {
                connection.rollback();
            } catch (DBException e) {
                mLog.debug("problem rolling back: " + ex.getMessage());
            }
            throw new RepositoryException("Error adding wizard definition: " + newValue.toString(), ex);
        } finally {
            if (connection != null) {
                connection.release();
            }
        }
    }

    /**
     * Deletes the completion bean from the underlying data store.
     *
     * @param wizardId int the wizard surrogate key.
     * @throws RepositoryException     upon deletion error.
     * @throws ObjectNotFoundException if the given wizard Id doesn't exist.
     */
    public void delete(final int wizardId) throws RepositoryException, ObjectNotFoundException {
        assert wizardId > 0;
        DBConnection dbConnection = null;
        try {
            WizDefinition wizDef = new WizDefinition();
            dbConnection = DBConnectionPool.getInstance(RequestRegistry.getDataContext()).getConnection("Delete Wizard Definition");
            dbConnection.setAutoCommit(false);
            wizDef.setConnection(dbConnection);
            wizDef.setId(wizardId);
            wizDef.retrieve();
            JDBCDataObject associatedObject = (JDBCDataObject) wizDef.getAdditionalInfo();
            associatedObject.setConnection(dbConnection);
            wizDef.delete();
            if (associatedObject != null) {
                if (associatedObject.find()) {
                    associatedObject.delete();
                }
            }
            CreationDefinition completionDef = new CreationDefinition();
            completionDef.setField(CreationDefinition.FLD_ID, wizardId);
            completionDef.setConnection(dbConnection);
            completionDef.deleteAll();
            CreationDetails completionDetails = new CreationDetails();
            completionDetails.setConnection(dbConnection);
            completionDetails.setField(CreationDetails.FLD_WIZARD_ID, wizardId);
            completionDetails.deleteAll();
            dbConnection.commit();
        } catch (DBRecordNotFoundException ex) {
            try {
                dbConnection.rollback();
            } catch (DBException e) {
                mLog.debug("problem rolling back: " + ex.getMessage());
            }
            throw new ObjectNotFoundException("Cannot find wizard by id of: " + wizardId + " it may have already been deleted.", ex);
        } catch (DBException ex) {
            try {
                dbConnection.rollback();
            } catch (DBException e) {
                mLog.debug("problem rolling back: " + ex.getMessage());
            }
            throw new RepositoryException("Error deleting wizard definition", ex);
        } finally {
            dbConnection.release();
        }
    }

    public CreationBeans findById(final int wizardId) throws RepositoryException, ObjectNotFoundException, RepositoryConversionException {
        try {
            WizDefinition wizDef = new WizDefinition();
            wizDef.setField(WizDefinition.FLD_ID, wizardId);
            wizDef.retrieve();
            CreationBeans returnValue = completionConverter.convertToBean(wizDef);
            assert returnValue != null;
            return returnValue;
        } catch (DBRecordNotFoundException ex) {
            throw new ObjectNotFoundException("Cannot find wizard by id of: " + wizardId, ex);
        } catch (DBException ex) {
            throw new RepositoryException("Error loading wizard definition", ex);
        }
    }

    public CreationBeans findById(final Object previouslyGeneratedKey) throws RepositoryException, ObjectNotFoundException, RepositoryConversionException {
        if (previouslyGeneratedKey == null) {
            throw new IllegalArgumentException("Previously Generated Key cannot be null");
        }
        if (previouslyGeneratedKey instanceof Integer) {
            return findById(((Integer) previouslyGeneratedKey).intValue());
        } else {
            int keyToUse = Integer.parseInt(previouslyGeneratedKey.toString());
            return findById(keyToUse);
        }
    }

    /**
     * Updates the completion bean.
     *
     * @param newValue CompletionBean
     * @throws RepositoryException     upon update erorr.
     * @throws ObjectNotFoundException if the wizard id specified in newValue
     *                                 doesn't exist in the underlying database object store.
     */
    public void update(final CreationBeans newValue) throws RepositoryException, ObjectNotFoundException {
        assert newValue != null;
        DBConnection updateTransaction = null;
        try {
            updateTransaction = DBConnectionPool.getInstance(RequestRegistry.getDataContext()).getConnection("Creation Wizard Update");
            updateTransaction.setAutoCommit(false);
            boolean success = false;
            WizDefinition oldDefinition = new WizDefinition();
            oldDefinition.setConnection(updateTransaction);
            oldDefinition.set(WizDefinition.FLD_ID, newValue.getWizardId());
            oldDefinition.retrieve();
            while (!success) {
                try {
                    WizDefinition wizDef = completionConverter.convertToDBObject(newValue);
                    wizDef.setConnection(updateTransaction);
                    assert wizDef != null;
                    wizDef.update();
                    success = true;
                } catch (IncompleteDetailsException incomplete) {
                    CreationDetails details = incomplete.getMissingDetail();
                    details.setField(CreationDetails.FLD_COMPLETION, FieldCompletion.NOT_INCLUDED.toString());
                    CreationDetails maxOrderQuery = new CreationDetails();
                    maxOrderQuery.setField(CreationDetails.FLD_WIZARD_ID, newValue.getWizardId().intValue());
                    int maxOrderNumber = (int) maxOrderQuery.max(CreationDetails.FLD_PART_ORDER);
                    details.setField(CreationDetails.FLD_PART_ORDER, maxOrderNumber + 1);
                    details.add();
                    mLog.warn("Found incomplete details: " + details.toString() + " have added it as 'fixed' part");
                }
            }
            updateTransaction.commit();
        } catch (DBRecordNotFoundException ex) {
            try {
                updateTransaction.rollback();
            } catch (DBException e) {
                mLog.debug("problem rolling back: " + ex.getMessage());
            }
            throw new ObjectNotFoundException("Cannot find wizard by id of: " + newValue.getWizardId(), ex);
        } catch (DBException ex) {
            try {
                updateTransaction.rollback();
            } catch (DBException e) {
                mLog.debug("problem rolling back: " + ex.getMessage());
            }
            throw new RepositoryException("Error adding wizard definition: " + newValue.toString(), ex);
        } finally {
            if (updateTransaction != null) {
                updateTransaction.release();
            }
        }
    }
}

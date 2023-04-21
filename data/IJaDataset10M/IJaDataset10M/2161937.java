package oracle.toplink.essentials.queryframework;

import oracle.toplink.essentials.exceptions.*;
import oracle.toplink.essentials.internal.sessions.UnitOfWorkImpl;

/**
 * <p><b>Purpose</b>:
 * Used for inserting or updating objects
 * WriteObjectQuery determines whether to perform a insert or an update on the database.
 *
 * <p><b>Responsibilities</b>:
 * <ul>
 * <li> Determines whether to perform a insert or an update on the database.
 * <li> Stores object in identity map for insert if required.
 * </ul>
 *
 * @author Yvon Lavoie
 * @since TOPLink/Java 1.0
 */
public class WriteObjectQuery extends ObjectLevelModifyQuery {

    public WriteObjectQuery() {
        super();
    }

    public WriteObjectQuery(Object objectToWrite) {
        this();
        setObject(objectToWrite);
    }

    public WriteObjectQuery(Call call) {
        this();
        setCall(call);
    }

    /**
     * INTERNAL:
     * Return if the object exists on the database or not.
     * This first checks existence in the chache, then on the database.
     */
    protected boolean doesObjectExist() {
        boolean doesExist;
        if (getSession().isUnitOfWork()) {
            doesExist = !((UnitOfWorkImpl) getSession()).isCloneNewObject(getObject());
            if (doesExist) {
                doesExist = ((UnitOfWorkImpl) getSession()).isObjectRegistered(getObject());
            }
        } else {
            DoesExistQuery existQuery = (DoesExistQuery) getDescriptor().getQueryManager().getDoesExistQuery().clone();
            existQuery.setObject(getObject());
            existQuery.setPrimaryKey(getPrimaryKey());
            existQuery.setDescriptor(getDescriptor());
            existQuery.setTranslationRow(getTranslationRow());
            doesExist = ((Boolean) getSession().executeQuery(existQuery)).booleanValue();
        }
        return doesExist;
    }

    /**
     * INTERNAL:
     * Perform a does exist check to decide whether to perform an insert or update and
     * delegate the work to the mechanism.  Does exists check will also perform an
     * optimistic lock check if required.
     * @exception  DatabaseException - an error has occurred on the database
     * @exception  OptimisticLockException - an error has occurred using the optimistic lock feature
     * @return object - the object being written.
     */
    public Object executeDatabaseQuery() throws DatabaseException, OptimisticLockException {
        if (getObjectChangeSet() != null) {
            return getQueryMechanism().executeWriteWithChangeSet();
        } else {
            return getQueryMechanism().executeWrite();
        }
    }

    /**
     * INTERNAL:
     * Decide whether to perform an insert, update or delete and
     * delegate the work to the mechanism.
     */
    public void executeCommit() throws DatabaseException, OptimisticLockException {
        boolean doesExist = doesObjectExist();
        boolean shouldBeDeleted = shouldObjectBeDeleted();
        if (doesExist) {
            if (shouldBeDeleted) {
                getQueryMechanism().deleteObjectForWrite();
            } else {
                getQueryMechanism().updateObjectForWrite();
            }
        } else if (!shouldBeDeleted) {
            getQueryMechanism().insertObjectForWrite();
        }
    }

    /**
     * INTERNAL:
     * Perform a does exist check to decide whether to perform an insert or update and
     * delegate the work to the mechanism.
     */
    public void executeCommitWithChangeSet() throws DatabaseException, OptimisticLockException {
        if (!getObjectChangeSet().isNew()) {
            if (!getSession().getCommitManager().isCommitInPreModify(objectChangeSet)) {
                getQueryMechanism().updateObjectForWriteWithChangeSet();
            }
        } else {
            if (getSession().getCommitManager().isCommitInPreModify(objectChangeSet)) {
                this.dontCascadeParts();
                getQueryMechanism().insertObjectForWriteWithChangeSet();
                getSession().getCommitManager().markShallowCommit(object);
            } else {
                getQueryMechanism().insertObjectForWriteWithChangeSet();
            }
        }
    }

    /**
     * INTERNAL:
     * Perform a shallow write. The decision, which shallow action should be 
     * executed is based on the existence of the associated object. If
     * the object exists, perform a shallow delete. Do a shallow
     * insert otherwise. 
     * Note that there currently is *no* shallow update operation. 
     * If shallow updates become necessary, the decision logic must
     * also perform a delete check as in {@link this.executeCommit}.
     */
    public void executeShallowWrite() {
        boolean doesExist = doesObjectExist();
        if (doesExist) {
            getQueryMechanism().shallowDeleteObjectForWrite(getObject(), this, getSession().getCommitManager());
        } else {
            getQueryMechanism().shallowInsertObjectForWrite(getObject(), this, getSession().getCommitManager());
        }
    }

    /**
     * PUBLIC:
     * Return if this is a write object query.
     */
    public boolean isWriteObjectQuery() {
        return true;
    }

    /**
     * INTERNAL:
     * Prepare the receiver for execution in a session.
     */
    public void prepareForExecution() throws QueryException {
        super.prepareForExecution();
        if ((getTranslationRow() == null) || (getTranslationRow().isEmpty())) {
            setTranslationRow(getDescriptor().getObjectBuilder().buildRowForTranslation(getObject(), getSession()));
        }
    }

    /**
     * INTERNAL:
     * Return whether a dependent object should be deleted from the database or not.  
     * Dependent objects should not be removed if not already scheduled for removal in a UoW. 
     * Returns "true" outside a UoW. Used by relationship mappings when cascading a delete operation.
     */
    public boolean shouldDependentObjectBeDeleted(Object object) {
        boolean shouldBeDeleted;
        if (getSession().isUnitOfWork()) {
            shouldBeDeleted = ((UnitOfWorkImpl) getSession()).isObjectDeleted(object);
        } else {
            shouldBeDeleted = true;
        }
        return shouldBeDeleted;
    }

    /**
     * INTERNAL:
     * Return if the attached object should be deleted from the database or not. 
     * This information is available only, if the session is a UoW. Returns "false" outside a UoW.
     * In this case an existence check should be performed and either an insert or update executed.
     * Only used internally.
     */
    protected boolean shouldObjectBeDeleted() {
        boolean shouldBeDeleted;
        if (getSession().isUnitOfWork()) {
            shouldBeDeleted = ((UnitOfWorkImpl) getSession()).isObjectDeleted(getObject());
        } else {
            shouldBeDeleted = false;
        }
        return shouldBeDeleted;
    }
}

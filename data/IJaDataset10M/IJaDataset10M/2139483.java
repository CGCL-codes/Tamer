package org.datanucleus.store.mapped.mapping;

import java.util.Collection;
import org.datanucleus.PropertyNames;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.exceptions.ReachableObjectNotCascadedException;
import org.datanucleus.store.scostore.CollectionStore;
import org.datanucleus.store.types.sco.SCO;
import org.datanucleus.store.types.sco.SCOCollection;
import org.datanucleus.store.types.sco.SCOContainer;
import org.datanucleus.store.types.sco.SCOUtils;
import org.datanucleus.util.NucleusLogger;

/**
 * Mapping for Collection/Set/List types.
 */
public class CollectionMapping extends AbstractContainerMapping implements MappingCallbacks {

    /**
     * Accessor for the Java type represented here.
     * @return The java type
     */
    public Class getJavaType() {
        return Collection.class;
    }

    public void insertPostProcessing(ObjectProvider op) {
    }

    /**
     * Method to be called after the insert of the owner class element.
     * @param ownerOP ObjectProvider of the owner
     */
    public void postInsert(ObjectProvider ownerOP) {
        ExecutionContext ec = ownerOP.getExecutionContext();
        Collection value = (Collection) ownerOP.provideField(mmd.getAbsoluteFieldNumber());
        if (containerIsStoredInSingleColumn()) {
            SCOUtils.validateObjectsForWriting(ec, value);
            return;
        }
        if (value == null) {
            replaceFieldWithWrapper(ownerOP, null, false, false);
            return;
        }
        Object[] collElements = value.toArray();
        if (!mmd.isCascadePersist()) {
            if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("007006", mmd.getFullFieldName()));
            }
            for (int i = 0; i < collElements.length; i++) {
                if (!ec.getApiAdapter().isDetached(collElements[i]) && !ec.getApiAdapter().isPersistent(collElements[i])) {
                    throw new ReachableObjectNotCascadedException(mmd.getFullFieldName(), collElements[i]);
                }
            }
            replaceFieldWithWrapper(ownerOP, value, false, false);
        } else {
            if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("007007", mmd.getFullFieldName()));
            }
            boolean needsAttaching = false;
            for (int i = 0; i < collElements.length; i++) {
                if (ownerOP.getExecutionContext().getApiAdapter().isDetached(collElements[i])) {
                    needsAttaching = true;
                    break;
                }
            }
            if (needsAttaching) {
                SCO collWrapper = replaceFieldWithWrapper(ownerOP, null, false, false);
                collWrapper.attachCopy(value);
            } else {
                if (value.size() > 0) {
                    ((CollectionStore) storeMgr.getBackingStoreForField(ownerOP.getExecutionContext().getClassLoaderResolver(), mmd, value.getClass())).addAll(ownerOP, value, 0);
                    replaceFieldWithWrapper(ownerOP, value, false, false);
                } else {
                    replaceFieldWithWrapper(ownerOP, null, false, false);
                }
            }
        }
    }

    /**
     * Method to be called after any update of the owner class element.
     * This method could be called in two situations
     * <ul>
     * <li>Update a collection field of an object by replacing the collection with a new collection, 
     * so UpdateRequest is called, which calls here</li>
     * <li>Persist a new object, and it needed to wait til the element was inserted so
     * goes into dirty state and then flush() triggers UpdateRequest, which comes here</li>
     * </ul>
     * @param ownerOP ObjectProvider of the owner
     */
    public void postUpdate(ObjectProvider ownerOP) {
        ExecutionContext ec = ownerOP.getExecutionContext();
        Collection value = (Collection) ownerOP.provideField(mmd.getAbsoluteFieldNumber());
        if (containerIsStoredInSingleColumn()) {
            SCOUtils.validateObjectsForWriting(ec, value);
            return;
        }
        if (value == null) {
            ((CollectionStore) storeMgr.getBackingStoreForField(ec.getClassLoaderResolver(), mmd, null)).clear(ownerOP);
            replaceFieldWithWrapper(ownerOP, null, false, false);
            return;
        }
        if (value instanceof SCOContainer) {
            SCOContainer sco = (SCOContainer) value;
            if (ownerOP.getObject() == sco.getOwner() && mmd.getName().equals(sco.getFieldName())) {
                sco.flush();
                return;
            }
            if (sco.getOwner() != null) {
                throw new NucleusException(LOCALISER.msg("CollectionMapping.WrongOwnerError")).setFatal();
            }
        }
        if (!mmd.isCascadeUpdate()) {
            if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("007008", mmd.getFullFieldName()));
            }
            return;
        }
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("007009", mmd.getFullFieldName()));
        }
        CollectionStore backingStore = ((CollectionStore) storeMgr.getBackingStoreForField(ec.getClassLoaderResolver(), mmd, value.getClass()));
        backingStore.update(ownerOP, value);
        replaceFieldWithWrapper(ownerOP, value, false, false);
    }

    /**
     * Method to be called before any delete of the owner class element.
     * @param sm StateManager of the owner
     */
    public void preDelete(ObjectProvider sm) {
        if (containerIsStoredInSingleColumn()) {
            return;
        }
        sm.getExecutionContext().getApiAdapter().isLoaded(sm, mmd.getAbsoluteFieldNumber());
        Collection value = (Collection) sm.provideField(mmd.getAbsoluteFieldNumber());
        if (value == null) {
            return;
        }
        boolean dependent = mmd.getCollection().isDependentElement();
        if (mmd.isCascadeRemoveOrphans()) {
            dependent = true;
        }
        boolean hasJoin = (mmd.getJoinMetaData() != null);
        boolean hasFK = false;
        if (!hasJoin) {
            if (mmd.getElementMetaData() != null && mmd.getElementMetaData().getForeignKeyMetaData() != null) {
                hasFK = true;
            } else if (mmd.getForeignKeyMetaData() != null) {
                hasFK = true;
            }
            AbstractMemberMetaData[] relatedMmds = mmd.getRelatedMemberMetaData(sm.getExecutionContext().getClassLoaderResolver());
            if (relatedMmds != null && relatedMmds[0].getForeignKeyMetaData() != null) {
                hasFK = true;
            }
        }
        if (sm.getExecutionContext().getNucleusContext().getPersistenceConfiguration().getStringProperty(PropertyNames.PROPERTY_DELETION_POLICY).equals("JDO2")) {
            hasFK = false;
        }
        if (sm.getExecutionContext().getManageRelations()) {
            sm.getExecutionContext().getRelationshipManager(sm).relationChange(mmd.getAbsoluteFieldNumber(), value, null);
        }
        if (dependent || hasJoin || !hasFK) {
            if (!(value instanceof SCO)) {
                value = (Collection) sm.wrapSCOField(mmd.getAbsoluteFieldNumber(), value, false, false, true);
            }
            value.clear();
            ((SCOCollection) value).flush();
        }
    }
}

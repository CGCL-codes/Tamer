package net.sf.joafip.store.service;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.heapfile.record.entity.DataRecordIdentifier;
import net.sf.joafip.heapfile.service.HeapException;
import net.sf.joafip.heapfile.service.IHeapDataManager;
import net.sf.joafip.store.entity.ClassDeclaredFields;
import net.sf.joafip.store.entity.heaprecordable.IHeapRecordable;
import net.sf.joafip.store.entity.objectio.IObjectIdentityKey;
import net.sf.joafip.store.service.garbage.GarbageException;
import net.sf.joafip.store.service.garbage.LinkManager;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableException;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.manager.ISubsituteSynchronizer;
import net.sf.joafip.store.service.objectio.manager.ObjectIOManager;
import net.sf.joafip.store.service.proxy.ProxyManager;
import org.apache.log4j.Logger;

/**
 * save operation for {@link Store}<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class StoreSaver {

    private static final Logger _log = Logger.getLogger(StoreSaver.class);

    private static final String FAILED_CANCEL_MODIFICATION_ON_FILE = "failed cancel modification on file";

    private static final String REFLECT_ERROR = "reflect error";

    private static final String FAILED_PUT_MODIFICATION_ON_FILE = "failed put modification on file";

    private static final String FAILED_SAVE_RECORD = "failed save record";

    private static final String IDENTITY_HASHCODE = ", identity hashcode ";

    private static final String FAILED_CREATE_SUBSTITUTE_OBJECT = "failed create substitute object";

    private static final String OBJECT_MUST_HAVE_DATA_RECORD_ASSOCIATED_TO_IT = "object must have data record associated to it";

    private static final String OBJECT_MUST_HAVE_STATE = "object must have state";

    private static final HelperReflect helperReflect = HelperReflect.getInstance();

    /** object storage management in file */
    private final IStore store;

    /** map substitute class by class (HashMap) */
    private final Map<Class, Class> substituteMap = new HashMap<Class, Class>();

    /** map of synchronizer by class (HashMap) */
    private final Map<Class, ISubsituteSynchronizer> synchronizerMap = new HashMap<Class, ISubsituteSynchronizer>();

    /**
	 * after {@link #MAX_MODIFICATION_COUNT} modification will save to win
	 * memory
	 */
    public static final int MAX_MODIFICATION_COUNT = 1000;

    /**
	 * used for visit by garbage and remove, count for modification since last
	 * save
	 */
    private int modificationCount = 0;

    /** object reading and writing on data file manager */
    private final ObjectIOManager objectIOManager;

    /** data manager on heap file */
    private final IHeapDataManager dataManager;

    /** manager of {@link IHeapRecordable} */
    private final HeapRecordableManager heapRecordableManager;

    /** cache of declared field of class */
    private final ClassDeclaredFields classDeclaredFields;

    /** true if garbage management activated */
    private final boolean garbageManagement;

    /** to manage link between object */
    private final LinkManager linkManager;

    /** object visited for saving set (HashSet) */
    private final Set<IObjectIdentityKey> visitedForSave = new HashSet<IObjectIdentityKey>();

    /** data record identifier of object attached to root set */
    private final Set<DataRecordIdentifier> attachedToRoot = new TreeSet<DataRecordIdentifier>();

    /**
	 * used by save, set of object witch for state changed, will be saved
	 * (HashSet)
	 */
    private final Set<IObjectIdentityKey> modifiedPool = new HashSet<IObjectIdentityKey>();

    /**
	 * number of object visited for save
	 */
    private int numberOfVisitedForSave = 0;

    /**
	 * number of modified object saved
	 */
    private int numberOfModified = 0;

    /** true if change log enabled */
    private boolean changeLogEnabled;

    public StoreSaver(final IStore store) {
        super();
        this.store = store;
        objectIOManager = store.getObjectIOManager();
        dataManager = store.getDataManager();
        heapRecordableManager = store.getHeapRecordableManager();
        classDeclaredFields = store.getClassDeclaredFields();
        garbageManagement = store.isGarbageManagement();
        linkManager = store.getLinkManager();
    }

    public void setSubstitution(final Class replaced, final Class substitute, final ISubsituteSynchronizer synchronizer) {
        substituteMap.put(replaced, substitute);
        synchronizerMap.put(substitute, synchronizer);
    }

    /**
	 * save the object modification, creation, and deletion on file<br>
	 * then clear all informations on object persistence, persistent object are
	 * detached<br>
	 * 
	 * @throws StoreException
	 * 
	 */
    public void save() throws StoreException {
        changeLogEnabled = store.isChangeLogEnabled();
        if (changeLogEnabled) {
            try {
                final String changeFileName = dataManager.getChangeFileName();
                objectIOManager.openChangeLog(changeFileName);
            } catch (Exception exception) {
                _log.warn("change log disabled", exception);
                changeLogEnabled = false;
            }
        }
        try {
            saveModification();
            visitedForSave.clear();
            modifiedPool.clear();
            attachedToRoot.clear();
            visitForSave(store.getStoreRoot());
            numberOfVisitedForSave = visitedForSave.size();
            linkManager.initializeLinkUpdate(attachedToRoot);
            checkObjectStateChange();
            visitedForSave.clear();
            try {
                linkManager.linkUpdate();
            } catch (GarbageException exception) {
                throw new StoreException(exception);
            }
            attachedToRoot.clear();
            saveModifiedPool();
        } finally {
            if (changeLogEnabled) {
                try {
                    objectIOManager.closeChangeLog();
                } catch (ObjectIOException exception) {
                    _log.error("failed close change log", exception);
                }
            }
        }
    }

    public void doNotSave() throws StoreException {
        clearStateAndModification();
        numberOfModified = 0;
        numberOfVisitedForSave = 0;
    }

    /**
	 * 
	 * @throws StoreException
	 */
    private void saveModifiedPool() throws StoreException {
        for (IObjectIdentityKey objectIdentityKey : modifiedPool) {
            final Object object = objectIdentityKey.getObject();
            try {
                objectIOManager.write(object);
            } catch (ObjectIOException exception) {
                try {
                    clearStateAndModification();
                } catch (Throwable t) {
                }
                _log.fatal(FAILED_SAVE_RECORD, exception);
                logFatalHaveState(objectIdentityKey);
                throw new StoreException(FAILED_SAVE_RECORD + " " + object.getClass(), exception);
            } catch (RuntimeException exception) {
                try {
                    clearStateAndModification();
                } catch (Throwable t) {
                }
                _log.fatal(FAILED_SAVE_RECORD, exception);
                logFatalHaveState(objectIdentityKey);
                throw exception;
            }
        }
        try {
            heapRecordableManager.save();
        } catch (HeapRecordableException exception) {
            throw new StoreException(exception);
        }
        try {
            dataManager.flush();
            numberOfModified = modifiedPool.size();
            modifiedPool.clear();
        } catch (HeapException exception) {
            try {
                clearStateAndModification();
            } catch (Throwable t) {
            }
            _log.fatal(FAILED_PUT_MODIFICATION_ON_FILE, exception);
            throw new StoreException(FAILED_PUT_MODIFICATION_ON_FILE, exception);
        }
    }

    private void logFatalHaveState(final IObjectIdentityKey objectIdentityKey) {
        _log.fatal("object " + objectIdentityKey + "\nhave state=" + objectIOManager.objectHavePersistenceState(objectIdentityKey));
    }

    /**
	 * clear the object modification, creation, and deletion on standby to do on
	 * file<br>
	 * clear all informations on object persistence, persistent object are
	 * detached<br>
	 * 
	 * @throws StoreException
	 */
    private void clearStateAndModification() throws StoreException {
        heapRecordableManager.doNotSave();
        modifiedPool.clear();
        try {
            dataManager.clearStandbyModification();
        } catch (HeapException exception) {
            _log.fatal(FAILED_CANCEL_MODIFICATION_ON_FILE);
            throw new StoreException(FAILED_CANCEL_MODIFICATION_ON_FILE, exception);
        }
    }

    public void resetModificationCount() {
        modificationCount = 0;
    }

    public void performModificationDone() throws StoreException {
        if (++modificationCount == MAX_MODIFICATION_COUNT) {
            saveModification();
        }
    }

    /**
	 * @throws StoreException
	 */
    public void saveModification() throws StoreException {
        try {
            if (modificationCount > 0) {
                heapRecordableManager.save();
                dataManager.flush();
                modificationCount = 0;
            }
        } catch (HeapRecordableException exception) {
            throw new StoreException(exception);
        } catch (HeapException exception) {
            throw new StoreException(exception);
        }
    }

    /**
	 * 
	 * @return number of modified object saved
	 */
    public int getNumberOfModified() {
        return numberOfModified;
    }

    /**
	 * 
	 * @return number of object visited for save
	 */
    public int getNumberOfVisitedForSave() {
        return numberOfVisitedForSave;
    }

    /**
	 * visit object that have a generic persistence mode to create
	 * {@link #visitedForSave}.<br>
	 * Also update {@link #attachedToRoot}<br>
	 * Also update {@link #modifiedPool} for object that have not generic
	 * persistence mode<br>
	 * 
	 * @param rootObject
	 *            the root object from witch start the visit
	 * @throws StoreException
	 */
    private void visitForSave(final Object rootObject) throws StoreException {
        IObjectIdentityKey objectToVisitIdentityKey;
        final Deque<IObjectIdentityKey> toVisitForSaveQueue = new LinkedList<IObjectIdentityKey>();
        final IObjectIdentityKey objectIdentityKey = objectIOManager.getObjectIdentityKey(rootObject);
        toVisitForSaveQueue.addLast(objectIdentityKey);
        objectToVisitIdentityKey = toVisitForSaveQueue.pollFirst();
        try {
            while (objectToVisitIdentityKey != null) {
                final Object objectToVisit = objectToVisitIdentityKey.getObject();
                objectIOManager.checkAssociateDataRecordCreateIfNeed(objectToVisit);
                final DataRecordIdentifier identifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(objectToVisit);
                attachedToRoot.add(identifier);
                visitOneObject(toVisitForSaveQueue, objectToVisitIdentityKey);
                objectToVisitIdentityKey = toVisitForSaveQueue.pollFirst();
            }
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
    }

    private void visitOneObject(final Deque<IObjectIdentityKey> toVisitForSaveQueue, final IObjectIdentityKey objectToVisitIdentityKey) throws StoreException {
        final Object objectToVisit = objectToVisitIdentityKey.getObject();
        final Class<?> objectToVisitClass = ProxyManager.classOfObject(objectToVisit);
        if (objectToVisitClass.isAnnotationPresent(NotStorableClass.class)) {
            throw new StoreException("class " + objectToVisitClass.getName() + " is not storable");
        }
        if (ProxyManager.isLoaded(objectToVisit) && !visitedForSave.contains(objectToVisitIdentityKey)) {
            visitedForSave.add(objectToVisitIdentityKey);
            if (haveGenericPersistence(objectToVisitClass)) {
                final Set<IObjectIdentityKey> soonToVisitSet;
                if (objectToVisitClass.isArray()) {
                    soonToVisitSet = arraySoonVisit(objectToVisit, objectToVisitClass);
                } else {
                    soonToVisitSet = objectSoonVisit(objectToVisit, objectToVisitClass);
                }
                addSoonToVisit(toVisitForSaveQueue, soonToVisitSet);
            } else {
                try {
                    if (objectIOManager.isMutableAndValueChanged(objectToVisit, objectToVisitClass)) {
                        addObjectToModifiedPool(objectToVisit);
                    }
                } catch (ObjectIOException exception) {
                    throw new StoreException(exception);
                }
            }
        }
    }

    private void addSoonToVisit(final Deque<IObjectIdentityKey> toVisitForSaveQueue, final Set<IObjectIdentityKey> soonToVisitSet) throws StoreException {
        for (IObjectIdentityKey objectIdentityKey : soonToVisitSet) {
            toVisitForSaveQueue.addLast(objectIdentityKey);
        }
    }

    /**
	 * check if array element needs to be visited for save
	 * 
	 * @param array
	 * @param arrayClass
	 * @return set of object to visit for save
	 * @throws StoreException
	 */
    private Set<IObjectIdentityKey> arraySoonVisit(final Object array, final Class<?> arrayClass) throws StoreException {
        try {
            final Set<IObjectIdentityKey> set = new HashSet<IObjectIdentityKey>();
            final Class arrayComponentType = arrayClass.getComponentType();
            if (!arrayComponentType.isPrimitive()) {
                final int arrayLength = Array.getLength(array);
                for (int index = 0; index < arrayLength; index++) {
                    final Object arrayElement = helperReflect.getArrayElement(array, index);
                    if (arrayElement != null) {
                        final IObjectIdentityKey objectIdentityKey = objectIOManager.getObjectIdentityKey(arrayElement);
                        addToListOfToVisitForSave(set, objectIdentityKey);
                    }
                }
            }
            return set;
        } catch (RuntimeException exception) {
            _log.fatal("checking state of array object class " + array.getClass());
            throw exception;
        } catch (ReflectException exception) {
            _log.fatal(REFLECT_ERROR, exception);
            throw new StoreException(exception);
        }
    }

    /**
	 * check if object fields needs to be visited for save
	 * 
	 * @param object
	 * @param objectClass
	 * @return set of object to visit for save
	 * @throws StoreException
	 */
    private Set<IObjectIdentityKey> objectSoonVisit(final Object object, final Class<?> objectClass) throws StoreException {
        try {
            final Set<IObjectIdentityKey> set = new HashSet<IObjectIdentityKey>();
            final Field[] fields = classDeclaredFields.allDeclaredFields(objectClass);
            for (Field field : fields) {
                final Class<?> type = field.getType();
                if (!type.isPrimitive()) {
                    field.setAccessible(true);
                    final Object fieldValue = helperReflect.getFieldValue(object, field);
                    if (fieldValue != null) {
                        final IObjectIdentityKey objectIdentityKey = objectIOManager.getObjectIdentityKey(fieldValue);
                        addToListOfToVisitForSave(set, objectIdentityKey);
                    }
                    field.setAccessible(false);
                }
            }
            return set;
        } catch (RuntimeException exception) {
            _log.fatal("checking state of object class " + object.getClass());
            throw exception;
        } catch (ReflectException exception) {
            _log.fatal(REFLECT_ERROR, exception);
            throw new StoreException(exception);
        }
    }

    private void addToListOfToVisitForSave(final Set<IObjectIdentityKey> set, final IObjectIdentityKey objectIdentityKey) throws StoreException {
        final Object object = objectIdentityKey.getObject();
        if (object != null) {
            final Class objectClass = ProxyManager.classOfObject(object);
            final Class substituteClass = substituteMap.get(objectClass);
            if (substituteClass == null) {
                set.add(objectIdentityKey);
            } else {
                Object substitute;
                try {
                    substitute = objectIOManager.getSubstitute(object);
                    if (substitute == null) {
                        substitute = substituteClass.newInstance();
                        objectIOManager.setSubstitute(object, substitute);
                        synchronizeSubstitute(substituteClass, object, substitute);
                    }
                    set.add(objectIOManager.getObjectIdentityKey(substitute));
                } catch (InstantiationException exception) {
                    _log.fatal(FAILED_CREATE_SUBSTITUTE_OBJECT);
                    throw new StoreException(FAILED_CREATE_SUBSTITUTE_OBJECT, exception);
                } catch (IllegalAccessException exception) {
                    _log.fatal(FAILED_CREATE_SUBSTITUTE_OBJECT);
                    throw new StoreException(FAILED_CREATE_SUBSTITUTE_OBJECT, exception);
                } catch (ObjectIOException exception) {
                    throw new StoreException(exception);
                }
            }
        }
    }

    /**
	 * synchronize substitute object with the object it replace
	 * 
	 * @param substiteClass
	 *            the object substitute class
	 * @param object
	 *            the original object
	 * @param objectSubstitute
	 *            the substitute object
	 */
    private void synchronizeSubstitute(final Class substiteClass, final Object object, final Object objectSubstitute) {
        final ISubsituteSynchronizer synchronizer = synchronizerMap.get(substiteClass);
        synchronizer.synchronizeSubstitute(object, objectSubstitute);
    }

    /**
	 * for all object in {@link #visitedForSave} check if state changed for
	 * {@link #modifiedPool} update
	 * 
	 * @throws StoreException
	 * 
	 */
    private void checkObjectStateChange() throws StoreException {
        try {
            for (IObjectIdentityKey objectIdentityKey : visitedForSave) {
                final Object object = objectIdentityKey.getObject();
                if (changeLogEnabled) {
                    objectIOManager.storeInChangeLog(object);
                }
                final Class<?> objectClass = ProxyManager.classOfObject(object);
                if (haveGenericPersistence(objectClass)) {
                    if (objectClass.isArray()) {
                        checkArrayStateChange(object, objectClass);
                    } else {
                        checkNotArrayStateChange(object, objectClass);
                    }
                }
            }
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
    }

    private void checkArrayStateChange(final Object array, final Class<?> arrayType) throws StoreException {
        try {
            final int arrayLength = Array.getLength(array);
            final Class arrayComponentType = arrayType.getComponentType();
            final Object[] originalValue = objectIOManager.getOriginalValue(array);
            if (originalValue == null) {
                addObjectToModifiedPool(array);
                if (!arrayComponentType.isPrimitive()) {
                    checkNotPrimitiveArrayStateOriginalValueUnknew(array, arrayLength);
                }
            } else if (arrayComponentType.isPrimitive()) {
                checkPrimitiveArrayState(array, arrayLength, originalValue);
            } else {
                checkNotPrimitiveArrayStateOriginalValueKnew(array, arrayLength, originalValue);
            }
        } catch (RuntimeException exception) {
            _log.fatal("checking state of array object class " + array.getClass());
            throw exception;
        } catch (ReflectException exception) {
            _log.fatal(REFLECT_ERROR, exception);
            throw new StoreException(exception);
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
    }

    private void checkPrimitiveArrayState(final Object array, final int arrayLength, final Object[] originalValueArray) throws ReflectException, StoreException {
        boolean stateChanged = arrayLength != originalValueArray.length;
        for (int index = 0; !stateChanged && index < arrayLength; index++) {
            final Object elementValue;
            elementValue = helperReflect.getArrayElement(array, index);
            final Object originalElementValue;
            originalElementValue = originalValueArray[index];
            stateChanged = elementValue == null && originalElementValue != null || !elementValue.equals(originalElementValue);
        }
        if (stateChanged) {
            addObjectToModifiedPool(array);
        }
    }

    private void checkNotPrimitiveArrayStateOriginalValueKnew(final Object array, final int arrayLength, final Object[] originalValueArray) throws ReflectException, StoreException {
        boolean stateChanged = false;
        final int originalArrayLength = originalValueArray.length;
        final int maxLength = arrayLength > originalArrayLength ? arrayLength : originalArrayLength;
        for (int index = 0; index < maxLength; index++) {
            final Object elementValue;
            final Class elementClass;
            if (index < arrayLength) {
                elementValue = helperReflect.getArrayElement(array, index);
                if (elementValue == null) {
                    elementClass = null;
                } else {
                    elementClass = ProxyManager.classOfObject(elementValue);
                }
            } else {
                elementValue = null;
                elementClass = null;
            }
            final Object originalElementValue;
            if (index < originalArrayLength) {
                originalElementValue = originalValueArray[index];
            } else {
                originalElementValue = null;
            }
            stateChanged |= checkReferenceChanged(array, elementValue, originalElementValue, elementClass);
        }
        if (stateChanged) {
            addObjectToModifiedPool(array);
        }
    }

    private void checkNotPrimitiveArrayStateOriginalValueUnknew(final Object array, final int arrayLength) throws ReflectException, StoreException {
        for (int index = 0; index < arrayLength; index++) {
            final Object elementValue = helperReflect.getArrayElement(array, index);
            final Class elementClass;
            if (elementValue == null) {
                elementClass = null;
            } else {
                elementClass = ProxyManager.classOfObject(elementValue);
            }
            checkReferenceChanged(array, elementValue, null, elementClass);
        }
    }

    /**
	 * check state change of an object
	 * 
	 * @param object
	 *            the object to check state
	 * @param objectClass
	 *            the object class
	 * @throws StoreException
	 * 
	 */
    private void checkNotArrayStateChange(final Object object, final Class<?> objectClass) throws StoreException {
        try {
            boolean stateChanged = false;
            final Field[] fields = classDeclaredFields.allDeclaredFields(objectClass);
            final Object[] originalValue = objectIOManager.getOriginalValue(object);
            final boolean notInFile = originalValue == null;
            if (notInFile) {
                stateChanged = true;
            }
            int index = 0;
            for (Field field : fields) {
                stateChanged |= checkFieldState(object, originalValue, index, field);
                index++;
            }
            if (stateChanged) {
                addObjectToModifiedPool(object);
            }
        } catch (RuntimeException exception) {
            _log.fatal("checking state of object class " + object.getClass());
            throw exception;
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
    }

    /**
	 * check field of an object state change
	 * 
	 * @param object
	 *            the object to check field state
	 * @param originalValue
	 *            original fields value ( values in file )
	 * @param index
	 * @param field
	 *            the field to check definition of th object
	 * @return true if state changed
	 * @throws StoreException
	 * 
	 */
    private boolean checkFieldState(final Object object, final Object[] originalValue, final int index, final Field field) throws StoreException {
        try {
            final boolean stateChanged;
            Object fieldValue = null;
            field.setAccessible(true);
            try {
                fieldValue = helperReflect.getFieldValue(object, field);
            } catch (ReflectException exception) {
                _log.fatal(REFLECT_ERROR, exception);
                throw new StoreException(exception);
            }
            final Class fieldClass;
            if (fieldValue == null) {
                fieldClass = null;
            } else {
                fieldClass = ProxyManager.classOfObject(fieldValue);
            }
            final Class<?> type = field.getType();
            final Object originalFieldValue;
            if (originalValue == null) {
                originalFieldValue = null;
            } else {
                originalFieldValue = originalValue[index];
            }
            if (type.isPrimitive()) {
                if (fieldValue == null && originalFieldValue != null || !fieldValue.equals(originalFieldValue)) {
                    stateChanged = true;
                } else {
                    stateChanged = false;
                }
            } else {
                stateChanged = checkReferenceChanged(object, fieldValue, originalFieldValue, fieldClass);
            }
            field.setAccessible(false);
            return stateChanged;
        } catch (RuntimeException exception) {
            _log.fatal("checking state of field " + field);
            throw exception;
        }
    }

    /**
	 * check if referenced changed<br>
	 * 
	 * @param referencingObject
	 *            the referencing object
	 * @param referencedObject
	 *            the referenced object by referecing, can be null if no more
	 *            referenced
	 * @param originalReferenced
	 *            the original (previously) referenced object by referencing,
	 *            can be null if no previously referenced
	 * @param referencedClass
	 *            the referenced object class
	 * @param objectClass
	 *            the referenced object class
	 * @return true if referencing object state changed
	 * @throws StoreException
	 */
    private boolean checkReferenceChanged(final Object referencingObject, final Object referencedObject, final Object originalReferenced, final Class objectClass) throws StoreException {
        final boolean stateChanged;
        try {
            if (originalReferenced != referencedObject) {
                referencedChanged(referencingObject, referencedObject, originalReferenced, objectClass);
                stateChanged = true;
            } else {
                stateChanged = false;
            }
            return stateChanged;
        } catch (StoreException exception) {
            _log.fatal("check reference error: original identity hashcode=" + System.identityHashCode(originalReferenced) + ", new identity hashcode " + System.identityHashCode(referencedObject));
            throw exception;
        }
    }

    /**
	 * operation for referenced object changed, means that referencedObject and
	 * originalReferenced are different for referencingObject
	 * 
	 * @param referencingObject
	 *            the referencing object
	 * @param referencedObject
	 *            the referenced object by referecing, can be null if no more
	 *            referenced
	 * @param originalReferenced
	 *            the original (previously) referenced object by referencing,
	 *            can be null if no previously referenced
	 * @param objectClass
	 *            the referenced object class
	 * @throws StoreException
	 */
    private void referencedChanged(final Object referencingObject, final Object referencedObject, final Object originalReferenced, final Class objectClass) throws StoreException {
        if (_log.isDebugEnabled()) {
            _log.debug("unreferenced: " + IDENTITY_HASHCODE + System.identityHashCode(originalReferenced) + " referenced: " + IDENTITY_HASHCODE + +System.identityHashCode(referencedObject));
        }
        if (referencedObject != null) {
            try {
                if (!objectIOManager.isMutable(referencedObject, objectClass)) {
                    addObjectToModifiedPool(referencedObject);
                }
            } catch (ObjectIOException exception) {
                throw new StoreException(exception);
            }
        }
        if (garbageManagement) {
            if (originalReferenced != null) {
                decrementNumberOfLink(referencingObject, originalReferenced);
            }
            if (referencedObject != null) {
                final Class referencedClass = ProxyManager.classOfObject(referencedObject);
                if (_log.isDebugEnabled()) {
                    _log.debug("new reference to " + referencedClass + IDENTITY_HASHCODE + System.identityHashCode(referencedObject));
                }
                incrementNumberOfLink(referencingObject, referencedObject);
            }
        }
    }

    /**
	 * incremente number of link to the object
	 * 
	 * @param referencingObject
	 * @param referencedObject
	 * @throws StoreException
	 * 
	 */
    private void incrementNumberOfLink(final Object referencingObject, final Object referencedObject) throws StoreException {
        final DataRecordIdentifier referencedDataRecordIdentifier;
        try {
            referencedDataRecordIdentifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(referencedObject);
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
        if (referencedDataRecordIdentifier == null) {
            throw new StoreException(OBJECT_MUST_HAVE_DATA_RECORD_ASSOCIATED_TO_IT);
        }
        final DataRecordIdentifier referencingDataRecordIdentifier;
        try {
            referencingDataRecordIdentifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(referencingObject);
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
        if (referencingDataRecordIdentifier == null) {
            throw new StoreException(OBJECT_MUST_HAVE_DATA_RECORD_ASSOCIATED_TO_IT);
        }
        linkManager.addLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        if (_log.isDebugEnabled()) {
            _log.debug("inc nb of link: " + "object " + referencedObject + " " + System.identityHashCode(referencedObject) + " class " + referencedObject.getClass());
        }
    }

    /**
	 * decremente number of link to the object
	 * 
	 * @param referencingObject
	 * @param referencedObject
	 * @throws StoreException
	 * 
	 */
    private void decrementNumberOfLink(final Object referencingObject, final Object referencedObject) throws StoreException {
        if (!objectIOManager.objectHavePersistenceState(referencedObject)) {
            throw new StoreException(OBJECT_MUST_HAVE_STATE);
        }
        final DataRecordIdentifier referencedDataRecordIdentifier;
        try {
            referencedDataRecordIdentifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(referencedObject);
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
        if (referencedDataRecordIdentifier == null) {
            throw new StoreException(OBJECT_MUST_HAVE_DATA_RECORD_ASSOCIATED_TO_IT);
        }
        final DataRecordIdentifier referencingDataRecordIdentifier;
        try {
            referencingDataRecordIdentifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(referencingObject);
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        }
        if (referencingDataRecordIdentifier == null) {
            throw new StoreException(OBJECT_MUST_HAVE_DATA_RECORD_ASSOCIATED_TO_IT);
        }
        linkManager.removeLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        if (_log.isDebugEnabled()) {
            _log.debug("decrement nb of link: object " + referencedObject + " " + System.identityHashCode(referencedObject) + " class " + referencedObject.getClass());
        }
    }

    /**
	 * add object to modified objet pool if not already in it<br>
	 * 
	 * @param object
	 * @throws StoreException
	 */
    private void addObjectToModifiedPool(final Object object) throws StoreException {
        if (object == null) {
            throw new IllegalArgumentException("object to save can not be null");
        }
        final IObjectIdentityKey objectIdentityKey = objectIOManager.getObjectIdentityKey(object);
        if (!modifiedPool.contains(objectIdentityKey)) {
            if (_log.isDebugEnabled()) {
                DataRecordIdentifier dataRecordIdentifier;
                try {
                    dataRecordIdentifier = objectIOManager.getDataRecordIdentifierAssociatedToObject(object);
                } catch (ObjectIOException exception) {
                    dataRecordIdentifier = null;
                }
                _log.debug("added to save " + object.getClass() + IDENTITY_HASHCODE + System.identityHashCode(object) + " data record identifier=" + dataRecordIdentifier);
            }
            modifiedPool.add(objectIdentityKey);
        }
    }

    /**
	 * 
	 * @param objectClass
	 * @return true if have generic persistence
	 */
    private boolean haveGenericPersistence(final Class<?> objectClass) {
        final boolean haveGenericPersistence;
        if (substituteMap.containsKey(objectClass)) {
            haveGenericPersistence = true;
        } else {
            haveGenericPersistence = objectIOManager.useGenericPersistence(objectClass);
        }
        return haveGenericPersistence;
    }
}

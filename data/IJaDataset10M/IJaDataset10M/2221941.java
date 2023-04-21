package net.sf.joafip.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.joafip.Fortest;
import net.sf.joafip.NoStorableAccess;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.entity.MutableInteger;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.IHeapDataManager;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.store.entity.EnumKey;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.garbage.DataRecordIdentifierRBTNode;
import net.sf.joafip.store.entity.garbage.ReferenceLink;
import net.sf.joafip.store.entity.objectio.IObjectStateMapCLeanListener;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.IGarbageListener;
import net.sf.joafip.store.service.IStore;
import net.sf.joafip.store.service.StoreClassNotFoundException;
import net.sf.joafip.store.service.StoreDataCorruptedException;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.StoreInvalidClassException;
import net.sf.joafip.store.service.StoreNotSerializableException;
import net.sf.joafip.store.service.StoreTooBigForSerializationException;
import net.sf.joafip.store.service.copier.CopierException;
import net.sf.joafip.store.service.export_import.out.IExporterListener;
import net.sf.joafip.store.service.garbage.recordmgr.LinkRecordManager;
import net.sf.joafip.store.service.objectio.manager.ISubsituteSynchronizer;
import net.sf.joafip.store.service.objectio.manager.ISubstituteObjectManager;
import net.sf.joafip.store.service.objectio.manager.ObjectIOManager;
import net.sf.joafip.store.service.objectio.serialize.input.IObjectInput;
import net.sf.joafip.store.service.objectio.serialize.output.IObjectOutput;
import net.sf.joafip.store.service.proxy.IInstanceFactory;
import net.sf.joafip.store.service.proxy.ProxyManager2;
import net.sf.joafip.store.service.saver.ISaveRecordActionListener;

/**
 * all delegation to {@link IStore } for manager for file persistence of java
 * object<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@NoStorableAccess
public abstract class AbstractFilePersistenceDelegatingToStore implements IFilePersistence, IFilePersistenceAutoSaver {

    protected final transient JoafipLogger logger = JoafipLogger.getLogger(getClass());

    /** to manage root object persistence */
    protected transient IStore store;

    /** for thread synchronization */
    protected transient JoafipMutex mutex;

    protected final ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider();

    protected void setStore(final IStore store) {
        this.store = store;
        mutex = store.getMutex();
        store.setClassLoaderProvider(classLoaderProvider);
        store.setSaver(this);
    }

    public IStore getStore() {
        return store;
    }

    public JoafipMutex getMutex() {
        return mutex;
    }

    protected void openStore(final boolean removeFiles) throws StoreException, StoreClassNotFoundException {
        store.open(removeFiles);
    }

    protected void closeStore() throws StoreException {
        store.close();
    }

    protected void clearStore() throws StoreException, StoreClassNotFoundException {
        store.clear();
    }

    @Override
    public void setAutoSaveEventListener(final IAutoSaveEventListener autoSaveEventListener) {
        store.setAutoSaveEventListener(autoSaveEventListener);
    }

    protected void storeNewAccessSession(final boolean exclusiveAccessSession) {
        store.newAccessSession(exclusiveAccessSession);
    }

    protected void storeSetInstanceFactory(final IInstanceFactory instanceFactory) {
        store.setInstanceFactory(instanceFactory);
    }

    protected void storeEndAccessSession() throws FilePersistenceException {
        try {
            store.endAccessSession();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    protected void storeSave(final boolean closing, final boolean autoSave) throws StoreException, StoreInvalidClassException, StoreNotSerializableException, StoreClassNotFoundException, StoreDataCorruptedException, StoreTooBigForSerializationException {
        store.save(closing, autoSave);
    }

    protected void storeDoNotSave() throws StoreException {
        store.doNotSave();
    }

    public Object newInstance(final Class<?> objectClass, final Class<?>[] parameterTypes, final Object[] initargs) {
        return store.newInstance(objectClass, parameterTypes, initargs);
    }

    public Object newInstance(final Class<?> objectClass) {
        return store.newInstance(objectClass);
    }

    protected void storeSetRoot(final Object rootObject, final Map<EnumKey, Enum<?>> storedEnumMap) throws StoreException, StoreInvalidClassException, StoreNotSerializableException, StoreClassNotFoundException, StoreDataCorruptedException {
        store.setRoot(rootObject, storedEnumMap);
    }

    /**
	 * 
	 * @return true if read succeed, false (in most of case it is because of an
	 *         empty file)
	 * @throws StoreException
	 * @throws StoreClassNotFoundException
	 * @throws StoreInvalidClassException
	 * @throws StoreDataCorruptedException
	 * @throws StoreNotSerializableException
	 */
    protected boolean storeReadRoot() throws StoreException, StoreClassNotFoundException, StoreInvalidClassException, StoreDataCorruptedException, StoreNotSerializableException {
        return store.readRoot();
    }

    protected Object storeGetRoot() {
        return store.getRoot();
    }

    /**
	 * 
	 * @return stored enum map
	 */
    protected Map<EnumKey, Enum<?>> getStoredEnumMap() {
        return store.getStoredEnumMap();
    }

    protected void setStoredEnumMap(final Map<EnumKey, Enum<?>> storedEnumMap) {
        store.setStoredEnumMap(storedEnumMap);
    }

    @Override
    public void setChangeLogEnabled(final boolean enabled, final int maxNumberOfChangeLog) throws FilePersistenceException {
        try {
            store.setChangeLogEnabled(enabled, maxNumberOfChangeLog);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setZipCompressionLevel(final int zipCompressionLevel) throws FilePersistenceException {
        try {
            store.setZipCompressionLevel(zipCompressionLevel);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setGarbageListener(final IGarbageListener listener) throws FilePersistenceException {
        try {
            store.setGarbageListener(listener);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setSubstitutionOfJavaUtilCollection(final boolean enabled) throws FilePersistenceException {
        try {
            store.setSubstitutionOfJavaUtilCollection(enabled);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setWriteSubstitution(final Class<?> replaced, final Class<?> substitute, final ISubsituteSynchronizer synchronizer) throws FilePersistenceException {
        try {
            store.setWriteSubstitution(replaced, substitute, synchronizer);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void removeWriteSubstitution(final Class<?> replaced) throws FilePersistenceException {
        try {
            store.removeWriteSubstitution(replaced);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setWriteSubstitution(final String replacedName, final Class<?> substitute, final ISubsituteSynchronizer synchronizer) throws FilePersistenceException {
        try {
            store.setWriteSubstitution(replacedName, substitute, synchronizer);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void removeWriteSubstitution(final String replacedName) throws FilePersistenceException, FilePersistenceClassNotFoundException {
        try {
            store.removeWriteSubstitution(replacedName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        } catch (final StoreClassNotFoundException exception) {
            throw new FilePersistenceClassNotFoundException(exception);
        }
    }

    @Override
    public void setNoLazyLoad(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setNoLazyLoad(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNoLazyLoad(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            store.setNoLazyLoad(objectClasses);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNoLazyLoad(final String objectClassName) {
        store.setNoLazyLoad(objectClassName);
    }

    @Override
    public void setNoLazyLoad(final String[] objectClassNames) {
        store.setNoLazyLoad(objectClassNames);
    }

    @Override
    public void setStoreNotUseStandardSerialization(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setStoreNotUseStandardSerialization(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreNotUseStandardSerialization(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setStoreNotUseStandardSerialization(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreNotUseStandardSerialization(final String objectClassName) throws FilePersistenceException {
        try {
            store.setStoreNotUseStandardSerialization(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreNotUseStandardSerialization(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setStorable(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndGZippedInOneRecord(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setStoreSerializeAndGZippedInOneRecord(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndGZippedInOneRecord(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setStoreSerializeAndGZippedInOneRecord(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndGZippedInOneRecord(final String objectClassName) throws FilePersistenceException {
        try {
            store.setStoreSerializeAndGZippedInOneRecord(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndGZippedInOneRecord(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setStoreSerializeAndGZippedInOneRecord(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndZippedInOneRecord(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setStoreSerializeAndZippedInOneRecord(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndZippedInOneRecord(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setStoreSerializeAndZippedInOneRecord(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndZippedInOneRecord(final String objectClassName) throws FilePersistenceException {
        try {
            store.setStoreSerializeAndZippedInOneRecord(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeAndZippedInOneRecord(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setStoreSerializeAndZippedInOneRecord(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeInOneRecord(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setStoreSerializeInOneRecord(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeInOneRecord(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setStoreSerializeInOneRecord(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeInOneRecord(final String objectClassName) throws FilePersistenceException {
        try {
            store.setStoreSerializeInOneRecord(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStoreSerializeInOneRecord(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setStoreSerializeInOneRecord(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStorable(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setStorable(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStorable(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setStorable(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStorable(final String objectClassName) throws FilePersistenceException {
        try {
            store.setStorable(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setStorable(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setStorable(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotStorable(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setNotStorable(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotStorable(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setNotStorable(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotStorable(final String objectClassName) throws FilePersistenceException {
        try {
            store.setNotStorable(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotStorable(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setNotStorable(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setDeprecatedInStore(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setDeprecatedInStore(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setDeprecatedInStore(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setDeprecatedInStore(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setDeprecatedInStore(final String objectClassName) throws FilePersistenceException {
        try {
            store.setDeprecatedInStore(objectClassName);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setDeprecatedInStore(final String[] objectClassNames) throws FilePersistenceException {
        try {
            for (final String objectClassName : objectClassNames) {
                store.setDeprecatedInStore(objectClassName);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setForceEnhance(final Class<?> objectClass) throws FilePersistenceException {
        try {
            store.setForceEnhance(objectClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setForceEnhance(final Class<?>[] objectClasses) throws FilePersistenceException {
        try {
            for (final Class<?> objectClass : objectClasses) {
                store.setForceEnhance(objectClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setForceEnhance(final String objectClassName) {
        store.setForceEnhance(objectClassName);
    }

    @Override
    public void setForceEnhance(final String[] objectClassNames) {
        for (final String objectClassName : objectClassNames) {
            store.setForceEnhance(objectClassName);
        }
    }

    @Override
    public void addToNotCheckMethod(final Class<?> clazz) {
        store.addToNotCheckMethod(clazz);
    }

    @Override
    public void addToNotCheckMethod(final Method method) {
        store.addToNotCheckMethod(method);
    }

    @Override
    public void addToNotCheckMethod(final Constructor<?> constructor) {
        store.addToNotCheckMethod(constructor);
    }

    @Override
    public void addToNotCheckMethod(final String objectClassName, final String absoluteMethodName) {
        store.addToNotCheckMethod(objectClassName, absoluteMethodName);
    }

    @Override
    public void setObjectIOForClass(final Class<?> objectClass, final Class<? extends IObjectInput> objectInputClass, final Class<? extends IObjectOutput> objectOutputClass) throws FilePersistenceException {
        try {
            store.setObjectIOForClass(objectClass, objectInputClass, objectOutputClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setObjectIOForClass(final String objectClassName, final Class<? extends IObjectInput> objectInputClass, final Class<? extends IObjectOutput> objectOutputClass) throws FilePersistenceException {
        try {
            store.setObjectIOForClass(objectClassName, objectInputClass, objectOutputClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void storedImmutableEnum(final Class<? extends Enum<?>> enumClass) throws FilePersistenceException {
        try {
            store.storedImmutableEnum(enumClass);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void storedImmutableEnum(final Class<? extends Enum<?>>[] enumClasses) throws FilePersistenceException {
        try {
            for (final Class<? extends Enum<?>> enumClass : enumClasses) {
                store.storedImmutableEnum(enumClass);
            }
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    @Deprecated
    public void storedEnum(final Class<? extends Enum<?>>[] enumClasses) throws FilePersistenceException {
        storedMutableEnum(enumClasses);
    }

    @Override
    @Deprecated
    public void storedEnum(final Class<? extends Enum<?>> enumClass) throws FilePersistenceException {
        storedMutableEnum(enumClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public void storedEnum(final Enum<?> enumObject) throws FilePersistenceException {
        storedMutableEnum((Class<? extends Enum<?>>) enumObject.getClass());
    }

    /**
	 * set enum state from file
	 * 
	 * @param enumObject
	 *            the enum for which state must be set
	 * @throws FilePersistenceException
	 */
    @Deprecated
    protected void setEnumState(final Enum<?> enumObject) throws FilePersistenceException {
        try {
            store.setEnumState(enumObject);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setSubstituteObjectManager(final Class<?> objectClass, final Class<?> substituteObjectClass, final ISubstituteObjectManager substituteObjectManager) throws FilePersistenceException {
        try {
            store.setSubstituteObjectManager(objectClass, substituteObjectClass, substituteObjectManager);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setSubstituteObjectManager(final String objectClassName, final Class<?> substituteObjectClass, final ISubstituteObjectManager substituteObjectManager) throws FilePersistenceException {
        try {
            store.setSubstituteObjectManager(objectClassName, substituteObjectClass, substituteObjectManager);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotPersistedField(final Class<?> clazz, final String[] fieldNames) throws FilePersistenceException {
        try {
            store.setNotPersistedField(clazz, fieldNames);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void setNotPersistedField(final String objectClassName, final String[] fieldNames) throws FilePersistenceException {
        try {
            store.setNotPersistedField(objectClassName, fieldNames);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int garbageSweep() throws FilePersistenceException {
        try {
            return store.garbageSweep();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    protected abstract void assertSessionClosed() throws FilePersistenceException;

    protected abstract void assertSessionOpenned() throws FilePersistenceException;

    @Override
    public boolean isGarbageManagement() throws FilePersistenceException {
        try {
            return store.isGarbageManagement();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void enableBackgroundGarbageSweep(final int sleepTime) throws FilePersistenceException {
        try {
            store.enableBackgroundGarbageSweep(sleepTime);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void disableBackgroundGarbageSweep() throws FilePersistenceException {
        try {
            store.disableBackgroundGarbageSweep();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public List<DataRecordIdentifier> usedForGarbageManagement() throws FilePersistenceException {
        try {
            return store.usedForGarbageManagement();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfGarbageCandidate() throws FilePersistenceException {
        try {
            return store.getNumberOfGarbageCandidate();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public List<DataRecordIdentifierRBTNode> getCandidate() throws FilePersistenceException {
        try {
            return store.getCandidate();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfToGarbage() throws FilePersistenceException {
        try {
            return store.getNumberOfToGarbage();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public List<DataRecordIdentifierRBTNode> getToGarbage() throws FilePersistenceException {
        try {
            return store.getToGarbage();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public ReferenceLink[] getAllReferenceLink() throws FilePersistenceException {
        try {
            return store.getAllReferenceLink();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfDataRecord() throws FilePersistenceException {
        try {
            return store.getNumberOfDataRecord();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfFreeRecord() throws FilePersistenceException {
        try {
            return store.getNumberOfFreeRecord();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public long usedSize() throws FilePersistenceException {
        try {
            return store.usedSize();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public long freeSize() throws FilePersistenceException {
        try {
            return store.freeSize();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public long totalSize() throws FilePersistenceException {
        try {
            return store.totalSize();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfObjectState() throws FilePersistenceException {
        try {
            return store.getNumberOfObjectState();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfWeakReference() throws FilePersistenceException {
        try {
            return store.getNumberOfWeakReference();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfReferenced() throws FilePersistenceException {
        try {
            return store.getNumberOfReferenced();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public Collection<ObjectAndPersistInfo> getObjectHavingStateSet() {
        return store.getObjectHavingStateSet();
    }

    @Override
    public int getNumberOfModified() throws FilePersistenceException {
        try {
            return store.getNumberOfModified();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public int getNumberOfVisited() throws FilePersistenceException {
        try {
            return store.getNumberOfVisitedForSave();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public String getStorageFileName() throws FilePersistenceException {
        try {
            return store.getStorageFileName();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    @Deprecated
    public Object copy(final Object sourceObject) throws FilePersistenceException {
        return deepCopy(sourceObject, true);
    }

    @Override
    public Object deepCopy(final Object sourceObject, final boolean forceLoad) throws FilePersistenceException {
        synchronized (mutex) {
            try {
                return store.deepCopy(sourceObject, forceLoad);
            } catch (final CopierException exception) {
                throw new FilePersistenceException(exception);
            }
        }
    }

    @Override
    public void xmlExport(final String directoryName, final String temporaryDirectoryName, final boolean exportPersistedClassByteCode) throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, FilePersistenceTooBigForSerializationException {
        synchronized (mutex) {
            try {
                assertSessionClosed();
                storeNewAccessSession(false);
                store.xmlExport(directoryName, temporaryDirectoryName, exportPersistedClassByteCode);
            } catch (final StoreException exception) {
                throw new FilePersistenceException(exception);
            } catch (final StoreClassNotFoundException exception) {
                throw new FilePersistenceClassNotFoundException(exception);
            } catch (final StoreInvalidClassException exception) {
                throw new FilePersistenceInvalidClassException(exception);
            } catch (final StoreDataCorruptedException exception) {
                throw new FilePersistenceDataCorruptedException(exception);
            } catch (final StoreNotSerializableException exception) {
                throw new FilePersistenceNotSerializableException(exception);
            } catch (final StoreTooBigForSerializationException exception) {
                throw new FilePersistenceTooBigForSerializationException(exception);
            } finally {
                storeEndAccessSession();
            }
        }
    }

    @Override
    public void setExportListener(final IExporterListener listener) {
        store.setExportListener(listener);
    }

    @Override
    public int getNumberOfObjectExported() {
        return store.getNumberOfObjectExported();
    }

    @Override
    public void setClassLoader(final ClassLoader classLoader) throws FilePersistenceException {
        classLoaderProvider.setClassLoader(classLoader);
    }

    @Override
    public ClassLoader getClassLoader() throws FilePersistenceException {
        return classLoaderProvider.getClassLoader();
    }

    @Override
    public URL getResource(final String resourceName) throws MalformedURLException {
        return classLoaderProvider.getResource(resourceName);
    }

    @Override
    public Object newProxyInstance(final Object object) throws FilePersistenceException {
        synchronized (mutex) {
            try {
                return store.newProxyInstance(object);
            } catch (final StoreException exception) {
                throw new FilePersistenceException(exception);
            }
        }
    }

    @Override
    public Object newProxyInstance(final Class<?> objectClass, final Class<?>[] parameterTypes, final Object[] initargs) throws FilePersistenceException {
        synchronized (mutex) {
            try {
                return store.newProxyInstance(objectClass, parameterTypes, initargs);
            } catch (final StoreException exception) {
                throw new FilePersistenceException(exception);
            }
        }
    }

    @Override
    public Object newProxyInstance(final Class<?> objectClass) throws FilePersistenceException {
        synchronized (mutex) {
            try {
                return store.newProxyInstance(objectClass);
            } catch (final StoreException exception) {
                throw new FilePersistenceException(exception);
            }
        }
    }

    @Override
    public void setStoreOnlyMarkedStorable(final boolean storeOnlyMarkedStorable) {
        store.setStoreOnlyMarkedStorable(storeOnlyMarkedStorable);
    }

    @Override
    public int getClassIdentifier(final Class<?> clazz) throws FilePersistenceException, FilePersistenceClassNotFoundException {
        synchronized (mutex) {
            try {
                assertSessionClosed();
                return store.getClassIdentifier(clazz);
            } catch (final StoreException exception) {
                throw new FilePersistenceException(exception);
            } catch (final StoreClassNotFoundException exception) {
                throw new FilePersistenceClassNotFoundException(exception);
            }
        }
    }

    @Override
    public Collection<ClassInfo> allClassInformation() {
        return store.allClassInformation();
    }

    @Override
    public boolean isProxy(final Object object) {
        return ProxyManager2.isProxyOrEnhanced(object);
    }

    @Override
    public void keptInMemory(final String key, final Object object) throws FilePersistenceException {
        try {
            store.keptInMemory(key, object);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void referencedByPesistedStaticField(final Class<?> classHavingStaticFieldReferenced) throws FilePersistenceException {
        try {
            store.referencedByPesistedStaticField(classHavingStaticFieldReferenced);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public void referencedByPesistedStaticField(final Class<?>[] classHavingStaticFieldReferenced) throws FilePersistenceException {
        for (Class<?> clazz : classHavingStaticFieldReferenced) {
            referencedByPesistedStaticField(clazz);
        }
    }

    @Override
    public String keyOfObjectKeptInMemory(final Object object) {
        return store.keyOfObjectKeptInMemory(object);
    }

    @Override
    public void setRecordSaveActions(final boolean recordActions) {
        store.setRecordSaveActions(recordActions);
    }

    @Override
    public void setRecordSaveActions(final boolean recordActions, final ISaveRecordActionListener saveRecordActionListener) {
        store.setRecordSaveActions(recordActions, saveRecordActionListener);
    }

    @Override
    public void setSaveRecordActionListener(final ISaveRecordActionListener saveRecordActionListener) {
        store.setSaveRecordActionListener(saveRecordActionListener);
    }

    @Override
    public Map<String, MutableInteger> getWroteObjectSet() {
        return store.getWroteObjectSet();
    }

    @Override
    public Set<String> getVisitedObjectSet() {
        return store.getVisitedObjectSet();
    }

    @Override
    public List<String> getWroteArrays() {
        return store.getWroteArrays();
    }

    @Override
    public void setMaintainedInMemoryEnabled(final boolean enabled) {
        store.setMaintainedInMemoryEnabled(enabled);
    }

    @Override
    public void maintainInMemorySetup(final int maintainedInMemoryQuota) {
        store.maintainInMemorySetup(maintainedInMemoryQuota);
    }

    @Override
    public void setAutoSaveEnabled(final boolean enabled) throws FilePersistenceException {
        if (enabled != store.isAutoSaveEnabled()) {
            assertSessionClosed();
            store.setAutoSaveEnabled(enabled);
        }
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return store.isAutoSaveEnabled();
    }

    @Override
    public void autoSaveSetup(final int maxInMemoryThreshold) {
        store.autoSaveSetup(maxInMemoryThreshold);
    }

    @Fortest
    public LinkRecordManager getLinkRecordManager() {
        return store.getLinkRecordManager();
    }

    @Fortest
    public ObjectIOManager getObjectIOManager() throws FilePersistenceException {
        try {
            return store.getObjectIOManager();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    /**
	 * get data record identifier associated to an object
	 * 
	 * @param object
	 *            object which for the data record identifier is ask for
	 * @return data record identifier associated to an object, null if none
	 * @throws FilePersistenceException
	 */
    @Fortest
    public DataRecordIdentifier getCurrentDataRecordIdentifierAssociatedToObject(final Object object) throws FilePersistenceException {
        try {
            return store.getDataRecordIdentifierAssociatedToObject(object);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    public Object createObjectReadingInStoreOrGetExisting(final DataRecordIdentifier dataRecordIdentifier) throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException {
        try {
            return store.createObjectReadingInStoreOrGetExisting(dataRecordIdentifier, false).getObject();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        } catch (final StoreClassNotFoundException exception) {
            throw new FilePersistenceClassNotFoundException(exception);
        } catch (final StoreInvalidClassException exception) {
            throw new FilePersistenceInvalidClassException(exception);
        } catch (final StoreDataCorruptedException exception) {
            throw new FilePersistenceDataCorruptedException(exception);
        } catch (final StoreNotSerializableException exception) {
            throw new FilePersistenceNotSerializableException(exception);
        }
    }

    @Override
    public ClassInfo knownAsNotExisting(final String className) throws FilePersistenceClassNotFoundException, FilePersistenceException {
        try {
            return store.knownAsNotExisting(className);
        } catch (final StoreClassNotFoundException exception) {
            throw new FilePersistenceClassNotFoundException(exception);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public Class<?> classOfObject(final Object object) throws FilePersistenceException {
        try {
            return store.classOfObject(object);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Override
    public ClassInfo classInfoOfObject(final Object object) throws FilePersistenceException {
        try {
            return store.classInfoOfObject(object);
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public IHeapDataManager getDataManager() throws FilePersistenceException {
        try {
            return store.getDataManager();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    @Override
    public List<DataRecordIdentifier> usedForClassNameManagement() throws FilePersistenceException {
        try {
            return store.usedForClassNameManagement();
        } catch (final StoreException exception) {
            throw new FilePersistenceException(exception);
        }
    }

    @Fortest
    public ObjectAndPersistInfo getObjectAndPersistInfoOfObjectFromQueue(final Object object) {
        return store.getObjectAndPersistInfoOfObjectFromQueue(object);
    }

    @Fortest
    public List<ObjectAndPersistInfo> getObjectAndPersistInfoOfObjectFromQueue() {
        return store.getObjectAndPersistInfoOfObjectFromQueue();
    }

    @Fortest
    public void listenStateMapClean(final IObjectStateMapCLeanListener listener) {
        store.listenStateMapClean(listener);
    }
}

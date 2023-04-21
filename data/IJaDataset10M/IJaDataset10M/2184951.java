package net.sf.joafip.store.service.objectio.manager;

import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.service.JoafipMutex;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.ObjectIOClassNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;
import net.sf.joafip.store.service.proxy.IProxyManagerForObjectIO;

/**
 * object io manager view for proxy lazy load
 * 
 * @author luc peuvrier
 * 
 */
public interface IObjectIOManagerForProxyObjectIO {

    /**
	 * Get existing, or create, object and its persistence information, for an
	 * object. If not existing then create it<br>
	 * Also set the data record associated to the object<br>
	 * 
	 * @param object
	 *            the object which for object and its persistence information is
	 *            ask
	 * @param proxyInstance
	 *            true if object is proxy instance
	 * @param objectClassInfo
	 *            the class information of the object
	 * @param dataRecordIdentifier
	 *            the data record identifier to associate to the object
	 * @return get or created object and its persistence information
	 * @throws ObjectIOException
	 * @throws ObjectIODataCorruptedException
	 */
    ObjectAndPersistInfo getOrCreateObjectPersistInfoOfObject(final Object object, boolean proxyInstance, final ClassInfo objectClassInfo, final DataRecordIdentifier dataRecordIdentifier) throws ObjectIOException, ObjectIODataCorruptedException;

    ObjectAndPersistInfo createObjectPersistInfoOfObject(final Object object, boolean proxyInstance, final ClassInfo objectClassInfo, final DataRecordIdentifier dataRecordIdentifier) throws ObjectIOException;

    /**
	 * Get existing, or create, object and its persistence information, for an
	 * object. If not existing then create object and its persistence
	 * information.<br>
	 * If persisted object and have not associated data record identifier, set
	 * the data record associated to the object creating a new data record
	 * identifier<br>
	 * 
	 * @param object
	 *            the object which for object and its persistence information is
	 *            ask
	 * @param proxyInstance
	 *            true if object is proxy instance (use by creation only)
	 * @param objectClassInfo
	 *            the class information of the object
	 * @param persisted
	 *            true if object to be persisted
	 * @return get or created object and its persistence information
	 * @throws ObjectIOException
	 * @throws ObjectIODataCorruptedException
	 */
    ObjectAndPersistInfo getOrCreateObjectPersistInfoOfObject(Object object, Boolean proxyInstance, ClassInfo objectClassInfo, boolean persisted) throws ObjectIOException, ObjectIODataCorruptedException;

    ObjectAndPersistInfo createObjectPersistInfoOfObject(Object object, Boolean proxyInstance, ClassInfo objectClassInfo, boolean persisted) throws ObjectIOException, ObjectIODataCorruptedException;

    /**
	 * 
	 * @param object
	 * @return object and persist info of object, null if none
	 * @throws ObjectIOException
	 */
    ObjectAndPersistInfo getObjectAndPersistInfoOfObject(Object object) throws ObjectIOException;

    /**
	 * Get existing, or create, object and its persistence information, for an
	 * object. If not existing then create object and its persistence
	 * information.<br>
	 * If persisted object and have not associated data record identifier, set
	 * the data record associated to the object creating a new data record
	 * identifier<br>
	 * 
	 * @param object
	 *            the object which for object and its persistence information is
	 *            ask
	 * @param proxyInstance
	 *            true if object is a proxy instance (use by creation only)
	 * @param persisted
	 *            true if object to be persisted
	 * @return get or created object and its persistence information
	 * @throws ObjectIOException
	 * @throws ObjectIODataCorruptedException
	 */
    ObjectAndPersistInfo getOrCreateObjectPersistInfoOfObject(Object object, Boolean proxyInstance, boolean persisted) throws ObjectIOException, ObjectIODataCorruptedException;

    void dataRecordIdentifierAssociatedToObjectSetted(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException;

    /**
	 * get the mutex for storage file access
	 * 
	 * @return mutex for storage file access
	 */
    JoafipMutex getStoreMutex();

    /**
	 * 
	 * @return current file access session identifier
	 */
    long getCurrentFileAccessSessionIdentifier();

    /**
	 * set proxy object state reading in file
	 * 
	 * @param objectAndPersistInfo
	 *            object that must be a proxy
	 * @throws ObjectIODataRecordNotFoundException
	 *             data record for object not found
	 * @throws ObjectIOException
	 *             set object state error
	 * @throws ObjectIOInvalidClassException
	 * @throws ObjectIODataCorruptedException
	 * @throws ObjectIOClassNotFoundException
	 * @throws ObjectIONotSerializableException
	 * @throws
	 */
    void setProxyObjectState(ObjectAndPersistInfo objectAndPersistInfo) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException;

    void newObjectLoaded() throws ObjectIOException;

    void unsetProxyObjectState(ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException, ObjectIOInvalidClassException;

    IProxyManagerForObjectIO getProxyManager2();

    ClassInfoFactory getClassInfoFactory();

    boolean isExclusiveAccessSession();

    /**
	 * 
	 * @return true if maintain object in memory
	 */
    boolean isMaintainInMemoryEnabled();

    boolean isAutoSaveEnabled();

    /**
	 * call when object is accessed
	 * 
	 * @param proxyObjectAndPersistInfo
	 */
    void objectIsAccessed(ObjectAndPersistInfo proxyObjectAndPersistInfo);

    boolean isRunAutosaveEnableAndDisable();

    void setRunAutosaveEnable(boolean runAutosaveEnable);
}

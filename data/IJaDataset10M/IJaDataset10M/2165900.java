package net.sf.joafip.store.service.proxy;

import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.entity.objectio.StorageInfo;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForProxyObjectIO;

/**
 * 
 * @author luc peuvrier
 * 
 */
public interface IProxyManagerForObjectIO {

    ClassInfo classInfoOfObject(Object object) throws ProxyException;

    /**
	 * create a new proxy instance for a class. The new instance is not
	 * constructed (no constructor call)
	 * 
	 * @param objectClassInfo
	 *            the class of object which for a proxy is to created
	 * @param storageInfo
	 * @param objectIOManager
	 *            object input/output manager for state reading from file
	 * @param dataRecordIdentifier
	 *            data record identifier of the instance to create, may be null
	 *            if not defined
	 * @param persisted
	 *            true if object to be persisted
	 * @return the created proxy for class
	 * @throws ProxyException
	 */
    ObjectAndPersistInfo newInstanceNoConstruction(ClassInfo objectClassInfo, StorageInfo storageInfo, IObjectIOManagerForProxyObjectIO objectIOManager, DataRecordIdentifier dataRecordIdentifier, boolean persisted) throws ProxyException;

    /**
	 * create a new proxy instance constructing object<br>
	 * 
	 * The <code>parameterTypes</code> parameter is an array of
	 * <code>Class</code> objects that identify the constructor's formal
	 * parameter types, in declared order.<br>
	 * 
	 * If the object class represents an inner class declared in a non-static
	 * context, the formal parameter types include the explicit enclosing
	 * instance as the first parameter.<br>
	 * 
	 * @param objectClassInfo
	 *            the class of object which for a proxy is to created
	 * @param parameterTypes
	 *            the parameter array.
	 * @param initargs
	 *            array of objects to be passed as arguments to the constructor
	 *            call; values of primitive types are wrapped in a wrapper
	 *            object of the appropriate type (e.g. a <tt>float</tt> in a
	 *            {@link java.lang.Float Float})
	 * @param storageInfo
	 * @param objectIOManager
	 *            object input/output manager for state reading from file
	 * @param dataRecordIdentifier
	 *            data record identifier of the instance to create, may be null
	 *            if not defined
	 * @param persisted
	 *            true if object to be persisted
	 * @return the created proxy for class
	 * @throws ProxyException
	 */
    Object newInstanceConstruct(ClassInfo objectClassInfo, Class<?>[] parameterTypes, Object[] initargs, StorageInfo storageInfo, IObjectIOManagerForProxyObjectIO objectIOManager, DataRecordIdentifier dataRecordIdentifier, boolean persisted) throws ProxyException;

    Object newInstanceConstruct(ClassInfo objectClassInfo, StorageInfo storageInfo, IObjectIOManagerForProxyObjectIO objectIOManager, DataRecordIdentifier dataRecordIdentifier, boolean persisted) throws ProxyException;

    void unloadAndAssociateToCurrentSession(Object object, IObjectIOManagerForObjectIO objectIOManager) throws ProxyException;

    ClassInfoFactory getClassInfoFactory();
}

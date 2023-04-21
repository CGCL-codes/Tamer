package net.sf.joafip.store.service.objectio.serialize.input;

import java.util.List;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.classinfo.FieldInfo;
import net.sf.joafip.store.entity.conversion.ValuedFieldList;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.ObjectIOClassNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;
import net.sf.joafip.store.service.proxy.IProxyManagerForObjectIO;

/**
 * input for serializable object, direct input ( no lazy loading )<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ObjectInputImmediateSerializable extends AbstractObjectInputImmediate {

    public ObjectInputImmediateSerializable(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final IProxyManagerForObjectIO proxyManager2, final HelperBinaryConversion helperBinaryConversion) {
        super(objectIOManager, classInfoFactory, proxyManager2, helperBinaryConversion);
    }

    /**
	 * for direct loading ( no lazy mode ), object initialization is done by
	 * object state setting
	 * 
	 * @param objectAndPersistInfo
	 * @param binary
	 * @param offset
	 * @throws ObjectIODataRecordNotFoundException
	 * @throws ObjectIOInvalidClassException
	 * @throws ObjectIODataCorruptedException
	 * @throws ObjectIOClassNotFoundException
	 * @throws ObjectIONotSerializableException
	 * @throws FileCorruptedException
	 */
    @Override
    protected void initializeObject(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectState(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected List<ObjectAndPersistInfo> readObjectStoredState(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOClassNotFoundException, ObjectIOInvalidClassException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        return readObjectStoredStateGeneric(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected void setObjectStateImpl(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectStateSerializable(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected void setObjectStateFromFieldValueListImpl(final ObjectAndPersistInfo objectAndPersistInfo, final ValuedFieldList valuedFieldList) throws ObjectIOException, ObjectIOInvalidClassException, ObjectIODataRecordNotFoundException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectStateFromFieldValueListSerializable(objectAndPersistInfo, valuedFieldList);
    }

    @Override
    protected FieldInfo[] getFieldToSetInfoImpl(final ClassInfo classInfo) throws ObjectIOException {
        try {
            return classInfo.getAllDeclaredTransientOrNotFields();
        } catch (ClassInfoException exception) {
            throw new ObjectIOException(exception);
        }
    }
}

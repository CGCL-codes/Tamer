package net.sf.joafip.store.service.objectio.serialize;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.heapfile.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.classinfo.FieldInfo;
import net.sf.joafip.store.entity.conversion.ValuedFieldList;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.entity.objectio.StorageInfo;
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
import net.sf.joafip.store.service.proxy.ProxyManager2;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ObjectInputStatic extends AbstractObjectInputGeneric {

    public ObjectInputStatic(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final ProxyManager2 proxyManager2, final HelperBinaryConversion helperBinaryConversion) {
        super(objectIOManager, classInfoFactory, proxyManager2, helperBinaryConversion);
    }

    protected int checkSignatureImpl(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOInvalidClassException, ObjectIOException {
        final ClassInfo classInfo = objectAndPersistInfo.getObjectClassInfo();
        return checkStaticFieldSignature(binary, offset, classInfo);
    }

    public void setObjectStaticState(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        initializeObject(objectAndPersistInfo, binary, offset);
    }

    protected void setObjectStateFromFieldValueList(final ObjectAndPersistInfo objectAndPersistInfo, final ValuedFieldList valuedFieldList) throws ObjectIOException, ObjectIOInvalidClassException {
        final ClassInfo classInfo = objectAndPersistInfo.getObjectClassInfo();
        if (classInfo.classDoesNotExist()) {
            throw new ObjectIOException("can not set object static field state since " + classInfo + " does not exist");
        }
        final FieldInfo[] fieldsInfo = getFieldInfoImpl(classInfo);
        setObjectStateFromFieldValueList(objectAndPersistInfo, valuedFieldList, fieldsInfo);
    }

    @Override
    protected FieldInfo[] getFieldInfoImpl(final ClassInfo classInfo) throws ObjectIOException {
        try {
            return classInfo.getAllDeclaredStaticFields();
        } catch (ClassInfoException exception) {
            throw new ObjectIOException(exception);
        }
    }

    @Override
    protected ObjectAndPersistInfo createObjectImpl(final byte[] binary, final int offset, final ClassInfo classInfo, final StorageInfo storageInfo, final DataRecordIdentifier dataRecordIdentifier) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIONotSerializableException {
        throw new ObjectIOException("unsupported");
    }

    @Override
    protected void initializeObject(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectState(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected void readObjectStoredState(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOClassNotFoundException, ObjectIOInvalidClassException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        readObjectStoredStateGeneric(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected void setObjectStateImpl(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectStateGeneric(objectAndPersistInfo, binary, offset, true);
    }

    @Override
    public boolean isImmediate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLazy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsetObjectState(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException, ObjectIOInvalidClassException {
    }
}

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
 * generic input for object, lazy input<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ObjectInputLazyGeneric extends AbstractObjectInputLazy {

    public ObjectInputLazyGeneric(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final HelperBinaryConversion helperBinaryConversion, final IProxyManagerForObjectIO proxyManager2) {
        super(objectIOManager, classInfoFactory, helperBinaryConversion, proxyManager2);
    }

    @Override
    protected void initializeObject(final ObjectAndPersistInfo object, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
    }

    @Override
    protected List<ObjectAndPersistInfo> readObjectStoredState(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOClassNotFoundException, ObjectIOInvalidClassException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        return readObjectStoredStateGeneric(objectAndPersistInfo, binary, offset);
    }

    @Override
    protected void setObjectStateImpl(final ObjectAndPersistInfo object, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIOInvalidClassException, ObjectIOClassNotFoundException, ObjectIODataCorruptedException, ObjectIONotSerializableException {
        setObjectStateGeneric(object, binary, offset, true);
    }

    @Override
    protected void setObjectStateFromFieldValueListImpl(final ObjectAndPersistInfo objectAndPersistInfo, final ValuedFieldList valuedFieldList) throws ObjectIOException, ObjectIOInvalidClassException {
        setObjectStateFromFieldValueList(objectAndPersistInfo, valuedFieldList);
    }

    @Override
    protected FieldInfo[] getFieldToSetInfoImpl(final ClassInfo classInfo) throws ObjectIOException {
        try {
            return classInfo.getAllDeclaredFieldsForGenericIO();
        } catch (ClassInfoException exception) {
            throw new ObjectIOException(exception);
        }
    }

    @Override
    public void unsetObjectState(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException, ObjectIOInvalidClassException {
        setObjectAllDeclaredFieldsValueToNull(objectAndItsClassInfo);
    }
}

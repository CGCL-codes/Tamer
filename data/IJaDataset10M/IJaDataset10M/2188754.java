package net.sf.joafip.store.service.objectio.serialize.output;

import java.lang.reflect.Array;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.reflect.ReflectException;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndItsClassInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.entity.objectio.ObjectClassInfoAndDeclared;
import net.sf.joafip.store.service.binary.BinaryConverterException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;
import net.sf.joafip.store.service.proxy.IProxyManagerForObjectIO;
import net.sf.joafip.store.service.proxy.ProxyException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ObjectOutputForArray extends AbstractObjectOutput {

    private static final String REFLECT_ERROR = "reflect error";

    public ObjectOutputForArray(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final IProxyManagerForObjectIO proxyManager2, final HelperBinaryConversion helperBinaryConversion) {
        super(objectIOManager, classInfoFactory, proxyManager2, helperBinaryConversion);
    }

    @Override
    public void prepareWrite(final ObjectAndPersistInfo arrayAndPersistInfo) throws ObjectIOException, ObjectIOInvalidClassException, ObjectIONotSerializableException, ObjectIODataCorruptedException {
        final ClassInfo arrayClassInfo = arrayAndPersistInfo.objectClassInfo;
        final ClassInfo arrayComponentType = arrayClassInfo.getComponentType();
        final ObjectClassInfoAndDeclared[] currentValue;
        if (arrayComponentType.isPrimitiveType()) {
            currentValue = prepareWriteArrayOfPrimitive(arrayAndPersistInfo, arrayComponentType);
        } else {
            currentValue = prepareWtiteOfArrayOfNotPrimitive(arrayAndPersistInfo, arrayComponentType);
        }
        arrayAndPersistInfo.currentValue = currentValue;
    }

    private ObjectClassInfoAndDeclared[] prepareWtiteOfArrayOfNotPrimitive(final ObjectAndItsClassInfo arrayAndItsClassInfo, final ClassInfo arrayComponentType) throws ObjectIOException, ObjectIODataCorruptedException {
        final Object array = arrayAndItsClassInfo.getObject();
        final int arrayLength = Array.getLength(array);
        final ObjectClassInfoAndDeclared[] currentValue = new ObjectClassInfoAndDeclared[arrayLength];
        for (int index = 0; index < arrayLength; index++) {
            final Object elementObject;
            try {
                elementObject = helperReflect.getArrayElement(array, index);
            } catch (ReflectException exception) {
                logger.fatal(REFLECT_ERROR, exception);
                throw new ObjectIOException(exception);
            }
            final ClassInfo elementClassInfo;
            if (elementObject == null) {
                elementClassInfo = ClassInfo.NULL;
            } else {
                try {
                    elementClassInfo = proxyManager2.classInfoOfObject(elementObject);
                } catch (ProxyException exception) {
                    throw new ObjectIOException(exception);
                }
            }
            final ObjectAndPersistInfo elementAndPersistInfo = objectIOManager.getOrCreateObjectPersistInfoOfObject(elementObject, null, elementClassInfo, true);
            currentValue[index] = new ObjectClassInfoAndDeclared(elementAndPersistInfo, arrayComponentType);
        }
        return currentValue;
    }

    private ObjectClassInfoAndDeclared[] prepareWriteArrayOfPrimitive(final ObjectAndPersistInfo arrayAndPersistInfo, final ClassInfo arrayComponentType) throws ObjectIOException, ObjectIODataCorruptedException {
        final Object array = arrayAndPersistInfo.getObject();
        final int arrayLength = Array.getLength(array);
        final ObjectClassInfoAndDeclared[] currentValue = new ObjectClassInfoAndDeclared[arrayLength];
        for (int index = 0; index < arrayLength; index++) {
            final Object elementObject;
            try {
                elementObject = helperReflect.getArrayElement(array, index);
            } catch (ReflectException exception) {
                logger.fatal(REFLECT_ERROR, exception);
                throw new ObjectIOException(exception);
            }
            final ObjectAndPersistInfo elementAndPersistInfo = objectIOManager.getOrCreateObjectPersistInfoOfObject(elementObject, Boolean.FALSE, arrayComponentType, false);
            currentValue[index] = new ObjectClassInfoAndDeclared(elementAndPersistInfo, arrayComponentType);
        }
        return currentValue;
    }

    @Override
    public int numberOfByteForData(final ObjectAndPersistInfo arrayAndItsClassInfo) {
        int size;
        final int elementByteSize;
        final ClassInfo arrayClassInfo = arrayAndItsClassInfo.objectClassInfo;
        final ClassInfo componentType = arrayClassInfo.getComponentType();
        elementByteSize = byteSizeForFieldOrElement(componentType);
        final int arrayLength = Array.getLength(arrayAndItsClassInfo.getObject());
        size = 2 * HelperBinaryConversion.INT_BYTE_SIZE + elementByteSize * arrayLength;
        return size;
    }

    @Override
    protected final ObjectClassInfoAndDeclared[] writeBodyAndItsHeader(final byte[] binary, final int bodyBeginOffset, final ObjectAndPersistInfo arrayAndItsClassInfo, final boolean updateOriginalValue) throws ObjectIOException, ObjectIODataCorruptedException {
        final int arrayLengthBeginOffset = bodyBeginOffset + HelperBinaryConversion.INT_BYTE_SIZE;
        final int arrayLength = Array.getLength(arrayAndItsClassInfo.getObject());
        int elementBeginOffset;
        try {
            elementBeginOffset = helperBinaryConversion.integerConverter.toBinary(binary, arrayLengthBeginOffset, true, arrayLength);
        } catch (BinaryConverterException exception) {
            throw new ObjectIOException(exception);
        }
        return writeCurrentValue(binary, elementBeginOffset, arrayAndItsClassInfo, updateOriginalValue);
    }

    @Override
    public ObjectClassInfoAndDeclared[] willNotBeWroteImpl(final ObjectAndPersistInfo array) throws ObjectIOException {
        return currentValuesToReferencedSoon(array);
    }

    @Override
    public boolean isValueChanged(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException {
        return isGenericValueChanged(objectAndItsClassInfo);
    }

    @Override
    public void setReferenceChangeListForNewObject(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException {
        setReferenceChangeListForGeneric(objectAndItsClassInfo);
    }
}

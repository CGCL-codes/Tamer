package net.sf.joafip.store.service.objectio.serialize;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
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
import net.sf.joafip.store.service.proxy.ProxyManager2;

/**
 * output for enum<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ObjectOutputForEnum extends AbstractObjectOutputGenericNotStatic {

    public ObjectOutputForEnum(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final ProxyManager2 proxyManager2, final HelperBinaryConversion helperBinaryConversion) {
        super(objectIOManager, classInfoFactory, proxyManager2, helperBinaryConversion);
    }

    public void prepareWrite(final ObjectAndPersistInfo object) throws ObjectIOException, ObjectIOInvalidClassException, ObjectIONotSerializableException, ObjectIODataCorruptedException {
        prepareWriteGenericAllDeclaredFields(object);
    }

    @Override
    public ObjectClassInfoAndDeclared[] willNotBeWritedImpl(final ObjectAndPersistInfo object) throws ObjectIOException {
        return currentValuesToReferencedSoon(object);
    }

    @Override
    public boolean isValueChanged(final ObjectAndPersistInfo objectAndItsClassInfo) throws ObjectIOException {
        return isGenericValueChanged(objectAndItsClassInfo);
    }

    @Override
    protected ObjectClassInfoAndDeclared[] writeBodyImpl(final byte[] binary, final int fieldBeginOffset, final ObjectAndPersistInfo object, final boolean updateOriginalValue) throws ObjectIOException, ObjectIODataCorruptedException {
        throw new ObjectIOException("no such method");
    }

    @Override
    protected int numberOfByteForData(final ObjectAndPersistInfo objectAndPersistInfo) throws ObjectIOException {
        final Enum enumObject = (Enum) objectAndPersistInfo.getObject();
        final int nameLength = enumObject.name().length();
        return numberOfByteForDataGeneric(objectAndPersistInfo) + HelperBinaryConversion.INT_BYTE_SIZE + HelperBinaryConversion.CHAR_BYTE_SIZE * nameLength;
    }

    @Override
    protected final ObjectClassInfoAndDeclared[] writeBodyAndItsHeader(final byte[] binary, final int bodyBeginOffset, final ObjectAndPersistInfo object, final boolean updateOriginalValue) throws ObjectIOException, ObjectIODataCorruptedException {
        final Enum enumObject = (Enum) object.getObject();
        final char[] charArray = enumObject.name().toCharArray();
        final ClassInfo classInfo = object.getObjectClassInfo();
        int offset = writeSignature(binary, bodyBeginOffset, classInfo);
        final int charArrayLength = charArray.length;
        try {
            offset = HelperBinaryConversion.integerConverter.toBinary(binary, offset, true, charArrayLength);
            for (int index = 0; index < charArrayLength; index++) {
                offset = HelperBinaryConversion.characterConverter.toBinary(binary, offset, true, charArray[index]);
            }
        } catch (BinaryConverterException exception) {
            throw new ObjectIOException(exception);
        }
        return writeCurrentValue(binary, offset, object, updateOriginalValue);
    }
}

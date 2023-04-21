package net.sf.joafip.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import net.sf.joafip.store.service.classinfo.ClassInfoFactory;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;
import net.sf.joafip.store.service.objectio.serialize.input.AbstractImmediateObjectInput;
import net.sf.joafip.store.service.proxy.IProxyManagerForObjectIO;

@NotStorableClass
public class Bob1ObjectInputForTestCustom extends AbstractImmediateObjectInput {

    public Bob1ObjectInputForTestCustom(final IObjectIOManagerForObjectIO objectIOManager, final ClassInfoFactory classInfoFactory, final IProxyManagerForObjectIO proxyManager2, final HelperBinaryConversion helperBinaryConversion) {
        super(objectIOManager, classInfoFactory, proxyManager2, helperBinaryConversion);
    }

    @Override
    protected ObjectAndPersistInfo createReadingBody(final byte[] binary, final int bodyBeginOffset, final ClassInfo objectClass, final DataRecordIdentifier dataRecordIdentifier) throws ObjectIOException, ObjectIODataRecordNotFoundException, ObjectIODataCorruptedException {
        ObjectInputStream ois = null;
        final ObjectAndPersistInfo objectAndItsClassInfo;
        try {
            final int length = binary.length - bodyBeginOffset;
            final byte[] objectBytes = new byte[length];
            System.arraycopy(binary, bodyBeginOffset, objectBytes, 0, length);
            ois = new ObjectInputStream(new ByteArrayInputStream(objectBytes));
            final Object object = ois.readObject();
            objectAndItsClassInfo = objectIOManager.getOrCreateObjectPersistInfoOfObject(object, false, classInfoFactory.getNoProxyClassInfo(object.getClass()), dataRecordIdentifier);
        } catch (IOException exception) {
            throw new ObjectIOException(exception);
        } catch (ClassNotFoundException exception) {
            throw new ObjectIOException(exception);
        } catch (ClassInfoException exception) {
            throw new ObjectIOException(exception);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException exception) {
            }
        }
        return objectAndItsClassInfo;
    }
}

package net.sf.joafip.store.service.objectio.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.manager.ClassNameManager;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ObjectIOSerializeGZipped extends AbstractCreateSettedComparableObjectIO {

    public ObjectIOSerializeGZipped(final IObjectIOManagerForObjectIO objectIOManager, final ClassNameManager classNameManager) {
        super(objectIOManager, classNameManager);
    }

    @Override
    protected Object createReadingBody(final byte[] binary, final int bodyBeginOffset, final Class objectClass) throws ObjectIOException, ObjectIODataRecordNotFoundException {
        ObjectInputStream ois = null;
        final Object object;
        try {
            final int length = binary.length - bodyBeginOffset;
            final byte[] serializeForm = new byte[length];
            System.arraycopy(binary, bodyBeginOffset, serializeForm, 0, length);
            final GZIPInputStream zip = new GZIPInputStream(new ByteArrayInputStream(serializeForm));
            ois = new ObjectInputStream(zip);
            object = ois.readObject();
            objectIOManager.setOriginalSerializedForm(object, serializeForm);
        } catch (IOException exception) {
            throw new ObjectIOException(exception);
        } catch (ClassNotFoundException exception) {
            throw new ObjectIOException(exception);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException exception) {
            }
        }
        return object;
    }

    public void writeBody(final byte[] binary, final int bodyBeginOffset, final Object object, final Class objectClass, final boolean updateOriginalValue) throws ObjectIOException {
        final byte[] serializedForm = getSerializedForm(object, objectClass);
        System.arraycopy(serializedForm, 0, binary, bodyBeginOffset, serializedForm.length);
        if (updateOriginalValue) {
            objectIOManager.setOriginalSerializedForm(object, serializedForm);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected byte[] serialize(final Object object, final Class objectClass) throws ObjectIOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream stream = null;
        try {
            final GZIPOutputStream zip = new GZIPOutputStream(output);
            stream = new ObjectOutputStream(zip);
            stream.writeObject(object);
        } catch (IOException exception) {
            throw new ObjectIOException(exception);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException exception) {
            }
        }
        return output.toByteArray();
    }

    /**
	 * all serializable are mutable
	 */
    @Override
    public boolean isMutable() {
        return true;
    }
}

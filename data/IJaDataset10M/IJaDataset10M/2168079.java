package com.amazon.carbonado;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.amazon.carbonado.repo.map.MapRepositoryBuilder;

/**
 * A CorruptEncodingException is caused when decoding an encoded record fails.
 *
 * @author Brian S O'Neill
 */
public class CorruptEncodingException extends FetchException {

    private static final long serialVersionUID = 2L;

    private transient Storable mStorable;

    public CorruptEncodingException() {
        super();
    }

    public CorruptEncodingException(String message) {
        super(message);
    }

    public CorruptEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorruptEncodingException(Throwable cause) {
        super(cause);
    }

    /**
     * @param expectedGeneration expected layout generation of decoded storable
     * @param actualGeneration actual layout generation of decoded storable
     */
    public CorruptEncodingException(int expectedGeneration, int actualGeneration) {
        super("Expected layout generation of " + expectedGeneration + ", but actual layout generation was " + actualGeneration);
    }

    /**
     * If the decoder can at least extract the primary key, it should set it here.
     */
    public void setStorableWithPrimaryKey(Storable s) {
        if (s != null) {
            s.markAllPropertiesClean();
        }
        mStorable = s;
    }

    /**
     * If the decoder was able to extract the primary key, it will be available in the
     * returned Storable. If this exception was re-constructed through serialization, then
     * the Storable is as well. As a result, it won't be bound to any Repository and
     * updating it will have no effect.
     *
     * @return partial Storable with primary key defined, or null if unable to
     * decode the key
     */
    public Storable getStorableWithPrimaryKey() {
        return mStorable;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (mStorable != null) {
            message = message + "; " + mStorable.toStringKeyOnly();
        }
        return message;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Storable s = mStorable;
        if (s == null) {
            out.write(0);
        } else {
            out.write(1);
            out.writeObject(s.storableType());
            try {
                s.writeTo(out);
            } catch (SupportException e) {
                throw new IOException(e);
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int h = in.read();
        if (h == 1) {
            try {
                Class<? extends Storable> type = (Class) in.readObject();
                Storable s = MapRepositoryBuilder.newRepository().storageFor(type).prepare();
                s.readFrom(in);
                mStorable = s;
            } catch (RepositoryException e) {
                throw new IOException(e);
            }
        }
    }
}

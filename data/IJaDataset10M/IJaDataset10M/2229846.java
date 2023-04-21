package redstone.prevalence;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 *  An object serializer using Java Serialization.
 *
 *  @author Greger Ohlson
 *  @version $Revision: 1.2 $
 */
public class SerializerImpl<T extends Serializable> implements Serializer<T> {

    public void serialize(T object, OutputStream output) {
        try {
            new ObjectOutputStream(output).writeObject(object);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not serialize object " + object, ioe);
        }
    }

    @SuppressWarnings("unchecked")
    public T deserialize(InputStream input) {
        try {
            return (T) new ObjectInputStream(input).readObject();
        } catch (EOFException eof) {
            return null;
        } catch (Exception ioe) {
            throw new RuntimeException("Could not deserialize object", ioe);
        }
    }
}

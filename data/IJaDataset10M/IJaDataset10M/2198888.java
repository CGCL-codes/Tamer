package org.impalaframework.util.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.impalaframework.exception.ExecutionException;

/**
 * <p>Assists with the serialization process and performs additional functionality based 
 * on serialization.</p>
 * <p>
 * <ul>
 * <li>Deep clone using serialization
 * <li>Serialize managing finally and IOException
 * <li>Deserialize managing finally and IOException
 * </ul>
 *
 * <p>This class throws exceptions for invalid <code>null</code> inputs.
 * Each method documents its behaviour in more detail.</p>
 * 
 * <p>Based on the class <code>SerializationUtils</code> from Apache commons lang.</p>
 * 
 * <p>Throwing <code>ExecutionException</code> rather than <code>SerializationException</code> on error.</p>
 * <p>Added <code>SerializationStreamFactory</code> with the purpose of customising serialization/deserialization mechanism</p>
 * 
 * <p>Note that the Thread Saftey of this class depends on the thread safety of the SerializationStreamFactory. 
 * The no-args constructor version is thread safe. Otherwise, it is probably safe not to assume thread safety
 *
 * @see org.impalaframework.util.serializeDefaultSerializationStreamFactory
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @author <a href="mailto:janekdb@yahoo.co.uk">Janek Bogucki</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author Stephen Colebourne
 * @author Jeff Varszegi
 * @author Gary Gregory
 * @author Phil Zoio
 */
public class SerializationHelper {

    private SerializationStreamFactory factory;

    /**
     * <p>Default constructor, which internally use an instance of <code>DefaultSerializationStreamFactory</code> </p>
     */
    public SerializationHelper() {
        super();
        factory = new DefaultSerializationStreamFactory();
    }

    public SerializationHelper(SerializationStreamFactory factory) {
        super();
        this.factory = factory;
    }

    public Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public void serialize(Serializable obj, OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            out = factory.getOutputStream(outputStream);
            out.writeObject(obj);
        } catch (IOException ex) {
            throw new ExecutionException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in = null;
        try {
            in = factory.getInputStream(inputStream);
            return in.readObject();
        } catch (ClassNotFoundException ex) {
            throw new ExecutionException(ex);
        } catch (IOException ex) {
            throw new ExecutionException(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }
}

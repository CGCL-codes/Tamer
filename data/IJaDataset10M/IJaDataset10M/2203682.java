package org.restlet.resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.restlet.data.MediaType;

/**
 * Representation based on a serializable Java object.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ObjectRepresentation extends OutputRepresentation {

    /** The serializable object. */
    private Object object;

    /**
     * Constructor reading the object from a serialized representation. This
     * representation must have the proper media type:
     * "application/x-java-serialized-object".
     * 
     * @param serializedRepresentation
     *            The serialized representation.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
    public ObjectRepresentation(Representation serializedRepresentation) throws IOException, ClassNotFoundException, IllegalArgumentException {
        super(MediaType.APPLICATION_JAVA_OBJECT);
        if (serializedRepresentation.getMediaType().equals(MediaType.APPLICATION_JAVA_OBJECT)) {
            ObjectInputStream ois = new ObjectInputStream(serializedRepresentation.getStream());
            this.object = ois.readObject();
            ois.close();
        } else {
            throw new IllegalArgumentException("The serialized representation must have this media type: " + MediaType.APPLICATION_JAVA_OBJECT.toString());
        }
    }

    /**
     * Constructor
     * 
     * @param object
     *            The serializable object.
     */
    public ObjectRepresentation(Serializable object) {
        super(MediaType.APPLICATION_JAVA_OBJECT);
        this.object = object;
    }

    /**
     * Returns the represented object.
     * 
     * @return The represented object.
     * @throws IOException
     */
    public Object getObject() throws IOException {
        return this.object;
    }

    /**
     * Writes the datum as a stream of bytes.
     * 
     * @param outputStream
     *            The stream to use when writing.
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(getObject());
        oos.close();
    }
}

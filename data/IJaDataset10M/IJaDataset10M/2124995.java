package org.coos.util.serialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * User: Arild Herstad, Telenor FoU
 * 
 * 
 */
public interface AFSerializer {

    /**
	 * This function must implement the serialization of the object.
	 * 
	 * @return a byte array with the objects data
	 * @throws IOException
	 */
    byte[] serialize() throws IOException;

    /**
	 * Use this function for resurrection of the object
	 * 
	 * @param data
	 *            The serialized data containing the object data
	 * @throws IOException
	 */
    ByteArrayInputStream deSerialize(byte[] data, AFClassLoader cl) throws IOException;
}

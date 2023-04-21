package net.sf.staccatocommons.testing.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author flbulgarelli
 * 
 */
public class SerializationAssert {

    public static <T extends Serializable> void assertCanSerialize(T object) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(ba).writeObject(object);
            T result = (T) new ObjectInputStream(new ByteArrayInputStream(ba.toByteArray())).readObject();
            assertEquals(result, object);
        } catch (Exception e) {
            fail("Should not have thrown an exception " + e.toString());
        }
    }
}

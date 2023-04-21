package be.vds.jtbtaskplanner.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ObjectSerializer {

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.close();
            return bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ObjectSerializer.class.getName()).severe(ex.getMessage());
        }
        return null;
    }

    public static Object deserialize(byte[] serializedObject) {
        Object result = null;
        if (serializedObject != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                result = ois.readObject();
                bis.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ObjectSerializer.class.getName()).severe(ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(ObjectSerializer.class.getName()).severe(ex.getMessage());
            }
        }
        return result;
    }

    public static Object cloneObject(Object object) {
        Object clone = null;
        byte[] copy = serialize(object);
        clone = deserialize(copy);
        return clone;
    }
}

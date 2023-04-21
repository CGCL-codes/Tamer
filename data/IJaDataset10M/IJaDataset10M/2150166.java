package self.lang;

import java.io.*;

public final class ObjectUtils {

    private ObjectUtils() {
    }

    public static Object deepClone(Object src) {
        if (src == null) throw new IllegalArgumentException("err.clonning.serialization");
        Object clone;
        try {
            ByteArrayOutputStream buff = new ByteArrayOutputStream(1024);
            ObjectOutputStream serialiser = new ObjectOutputStream(buff);
            try {
                serialiser.writeObject(src);
            } finally {
                serialiser.close();
            }
            ByteArrayInputStream buffIn = new ByteArrayInputStream(buff.toByteArray());
            ObjectInputStream deserialiser = new ObjectInputStream(buffIn);
            try {
                clone = deserialiser.readObject();
            } finally {
                deserialiser.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
            throw new IllegalArgumentException("err.cloneing.serialization");
        }
        return clone;
    }

    public static boolean isSame(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 != null) return o1.equals(o2);
        return false;
    }
}

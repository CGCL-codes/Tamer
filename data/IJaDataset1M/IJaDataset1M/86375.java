package net.sf.crispy.impl.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serializer {

    public static void serialize(Object pvObject, OutputStream pvOutputStream) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(pvOutputStream);
        oos.writeObject(pvObject);
        oos.close();
    }

    public static byte[] serialize(Object pvObject) throws Exception {
        ByteArrayOutputStream lvArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(lvArrayOutputStream);
        oos.writeObject(pvObject);
        byte[] lvBytes = lvArrayOutputStream.toByteArray();
        oos.close();
        return lvBytes;
    }

    public static Object deserialize(InputStream pvInputStream) throws IOException, ClassNotFoundException {
        Object lvReturn = null;
        ObjectInputStream ois = new ObjectInputStream(pvInputStream);
        lvReturn = ois.readObject();
        ois.close();
        return lvReturn;
    }

    public static Object deserialize(byte pvBytes[]) throws IOException, ClassNotFoundException {
        Object lvReturn = null;
        ByteArrayInputStream lvArrayInputStream = new ByteArrayInputStream(pvBytes);
        ObjectInputStream ois = new ObjectInputStream(lvArrayInputStream);
        lvReturn = ois.readObject();
        ois.close();
        return lvReturn;
    }
}

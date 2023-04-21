package br.uece.tcc.fh.jxta.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author pedro
 * 
 */
public class MessageUtil {

    public static byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream outputToString = new ObjectOutputStream(baos);
        outputToString.writeObject(obj);
        outputToString.close();
        return baos.toByteArray();
    }

    public static Object toObject(byte[] array) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}

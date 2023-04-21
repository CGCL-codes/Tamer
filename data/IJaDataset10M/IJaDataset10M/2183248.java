package tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeCopy<T> {

    private final T valueToCopy;

    public SerializeCopy(T t) {
        valueToCopy = t;
    }

    @SuppressWarnings("unchecked")
    public T getDeepCopy() throws NotSerializableException {
        ObjectOutputStream outStream = null;
        ObjectInputStream inStream = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            outStream = new ObjectOutputStream(byteOut);
            outStream.writeObject(valueToCopy);
            outStream.flush();
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            inStream = new ObjectInputStream(byteIn);
            return (T) inStream.readObject();
        } catch (NotSerializableException e) {
            throw (e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

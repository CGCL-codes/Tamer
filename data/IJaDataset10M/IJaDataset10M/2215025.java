package org.regola.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Clonator {

    @SuppressWarnings("unchecked")
    public static <T> T clone(T o) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(b);
            out.writeObject(o);
            out.close();
            ByteArrayInputStream bi = new ByteArrayInputStream(b.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bi);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

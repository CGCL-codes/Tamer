package edu.ucsd.ncmir.ontology;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;

class SerializableHashSet<K> extends HashSet<K> {

    private static final long serialVersionUID = 42L;

    private void writeObject(ObjectOutputStream oos) throws IOException {
        Object[] list = this.toArray();
        Arrays.sort(list);
        oos.writeObject(list);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Object[] list = (Object[]) ois.readObject();
        for (Object s : list) this.add((K) s);
    }
}

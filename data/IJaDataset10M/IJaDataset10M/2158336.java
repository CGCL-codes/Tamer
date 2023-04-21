package jneural;

import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.Serializable;

public class SerializableMap<K, V> extends HashMap<K, V> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        while (true) {
            Object obj1 = in.readObject();
            if ((obj1 instanceof Integer) && (Integer) obj1 == 0) {
                break;
            }
            V obj2 = (V) in.readObject();
            put((K) obj1, (V) obj2);
        }
    }

    public void writeObject(java.io.ObjectOutputStream out) throws IOException {
        Set<K> eset = this.keySet();
        Iterator<K> it = this.keySet().iterator();
        ;
        while (it.hasNext()) {
            K k = it.next();
            out.writeObject(k);
            out.writeObject(get(k));
        }
        out.writeObject(new Integer(0));
    }
}

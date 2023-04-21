package es.realtimesystems.simplemulticast;

import java.util.HashMap;
import java.util.Iterator;

public class PruebaHashMap {

    HashMap hash = null;

    public PruebaHashMap() {
        hash = new HashMap();
        hash.put(new Integer(1), new Integer(1));
        hash.put(new Integer(2), new Integer(2));
        hash.put(new Integer(3), new Integer(3));
        hash.put(new Integer(4), new Integer(4));
    }

    void run() {
        Log.log("", "Vamos a ver los que tiene el hash..");
        Iterator i = hash.values().iterator();
        while (i.hasNext()) {
            Integer ent = (Integer) i.next();
            Log.log("", "Entero: " + ent);
        }
        Log.log("", "Modificamos un value...");
        Integer ent = (Integer) hash.get(new Integer(4));
        int valor = ent.intValue();
        valor++;
        hash.put(new Integer(4), new Integer(valor));
        i = hash.values().iterator();
        while (i.hasNext()) {
            ent = (Integer) i.next();
            Log.log("", "Entero: " + ent);
        }
    }

    static final void main(String[] args) {
        PruebaHashMap p = new PruebaHashMap();
        p.run();
    }
}

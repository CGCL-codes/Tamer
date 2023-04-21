package org.coos.util.serialize;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Hashtable helper for persitance and resurrection.
 * 
 * @author Geir Melby, Tellu AS
 */
public class HashtableHelper {

    /**
	 * Helper for Hashtable serialization.
	 * 
	 * @param hTable
	 * @return
	 * @throws IOException
	 */
    public static byte[] persist(Hashtable hTable) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        persist(hTable, dout);
        return bout.toByteArray();
    }

    public static void persist(Hashtable hTable, DataOutputStream dout) throws IOException {
        if (hTable == null) {
            dout.writeBoolean(false);
        } else {
            dout.writeBoolean(true);
            VectorHelper.persist(getValues(hTable), dout);
            Vector keys = new Vector();
            Enumeration eKeys = hTable.keys();
            while (eKeys.hasMoreElements()) {
                keys.addElement(eKeys.nextElement());
            }
            VectorHelper.persist(keys, dout);
        }
        dout.flush();
    }

    public static Hashtable resurrect(DataInputStream din, AFClassLoader cl) throws IOException {
        if (din.readBoolean()) {
            Hashtable ht = new Hashtable();
            Vector values = VectorHelper.resurrect(din, cl);
            Vector keys = VectorHelper.resurrect(din, cl);
            if (keys.size() != values.size()) {
                throw (new IOException());
            }
            for (int i = 0; i < keys.size(); i++) {
                ht.put(keys.elementAt(i), values.elementAt(i));
            }
            return ht;
        }
        return null;
    }

    private static Vector getValues(Hashtable h1) {
        Vector v1 = new Vector();
        Enumeration e = h1.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            v1.addElement(o);
        }
        return v1;
    }
}

package com.ibm.awb.weakref;

import java.util.Enumeration;
import java.util.Hashtable;

public class WeakRefTable {

    private Hashtable _ref_table = new Hashtable();

    public void add(WeakRef ref) {
        this._ref_table.put(ref.getRefID(), ref);
    }

    protected void debug(WeakRef current, String title) {
        System.out.println(title);
        System.out.println(this.toString(current));
    }

    public WeakRef getWeakRef(Object id) {
        return (WeakRef) this._ref_table.get(id);
    }

    @Override
    public String toString() {
        return this.toString(null);
    }

    protected String toString(WeakRef current) {
        Enumeration e = this._ref_table.elements();
        int i = 0;
        StringBuffer buf = new StringBuffer();
        while (e.hasMoreElements()) {
            WeakRef w = (WeakRef) e.nextElement();
            if (w == current) {
                buf.append("*[" + i + "]" + w.toString() + "\n");
            } else {
                buf.append(" [" + i + "]" + w.toString() + "\n");
            }
            i++;
        }
        return buf.toString();
    }

    synchronized void unreference(WeakRef wref) {
        synchronized (wref) {
            wref._ref_count--;
            if (wref._ref_count == 0) {
                if (this._ref_table.contains(wref)) {
                    this._ref_table.remove(wref.getRefID());
                }
            }
        }
    }
}

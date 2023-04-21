package org.xmlcml.cml.legacy2cml.graphics.wmf;

import java.util.Hashtable;
import java.util.Vector;

public class WMFHandleTable {

    private Vector<Integer> handleTable;

    private Hashtable<Integer, MetaFileRecord> MRecordTable;

    public WMFHandleTable() {
        Integer i;
        i = new Integer(-1);
        handleTable = new Vector<Integer>();
        this.MRecordTable = new Hashtable<Integer, MetaFileRecord>();
    }

    public MetaFileRecord selectObject(int index) {
        Integer i;
        Integer j;
        MetaFileRecord m;
        i = new Integer(-1);
        try {
            i = handleTable.elementAt(index);
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println(e);
        }
        m = MRecordTable.get(i);
        return (m);
    }

    public void deleteObject(int index) {
        Integer i;
        i = new Integer(-1);
        handleTable.setElementAt((Integer) i, index);
        i = new Integer(index);
        MRecordTable.remove(i);
    }

    public void addObject(int recordValue, MetaFileRecord m) {
        int index;
        Integer h;
        Integer i;
        h = new Integer(recordValue);
        i = new Integer(-1);
        if (handleTable.contains(i)) {
            index = handleTable.indexOf(i);
            handleTable.setElementAt(h, index);
        } else {
            handleTable.addElement(h);
        }
        MRecordTable.put(h, m);
    }
}

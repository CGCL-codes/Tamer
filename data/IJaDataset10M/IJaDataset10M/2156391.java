package org.tumugame;

import java.util.Hashtable;
import java.util.Vector;

interface ExportObject {

    public void doExport(Exporter exporter);
}

interface ImportObject {

    public void doImport(Importer importer);
}

class Exporter {

    private Hashtable done = new Hashtable();

    private Hashtable todo = new Hashtable();

    private boolean active = false;

    public void exportInt(int value) {
    }

    public void exportString(String s) {
    }

    public void exportVector(Vector v) {
        int i, size = v.size();
        exportInt(size);
        for (i = 0; i < size; i++) {
            exportObject(v.elementAt(i));
        }
    }

    public void exportObject(Object object) {
        if (object instanceof Integer) {
            Integer i = (Integer) object;
            exportInt(i.intValue());
        } else if (object instanceof String) {
            String s = (String) object;
            exportString(s);
        } else if (object instanceof BaseObject) {
            BaseObject baseObject = (BaseObject) object;
            if (active) {
                if (!done.containsKey(object)) todo.put(object, object);
            } else {
                active = true;
                done.put(object, object);
                todo.remove(object);
                baseObject.doExport(this);
                active = false;
            }
        } else if (object instanceof ExportObject) {
            ExportObject exportObject = (ExportObject) object;
            boolean old = active;
            active = true;
            exportObject.doExport(this);
            active = old;
        }
    }
}

abstract class Importer {

    public abstract int importInt();

    public Vector importVector() {
        int i, size = importInt();
        Vector v = new Vector(size);
        for (i = 0; i < size; i++) {
            v.add(importObject());
        }
        return v;
    }

    public Object importObject() {
        return null;
    }

    public void importBaseObject(BaseObject object) {
    }
}

class DirectExporter {
}

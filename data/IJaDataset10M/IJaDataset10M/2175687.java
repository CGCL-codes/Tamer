package org.nakedobjects.persistence.file;

import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.persistence.defaults.SerialOid;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A logical collection of elements of a specified type
 */
public class ObjectData extends Data {

    private Hashtable fields;

    public ObjectData(NakedObjectSpecification type, SerialOid oid) {
        super(type, oid);
        fields = new Hashtable();
    }

    public String toString() {
        return "ObjectData[type=" + getClassName() + ",oid=" + getOid() + ",fields=" + fields + "]";
    }

    public void set(String fieldName, Object oid) {
        if (oid == null) {
            fields.remove(fieldName);
        } else {
            fields.put(fieldName, oid);
        }
    }

    void saveValue(String fieldName, boolean isEmpty, String encodedString) {
        if (isEmpty) {
            fields.remove(fieldName);
        } else {
            fields.put(fieldName, encodedString);
        }
    }

    public void set(String fieldName, String value) {
        fields.put(fieldName, value);
    }

    public Object get(String fieldName) {
        return fields.get(fieldName);
    }

    public String value(String fieldName) {
        return (String) get(fieldName);
    }

    public String id(String fieldName) {
        Object field = get(fieldName);
        return field == null ? null : "" + ((SerialOid) field).getSerialNo();
    }

    void initCollection(SerialOid collectionOid, String fieldName) {
        fields.put(fieldName, new ReferenceVector(collectionOid));
    }

    void addElement(String fieldName, SerialOid elementOid) {
        if (!fields.containsKey(fieldName)) {
            throw new NakedObjectRuntimeException("Field " + fieldName + " not found  in hashtable");
        }
        ReferenceVector v = (ReferenceVector) fields.get(fieldName);
        v.add(elementOid);
    }

    ReferenceVector elements(String fieldName) {
        return (ReferenceVector) fields.get(fieldName);
    }

    public Enumeration fields() {
        return fields.keys();
    }

    void addAssociation(NakedObject fieldContent, String fieldName, boolean ensurePersistent) {
        boolean notAlreadyPersistent = fieldContent != null && fieldContent.getOid() == null;
        if (ensurePersistent && notAlreadyPersistent) {
            throw new IllegalStateException("Cannot save an object that is not persistent: " + fieldContent);
        }
        set(fieldName, fieldContent == null ? null : fieldContent.getOid());
    }

    void addInternalCollection(InternalCollection collection, String fieldName, boolean ensurePersistent) {
        SerialOid coid = (SerialOid) collection.getOid();
        initCollection(coid, fieldName);
        int size = collection.size();
        for (int i = 0; i < size; i++) {
            NakedObject element = collection.elementAt(i);
            Object elementOid = element.getOid();
            if (elementOid == null) {
                throw new IllegalStateException("Element is not persistent " + element);
            }
            addElement(fieldName, (SerialOid) elementOid);
        }
    }
}

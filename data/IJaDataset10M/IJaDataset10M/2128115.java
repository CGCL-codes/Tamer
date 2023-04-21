package org.datanucleus.store.json.fieldmanager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.fieldmanager.FieldManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FieldManager for inserting data into JSON.
 */
public class InsertFieldManager implements FieldManager {

    ObjectProvider sm;

    JSONObject jsonobj;

    public InsertFieldManager(ObjectProvider sm, JSONObject jsonobj) {
        this.sm = sm;
        this.jsonobj = jsonobj;
        try {
            jsonobj.put("class", sm.getClassMetaData().getFullClassName());
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public String fetchStringField(int fieldNumber) {
        return null;
    }

    public short fetchShortField(int fieldNumber) {
        return 0;
    }

    public Object fetchObjectField(int fieldNumber) {
        return null;
    }

    public long fetchLongField(int fieldNumber) {
        return 0;
    }

    public int fetchIntField(int fieldNumber) {
        return 0;
    }

    public float fetchFloatField(int fieldNumber) {
        return 0;
    }

    public double fetchDoubleField(int fieldNumber) {
        return 0;
    }

    public char fetchCharField(int fieldNumber) {
        return 0;
    }

    public byte fetchByteField(int fieldNumber) {
        return 0;
    }

    public boolean fetchBooleanField(int fieldNumber) {
        return false;
    }

    public void storeStringField(int fieldNumber, String value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            if (value == null) {
                jsonobj.put(name, JSONObject.NULL);
            } else {
                jsonobj.put(name, value);
            }
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeShortField(int fieldNumber, short value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (int) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeObjectField(int fieldNumber, Object value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            if (value == null) {
                jsonobj.put(name, JSONObject.NULL);
            } else {
                if (value instanceof Date) {
                    jsonobj.put(name, ((Date) value).getTime());
                } else if (value instanceof Calendar) {
                    jsonobj.put(name, ((Calendar) value).getTimeInMillis());
                } else if (value instanceof BigDecimal) {
                    jsonobj.put(name, value);
                } else if (value instanceof BigInteger) {
                    jsonobj.put(name, value);
                } else {
                    JSONObject jsonobjfield = new JSONObject(value);
                    jsonobjfield.put("class", value.getClass().getName());
                    jsonobj.put(name, jsonobjfield);
                }
            }
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeLongField(int fieldNumber, long value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (long) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeIntField(int fieldNumber, int value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (int) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeFloatField(int fieldNumber, float value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (double) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeDoubleField(int fieldNumber, double value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (double) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeCharField(int fieldNumber, char value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, new Character(value));
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeByteField(int fieldNumber, byte value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (int) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }

    public void storeBooleanField(int fieldNumber, boolean value) {
        AbstractMemberMetaData mmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        String name = mmd.getName();
        if (mmd.hasExtension("member-name")) {
            name = mmd.getValueForExtension("member-name");
        }
        try {
            jsonobj.put(name, (boolean) value);
        } catch (JSONException e) {
            throw new NucleusException(e.getMessage(), e);
        }
    }
}

package com.versant.core.jdo;

import com.versant.core.common.OID;
import com.versant.core.common.NewObjectOID;
import com.versant.core.metadata.ModelMetaData;
import com.versant.core.metadata.MDStatics;
import com.versant.core.metadata.ClassMetaData;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import com.versant.core.common.BindingSupportImpl;

/**
 * This is the JDO Genie specific class that represents datastore id
 * instances for usage by users. Requests for the id instance of an
 * datastore instance will return this and the same for getObjectIdClass.
 */
public final class VersantOid implements Externalizable {

    private transient PMCacheEntry ce;

    public transient OID actualOID;

    public int classId;

    public long pk;

    public VersantOid() {
    }

    public VersantOid(OID actualOID, ModelMetaData jmd, boolean resolved) {
        this.actualOID = actualOID;
        if (resolved) {
            classId = actualOID.getClassMetaData().classId;
        } else {
            classId = actualOID.getAvailableClassId();
        }
        pk = actualOID.getLongPrimaryKey();
    }

    public VersantOid(PCStateMan sm, ModelMetaData jmd, boolean resolved) {
        actualOID = sm.oid;
        if (sm.oid.isNew() && ((NewObjectOID) sm.oid).realOID != null) {
            actualOID = ((NewObjectOID) sm.oid).realOID;
        } else {
            this.ce = sm.cacheEntry;
        }
        classId = actualOID.getAvailableClassId();
        pk = actualOID.getLongPrimaryKey();
    }

    public VersantOid(String s) {
        try {
            char c = s.charAt(0);
            int classId = c - '0';
            checkClassIDDigit(classId, c, s);
            int i = 1;
            for (; ; ) {
                c = s.charAt(i++);
                if (c == MDStatics.OID_CHAR_SEPERATOR) break;
                int digit = c - '0';
                checkClassIDDigit(digit, c, s);
                classId = classId * 10 + digit;
            }
            this.classId = classId;
            c = s.charAt(i++);
            int n = s.length();
            long pk = c - '0';
            checkPkDigit(pk, c, s);
            for (; i < n; ) {
                c = s.charAt(i++);
                int digit = c - '0';
                checkPkDigit(digit, c, s);
                pk = pk * 10 + digit;
            }
            this.pk = pk;
        } catch (Exception e) {
            if (BindingSupportImpl.getInstance().isOwnInvalidObjectIdException(e)) {
                throw (RuntimeException) e;
            } else {
                throw BindingSupportImpl.getInstance().invalidObjectId("Invalid OID String: '" + s + "': " + e, e);
            }
        }
    }

    private void checkClassIDDigit(int digit, char c, String s) {
        if (digit < 0 || digit > 9) {
            throw BindingSupportImpl.getInstance().invalidObjectId("Invalid digit '" + c + "' in classID for OID String: '" + s + "'", null);
        }
    }

    private void checkPkDigit(long digit, char c, String s) {
        if (digit < 0 || digit > 9) {
            throw BindingSupportImpl.getInstance().invalidObjectId("Invalid digit '" + c + "' in primary key for OID String: '" + s + "'", null);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        convertToRealOID();
        out.writeInt(classId);
        out.writeLong(pk);
    }

    public void readExternal(ObjectInput in) throws IOException {
        classId = in.readInt();
        pk = in.readLong();
    }

    public String toString() {
        convertToRealOID();
        return classId + "-" + pk;
    }

    private void convertToRealOID() {
        if (actualOID == null) return;
        if (!actualOID.isNew()) return;
        NewObjectOID newOid = (NewObjectOID) actualOID;
        getActualOID(newOid);
        classId = newOid.getCmd().classId;
        pk = actualOID.getLongPrimaryKey();
    }

    /**
     * Attempt to obtain the real oid for this newOid.
     */
    private void getActualOID(NewObjectOID newOid) {
        if (newOid.realOID != null) {
            actualOID = ((NewObjectOID) actualOID).realOID;
        } else {
            if (ce != null) {
                PCStateMan sm = (PCStateMan) ce.get();
                if (sm != null && !sm.getPmProxy().isClosed()) {
                    actualOID = sm.getRealOID();
                } else {
                    throw BindingSupportImpl.getInstance().exception("The transaction is which this " + "JDO Object Id was used was never finished");
                }
            } else {
                throw BindingSupportImpl.getInstance().exception("The transaction is which this " + "JDO Object Id was used was never finished");
            }
        }
    }

    public boolean equals(Object object) {
        convertToRealOID();
        if (object instanceof VersantOid) {
            VersantOid o = (VersantOid) object;
            o.convertToRealOID();
            return classId == o.classId && pk == o.pk;
        }
        return false;
    }

    public int hashCode() {
        convertToRealOID();
        return classId + (int) pk * 29;
    }
}

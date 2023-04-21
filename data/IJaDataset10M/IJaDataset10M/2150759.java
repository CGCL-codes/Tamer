package com.versant.core.jdo;

import com.versant.core.common.State;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.StateManager;
import com.versant.core.common.BindingSupportImpl;
import com.versant.core.common.OID;

/**
 * State managed used to get at the fields of a detached instance.
 */
public class VersantDetachStateManager implements VersantStateManager {

    DetachStateContainer dsc;

    public OID getOID() {
        return null;
    }

    public PersistenceCapable getPersistenceCapable() {
        return null;
    }

    public DetachStateContainer getDsc() {
        return dsc;
    }

    public void setDsc(DetachStateContainer dsc) {
        this.dsc = dsc;
    }

    public byte replacingFlags(PersistenceCapable persistenceCapable) {
        return 0;
    }

    public StateManager replacingStateManager(PersistenceCapable persistenceCapable, StateManager stateManager) {
        return stateManager;
    }

    public boolean isDirty(PersistenceCapable persistenceCapable) {
        return false;
    }

    public boolean isTransactional(PersistenceCapable persistenceCapable) {
        return false;
    }

    public boolean isPersistent(PersistenceCapable persistenceCapable) {
        return false;
    }

    public boolean isNew(PersistenceCapable persistenceCapable) {
        return false;
    }

    public boolean isDeleted(PersistenceCapable persistenceCapable) {
        return false;
    }

    public PersistenceManager getPersistenceManager(PersistenceCapable persistenceCapable) {
        return null;
    }

    public void makeDirty(PersistenceCapable persistenceCapable, String s) {
        ((VersantDetachable) persistenceCapable).versantMakeDirty(s);
    }

    public Object getObjectId(PersistenceCapable persistenceCapable) {
        return null;
    }

    public Object getTransactionalObjectId(PersistenceCapable persistenceCapable) {
        return null;
    }

    public boolean isLoaded(PersistenceCapable persistenceCapable, int i) {
        return ((VersantDetachable) persistenceCapable).versantIsLoaded(i);
    }

    public void preSerialize(PersistenceCapable persistenceCapable) {
    }

    public boolean getBooleanField(PersistenceCapable persistenceCapable, int i, boolean b) {
        return b;
    }

    public char getCharField(PersistenceCapable persistenceCapable, int i, char c) {
        return c;
    }

    public byte getByteField(PersistenceCapable persistenceCapable, int i, byte b) {
        return b;
    }

    public short getShortField(PersistenceCapable persistenceCapable, int i, short i1) {
        return i1;
    }

    public int getIntField(PersistenceCapable persistenceCapable, int i, int i1) {
        return i1;
    }

    public long getLongField(PersistenceCapable persistenceCapable, int i, long l) {
        return l;
    }

    public float getFloatField(PersistenceCapable persistenceCapable, int i, float v) {
        return v;
    }

    public double getDoubleField(PersistenceCapable persistenceCapable, int i, double v) {
        return v;
    }

    public String getStringField(PersistenceCapable persistenceCapable, int i, String s) {
        return s;
    }

    public Object getObjectField(PersistenceCapable persistenceCapable, int i, Object o) {
        return o;
    }

    public void setBooleanField(PersistenceCapable persistenceCapable, int i, boolean b, boolean b1) {
    }

    public void setCharField(PersistenceCapable persistenceCapable, int i, char c, char c1) {
    }

    public void setByteField(PersistenceCapable persistenceCapable, int i, byte b, byte b1) {
    }

    public void setShortField(PersistenceCapable persistenceCapable, int i, short i1, short i2) {
    }

    public void setIntField(PersistenceCapable persistenceCapable, int i, int i1, int i2) {
    }

    public void setLongField(PersistenceCapable persistenceCapable, int i, long l, long l1) {
    }

    public void setFloatField(PersistenceCapable persistenceCapable, int i, float v, float v1) {
    }

    public void setDoubleField(PersistenceCapable persistenceCapable, int i, double v, double v1) {
    }

    public void setStringField(PersistenceCapable persistenceCapable, int i, String s, String s1) {
    }

    public void setObjectField(PersistenceCapable persistenceCapable, int i, Object o, Object o1) {
    }

    public void providedBooleanField(PersistenceCapable persistenceCapable, int i, boolean b) {
    }

    public void providedCharField(PersistenceCapable persistenceCapable, int i, char c) {
    }

    public void providedByteField(PersistenceCapable persistenceCapable, int i, byte b) {
    }

    public void providedShortField(PersistenceCapable persistenceCapable, int i, short i1) {
    }

    public void providedIntField(PersistenceCapable persistenceCapable, int i, int i1) {
    }

    public void providedLongField(PersistenceCapable persistenceCapable, int i, long l) {
    }

    public void providedFloatField(PersistenceCapable persistenceCapable, int i, float v) {
    }

    public void providedDoubleField(PersistenceCapable persistenceCapable, int i, double v) {
    }

    public void providedStringField(PersistenceCapable persistenceCapable, int i, String s) {
    }

    public void providedObjectField(PersistenceCapable persistenceCapable, int i, Object o) {
    }

    public boolean replacingBooleanField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            boolean temp = state.getBooleanFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return false;
    }

    public char replacingCharField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            char temp = state.getCharFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public byte replacingByteField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            byte temp = state.getByteFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public short replacingShortField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            short temp = state.getShortFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public int replacingIntField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            int temp = state.getIntFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            ((VersantDetachable) pc).versantSetLoaded(field);
            handleException(e);
        }
        return 0;
    }

    public float replacingFloatField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            float temp = state.getFloatFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public double replacingDoubleField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            double temp = state.getDoubleFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public long replacingLongField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            long temp = state.getLongFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return 0;
    }

    public String replacingStringField(final PersistenceCapable pc, final int field) {
        try {
            State state = dsc.getState(pc);
            String temp = state.getStringFieldAbs(field);
            ((VersantDetachable) pc).versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    public Object replacingObjectField(final PersistenceCapable pc, final int field) {
        try {
            VersantDetachable detachable = ((VersantDetachable) pc);
            Object temp = dsc.getObjectField(detachable, field);
            detachable.versantSetLoaded(field);
            return temp;
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    private final void handleException(Exception x) {
        if (BindingSupportImpl.getInstance().isOwnException(x)) {
            throw (RuntimeException) x;
        } else {
            throw BindingSupportImpl.getInstance().internal(x.getMessage(), x);
        }
    }

    public void makeDirty(PersistenceCapable pc, int managedFieldNo) {
        ((VersantDetachable) pc).versantMakeDirty(managedFieldNo);
    }

    public void fillNewAppPKField(int fieldNo) {
    }
}

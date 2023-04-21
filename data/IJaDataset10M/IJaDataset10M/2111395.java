package com.versant.core.jdo;

import com.versant.core.metadata.ClassMetaData;
import com.versant.core.metadata.FetchGroup;
import com.versant.core.metadata.FieldMetaData;
import com.versant.core.metadata.ModelMetaData;
import com.versant.core.server.OIDGraph;
import javax.jdo.spi.PersistenceCapable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import java.util.*;
import com.versant.core.common.*;
import com.versant.core.util.IntArray;
import com.versant.core.util.OIDObjectOutput;
import com.versant.core.util.OIDObjectInput;

/**
 * This is a wrapper of the state instance to be used by the mem query classes.
 */
public final class QueryStateWrapper extends State {

    private final PCStateMan pcStateMan;

    private final PMProxy pm;

    private ClassMetaData cmd;

    public QueryStateWrapper() {
        pcStateMan = null;
        pm = null;
    }

    public boolean isCacheble() {
        return false;
    }

    public boolean isFieldNullorZero(int stateFieldNo) {
        return false;
    }

    public QueryStateWrapper(PCStateMan pcStateMan, PMProxy pm) {
        this.pcStateMan = pcStateMan;
        this.pm = pm;
    }

    /**
     * Return the internal value for the field.
     */
    public Object getInternalObjectField(int field) {
        return pcStateMan.getObjectField(null, cmd.stateFields[field].managedFieldNo, null);
    }

    public Collection getInternalStateCollection(int field) {
        Collection col = (Collection) getInternalObjectField(field);
        int size = col == null ? 0 : col.size();
        List results = new ArrayList(size);
        if (size == 0) {
            return results;
        }
        for (Iterator iterator = col.iterator(); iterator.hasNext(); ) {
            PersistenceCapable pc = (PersistenceCapable) iterator.next();
            results.add(pm.getInternalSM(pc).queryStateWrapper);
        }
        return results;
    }

    public boolean containsValidAppIdFields() {
        return false;
    }

    public void unmanageSCOFields() {
    }

    public boolean checkKeyFields(OID oid) {
        return true;
    }

    public boolean containsKey(int field, Object key) {
        FieldMetaData fmd = getCMD().stateFields[field];
        if (fmd.keyTypeMetaData != null) {
            Map map = ((Map) getInternalObjectField(field));
            Set keys = map.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                PersistenceCapable capable = (PersistenceCapable) iterator.next();
                if (pm.getInternalOID(capable).equals(key)) {
                    return true;
                }
            }
        } else {
            return (((Map) getInternalObjectField(field)).containsKey(key));
        }
        return false;
    }

    public void setCmd(ClassMetaData cmd) {
        this.cmd = cmd;
    }

    private ClassMetaData getCMD() {
        return cmd;
    }

    public Collection getInternalOIDCollection(int field) {
        Collection col = (Collection) getInternalObjectField(field);
        int size = col == null ? 0 : col.size();
        List results = new ArrayList(size);
        if (size == 0) {
            return results;
        }
        for (Iterator iterator = col.iterator(); iterator.hasNext(); ) {
            PersistenceCapable pc = (PersistenceCapable) iterator.next();
            results.add(pm.getInternalSM(pc).oid);
        }
        return results;
    }

    public Collection getInternalOIDValueCollectionForMap(int field) {
        Collection col = ((Map) getInternalObjectField(field)).values();
        int size = col == null ? 0 : col.size();
        List results = new ArrayList(size);
        if (size == 0) {
            return results;
        }
        for (Iterator iterator = col.iterator(); iterator.hasNext(); ) {
            PersistenceCapable pc = (PersistenceCapable) iterator.next();
            results.add(pm.getInternalSM(pc).oid);
        }
        return results;
    }

    public Collection getInternalValueCollectionForMap(int field) {
        Map m = (Map) getInternalObjectField(field);
        return m.values();
    }

    public OID getOID(int field) {
        Object obj = getInternalObjectField(field);
        if (obj instanceof PersistenceCapable) {
            try {
                obj = pm.getInternalOID((PersistenceCapable) obj);
            } catch (Exception e) {
                obj = null;
            }
        }
        return (OID) obj;
    }

    public QueryStateWrapper getState(int field) {
        QueryStateWrapper state = pm.getInternalSM((PersistenceCapable) getInternalObjectField(field)).queryStateWrapper;
        return state;
    }

    public boolean getBooleanField(int field) {
        return pcStateMan.getBooleanField(null, cmd.stateFields[field].managedFieldNo, false);
    }

    public char getCharField(int field) {
        return pcStateMan.getCharField(null, cmd.stateFields[field].managedFieldNo, (char) 0);
    }

    public byte getByteField(int field) {
        return pcStateMan.getByteField(null, cmd.stateFields[field].managedFieldNo, (byte) 0);
    }

    public short getShortField(int field) {
        return pcStateMan.getShortField(null, cmd.stateFields[field].managedFieldNo, (short) 0);
    }

    public int getIntField(int field) {
        return pcStateMan.getIntField(null, cmd.stateFields[field].managedFieldNo, 0);
    }

    public long getLongField(int field) {
        return pcStateMan.getLongField(null, cmd.stateFields[field].managedFieldNo, 0);
    }

    public long getLongFieldInternal(int field) {
        return getLongField(field);
    }

    public float getFloatField(int field) {
        return pcStateMan.getFloatField(null, cmd.stateFields[field].managedFieldNo, 0);
    }

    public double getDoubleField(int field) {
        return pcStateMan.getDoubleField(null, cmd.stateFields[field].managedFieldNo, 0);
    }

    public String getStringField(int field) {
        return pcStateMan.getStringField(null, cmd.stateFields[field].managedFieldNo, null);
    }

    public void writeExternal(ObjectOutput out) {
        throw createBadMethodException();
    }

    private RuntimeException createBadMethodException() {
        return BindingSupportImpl.getInstance().internal("Should not be called");
    }

    public void readExternal(ObjectInput in) {
        throw createBadMethodException();
    }

    public boolean isHollow() {
        throw createBadMethodException();
    }

    public State newInstance() {
        throw createBadMethodException();
    }

    public int getClassIndex() {
        throw createBadMethodException();
    }

    public ClassMetaData getClassMetaData(ModelMetaData jmd) {
        throw createBadMethodException();
    }

    public boolean isDirty() {
        throw createBadMethodException();
    }

    public boolean isDirty(int fieldNo) {
        throw createBadMethodException();
    }

    public void makeDirtyAbs(int fieldNo) {
        throw createBadMethodException();
    }

    public void clearDirtyFields() {
        throw createBadMethodException();
    }

    public void updateFrom(State state) {
        throw createBadMethodException();
    }

    public void updateNonFilled(State state) {
        throw createBadMethodException();
    }

    public int[] updateChanged(State state) {
        throw createBadMethodException();
    }

    public void clearNonAutoSetFields() {
        throw createBadMethodException();
    }

    public void retrieve(VersantPersistenceManagerImp sm) {
        throw createBadMethodException();
    }

    public void clearNonFilled(State state) {
        throw createBadMethodException();
    }

    public void clearCollectionFields() {
        throw createBadMethodException();
    }

    public void clearSCOFields() {
        throw createBadMethodException();
    }

    public void clearTransactionNonPersistentFields() {
        throw createBadMethodException();
    }

    public boolean fillToStoreState(State stateToStore, PersistenceContext sm, VersantStateManager pcStateMan) {
        throw createBadMethodException();
    }

    public State getCopy() {
        throw createBadMethodException();
    }

    public void copyFieldsForOptimisticLocking(State state, VersantPersistenceManagerImp sm) {
        throw createBadMethodException();
    }

    public void copyOptimisticLockingField(State state) {
        throw createBadMethodException();
    }

    public int replaceSCOFields(PersistenceCapable owner, VersantPersistenceManagerImp sm, int[] absFieldNos) {
        throw createBadMethodException();
    }

    public void addRefs(VersantPersistenceManagerImp sm, PCStateMan pcStateMan) {
        throw createBadMethodException();
    }

    public void clear() {
        throw createBadMethodException();
    }

    public void makeClean() {
        throw createBadMethodException();
    }

    public void setClassMetaData(ClassMetaData cmd) {
        throw createBadMethodException();
    }

    public ClassMetaData getClassMetaData() {
        throw createBadMethodException();
    }

    public boolean containsField(int stateFieldNo) {
        throw createBadMethodException();
    }

    public boolean containsFieldAbs(int absFieldNo) {
        throw createBadMethodException();
    }

    public boolean containFields(int[] stateFieldNos) {
        throw createBadMethodException();
    }

    public boolean containFieldsAbs(int[] absFieldNos) {
        throw createBadMethodException();
    }

    public boolean isEmpty() {
        throw createBadMethodException();
    }

    public boolean containsFetchGroup(FetchGroup fetchGroup) {
        throw createBadMethodException();
    }

    public int getFieldNos(int[] buf) {
        throw createBadMethodException();
    }

    public int getPass1FieldNos(int[] buf) {
        throw createBadMethodException();
    }

    public int getPass2FieldNos(int[] buf) {
        throw createBadMethodException();
    }

    public boolean containsPass1Fields() {
        throw createBadMethodException();
    }

    public boolean containsPass2Fields() {
        throw createBadMethodException();
    }

    public int compareToPass1(State state) {
        throw createBadMethodException();
    }

    public boolean hasSameFields(State state) {
        throw createBadMethodException();
    }

    public boolean hasSameNullFields(State state, State mask) {
        throw createBadMethodException();
    }

    public boolean isNull(int stateFieldNo) {
        throw createBadMethodException();
    }

    public boolean containsApplicationIdentityFields() {
        throw createBadMethodException();
    }

    public void clearApplicationIdentityFields() {
        throw createBadMethodException();
    }

    public void copyFields(OID oid) {
        throw createBadMethodException();
    }

    public boolean replaceNewObjectOIDs(int[] fieldNos, int fieldNosLength) {
        throw createBadMethodException();
    }

    public void findDirectEdges(OIDGraph graph, IntArray edges) {
        throw createBadMethodException();
    }

    public void updateAutoSetFieldsCreated(Date now) {
        throw createBadMethodException();
    }

    public void updateAutoSetFieldsModified(Date now, State oldState) {
        throw createBadMethodException();
    }

    public void copyKeyFields(OID oid) {
        throw createBadMethodException();
    }

    public void copyKeyFieldsUpdate(OID oid) {
        throw createBadMethodException();
    }

    public boolean getBooleanFieldAbs(int absFieldNo) {
        throw createBadMethodException();
    }

    public char getCharFieldAbs(int field) {
        throw createBadMethodException();
    }

    public byte getByteFieldAbs(int field) {
        throw createBadMethodException();
    }

    public short getShortFieldAbs(int field) {
        throw createBadMethodException();
    }

    public int getIntFieldAbs(int field) {
        throw createBadMethodException();
    }

    public long getLongFieldAbs(int field) {
        throw createBadMethodException();
    }

    public float getFloatFieldAbs(int field) {
        throw createBadMethodException();
    }

    public double getDoubleFieldAbs(int field) {
        throw createBadMethodException();
    }

    public String getStringFieldAbs(int field) {
        throw createBadMethodException();
    }

    public Object getObjectField(int field, PersistenceCapable owningPC, PersistenceContext sm, OID oid) {
        throw createBadMethodException();
    }

    public Object getObjectFieldAbs(int field, PersistenceCapable owningPC, PersistenceContext sm, OID oid) {
        throw createBadMethodException();
    }

    public void setBooleanField(int field, boolean newValue) {
        throw createBadMethodException();
    }

    public void setBooleanFieldAbs(int field, boolean newValue) {
        throw createBadMethodException();
    }

    public void setCharField(int field, char newValue) {
        throw createBadMethodException();
    }

    public void setCharFieldAbs(int field, char newValue) {
        throw createBadMethodException();
    }

    public void setByteField(int field, byte newValue) {
        throw createBadMethodException();
    }

    public void setByteFieldAbs(int field, byte newValue) {
        throw createBadMethodException();
    }

    public void setShortField(int field, short newValue) {
        throw createBadMethodException();
    }

    public void setShortFieldAbs(int field, short newValue) {
        throw createBadMethodException();
    }

    public void setIntField(int field, int newValue) {
        throw createBadMethodException();
    }

    public void setIntFieldAbs(int field, int newValue) {
        throw createBadMethodException();
    }

    public void setLongField(int field, long newValue) {
        throw createBadMethodException();
    }

    public void setLongFieldAbs(int field, long newValue) {
        throw createBadMethodException();
    }

    public void setFloatField(int field, float newValue) {
        throw createBadMethodException();
    }

    public void setFloatFieldAbs(int field, float newValue) {
        throw createBadMethodException();
    }

    public void setDoubleField(int field, double newValue) {
        throw createBadMethodException();
    }

    public void setDoubleFieldAbs(int field, double newValue) {
        throw createBadMethodException();
    }

    public void setStringField(int field, String newValue) {
        throw createBadMethodException();
    }

    public void setStringFieldAbs(int field, String newValue) {
        throw createBadMethodException();
    }

    public void setObjectField(int field, Object newValue) {
        throw createBadMethodException();
    }

    public void setObjectFieldAbs(int field, Object newValue) {
        throw createBadMethodException();
    }

    public void setObjectFieldUnresolved(int field, Object newValue) {
        throw createBadMethodException();
    }

    public void setObjectFieldUnresolvedAbs(int field, Object newValue) {
        throw createBadMethodException();
    }

    public Object getInternalObjectFieldAbs(int field) {
        throw createBadMethodException();
    }

    public void setInternalBooleanField(int field, boolean newValue) {
        throw createBadMethodException();
    }

    public void setInternalBooleanFieldAbs(int field, boolean newValue) {
        throw createBadMethodException();
    }

    public void setInternalCharField(int field, char newValue) {
        throw createBadMethodException();
    }

    public void setInternalCharFieldAbs(int field, char newValue) {
        throw createBadMethodException();
    }

    public void setInternalByteField(int field, byte newValue) {
        throw createBadMethodException();
    }

    public void setInternalByteFieldAbs(int field, byte newValue) {
        throw createBadMethodException();
    }

    public void setInternalShortField(int field, short newValue) {
        throw createBadMethodException();
    }

    public void setInternalShortFieldAbs(int field, short newValue) {
        throw createBadMethodException();
    }

    public void setInternalIntField(int field, int newValue) {
        throw createBadMethodException();
    }

    public void setInternalIntFieldAbs(int field, int newValue) {
        throw createBadMethodException();
    }

    public void setInternalLongField(int field, long newValue) {
        throw createBadMethodException();
    }

    public void setInternalLongFieldAbs(int field, long newValue) {
        throw createBadMethodException();
    }

    public void setInternalFloatField(int field, float newValue) {
        throw createBadMethodException();
    }

    public void setInternalFloatFieldAbs(int field, float newValue) {
        throw createBadMethodException();
    }

    public void setInternalDoubleField(int field, double newValue) {
        throw createBadMethodException();
    }

    public void setInternalDoubleFieldAbs(int field, double newValue) {
        throw createBadMethodException();
    }

    public void setInternalStringField(int field, String newValue) {
        throw createBadMethodException();
    }

    public void setInternalStringFieldAbs(int field, String newValue) {
        throw createBadMethodException();
    }

    public void setInternalObjectField(int field, Object newValue) {
        throw createBadMethodException();
    }

    public void setInternalObjectFieldAbs(int field, Object newValue) {
        throw createBadMethodException();
    }

    public String getVersion() {
        return Debug.VERSION;
    }

    /**
     * Is this state field nummber resolved for the Client
     */
    public boolean isResolvedForClient(int stateFieldNo) {
        throw createBadMethodException();
    }

    public Object getOptimisticLockingValue() {
        throw createBadMethodException();
    }

    public void setFilled(int field) {
        throw createBadMethodException();
    }

    public void fillForRead(State dest, VersantPersistenceManagerImp sm) {
        throw createBadMethodException();
    }

    public void clearFilledFlags() {
        throw createBadMethodException();
    }

    public int getPass1FieldRefFieldNosWithNewOids(int[] stateFieldNoBuf) {
        throw createBadMethodException();
    }

    public void deleteSecondaryFields(ArrayList arraylist) {
        throw createBadMethodException();
    }

    public void addOneToManyInverseFieldsForL2Evict(VersantPersistenceManagerImp pm) {
        throw createBadMethodException();
    }

    public void writeExternal(OIDObjectOutput os) throws IOException {
        throw createBadMethodException();
    }

    public void readExternal(OIDObjectInput is) throws IOException, ClassNotFoundException {
        throw createBadMethodException();
    }
}

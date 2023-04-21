package org.dmd.dmp.shared.generated.types;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.enums.ValueTypeEnum;
import org.dmd.dmp.shared.generated.dmo.DenotifyRequestDMO;

/**
 * The DmcTypeDenotifyRequestREFSET provides storage for a set of DenotifyRequestDMO
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNormalREFType(DmoTypeFormatter.java:256)
 */
@SuppressWarnings("serial")
public class DmcTypeDenotifyRequestREFSET extends DmcTypeDenotifyRequestREF implements Serializable {

    protected Set<DenotifyRequestDMO> value;

    public DmcTypeDenotifyRequestREFSET() {
        value = null;
    }

    public DmcTypeDenotifyRequestREFSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<DenotifyRequestDMO>(); else value = new TreeSet<DenotifyRequestDMO>();
    }

    @Override
    public DmcTypeDenotifyRequestREFSET getNew() {
        return (new DmcTypeDenotifyRequestREFSET(attrInfo));
    }

    @Override
    public DmcAttribute<DenotifyRequestDMO> cloneIt() {
        synchronized (this) {
            DmcTypeDenotifyRequestREFSET rc = getNew();
            for (DenotifyRequestDMO val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public DenotifyRequestDMO add(Object v) throws DmcValueException {
        synchronized (this) {
            DenotifyRequestDMO rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public DenotifyRequestDMO del(Object v) {
        synchronized (this) {
            DenotifyRequestDMO rc = null;
            if (value == null) return (rc);
            try {
                rc = typeCheck(v);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("Incompatible type passed to del():" + getName(), e));
            }
            if (value.contains(rc)) {
                value.remove(rc);
                if (value.size() == 0) value = null;
            } else rc = null;
            return (rc);
        }
    }

    @Override
    public Iterator<DenotifyRequestDMO> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<DenotifyRequestDMO>(value)).iterator()); else return ((new TreeSet<DenotifyRequestDMO>(value)).iterator());
        }
    }

    public Set<DenotifyRequestDMO> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<DenotifyRequestDMO>(value)); else return (new TreeSet<DenotifyRequestDMO>(value));
        }
    }

    @Override
    public int getMVSize() {
        synchronized (this) {
            if (value == null) return (0);
            return (value.size());
        }
    }

    @Override
    public boolean contains(Object v) {
        synchronized (this) {
            if (value == null) return (false);
            try {
                DenotifyRequestDMO val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

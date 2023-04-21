package org.dmd.dms.generated.types;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.enums.ValueTypeEnum;
import org.dmd.dmc.DmcObject;
import org.dmd.dmc.types.DmcTypeDmcObject;

/**
 * The DmcTypeDmcObjectSET provides storage for a set of DmcObject
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:272)
 */
@SuppressWarnings("serial")
public class DmcTypeDmcObjectSET extends DmcTypeDmcObject implements Serializable {

    protected Set<DmcObject> value;

    public DmcTypeDmcObjectSET() {
        value = null;
    }

    public DmcTypeDmcObjectSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<DmcObject>(); else value = new TreeSet<DmcObject>();
    }

    @Override
    public DmcTypeDmcObjectSET getNew() {
        return (new DmcTypeDmcObjectSET(attrInfo));
    }

    @Override
    public DmcAttribute<DmcObject> cloneIt() {
        synchronized (this) {
            DmcTypeDmcObjectSET rc = getNew();
            for (DmcObject val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public DmcObject add(Object v) throws DmcValueException {
        synchronized (this) {
            DmcObject rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public DmcObject del(Object v) {
        synchronized (this) {
            DmcObject rc = null;
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
    public Iterator<DmcObject> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<DmcObject>(value)).iterator()); else return ((new TreeSet<DmcObject>(value)).iterator());
        }
    }

    public Set<DmcObject> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<DmcObject>(value)); else return (new TreeSet<DmcObject>(value));
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
                DmcObject val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

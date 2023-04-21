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
import org.dmd.dmp.shared.generated.enums.DMPEventTypeEnum;

/**
 * The DmcTypeDMPEventTypeEnumSET provides storage for a set of DMPEventTypeEnum
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpEnumType(DmoTypeFormatter.java:362)
 */
@SuppressWarnings("serial")
public class DmcTypeDMPEventTypeEnumSET extends DmcTypeDMPEventTypeEnum implements Serializable {

    protected Set<DMPEventTypeEnum> value;

    public DmcTypeDMPEventTypeEnumSET() {
        value = null;
    }

    public DmcTypeDMPEventTypeEnumSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<DMPEventTypeEnum>(); else value = new TreeSet<DMPEventTypeEnum>();
    }

    @Override
    public DmcTypeDMPEventTypeEnumSET getNew() {
        return (new DmcTypeDMPEventTypeEnumSET(attrInfo));
    }

    @Override
    public DmcAttribute<DMPEventTypeEnum> cloneIt() {
        synchronized (this) {
            DmcTypeDMPEventTypeEnumSET rc = getNew();
            for (DMPEventTypeEnum val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public DMPEventTypeEnum add(Object v) throws DmcValueException {
        synchronized (this) {
            DMPEventTypeEnum rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public DMPEventTypeEnum del(Object v) {
        synchronized (this) {
            DMPEventTypeEnum rc = null;
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
    public Iterator<DMPEventTypeEnum> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<DMPEventTypeEnum>(value)).iterator()); else return ((new TreeSet<DMPEventTypeEnum>(value)).iterator());
        }
    }

    public Set<DMPEventTypeEnum> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<DMPEventTypeEnum>(value)); else return (new TreeSet<DMPEventTypeEnum>(value));
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
                DMPEventTypeEnum val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

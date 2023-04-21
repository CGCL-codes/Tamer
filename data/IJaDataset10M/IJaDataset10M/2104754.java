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

/**
 * The DmcTypeValueTypeEnumSET provides storage for a set of ValueTypeEnum
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:234)
 */
@SuppressWarnings("serial")
public class DmcTypeValueTypeEnumSET extends DmcTypeValueTypeEnum implements Serializable {

    protected Set<ValueTypeEnum> value;

    public DmcTypeValueTypeEnumSET() {
        value = null;
    }

    public DmcTypeValueTypeEnumSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<ValueTypeEnum>(); else value = new TreeSet<ValueTypeEnum>();
    }

    @Override
    public DmcTypeValueTypeEnumSET getNew() {
        return (new DmcTypeValueTypeEnumSET(attrInfo));
    }

    @Override
    public DmcAttribute<ValueTypeEnum> cloneIt() {
        synchronized (this) {
            DmcTypeValueTypeEnumSET rc = getNew();
            for (ValueTypeEnum val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public ValueTypeEnum add(Object v) throws DmcValueException {
        synchronized (this) {
            ValueTypeEnum rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public ValueTypeEnum del(Object v) {
        synchronized (this) {
            ValueTypeEnum rc = null;
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
    public Iterator<ValueTypeEnum> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<ValueTypeEnum>(value)).iterator()); else return ((new TreeSet<ValueTypeEnum>(value)).iterator());
        }
    }

    public Set<ValueTypeEnum> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<ValueTypeEnum>(value)); else return (new TreeSet<ValueTypeEnum>(value));
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
                ValueTypeEnum val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

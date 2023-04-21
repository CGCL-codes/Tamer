package org.dmd.dmp.server.servlet.generated.types;

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
 * The DmcTypeUserRIREFSET provides storage for a set of UserRIREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:532)
 */
@SuppressWarnings("serial")
public class DmcTypeUserRIREFSET extends DmcTypeUserRIREF implements Serializable {

    protected Set<UserRIREF> value;

    public DmcTypeUserRIREFSET() {
        value = null;
    }

    public DmcTypeUserRIREFSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<UserRIREF>(); else value = new TreeSet<UserRIREF>();
    }

    @Override
    public DmcTypeUserRIREFSET getNew() {
        return (new DmcTypeUserRIREFSET(attrInfo));
    }

    @Override
    public DmcAttribute<UserRIREF> cloneIt() {
        synchronized (this) {
            DmcTypeUserRIREFSET rc = getNew();
            for (UserRIREF val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public UserRIREF add(Object v) throws DmcValueException {
        synchronized (this) {
            UserRIREF rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public UserRIREF del(Object v) {
        synchronized (this) {
            UserRIREF rc = null;
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
    public Iterator<UserRIREF> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<UserRIREF>(value)).iterator()); else return ((new TreeSet<UserRIREF>(value)).iterator());
        }
    }

    public Set<UserRIREF> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<UserRIREF>(value)); else return (new TreeSet<UserRIREF>(value));
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
                UserRIREF val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

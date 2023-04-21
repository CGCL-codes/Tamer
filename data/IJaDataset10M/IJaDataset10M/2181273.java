package org.dmd.mvw.tools.mvwgenerator.generated.types;

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
 * The DmcTypeActionBindingREFSET provides storage for a set of ActionBindingREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSETType(GenUtility.java:2460)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:532)
 */
@SuppressWarnings("serial")
public class DmcTypeActionBindingREFSET extends DmcTypeActionBindingREF implements Serializable {

    protected Set<ActionBindingREF> value;

    public DmcTypeActionBindingREFSET() {
        value = null;
    }

    public DmcTypeActionBindingREFSET(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHSET) value = new HashSet<ActionBindingREF>(); else value = new TreeSet<ActionBindingREF>();
    }

    @Override
    public DmcTypeActionBindingREFSET getNew() {
        return (new DmcTypeActionBindingREFSET(attrInfo));
    }

    @Override
    public DmcAttribute<ActionBindingREF> cloneIt() {
        synchronized (this) {
            DmcTypeActionBindingREFSET rc = getNew();
            for (ActionBindingREF val : value) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public ActionBindingREF add(Object v) throws DmcValueException {
        synchronized (this) {
            ActionBindingREF rc = typeCheck(v);
            if (value == null) initValue();
            if (!value.add(rc)) rc = null;
            return (rc);
        }
    }

    @Override
    public ActionBindingREF del(Object v) {
        synchronized (this) {
            ActionBindingREF rc = null;
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
    public Iterator<ActionBindingREF> getMV() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return ((new HashSet<ActionBindingREF>(value)).iterator()); else return ((new TreeSet<ActionBindingREF>(value)).iterator());
        }
    }

    public Set<ActionBindingREF> getMVCopy() {
        synchronized (this) {
            if (attrInfo.valueType == ValueTypeEnum.HASHSET) return (new HashSet<ActionBindingREF>(value)); else return (new TreeSet<ActionBindingREF>(value));
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
                ActionBindingREF val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

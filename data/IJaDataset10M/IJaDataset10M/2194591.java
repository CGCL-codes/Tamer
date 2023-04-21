package org.dmd.dms.generated.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcObject;
import org.dmd.dmc.types.DmcTypeDmcObject;

/**
 * The DmcTypeDmcObjectMV provides storage for a multi-valued DmcObject
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMVType(GenUtility.java:2177)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:271)
 */
@SuppressWarnings("serial")
public class DmcTypeDmcObjectMV extends DmcTypeDmcObject implements Serializable {

    protected ArrayList<DmcObject> value;

    public DmcTypeDmcObjectMV() {
    }

    public DmcTypeDmcObjectMV(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public DmcTypeDmcObjectMV getNew() {
        return (new DmcTypeDmcObjectMV(attrInfo));
    }

    @Override
    public DmcAttribute<DmcObject> cloneIt() {
        synchronized (this) {
            DmcTypeDmcObjectMV rc = getNew();
            if (attrInfo.indexSize == 0) {
                for (DmcObject val : value) try {
                    rc.add(val);
                } catch (DmcValueException e) {
                    throw (new IllegalStateException("typeCheck() should never fail here!", e));
                }
            } else {
                for (int index = 0; index < value.size(); index++) try {
                    rc.setMVnth(index, value.get(index));
                } catch (DmcValueException e) {
                    throw (new IllegalStateException("typeCheck() should never fail here!", e));
                }
            }
            return (rc);
        }
    }

    @Override
    public DmcObject add(Object v) throws DmcValueException {
        synchronized (this) {
            DmcObject rc = typeCheck(v);
            if (value == null) value = new ArrayList<DmcObject>();
            value.add(rc);
            return (rc);
        }
    }

    @Override
    public DmcObject del(Object v) {
        synchronized (this) {
            DmcObject key = null;
            DmcObject rc = null;
            try {
                key = typeCheck(v);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("Incompatible type passed to del():" + getName(), e));
            }
            int indexof = value.indexOf(key);
            if (indexof != -1) {
                rc = value.get(indexof);
                value.remove(rc);
            }
            return (rc);
        }
    }

    @Override
    public Iterator<DmcObject> getMV() {
        synchronized (this) {
            ArrayList<DmcObject> clone = new ArrayList<DmcObject>(value);
            return (clone.iterator());
        }
    }

    public ArrayList<DmcObject> getMVCopy() {
        synchronized (this) {
            ArrayList<DmcObject> clone = new ArrayList<DmcObject>(value);
            return (clone);
        }
    }

    @Override
    public int getMVSize() {
        synchronized (this) {
            if (attrInfo.indexSize == 0) {
                if (value == null) return (0);
                return (value.size());
            } else return (attrInfo.indexSize);
        }
    }

    @Override
    public DmcObject getMVnth(int index) {
        synchronized (this) {
            if (value == null) return (null);
            return (value.get(index));
        }
    }

    @Override
    public DmcObject setMVnth(int index, Object v) throws DmcValueException {
        synchronized (this) {
            if (attrInfo.indexSize == 0) throw (new IllegalStateException("Attribute: " + attrInfo.name + " is not indexed. You can't use setMVnth()."));
            if ((index < 0) || (index >= attrInfo.indexSize)) throw (new IllegalStateException("Index " + index + " for attribute: " + attrInfo.name + " is out of range: 0 <= index < " + attrInfo.indexSize));
            DmcObject rc = null;
            if (v != null) rc = typeCheck(v);
            if (value == null) {
                value = new ArrayList<DmcObject>(attrInfo.indexSize);
                for (int i = 0; i < attrInfo.indexSize; i++) value.add(null);
            }
            value.set(index, rc);
            return (rc);
        }
    }

    @Override
    public boolean hasValue() {
        synchronized (this) {
            boolean rc = false;
            if (attrInfo.indexSize == 0) throw (new IllegalStateException("Attribute: " + attrInfo.name + " is not indexed. You can't use hasValue()."));
            if (value == null) return (rc);
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i) != null) {
                    rc = true;
                    break;
                }
            }
            return (rc);
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

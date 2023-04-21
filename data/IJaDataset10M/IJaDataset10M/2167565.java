package org.dmd.features.extgwt.generated.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeMvcMenuItemREFMV provides storage for a multi-valued MvcMenuItemREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMVType(GenUtility.java:2153)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:523)
 */
@SuppressWarnings("serial")
public class DmcTypeMvcMenuItemREFMV extends DmcTypeMvcMenuItemREF implements Serializable {

    protected ArrayList<MvcMenuItemREF> value;

    public DmcTypeMvcMenuItemREFMV() {
    }

    public DmcTypeMvcMenuItemREFMV(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public DmcTypeMvcMenuItemREFMV getNew() {
        return (new DmcTypeMvcMenuItemREFMV(attrInfo));
    }

    @Override
    public DmcAttribute<MvcMenuItemREF> cloneIt() {
        synchronized (this) {
            DmcTypeMvcMenuItemREFMV rc = getNew();
            if (attrInfo.indexSize == 0) {
                for (MvcMenuItemREF val : value) try {
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
    public MvcMenuItemREF add(Object v) throws DmcValueException {
        synchronized (this) {
            MvcMenuItemREF rc = typeCheck(v);
            if (value == null) value = new ArrayList<MvcMenuItemREF>();
            value.add(rc);
            return (rc);
        }
    }

    @Override
    public MvcMenuItemREF del(Object v) {
        synchronized (this) {
            MvcMenuItemREF key = null;
            MvcMenuItemREF rc = null;
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
    public Iterator<MvcMenuItemREF> getMV() {
        synchronized (this) {
            ArrayList<MvcMenuItemREF> clone = new ArrayList<MvcMenuItemREF>(value);
            return (clone.iterator());
        }
    }

    public ArrayList<MvcMenuItemREF> getMVCopy() {
        synchronized (this) {
            ArrayList<MvcMenuItemREF> clone = new ArrayList<MvcMenuItemREF>(value);
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
    public MvcMenuItemREF getMVnth(int index) {
        synchronized (this) {
            if (value == null) return (null);
            return (value.get(index));
        }
    }

    @Override
    public MvcMenuItemREF setMVnth(int index, Object v) throws DmcValueException {
        synchronized (this) {
            if (attrInfo.indexSize == 0) throw (new IllegalStateException("Attribute: " + attrInfo.name + " is not indexed. You can't use setMVnth()."));
            if ((index < 0) || (index >= attrInfo.indexSize)) throw (new IllegalStateException("Index " + index + " for attribute: " + attrInfo.name + " is out of range: 0 <= index < " + attrInfo.indexSize));
            MvcMenuItemREF rc = null;
            if (v != null) rc = typeCheck(v);
            if (value == null) {
                value = new ArrayList<MvcMenuItemREF>(attrInfo.indexSize);
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
            boolean rc = false;
            try {
                MvcMenuItemREF val = typeCheck(v);
                rc = value.contains(val);
            } catch (DmcValueException e) {
            }
            return (rc);
        }
    }
}

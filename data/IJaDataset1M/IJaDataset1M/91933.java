package org.dmd.features.extgwt.generated.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeMvcPopupMenuREFMV provides storage for a multi-valued MvcPopupMenuREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMVType(GenUtility.java:2153)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:523)
 */
@SuppressWarnings("serial")
public class DmcTypeMvcPopupMenuREFMV extends DmcTypeMvcPopupMenuREF implements Serializable {

    protected ArrayList<MvcPopupMenuREF> value;

    public DmcTypeMvcPopupMenuREFMV() {
    }

    public DmcTypeMvcPopupMenuREFMV(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public DmcTypeMvcPopupMenuREFMV getNew() {
        return (new DmcTypeMvcPopupMenuREFMV(attrInfo));
    }

    @Override
    public DmcAttribute<MvcPopupMenuREF> cloneIt() {
        synchronized (this) {
            DmcTypeMvcPopupMenuREFMV rc = getNew();
            if (attrInfo.indexSize == 0) {
                for (MvcPopupMenuREF val : value) try {
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
    public MvcPopupMenuREF add(Object v) throws DmcValueException {
        synchronized (this) {
            MvcPopupMenuREF rc = typeCheck(v);
            if (value == null) value = new ArrayList<MvcPopupMenuREF>();
            value.add(rc);
            return (rc);
        }
    }

    @Override
    public MvcPopupMenuREF del(Object v) {
        synchronized (this) {
            MvcPopupMenuREF key = null;
            MvcPopupMenuREF rc = null;
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
    public Iterator<MvcPopupMenuREF> getMV() {
        synchronized (this) {
            ArrayList<MvcPopupMenuREF> clone = new ArrayList<MvcPopupMenuREF>(value);
            return (clone.iterator());
        }
    }

    public ArrayList<MvcPopupMenuREF> getMVCopy() {
        synchronized (this) {
            ArrayList<MvcPopupMenuREF> clone = new ArrayList<MvcPopupMenuREF>(value);
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
    public MvcPopupMenuREF getMVnth(int index) {
        synchronized (this) {
            if (value == null) return (null);
            return (value.get(index));
        }
    }

    @Override
    public MvcPopupMenuREF setMVnth(int index, Object v) throws DmcValueException {
        synchronized (this) {
            if (attrInfo.indexSize == 0) throw (new IllegalStateException("Attribute: " + attrInfo.name + " is not indexed. You can't use setMVnth()."));
            if ((index < 0) || (index >= attrInfo.indexSize)) throw (new IllegalStateException("Index " + index + " for attribute: " + attrInfo.name + " is out of range: 0 <= index < " + attrInfo.indexSize));
            MvcPopupMenuREF rc = null;
            if (v != null) rc = typeCheck(v);
            if (value == null) {
                value = new ArrayList<MvcPopupMenuREF>(attrInfo.indexSize);
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
                MvcPopupMenuREF val = typeCheck(v);
                rc = value.contains(val);
            } catch (DmcValueException e) {
            }
            return (rc);
        }
    }
}

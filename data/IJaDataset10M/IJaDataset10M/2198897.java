package org.dmd.dmp.shared.generated.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmp.shared.generated.enums.ResponseCategoryEnum;

/**
 * The DmcTypeResponseCategoryEnumMV provides storage for a multi-valued ResponseCategoryEnum
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMVType(GenUtility.java:2177)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpEnumType(DmoTypeFormatter.java:361)
 */
@SuppressWarnings("serial")
public class DmcTypeResponseCategoryEnumMV extends DmcTypeResponseCategoryEnum implements Serializable {

    protected ArrayList<ResponseCategoryEnum> value;

    public DmcTypeResponseCategoryEnumMV() {
    }

    public DmcTypeResponseCategoryEnumMV(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public DmcTypeResponseCategoryEnumMV getNew() {
        return (new DmcTypeResponseCategoryEnumMV(attrInfo));
    }

    @Override
    public DmcAttribute<ResponseCategoryEnum> cloneIt() {
        synchronized (this) {
            DmcTypeResponseCategoryEnumMV rc = getNew();
            if (attrInfo.indexSize == 0) {
                for (ResponseCategoryEnum val : value) try {
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
    public ResponseCategoryEnum add(Object v) throws DmcValueException {
        synchronized (this) {
            ResponseCategoryEnum rc = typeCheck(v);
            if (value == null) value = new ArrayList<ResponseCategoryEnum>();
            value.add(rc);
            return (rc);
        }
    }

    @Override
    public ResponseCategoryEnum del(Object v) {
        synchronized (this) {
            ResponseCategoryEnum key = null;
            ResponseCategoryEnum rc = null;
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
    public Iterator<ResponseCategoryEnum> getMV() {
        synchronized (this) {
            ArrayList<ResponseCategoryEnum> clone = new ArrayList<ResponseCategoryEnum>(value);
            return (clone.iterator());
        }
    }

    public ArrayList<ResponseCategoryEnum> getMVCopy() {
        synchronized (this) {
            ArrayList<ResponseCategoryEnum> clone = new ArrayList<ResponseCategoryEnum>(value);
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
    public ResponseCategoryEnum getMVnth(int index) {
        synchronized (this) {
            if (value == null) return (null);
            return (value.get(index));
        }
    }

    @Override
    public ResponseCategoryEnum setMVnth(int index, Object v) throws DmcValueException {
        synchronized (this) {
            if (attrInfo.indexSize == 0) throw (new IllegalStateException("Attribute: " + attrInfo.name + " is not indexed. You can't use setMVnth()."));
            if ((index < 0) || (index >= attrInfo.indexSize)) throw (new IllegalStateException("Index " + index + " for attribute: " + attrInfo.name + " is out of range: 0 <= index < " + attrInfo.indexSize));
            ResponseCategoryEnum rc = null;
            if (v != null) rc = typeCheck(v);
            if (value == null) {
                value = new ArrayList<ResponseCategoryEnum>(attrInfo.indexSize);
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
                ResponseCategoryEnum val = typeCheck(v);
                return (value.contains(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }
}

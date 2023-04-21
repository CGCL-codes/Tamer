package org.dmd.features.extgwt.generated.types;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcMappedAttributeIF;
import org.dmd.dms.generated.enums.ValueTypeEnum;
import org.dmd.dmc.types.StringName;

/**
 * The DmcTypeMvcMenuItemREFMAP provides storage for a map of MvcMenuItemREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMAPType(GenUtility.java:2627)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:532)
 */
@SuppressWarnings("serial")
public class DmcTypeMvcMenuItemREFMAP extends DmcTypeMvcMenuItemREF implements Serializable {

    protected Map<StringName, MvcMenuItemREF> value;

    public DmcTypeMvcMenuItemREFMAP() {
        value = null;
    }

    public DmcTypeMvcMenuItemREFMAP(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) value = new HashMap<StringName, MvcMenuItemREF>(); else value = new TreeMap<StringName, MvcMenuItemREF>();
    }

    @Override
    public DmcTypeMvcMenuItemREFMAP getNew() {
        return (new DmcTypeMvcMenuItemREFMAP(attrInfo));
    }

    @Override
    public DmcAttribute<MvcMenuItemREF> cloneIt() {
        synchronized (this) {
            DmcTypeMvcMenuItemREFMAP rc = getNew();
            for (MvcMenuItemREF val : value.values()) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public MvcMenuItemREF add(Object v) throws DmcValueException {
        synchronized (this) {
            MvcMenuItemREF newval = typeCheck(v);
            if (value == null) initValue();
            StringName key = (StringName) ((DmcMappedAttributeIF) newval).getKey();
            MvcMenuItemREF oldval = value.put(key, newval);
            if (oldval != null) {
                if (oldval.valuesAreEqual(newval)) newval = null;
            }
            return (newval);
        }
    }

    @Override
    public MvcMenuItemREF del(Object key) {
        synchronized (this) {
            if (key instanceof StringName) return (value.remove(key)); else throw (new IllegalStateException("Incompatible key type: " + key.getClass().getName() + " passed to del():" + getName()));
        }
    }

    @Override
    public Iterator<MvcMenuItemREF> getMV() {
        synchronized (this) {
            Map<StringName, MvcMenuItemREF> clone = null;
            if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) clone = new HashMap<StringName, MvcMenuItemREF>(value); else clone = new TreeMap<StringName, MvcMenuItemREF>(value);
            return (clone.values().iterator());
        }
    }

    public Map<StringName, MvcMenuItemREF> getMVCopy() {
        synchronized (this) {
            Map<StringName, MvcMenuItemREF> clone = null;
            if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) clone = new HashMap<StringName, MvcMenuItemREF>(value); else clone = new TreeMap<StringName, MvcMenuItemREF>(value);
            return (clone);
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
    public MvcMenuItemREF getByKey(Object key) {
        synchronized (this) {
            if (key instanceof StringName) return (value.get(key)); else throw (new IllegalStateException("Incompatible type: " + key.getClass().getName() + " passed to del():" + getName()));
        }
    }

    @Override
    public boolean contains(Object v) {
        synchronized (this) {
            boolean rc = false;
            try {
                MvcMenuItemREF val = typeCheck(v);
                rc = value.containsValue(val);
            } catch (DmcValueException e) {
            }
            return (rc);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (this) {
            boolean rc = false;
            if (key instanceof StringName) rc = value.containsKey(key);
            return (rc);
        }
    }
}

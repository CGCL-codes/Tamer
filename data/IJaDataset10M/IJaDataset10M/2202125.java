package org.dmd.mvw.tools.mvwgenerator.generated.types;

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
import org.dmd.dmc.types.CamelCaseName;

/**
 * The DmcTypeWebApplicationREFMAP provides storage for a map of WebApplicationREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpMAPType(GenUtility.java:2686)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:540)
 */
@SuppressWarnings("serial")
public class DmcTypeWebApplicationREFMAP extends DmcTypeWebApplicationREF implements Serializable {

    protected Map<CamelCaseName, WebApplicationREF> value;

    public DmcTypeWebApplicationREFMAP() {
        value = null;
    }

    public DmcTypeWebApplicationREFMAP(DmcAttributeInfo ai) {
        super(ai);
        initValue();
    }

    void initValue() {
        if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) value = new HashMap<CamelCaseName, WebApplicationREF>(); else value = new TreeMap<CamelCaseName, WebApplicationREF>();
    }

    public CamelCaseName firstKey() {
        if (attrInfo.valueType == ValueTypeEnum.TREEMAPPED) {
            if (value == null) return (null);
            TreeMap<CamelCaseName, WebApplicationREF> map = (TreeMap<CamelCaseName, WebApplicationREF>) value;
            return (map.firstKey());
        }
        throw (new IllegalStateException("Attribute " + attrInfo.name + " is HASHMAPPED and doesn't support firstKey()"));
    }

    @Override
    public DmcTypeWebApplicationREFMAP getNew() {
        return (new DmcTypeWebApplicationREFMAP(attrInfo));
    }

    @Override
    public DmcAttribute<WebApplicationREF> cloneIt() {
        synchronized (this) {
            DmcTypeWebApplicationREFMAP rc = getNew();
            for (WebApplicationREF val : value.values()) try {
                rc.add(val);
            } catch (DmcValueException e) {
                throw (new IllegalStateException("typeCheck() should never fail here!", e));
            }
            return (rc);
        }
    }

    @Override
    public WebApplicationREF add(Object v) throws DmcValueException {
        synchronized (this) {
            WebApplicationREF newval = typeCheck(v);
            if (value == null) initValue();
            CamelCaseName key = (CamelCaseName) ((DmcMappedAttributeIF) newval).getKey();
            WebApplicationREF oldval = value.put(key, newval);
            if (oldval != null) {
                if (oldval.valuesAreEqual(newval)) newval = null;
            }
            return (newval);
        }
    }

    @Override
    public WebApplicationREF del(Object key) {
        synchronized (this) {
            if (key instanceof CamelCaseName) return (value.remove(key)); else throw (new IllegalStateException("Incompatible key type: " + key.getClass().getName() + " passed to del():" + getName()));
        }
    }

    @Override
    public Iterator<WebApplicationREF> getMV() {
        synchronized (this) {
            Map<CamelCaseName, WebApplicationREF> clone = null;
            if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) clone = new HashMap<CamelCaseName, WebApplicationREF>(value); else clone = new TreeMap<CamelCaseName, WebApplicationREF>(value);
            return (clone.values().iterator());
        }
    }

    public Map<CamelCaseName, WebApplicationREF> getMVCopy() {
        synchronized (this) {
            Map<CamelCaseName, WebApplicationREF> clone = null;
            if (attrInfo.valueType == ValueTypeEnum.HASHMAPPED) clone = new HashMap<CamelCaseName, WebApplicationREF>(value); else clone = new TreeMap<CamelCaseName, WebApplicationREF>(value);
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
    public WebApplicationREF getByKey(Object key) {
        synchronized (this) {
            if (key instanceof CamelCaseName) return (value.get((CamelCaseName) key)); else throw (new IllegalStateException("Incompatible type: " + key.getClass().getName() + " passed to del():" + getName()));
        }
    }

    @Override
    public boolean contains(Object v) {
        synchronized (this) {
            try {
                WebApplicationREF val = typeCheck(v);
                return (value.containsValue(val));
            } catch (DmcValueException e) {
                return (false);
            }
        }
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (this) {
            if (key instanceof CamelCaseName) return (value.containsKey(key));
            return (false);
        }
    }
}

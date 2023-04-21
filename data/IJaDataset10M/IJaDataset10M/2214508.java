package jfun.yan.xml.nuts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jfun.util.Misc;
import jfun.yan.Component;
import jfun.yan.ComponentBinder;
import jfun.yan.Components;
import jfun.yan.Creator;
import jfun.yan.SimpleComponent;
import jfun.yan.etc.TypedBinder;
import jfun.yan.util.Utils;
import jfun.yan.xml.NutsUtils;

/**
 * Nut class for &lt;map&gt; tag.
 * <p>
 * @author Ben Yu
 * Nov 9, 2005 11:42:15 PM
 */
public class MapNut extends EntriesNut {

    private Class type = HashMap.class;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        if (!Map.class.isAssignableFrom(type)) {
            raise(Misc.getTypeName(type) + " is not a subtype of java.util.Map");
        }
        this.type = type;
    }

    public Map createMap(int sz) {
        try {
            return Utils.createMap(type, sz);
        } catch (Exception e) {
            throw raise(e);
        }
    }

    private static boolean hasComponent(List list) {
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            if (it.next() instanceof Creator) {
                return true;
            }
        }
        return false;
    }

    private static Component[] asComponents(List list) {
        final Component[] result = new Component[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = NutsUtils.asComponent(list.get(i));
        }
        return result;
    }

    public Component eval() {
        checkMandatory("map type", type);
        final List keylist = getKeys();
        final int sz = keylist.size();
        final Component[] vals = getEntryComponents();
        final Component step1 = new SimpleComponent(type) {

            public Object create() {
                return createMap(sz);
            }
        };
        if (hasComponent(keylist)) {
            final Component[] keys = asComponents(keylist);
            final Class keyType = getKey_type();
            final Component keysComponent = keyType == null ? Components.array(keys) : Components.array(keys, keyType);
            final ComponentBinder binder = new TypedBinder(type) {

                public Creator bind(Object v) {
                    return Components.storeMap(step1, (Object[]) v, vals);
                }
            };
            return keysComponent.bind(binder);
        } else {
            return Components.storeMap(step1, keylist.toArray(), vals);
        }
    }
}

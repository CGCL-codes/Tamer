package org.dcm4chee.xero.metadata.access;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.dcm4chee.xero.metadata.MetaData;
import org.dcm4chee.xero.metadata.MetaDataBean;
import org.dcm4chee.xero.metadata.MetaDataUser;

/**
 * A MapWithDefaults is a map of values, where it can ask some pre-configured meta-data about what
 * defaults it should use coming from the meta-data bean.
 * This is closely related to a lazy map that knows how to create objects from a factory if needed.
 * 
 * @author bwallace
 *
 */
@SuppressWarnings("serial")
public class MapWithDefaults extends LazyMap implements MetaDataUser {

    MetaDataBean mdb;

    boolean wasEager = false;

    public MapWithDefaults() {
    }

    public MapWithDefaults(MetaDataBean mdb) {
        super();
        this.mdb = mdb;
    }

    public MapWithDefaults(MetaDataBean mdb, Map<String, Object> lazy) {
        super(lazy);
        this.mdb = mdb;
    }

    /** Get the lazy object from the meta-data object associated with this map. */
    public Object getLazy(Object key) {
        MetaDataBean child = mdb.getChild((String) key);
        if (child == null) return super.getLazy(key);
        Object v = child.getValue();
        if (v != null) return v;
        return child;
    }

    /** Does an eager load of all values - this can be useful for configuration where runtime safety is important. */
    public void eager() {
        if (wasEager) return;
        this.addAllLazy();
        wasEager = true;
    }

    /** Adds all the lazy information to the map */
    public boolean addAllLazy() {
        if (super.addAllLazy()) return true;
        for (Map.Entry<String, MetaDataBean> me : mdb.metaDataEntrySet()) {
            this.get(me.getKey());
        }
        return false;
    }

    /** Ensure that all values are in the entry set if it is used. */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        if (!wasEager) eager();
        return super.entrySet();
    }

    /** Sets the meta data to use. */
    public void setMetaData(MetaDataBean metaDataBean) {
        this.mdb = metaDataBean;
    }

    /** Sets the lazy map */
    @MetaData(required = false)
    public void setLazy(Map<String, Object> lazy) {
        this.lazy = lazy;
    }
}

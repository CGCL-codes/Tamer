package spc.gaius.actalis.systems;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import org.apache.openjpa.abstractstore.AbstractStoreManager;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.event.OrphanedKeyAction;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.PCState;
import org.apache.openjpa.lib.rop.ListResultObjectProvider;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.OpenJPAId;
import org.apache.openjpa.util.OptimisticException;
import org.apache.openjpa.util.StoreException;
import spc.gaius.actalis.util.BiDiMap;

public abstract class AbstractSystemStoreManager extends AbstractStoreManager {

    protected String resources;

    SystemConfiguration _conf;

    private SystemStore _store;

    private Collection _updates;

    private Collection _deletes;

    private Collection _new;

    private BiDiMap _initialized;

    public AbstractSystemStoreManager() {
    }

    /**
     * Override to use your configuration type
     */
    protected abstract OpenJPAConfiguration newConfiguration();

    public void setResources(String resources) {
        this.resources = resources;
    }

    protected void open() {
        _conf = (SystemConfiguration) ctx.getConfiguration();
        _store = _conf.getStore();
        _store.open();
        _initialized = new BiDiMap();
    }

    public void close() {
        _store.close();
    }

    public boolean exists(OpenJPAStateManager sm, Object context) {
        return _store.getData(sm.getMetaData(), sm.getObjectId()) != null;
    }

    public boolean initialize(OpenJPAStateManager sm, PCState state, FetchConfiguration fetch, Object context) {
        ObjectData data;
        if (context != null) {
            data = (ObjectData) context;
        } else {
            data = _store.getData(sm.getMetaData(), sm.getObjectId());
        }
        if (data == null) {
            return false;
        }
        sm.initialize(data.getMetaData().getDescribedType(), state);
        data.load(sm, fetch);
        _initialized.put(sm, data);
        return true;
    }

    public boolean load(OpenJPAStateManager sm, BitSet fields, FetchConfiguration fetch, int lockLevel, Object context) {
        ObjectData data;
        if (context != null) {
            data = (ObjectData) context;
        } else {
            data = (ObjectData) _initialized.get(sm);
        }
        if (data == null) {
            data = _store.getData(sm.getMetaData(), sm.getObjectId());
        }
        if (data == null) {
            return false;
        }
        data.load(sm, fields, fetch, _conf.getLoadPartial());
        return true;
    }

    public void begin() {
        _store.beginTransaction();
    }

    public void commit() {
        try {
            _store.endTransaction(_new, _updates, _deletes);
        } finally {
            _updates = null;
            _deletes = null;
            _new = null;
        }
    }

    public void rollback() {
        _updates = null;
        _deletes = null;
        _new = null;
        _store.endTransaction(null, null, null);
    }

    protected Collection flush(Collection pNew, Collection pNewUpdated, Collection pNewFlushedDeleted, Collection pDirty, Collection pDeleted) {
        Collection exceps = new LinkedList();
        _updates = new ArrayList(pDirty.size());
        _new = new ArrayList(pNew.size());
        _deletes = new ArrayList(pDeleted.size());
        for (Iterator itr = pNew.iterator(); itr.hasNext(); ) {
            OpenJPAStateManager sm = (OpenJPAStateManager) itr.next();
            Object oid = sm.getObjectId();
            ObjectData data = _store.getData(sm.getMetaData(), oid);
            if (data != null) {
                exceps.add(new OptimisticException(sm.getManagedInstance()));
                continue;
            }
            data = _conf.newDataInstance(sm.getMetaData(), oid);
            data.store(sm);
            _new.add(data);
        }
        for (Iterator itr = pDirty.iterator(); itr.hasNext(); ) {
            OpenJPAStateManager sm = (OpenJPAStateManager) itr.next();
            if (sm.isDeleted()) {
                continue;
            }
            if (sm.isNew()) {
                continue;
            }
            ObjectData data = _store.getData(sm.getMetaData(), sm.getObjectId());
            if (data == null || (data.getVersion() != null && !data.getVersion().equals(sm.getVersion()))) {
                exceps.add(new OptimisticException(sm.getManagedInstance()));
                continue;
            }
            data = (ObjectData) data.clone();
            data.store(sm);
            _updates.add(data);
        }
        for (Iterator itr = pDeleted.iterator(); itr.hasNext(); ) {
            OpenJPAStateManager sm = (OpenJPAStateManager) itr.next();
            ObjectData data = _store.getData(sm.getMetaData(), sm.getObjectId());
            if (data != null) {
                _deletes.add(data);
            }
        }
        return exceps;
    }

    public ResultObjectProvider executeExtent(ClassMetaData meta, boolean subclasses, FetchConfiguration fetch) {
        ObjectData[] datas = _store.getData(meta);
        Class candidate = meta.getDescribedType();
        List pcs = new ArrayList(datas.length);
        for (int i = 0; i < datas.length; i++) {
            Class c = datas[i].getMetaData().getDescribedType();
            if (c != candidate && (!subclasses || !candidate.isAssignableFrom(c))) {
                continue;
            }
            pcs.add(ctx.find(datas[i].getId(), fetch, null, datas[i], 0));
        }
        return new ListResultObjectProvider(pcs);
    }
}

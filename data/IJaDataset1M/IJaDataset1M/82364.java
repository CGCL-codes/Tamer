package org.nakedobjects.plugins.html.context;

import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.commons.ensure.Assert;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.memento.Memento;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;

public class TransientObjectMapping implements ObjectMapping {

    private final Oid oid;

    private final Memento memento;

    public TransientObjectMapping(final NakedObject adapter) {
        oid = adapter.getOid();
        Assert.assertTrue("OID is for persistent", oid.isTransient());
        Assert.assertTrue("adapter is for persistent", adapter.isTransient());
        memento = new Memento(adapter);
    }

    public void debug(final DebugString debug) {
        memento.debug(debug);
    }

    public Oid getOid() {
        return oid;
    }

    public NakedObject getObject() {
        return getAdapterManager().getAdapterFor(oid);
    }

    @Override
    public int hashCode() {
        return oid.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof TransientObjectMapping) {
            return ((TransientObjectMapping) obj).oid.equals(oid);
        }
        return false;
    }

    @Override
    public String toString() {
        return "TRANSIENT : " + oid + " : " + memento;
    }

    public Version getVersion() {
        return null;
    }

    public void checkVersion(final NakedObject object) {
    }

    public void restoreToLoader() {
        memento.recreateObject();
    }

    public void updateVersion() {
    }

    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
}

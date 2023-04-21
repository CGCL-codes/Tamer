package com.versant.core.common;

import com.versant.core.server.StateContainer;
import com.versant.core.metadata.FetchGroup;
import com.versant.core.common.OID;
import com.versant.core.common.State;

/**
 * Dummy implementation of StateContainer that does not contain anything
 * and discards anything added to it.
 */
public class DummyStateContainer implements StateContainer {

    public static final DummyStateContainer INSTANCE = new DummyStateContainer();

    public void visited(OID oid) {
    }

    public boolean isStateRequired(OID oid, FetchGroup fetchGroup) {
        return false;
    }

    public void addState(OID oid, State state) {
        throw BindingSupportImpl.getInstance().notImplemented("");
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public State add(OID key, State value) {
        return null;
    }

    public State get(Object key) {
        return null;
    }
}

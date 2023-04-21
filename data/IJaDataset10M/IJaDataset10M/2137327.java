package com.directmodelling.api;

import com.directmodelling.impl.ExplicitUpdatesTracker;
import com.directmodelling.impl.SimpleContext;
import com.directmodelling.stm.Storage;
import com.directmodelling.stm.impl.VersionImpl;

public final class DirectModelling {

    public static void init(Storage storage, Updates.Tracker updatesTracker) {
        Storage.Util.current = new SimpleContext<Storage>(storage);
        Updates.tracker = updatesTracker;
    }

    public static void initForTests() {
        init(new VersionImpl(), new ExplicitUpdatesTracker());
    }

    public static void init() {
        if (Storage.Util.current == null) initForTests();
    }
}

package net.teqlo.db.remote;

import net.teqlo.TeqloException;
import net.teqlo.db.User;
import net.teqlo.db.impl.PreferencesCache;
import net.teqlo.util.Preferences;

public class RemotePreferencesCache extends PreferencesCache {

    public RemotePreferencesCache() throws TeqloException {
        super();
    }

    public Preferences fetch(String prefsDocumentFqn, Object fetchData) throws TeqloException {
        return new RemotePreferences((User) fetchData, prefsDocumentFqn);
    }
}

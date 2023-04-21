package org.waveprotocol.box.server.authentication;

import com.google.common.base.Preconditions;
import org.waveprotocol.box.server.persistence.AccountStore;

/**
 * This class holds a reference to a global AccountStore object. It is used in
 * classes which are not instantiated by Guice, and for which the only way to
 * access the account store is via static singletons. This is the case for JAAS
 * configured login modules, like AccountStoreLoginModule.
 *
 * @author josephg@gmail.com (Joseph Gentle)
 */
public class AccountStoreHolder {

    private static AccountStore store = null;

    private static String defaultDomain = null;

    public static synchronized void init(AccountStore store, String defaultDomain) {
        Preconditions.checkNotNull(store, "Account store cannot be null");
        Preconditions.checkNotNull(defaultDomain, "Default domain cannot be null");
        Preconditions.checkState(AccountStoreHolder.store == null, "Account store already set");
        AccountStoreHolder.store = store;
        AccountStoreHolder.defaultDomain = defaultDomain.toLowerCase();
    }

    /**
   * @return the non-null account store.
   */
    public static AccountStore getAccountStore() {
        Preconditions.checkNotNull(store, "Account store not set");
        return store;
    }

    /**
   * @return the non-null default domain
   */
    public static String getDefaultDomain() {
        Preconditions.checkNotNull(defaultDomain, "Default domain not set");
        return defaultDomain;
    }

    /** Needed for testing. */
    public static void resetForTesting() {
        store = null;
        defaultDomain = null;
    }
}

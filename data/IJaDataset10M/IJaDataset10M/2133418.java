package org.wicketopia.persistence;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.WicketRuntimeException;
import org.wicketopia.context.Context;

public class PersistenceUtils {

    private static final MetaDataKey<PersistenceProvider> PERSISTENCE_PROVIDER_KEY = new MetaDataKey<PersistenceProvider>() {
    };

    public static PersistenceProvider getProvider(Context context) {
        PersistenceProvider provider = context.getAttribute(PERSISTENCE_PROVIDER_KEY);
        if (provider == null) {
            throw new WicketRuntimeException("PersistenceProvider not found in context.  Please call PersistenceUtils.setProvider() method.");
        }
        return provider;
    }

    public static void setProvider(Context context, PersistenceProvider persistenceProvider) {
        context.setAttribute(PERSISTENCE_PROVIDER_KEY, persistenceProvider);
    }
}

package dash.obtain.provider;

import java.util.HashSet;
import java.util.Set;
import dash.obtain.initialize.Config;

/**
 *
 * The ProviderService acts as the entry point into the Provider lookup activity.
 *
 * <p>The ProviderService is configured (via the Config utility) with the classname of
 * a Provider to act as the "strategy" for Provider lookups.
 *
 * <p>The property key used to lookup the default Provider strategy class is "provider.strategy".
 *
 * @see dash.obtain.provider.builtin.DefaultProviderStrategy
 * @author jheintz
 *
 */
public class ProviderService {

    public static final String PROVIDER_STRATEGY = "provider.strategy";

    private static volatile Provider _globalProvider;

    public static Provider getProvider() {
        Provider result = _globalProvider;
        if (result == null) {
            synchronized (ProviderService.class) {
                if (result == null) {
                    result = buildGlobalProvider();
                    _globalProvider = result;
                }
            }
        }
        return result;
    }

    static Provider buildGlobalProvider() {
        Provider result = null;
        result = buildProviderByString(Config.getProperty(PROVIDER_STRATEGY));
        return result;
    }

    static Provider buildProviderByString(String className) {
        if (className == null || "".equals(className)) throw new IllegalStateException("No provider Class name given");
        try {
            return (Provider) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(className, e);
        }
    }

    public static synchronized void setProvider(Provider defaultProvider) {
        _globalProvider = defaultProvider;
    }

    static ThreadLocal<Set<ObtainLookup>> currentLookups = new ThreadLocal<Set<ObtainLookup>>() {

        protected synchronized Set<ObtainLookup> initialValue() {
            return new HashSet<ObtainLookup>();
        }
    };

    /**
	 *
	 *
	 * @param lookup
	 * @return
	 */
    public static Object lookup(ObtainLookup lookup) {
        Set<ObtainLookup> lookups = currentLookups.get();
        if (lookups.contains(lookup)) {
            return null;
        }
        boolean firstLookup = lookups.size() == 0;
        lookups.add(lookup);
        try {
            Provider globProv = ProviderService.getProvider();
            Object result = globProv.lookup(lookup);
            if (firstLookup && result == null) throw new NullPointerException("Null object reference");
            return result;
        } finally {
            lookups.remove(lookup);
        }
    }

    static void clearGlobalProvider() {
        _globalProvider = null;
    }
}

package emil.poker.ai.opponentEvaluators;

import java.util.HashMap;
import java.util.Map;

public class CachedObjects {

    private Map<CachedObjectKey, Object> cachedObjects;

    public Map<CachedObjectKey, Object> getCachedObjects() {
        return cachedObjects;
    }

    public void setCachedObjects(Map<CachedObjectKey, Object> cachedObjects) {
        this.cachedObjects = cachedObjects;
    }

    private static synchronized CachedObjects getInstance() {
        if (_instance == null) _instance = new CachedObjects();
        return _instance;
    }

    private static CachedObjects _instance;

    private CachedObjects() {
        cachedObjects = new HashMap<CachedObjectKey, Object>();
    }

    /**
	 * 
	 * @param player
	 * @param id
	 * @param name
	 * @return
	 */
    public static Object getCachedObject(CachedObjectKey key) {
        return getInstance().getCachedObjects().get(key);
    }

    /**
	 * 
	 * @param player
	 * @param id
	 * @param evaluator
	 */
    public static void cache(CachedObjectKey key, Object value) {
        getInstance().getCachedObjects().put(key, value);
    }

    public static void clear() {
        getInstance().getCachedObjects().clear();
    }
}

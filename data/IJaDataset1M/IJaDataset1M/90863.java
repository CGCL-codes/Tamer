package dash.obtain.provider.builtin;

import java.util.logging.Logger;
import dash.obtain.provider.ObtainLookup;
import dash.obtain.provider.Provider;

/**
 * @author jheintz
 *
 */
public class LoggerProviderStrategy implements Provider {

    public Object lookup(ObtainLookup lookup) {
        if (lookup.fieldClass == Logger.class && (lookup.fieldKey == null || "".equals(lookup.fieldKey))) {
            return Logger.getLogger(lookup.fieldDeclaringClass.getName());
        }
        return null;
    }
}

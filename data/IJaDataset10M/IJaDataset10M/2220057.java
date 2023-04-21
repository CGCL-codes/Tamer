package at.riemers.zero.base;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author tobias
 */
public class DatabaseMaeesageCacheResourceBundle extends ResourceBundle {

    private DatabaseMessageCache databaseMessageCache;

    private String language;

    public DatabaseMaeesageCacheResourceBundle(DatabaseMessageCache databaseMessageCache, String language) {
        this.databaseMessageCache = databaseMessageCache;
        this.language = language;
    }

    @Override
    protected Object handleGetObject(String key) {
        return databaseMessageCache.resolveCode(key, new Locale(language));
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(databaseMessageCache.getKeys());
    }
}

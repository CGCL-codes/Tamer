package at.riemers.zero.base;

import java.text.MessageFormat;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractMessageSource;

/**
 *
 * @author tobias
 */
public class DatabaseMessageSource extends AbstractMessageSource {

    private DatabaseMessageCache databaseMessageCache;

    private static final Logger log = Logger.getLogger(DatabaseMessageSource.class);

    protected synchronized MessageFormat resolveCode(String code, Locale locale) {
        return createMessageFormat(databaseMessageCache.resolveCode(code, locale), locale);
    }

    public DatabaseMessageCache getDatabaseMessageCache() {
        return databaseMessageCache;
    }

    public void setDatabaseMessageCache(DatabaseMessageCache databaseMessageCache) {
        this.databaseMessageCache = databaseMessageCache;
    }
}

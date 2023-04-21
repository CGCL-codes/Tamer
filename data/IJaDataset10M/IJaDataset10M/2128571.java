package ti.targetinfo;

import java.io.IOException;
import ti.io.UDataInputStream;

/**
 * A sub-interface for database factories that can automatically create an
 * (for ex, empty) database for caching purposes, which should not be
 * serialized
 * 
 * @author robclark
 */
public abstract class CacheDatabaseFactory extends AutoloadDatabaseFactory {

    public final Database loadDatabase(UDataInputStream in) throws IOException {
        return null;
    }

    public abstract CacheDatabase loadDatabase();
}

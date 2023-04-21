package au.edu.archer.metadata.mde.schema;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import au.edu.archer.metadata.spi.MultiplexProvider;
import au.edu.archer.metadata.spi.SchemaRepository;
import au.edu.archer.metadata.spi.SchemaRepositoryException;
import au.edu.archer.metadata.spi.ServiceProviderException;

/**
 * The schema cache looks up MSS schemas using one or more SchemaRepository instances,
 * and stores them as SchemaCache objects in a memory resource aware cache.  The cache
 * implements cache expiry using expiry times provided by the SchemaRepository layer.
 * 
 * @author scrawley@itee.uq.edu.au
 */
public class SchemaCache implements SchemaRepository, MultiplexProvider {

    private SchemaRepository[] repositories;

    private Map<String, SoftReference<Schema>> cache;

    private final Logger logger = Logger.getLogger(SchemaCache.class);

    public SchemaCache() {
    }

    public SchemaCache(List<SchemaRepository> repositories) {
        setRepositories(repositories);
    }

    public void setRepositories(List<SchemaRepository> repositories) {
        if (repositories.isEmpty()) {
            throw new IllegalArgumentException("empty repository list");
        }
        this.repositories = repositories.toArray(new SchemaRepository[repositories.size()]);
        this.cache = new HashMap<String, SoftReference<Schema>>(64, 0.5F);
    }

    @Override
    public void init(ServletContext context) throws ServiceProviderException {
        for (SchemaRepository child : repositories) {
            child.init(context);
        }
    }

    /**
     * Lookup an MSS schema using the sysname as a key.  If the schema has already been
     * fetched and is cached, the cached copy is checked to see if it has expired, and
     * either returned as-is, or refetched.
     * 
     * @param sysname the sysname string we use to identify a schema.
     * @return the schema
     * @throws SchemaRepositoryException if we cannot fetch the schema from a repository.
     */
    public Schema lookup(String sysname) throws SchemaRepositoryException {
        SoftReference<Schema> ref = cache.get(sysname);
        Schema schema = null;
        if (ref != null) {
            schema = ref.get();
            if (schema != null && schema.getExpiry() >= System.currentTimeMillis()) {
                return schema;
            }
        }
        SchemaRepositoryException firstException = null;
        for (SchemaRepository repos : repositories) {
            try {
                Schema tmp = repos.lookup(sysname);
                long expiry = tmp.getExpiry();
                if (schema == null) {
                    schema = new Schema(sysname, tmp.getMSSData(), expiry);
                    logger.debug("Creating schema cache entry for '" + sysname + "' with expiry " + expiry);
                    cache.put(sysname, new SoftReference<Schema>(schema));
                } else {
                    logger.debug("Refreshing schema cache entry for '" + sysname + "' with expiry " + expiry);
                    schema.refresh(tmp.getMSSData(), expiry);
                }
                logger.debug("Done");
                return schema;
            } catch (SchemaRepositoryException ex) {
                if (firstException == null) {
                    firstException = ex;
                }
            }
        }
        throw firstException;
    }
}

package com.ontotext.ordi.trree;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ontotext.ordi.exception.ORDIException;
import com.ontotext.ordi.exception.ORDIRuntimeException;
import com.ontotext.ordi.tripleset.TConnection;
import com.ontotext.trree.AbstractInferencer;
import com.ontotext.trree.AbstractRepository;
import com.ontotext.trree.EntityPool;

public class TRREEAdapter extends AbstractTRREEAdapter {

    public static final String PARAM_CACHE_SIZE = "cache-size";

    public static final String PARAM_ENTITY_INDEX_SIZE = "entity-index-size";

    public static final String PARAM_FTS_INDEX_POLICY = "ftsIndexPolicy";

    public static final String PARAM_KEEP_PERSISTED_DATA = "keep-persisted-data";

    public static final String PARAM_PARTIALRDFS = "partialRDFS";

    public static final String PARAM_REPOSITORY_TYPE = "repository-type";

    public static final String PARAM_RULESET = "ruleset";

    public static final String PARAM_STORAGE_DIRECTORY = "storage-folder";

    public static final String PARAM_TOKEN_INDEX_CACHE_SIZE = "tokenIndexCacheSize";

    private static final Map<String, TRREEAdapter> usedStoragePaths = new HashMap<String, TRREEAdapter>();

    private Set<TRREEConnection> connections = new HashSet<TRREEConnection>();

    private AbstractRepository repository;

    private AbstractInferencer inferencer;

    private EntityPool pool;

    private boolean isShutDown = false;

    private static Logger logger = LoggerFactory.getLogger(TRREEAdapter.class);

    private Map<String, Object> conf = new HashMap<String, Object>();

    public TRREEAdapter(Map<Object, Object> props) {
        super(props);
        int cacheSize = (int) 4096;
        try {
            if (props.containsKey(PARAM_CACHE_SIZE)) {
                cacheSize = Integer.parseInt(String.valueOf(props.get(PARAM_CACHE_SIZE)));
            }
        } catch (NumberFormatException e) {
        }
        conf.put(PARAM_CACHE_SIZE, cacheSize);
        int entityIndexCache = (int) 10E+6;
        try {
            if (props.containsKey(PARAM_ENTITY_INDEX_SIZE)) {
                entityIndexCache = Integer.parseInt(String.valueOf(props.get(PARAM_ENTITY_INDEX_SIZE)));
            }
        } catch (NumberFormatException e) {
        }
        conf.put(PARAM_ENTITY_INDEX_SIZE, entityIndexCache);
        if ("false".equals(props.get(PARAM_KEEP_PERSISTED_DATA))) {
            conf.put(PARAM_KEEP_PERSISTED_DATA, false);
        } else {
            conf.put(PARAM_KEEP_PERSISTED_DATA, true);
        }
        if ("false".equals(props.get(PARAM_PARTIALRDFS))) {
            conf.put(PARAM_PARTIALRDFS, false);
        } else {
            conf.put(PARAM_PARTIALRDFS, true);
        }
        if ("file".equals(props.get(PARAM_REPOSITORY_TYPE))) {
            conf.put(PARAM_REPOSITORY_TYPE, false);
        } else {
            conf.put(PARAM_REPOSITORY_TYPE, true);
        }
        String ruleSet = "empty";
        if (props.get(PARAM_RULESET) != null) ruleSet = props.get(PARAM_RULESET).toString();
        conf.put(PARAM_RULESET, ruleSet);
        String storage = System.getProperty("user.dir");
        if (props.get(PARAM_STORAGE_DIRECTORY) instanceof String) storage = (String) props.get(PARAM_STORAGE_DIRECTORY);
        storage = storage.endsWith(File.separator) ? storage : storage + File.separatorChar;
        conf.put(PARAM_STORAGE_DIRECTORY, storage);
        int tokenIndexCache = (int) 10E+3;
        try {
            if (props.containsKey(PARAM_TOKEN_INDEX_CACHE_SIZE)) {
                tokenIndexCache = Integer.parseInt(String.valueOf(props.get(PARAM_TOKEN_INDEX_CACHE_SIZE)));
            }
        } catch (NumberFormatException e) {
        }
        conf.put(PARAM_TOKEN_INDEX_CACHE_SIZE, tokenIndexCache);
        init(conf);
    }

    private void init(Map<String, Object> conf) {
        logger.info("Instantiate TRREEAdapter with: " + conf.toString());
        File f = new File((String) conf.get(PARAM_STORAGE_DIRECTORY));
        synchronized (usedStoragePaths) {
            if (usedStoragePaths.containsKey(f.getAbsolutePath())) {
                String msg = String.format("The TRREE storage path (%s) is already used by another instance!", f.getAbsolutePath());
                logger.error(msg);
                throw new ORDIRuntimeException(msg);
            }
            usedStoragePaths.put(f.getAbsolutePath(), this);
        }
        try {
            pool = TRREEFactory.createPool((String) conf.get(PARAM_STORAGE_DIRECTORY), (Integer) conf.get(PARAM_ENTITY_INDEX_SIZE), (Integer) conf.get(PARAM_TOKEN_INDEX_CACHE_SIZE), (Boolean) conf.get(PARAM_KEEP_PERSISTED_DATA), 1);
            repository = TRREEFactory.createRepository((String) conf.get(PARAM_STORAGE_DIRECTORY), (Boolean) conf.get(PARAM_REPOSITORY_TYPE), (Integer) conf.get(PARAM_CACHE_SIZE), pool);
            inferencer = TRREEFactory.createInferencer(repository, pool, (String) conf.get(PARAM_STORAGE_DIRECTORY), (String) conf.get(PARAM_RULESET), (Boolean) conf.get(PARAM_PARTIALRDFS));
        } catch (Exception e) {
            logger.error("Could not init TRREE.", e);
            usedStoragePaths.remove(f.getAbsolutePath());
            throw new ORDIRuntimeException(e);
        }
    }

    public synchronized TConnection getConnection() {
        if (isShutDown) {
            throw new IllegalStateException("The instance was shutdown!");
        }
        TRREEConnection connection = new TRREEConnection(this);
        connections.add(connection);
        return connection;
    }

    public TConnection getConnection(String user, String pass) {
        return getConnection();
    }

    public synchronized void shutdown() {
        if (isShutDown) return;
        for (TRREEConnection connection : connections) {
            try {
                connection.close();
            } catch (ORDIException oe) {
            }
        }
        synchronized (usedStoragePaths) {
            usedStoragePaths.values().remove(this);
        }
        if (inferencer != null) {
            inferencer.shutdown();
        }
        repository.shutdown();
        pool.shutdown();
        isShutDown = true;
        inferencer = null;
        repository = null;
        pool = null;
    }

    public boolean isShutdown() {
        return isShutDown;
    }

    public AbstractRepository getRepository() {
        return repository;
    }

    public AbstractInferencer getInferencer() {
        return inferencer;
    }

    public EntityPool getPool() {
        return pool;
    }

    protected void finalize() throws Throwable {
        if (isShutDown == false) shutdown();
        super.finalize();
    }
}

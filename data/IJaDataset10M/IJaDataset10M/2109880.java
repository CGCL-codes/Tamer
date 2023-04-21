package dbs_project.database;

import com.google.common.base.Preconditions;
import dbs_project.index.IndexLayer;
import dbs_project.persistence.PersistenceLayer;
import dbs_project.query.QueryLayer;
import dbs_project.storage.StorageLayer;
import org.openide.util.Lookup;

/**
 * Please do not modify! All changes to this class will be reverted.
 * 
 * This class provides access to your database implementation.
 * 
 * We will activate the remaining layers for every milestone.
 */
public final class Database {

    private static final Database INSTANCE = new Database();

    private final StorageLayer storageLayer;

    private final IndexLayer indexLayer;

    private final QueryLayer queryLayer;

    private final PersistenceLayer persistenceLayer;

    private Database() {
        final Lookup lookup = Lookup.getDefault();
        this.storageLayer = lookup.lookup(StorageLayer.class);
        this.indexLayer = lookup.lookup(IndexLayer.class);
        this.queryLayer = lookup.lookup(QueryLayer.class);
        this.persistenceLayer = lookup.lookup(PersistenceLayer.class);
        checkPreconditions();
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(storageLayer, "Did not find StorageLayer implementation!");
        Preconditions.checkNotNull(indexLayer, "Did not find IndexLayer implementation!");
        Preconditions.checkNotNull(queryLayer, "Did not find QueryLayer implementation!");
        Preconditions.checkNotNull(persistenceLayer, "Did not find PersistenceLayer implementation!");
    }

    public StorageLayer getStorageLayer() {
        return storageLayer;
    }

    public IndexLayer getIndexLayer() {
        return indexLayer;
    }

    public QueryLayer getQueryLayer() {
        return queryLayer;
    }

    public PersistenceLayer getPersistenceLayer() {
        return persistenceLayer;
    }

    public static Database getInstance() {
        return INSTANCE;
    }
}

package er.extensions;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EODatabase;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EODatabaseOperation;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

public class ERXDatabaseContext extends EODatabaseContext {

    private static ThreadLocal _fetching = new ThreadLocal();

    public ERXDatabaseContext(EODatabase database) {
        super(new ERXDatabase(database));
    }

    public static boolean isFetching() {
        Boolean fetching = (Boolean) _fetching.get();
        return fetching != null && fetching.booleanValue();
    }

    public static void setFetching(boolean fetching) {
        _fetching.set(Boolean.valueOf(fetching));
    }

    public NSArray objectsForSourceGlobalID(EOGlobalID gid, String name, EOEditingContext context) {
        NSArray results;
        boolean fetching = isFetching();
        if (!fetching) {
            setFetching(true);
        }
        try {
            results = super.objectsForSourceGlobalID(gid, name, context);
        } finally {
            if (!fetching) {
                setFetching(false);
            }
        }
        return results;
    }

    public NSArray _objectsWithFetchSpecificationEditingContext(EOFetchSpecification fetchSpec, EOEditingContext context) {
        NSArray results;
        boolean fetching = isFetching();
        if (!fetching) {
            setFetching(!fetchSpec.refreshesRefetchedObjects());
        }
        try {
            results = super._objectsWithFetchSpecificationEditingContext(fetchSpec, context);
        } finally {
            if (!fetching) {
                setFetching(false);
            }
        }
        return results;
    }

    public void _verifyNoChangesToReadonlyEntity(EODatabaseOperation dbOp) {
        EOEntity entity = dbOp.entity();
        if (entity.isReadOnly()) {
            switch(dbOp.databaseOperator()) {
                case 0:
                    return;
                case 1:
                    throw new IllegalStateException("cannot insert object:" + dbOp.object() + " that corresponds to read-only entity: " + entity.name() + " in databaseContext " + this);
                case 3:
                    throw new IllegalStateException("cannot delete object:" + dbOp.object() + " that corresponds to read-only entity:" + entity.name() + " in databaseContext " + this);
                case 2:
                    if (!dbOp.dbSnapshot().equals(dbOp.newRow())) {
                        throw new IllegalStateException("cannot update '" + dbOp.rowDiffsForAttributes(entity.attributes()).allKeys() + "' keys on object:" + dbOp.object() + " that corresponds to read-only entity: " + entity.name() + " in databaseContext " + this);
                    } else {
                        return;
                    }
            }
        }
        if (dbOp.databaseOperator() == 2 && ((Boolean) NSKeyValueCoding.Utility.valueForKey(entity, "_hasNonUpdateableAttributes")).booleanValue()) {
            NSArray keys = (NSArray) NSKeyValueCoding.Utility.valueForKey(entity, "dbSnapshotKeys");
            NSDictionary dbSnapshot = dbOp.dbSnapshot();
            NSDictionary newRow = dbOp.newRow();
            for (int i = keys.count() - 1; i >= 0; i--) {
                String key = (String) keys.objectAtIndex(i);
                EOAttribute att = entity.attributeNamed(key);
                if (att != null && att._isNonUpdateable() && !dbSnapshot.objectForKey(key).equals(newRow.objectForKey(key))) {
                    if (att.isReadOnly()) {
                        throw new IllegalStateException("cannot update read-only key '" + key + "' on object:" + dbOp.object() + " of entity: " + entity.name() + " in databaseContext " + this);
                    } else {
                        throw new IllegalStateException("cannot update primary-key '" + key + "' from '" + dbSnapshot.objectForKey(key) + "' to '" + newRow.objectForKey(key) + "' on object:" + dbOp.object() + " of entity: " + entity.name() + " in databaseContext " + this);
                    }
                }
            }
        }
    }
}

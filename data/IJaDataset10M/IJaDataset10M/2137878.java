package org.kotemaru.gae.dstool.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.memcache.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import org.kotemaru.gae.dstool.DsToolConst;
import org.kotemaru.util.*;

/**
 */
public class EntityRemover implements DsToolConst {

    private DatastoreService datasotre = DatastoreServiceFactory.getDatastoreService();

    private int removeCount = 0;

    public EntityRemover() {
    }

    public int getRemoveCount() {
        return removeCount;
    }

    public int remove(String kind) throws Exception {
        while (removeOne(kind) > 0) ;
        return removeCount;
    }

    private int removeOne(String kind) throws Exception {
        int count = 0;
        Iterator<Entity> ite = iterate(kind, 1000);
        while (ite.hasNext()) {
            datasotre.delete(ite.next().getKey());
            removeCount++;
            count++;
        }
        return count;
    }

    private Iterator<Entity> iterate(String kind, int limit) throws Exception {
        Query query = new Query(kind);
        query.setKeysOnly();
        PreparedQuery preQuery = datasotre.prepare(query);
        FetchOptions op = FetchOptions.Builder.withChunkSize(100);
        op = op.limit(limit);
        return preQuery.asIterator(op);
    }
}

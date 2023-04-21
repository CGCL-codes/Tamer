package info.olteanu.utils.remoteservices.cache;

import info.olteanu.utils.*;
import info.olteanu.utils.chron.*;
import info.olteanu.utils.remoteservices.*;
import java.util.*;
import info.olteanu.utils.objectcache.*;

public class AutoFlushCachedLineRemoteService implements CachedRemoteServiceIf, ObjectCacheCaller<String[], String[]> {

    private static boolean DEBUG = false;

    private String[] nullCache;

    private static int ID = 0;

    private RemoteService cached;

    private ObjectCache<String[], String[]> cache;

    public AutoFlushCachedLineRemoteService(RemoteService rs, int maxSize, int historyLengthWhenFlush) {
        ID++;
        this.cached = rs;
        cache = new ObjectCache<String[], String[]>(this, maxSize, historyLengthWhenFlush, DEBUG, "AutoFlushCachedLineRemoteService" + ID);
        nullCache = null;
    }

    public String[] service(String[] input) throws RemoteException {
        if (input.length == 0) {
            if (nullCache == null) nullCache = cached.service(input);
            return nullCache;
        }
        if (input.length > 1) throw new RemoteException(this.getClass().getName() + " expects one line of input. Found " + input.length);
        try {
            return cache.getValue(input[0], input);
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            throw new Error("unexpected", e);
        }
    }

    public String[] callWhenMiss(String[] arg) throws Exception {
        return cached.service(arg);
    }

    public void flush() {
        cache.flush();
    }
}

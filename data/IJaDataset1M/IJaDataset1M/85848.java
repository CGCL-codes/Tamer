package com.liferay.util;

import java.util.Map;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

/**
 * <a href="SimpleCachePool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.6 $
 *
 */
public class SimpleCachePool {

    public static Object get(String id) {
        return _getInstance()._get(id);
    }

    public static void put(String id, Object obj) {
        _getInstance()._put(id, obj);
    }

    public static Object remove(String id) {
        return _getInstance()._remove(id);
    }

    private static SimpleCachePool _getInstance() {
        if (_instance == null) {
            synchronized (SimpleCachePool.class) {
                if (_instance == null) {
                    _instance = new SimpleCachePool();
                }
            }
        }
        return _instance;
    }

    private SimpleCachePool() {
        _scPool = new ConcurrentReaderHashMap(_SIZE);
    }

    private Object _get(String id) {
        return (Object) _scPool.get(id);
    }

    private void _put(String id, Object ds) {
        _scPool.put(id, ds);
    }

    private Object _remove(String id) {
        return _scPool.remove(id);
    }

    private static SimpleCachePool _instance;

    private static int _SIZE = 100000;

    private Map _scPool;
}

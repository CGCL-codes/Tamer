package com.liferay.portal.servlet;

import java.util.Map;
import com.liferay.util.CollectionFactory;

/**
 * <a href="PortletContextPool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.3 $
 *
 */
public class PortletContextPool {

    public static PortletContextWrapper get(String portletId) {
        return _getInstance()._get(portletId);
    }

    public static void put(String portletId, PortletContextWrapper pcw) {
        _getInstance()._put(portletId, pcw);
    }

    public static PortletContextWrapper remove(String portletId) {
        return _getInstance()._remove(portletId);
    }

    private static PortletContextPool _getInstance() {
        if (_instance == null) {
            synchronized (PortletContextPool.class) {
                if (_instance == null) {
                    _instance = new PortletContextPool();
                }
            }
        }
        return _instance;
    }

    private PortletContextPool() {
        _portletContextPool = CollectionFactory.getSyncHashMap();
    }

    private PortletContextWrapper _get(String portletId) {
        return (PortletContextWrapper) _portletContextPool.get(portletId);
    }

    private void _put(String portletId, PortletContextWrapper pcw) {
        _portletContextPool.put(portletId, pcw);
    }

    private PortletContextWrapper _remove(String portletId) {
        return (PortletContextWrapper) _portletContextPool.remove(portletId);
    }

    private static PortletContextPool _instance;

    private Map _portletContextPool;
}

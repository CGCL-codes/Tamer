package com.liferay.portlet;

import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.util.CachePropsUtil;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

/**
 * <a href="ActionResponseFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ActionResponseFactory {

    public static ActionResponseImpl create(ActionRequestImpl req, HttpServletResponse res, String portletName, User user, Layout layout, WindowState windowState, PortletMode portletMode) throws Exception {
        ActionResponseImpl actionResponseImpl = null;
        if (CachePropsUtil.COMMONS_POOL_ENABLED) {
            actionResponseImpl = (ActionResponseImpl) _instance._pool.borrowObject();
        } else {
            actionResponseImpl = new ActionResponseImpl();
        }
        actionResponseImpl.init(req, res, portletName, user, layout, windowState, portletMode);
        return actionResponseImpl;
    }

    public static void recycle(ActionResponseImpl actionResponseImpl) throws Exception {
        if (CachePropsUtil.COMMONS_POOL_ENABLED) {
            _instance._pool.returnObject(actionResponseImpl);
        }
    }

    private ActionResponseFactory() {
        _pool = new StackObjectPool(new Factory());
    }

    private static ActionResponseFactory _instance = new ActionResponseFactory();

    private ObjectPool _pool;

    private class Factory extends BasePoolableObjectFactory {

        public Object makeObject() {
            return new ActionResponseImpl();
        }

        public void passivateObject(Object obj) {
            ActionResponseImpl actionResponseImpl = (ActionResponseImpl) obj;
            actionResponseImpl.recycle();
        }
    }
}

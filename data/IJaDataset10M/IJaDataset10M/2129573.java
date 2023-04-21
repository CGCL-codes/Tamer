package org.eralyautumn.common.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.eralyautumn.common.GlobalBase;
import org.eralyautumn.common.persistence.CacheManager;
import org.eralyautumn.common.persistence.MapManager;
import org.eralyautumn.common.util.Constants;
import org.eralyautumn.common.service.OnlineService;
import org.eralyautumn.message.session.Session;

/**
 * 控制器基类,所有控制器方法参数里面必须包含IoSession和GameMessage,Model为可选参数,参数顺序随意
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * 
 * @version 1.0
 * 
 * 2010-2-24
 */
public abstract class BaseController extends GlobalBase {

    protected final String PAGE = "page";

    @Autowired
    @Qualifier("memManager")
    protected CacheManager memManager;

    @Autowired
    @Qualifier("mapManager")
    protected MapManager mapManager;

    @Autowired
    protected OnlineService onlineService;

    public BaseController() {
    }

    public static String getUserId(Session session) {
        return (String) session.getAttribute(Constants.LOGIN_USER);
    }
}

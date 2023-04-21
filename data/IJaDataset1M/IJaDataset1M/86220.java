package com.companyname.common.viewframe.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.base.web.service.AbstractTreeService;
import com.companyname.common.viewframe.manager.HomeManager;
import com.companyname.common.viewframe.model.Home;
import com.companyname.common.viewframe.view.foreveryone.HomeNodeOfAreaTree4EveryOne;
import com.companyname.common.viewframe.view.foreveryone.RootNodeOfAreaTree4EveryOne;

/**
 * <p>Title:  - 查询分区,将结果通过树图表现</p>
 * <p>Description:
 *      如果有多种节点类型(根节点除外),必须传
 *      递多个根节点实体
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 公司名</p>
 * @ $Author: author $
 * @ $Date: 2005/02/15 09:14:59 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class FindAreasForTreeViewService extends AbstractTreeService {

    /** 节点类型－根结点 */
    public static String ROOTNODE = "0";

    /** 节点类型－ 主页：非根节点 */
    public static String HOME = "1";

    static Logger logger = Logger.getLogger(FindAreasForTreeViewService.class);

    private HomeManager homeManager;

    /**
         * @param homeManager 要设置的 homeManager。
         */
    public void setHomeManager(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    /** 获取树节点对应的models */
    public void createModels(Map modelsMap, HttpServletRequest request) {
        Home rootHome = new Home();
        modelsMap.put(ROOTNODE, rootHome);
        modelsMap.put(HOME, this.homeManager.findAllHomes());
    }

    /** 设置nodeModel's views */
    public void setNodeViews(Map viewsMap, HttpServletRequest request) {
        viewsMap.put(ROOTNODE, new RootNodeOfAreaTree4EveryOne());
        viewsMap.put(HOME, new HomeNodeOfAreaTree4EveryOne());
    }
}

package com.asoft.common.sysframe.service;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.asoft.common.base.web.service.AbstractTreeService;
import com.asoft.common.sysframe.manager.OrgManager;
import com.asoft.common.sysframe.model.Org;
import com.asoft.common.sysframe.view.foreveryone.OrgNodeOfPostTree4EveryOne;
import com.asoft.common.sysframe.view.foreveryone.OrgRootNodeOfPostTree4EveryOne;

/**
 * <p>Title:  - 查询角色,将结果通过树图表现</p>
 * <p>Description:
 *      如果有多种节点类型(根节点除外),必须传
 *      递多个根节点实体
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: asoft</p>
 * @ $Author: author $
 * @ $Date: 2005/02/15 09:14:59 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class FindPostsForTreeViewWithOrgRootService extends AbstractTreeService {

    /** 节点类型－根结点 */
    public static String ROOTNODE = "0";

    /** 节点类型－ 机构：非根节点 */
    public static String ORG = "1";

    static Logger logger = Logger.getLogger(FindPostsForTreeViewWithOrgRootService.class);

    private OrgManager orgManager;

    public void setOrgManager(OrgManager orgManager) {
        this.orgManager = orgManager;
    }

    /** 获取树节点对应的models */
    public void createModels(Map modelsMap, HttpServletRequest request) {
        logger.debug("1. 获取查询条件.....");
        String orgId = this.getValueFromRequestParameter(request, "orgId", null);
        request.getSession().setAttribute("fromWhere", request.getParameter("fromWhere"));
        logger.debug("2. 获取查询条件完毕,开始查询.....");
        Org rootOrg;
        if (orgId == null) {
            Set orgs = new LinkedHashSet(this.orgManager.findOrgs(null));
            rootOrg = new Org();
            rootOrg.setSubOrgs(orgs);
        } else {
            rootOrg = (Org) this.orgManager.get(orgId);
        }
        modelsMap.put(ROOTNODE, rootOrg);
        modelsMap.put(ORG, rootOrg.getAllSubOrgs());
    }

    /** 设置nodeModel's views */
    public void setNodeViews(Map viewsMap, HttpServletRequest request) {
        viewsMap.put(ROOTNODE, new OrgRootNodeOfPostTree4EveryOne());
        viewsMap.put(ORG, new OrgNodeOfPostTree4EveryOne());
    }
}

package com.aimluck.eip.modules.actions.workflow;

import org.apache.jetspeed.portal.portlets.VelocityPortlet;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.modules.actions.common.ALBaseAction;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.workflow.WorkflowCategorySelectData;
import com.aimluck.eip.workflow.WorkflowRouteSelectData;

/**
 * ワークフローの管理者用アクションクラスです。 <BR>
 * 
 */
public class WorkflowAdminAction extends ALBaseAction {

    /** logger */
    @SuppressWarnings("unused")
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(WorkflowAdminAction.class.getName());

    /**
   * 通常表示の際の処理を記述します。 <BR>
   * 
   * @param portlet
   * @param context
   * @param rundata
   * @throws Exception
   */
    @Override
    protected void buildNormalContext(VelocityPortlet portlet, Context context, RunData rundata) throws Exception {
        if (getMode() == null) {
            doWorkflow_category_list(rundata, context);
        }
    }

    /**
   * カテゴリを一覧表示します。 <BR>
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    public void doWorkflow_category_list(RunData rundata, Context context) throws Exception {
        WorkflowCategorySelectData listData = new WorkflowCategorySelectData();
        listData.initField();
        listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1a-rows")));
        listData.doViewList(this, rundata, context);
        setTemplate(rundata, "workflow-category-list");
    }

    /**
   * 申請経路を一覧表示します。 <BR>
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    public void doWorkflow_route_list(RunData rundata, Context context) throws Exception {
        WorkflowRouteSelectData listData = new WorkflowRouteSelectData();
        listData.initField();
        listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1a-rows")));
        listData.doViewList(this, rundata, context);
        setTemplate(rundata, "workflow-route-list");
    }
}

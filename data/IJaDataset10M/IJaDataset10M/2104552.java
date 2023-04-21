package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.workflow.WorkflowRouteSelectData;

/**
 * ワークフロー分類の一覧を処理するクラスです。 <br />
 * 
 */
public class WorkflowRouteListScreen extends WorkflowRouteScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(WorkflowRouteListScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            WorkflowRouteSelectData listData = new WorkflowRouteSelectData();
            listData.initField();
            listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1a-rows")));
            listData.doViewList(this, rundata, context);
            String layout_template = "portlets/html/ja/ajax-workflow-route-list.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[WorkflowRouteListScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }
}

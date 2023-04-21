package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.portal.portlets.VelocityPortlet;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.msgboard.MsgboardTopicSelectData;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 掲示板トピックの一覧を処理するクラスです。
 * 
 */
public class MsgboardTopicListScreen extends MsgboardTopicScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(MsgboardTopicListScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        VelocityPortlet portlet = ALEipUtils.getPortlet(rundata, context);
        String mode = rundata.getParameters().getString(ALEipConstants.MODE);
        try {
            if ("update".equals(mode)) {
                updateState(rundata, context, portlet);
            }
            MsgboardTopicSelectData listData = new MsgboardTopicSelectData();
            listData.initField();
            listData.loadCategoryList(rundata, context);
            listData.setRowsNum(Integer.parseInt(portlet.getPortletConfig().getInitParameter("p1b-rows")));
            listData.doViewList(this, rundata, context);
            String layout_template = "portlets/html/ja/ajax-msgboard-topic-list.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[MsgboardTopicListScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }
}

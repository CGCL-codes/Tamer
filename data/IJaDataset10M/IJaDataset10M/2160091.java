package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.portal.portlets.VelocityPortlet;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.todo.ToDoSelectData;
import com.aimluck.eip.todo.ToDoStateUpdateData;
import com.aimluck.eip.todo.util.ToDoUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ToDoの一覧を処理するクラスです。 <br />
 * 
 */
public class ToDoScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ToDoScreen.class.getName());

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
            ALEipUtils.removeTemp(rundata, context, "tab");
            ToDoSelectData listData = new ToDoSelectData();
            listData.initField();
            listData.setRowsNum(Integer.parseInt(portlet.getPortletConfig().getInitParameter("p1a-rows")));
            listData.setTableColumNum(Integer.parseInt(portlet.getPortletConfig().getInitParameter("p0e-rows")));
            listData.setStrLength(0);
            listData.loadCategoryList(rundata);
            listData.doViewList(this, rundata, context);
            String layout_template = "portlets/html/ja/ajax-todo.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[ToDoScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }

    protected void updateState(RunData rundata, Context context, VelocityPortlet portlet) {
        ToDoStateUpdateData data = new ToDoStateUpdateData();
        data.initField();
        data.doUpdate(this, rundata, context);
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return ToDoUtils.TODO_PORTLET_NAME;
    }
}

package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.todo.ToDoPublicSelectData;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 公開ToDoの詳細画面を処理するクラスです。 <br />
 * 
 */
public class ToDoPublicDetailScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ToDoPublicDetailScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            ToDoPublicSelectData detailData = new ToDoPublicSelectData();
            detailData.initField();
            detailData.doViewDetail(this, rundata, context);
            String entityid = ALEipUtils.getTemp(rundata, context, ALEipConstants.ENTITY_ID);
            context.put(ALEipConstants.ENTITY_ID, entityid);
            String layout_template = "portlets/html/ja/ajax-todo-public-detail.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[ToDoPublicDetailScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }
}

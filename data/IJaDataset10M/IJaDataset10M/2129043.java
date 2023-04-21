package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.cabinet.CabinetSelectData;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 共有フォルダのフォルダの一覧を処理するクラスです。 <br />
 * 
 */
public class CabinetListScreen extends CabinetScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(CabinetListScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            CabinetSelectData listData = new CabinetSelectData();
            listData.initField();
            listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1b-rows")));
            listData.doViewList(this, rundata, context);
            String layout_template = "portlets/html/ja/ajax-cabinet-list.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[CabinetListScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }
}

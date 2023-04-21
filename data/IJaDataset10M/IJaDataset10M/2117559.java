package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.account.AccountPositionSelectData;
import com.aimluck.eip.account.util.AccountUtils;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 役職の詳細画面を処理するクラスです。 <br />
 * 
 */
public class AccountPositionDetailScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AccountPositionDetailScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            AccountPositionSelectData detailData = new AccountPositionSelectData();
            detailData.initField();
            detailData.doViewDetail(this, rundata, context);
            String entityid = ALEipUtils.getTemp(rundata, context, ALEipConstants.ENTITY_ID);
            context.put(ALEipConstants.ENTITY_ID, entityid);
            String layout_template = "portlets/html/ja/ajax-account-position-detail.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[AccountPositionDetailScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return AccountUtils.ACCOUNT_PORTLET_NAME;
    }
}

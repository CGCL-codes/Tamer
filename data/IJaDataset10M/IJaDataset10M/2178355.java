package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.util.ALEipUtils;
import com.aimluck.eip.webmail.WebMailFilterOrderFormData;
import com.aimluck.eip.webmail.util.WebMailUtils;

/**
 * フィルタの順番変更を処理するクラスです。 <br />
 * 
 */
public class WebMailFilterOrderFormScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(WebMailFilterOrderFormScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            doWebMail_filter_order_form(rundata, context);
        } catch (Exception ex) {
            logger.error("[WebMailFilterOrderFormScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }

    private void doWebMail_filter_order_form(RunData rundata, Context context) {
        WebMailFilterOrderFormData formData = new WebMailFilterOrderFormData();
        formData.initField();
        formData.doViewForm(this, rundata, context);
        String layout_template = "portlets/html/ja/ajax-webmail-filter-order-form.vm";
        setTemplate(rundata, context, layout_template);
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return WebMailUtils.WEBMAIL_PORTLET_NAME;
    }
}

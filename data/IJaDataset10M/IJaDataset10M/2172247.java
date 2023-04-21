package com.aimluck.eip.modules.screens;

import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.gadgets.GadgetsAdminFormData;
import com.aimluck.eip.gadgets.util.GadgetsUtils;

/**
 *
 */
public class GadgetsAdminFormScreen extends ALVelocityScreen {

    /**
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        GadgetsAdminFormData formData = new GadgetsAdminFormData();
        formData.initField();
        formData.doViewForm(this, rundata, context);
        String template = "portlets/html/ja/ajax-gadgets-admin-form.vm";
        setTemplate(rundata, context, template);
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return GadgetsUtils.GADGETS_ADMIN_PORTLET_NAME;
    }
}

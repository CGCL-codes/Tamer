package org.yehongyu.websale.modules.screens.manage.permission;

import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.yehongyu.websale.common.secure.NoSecureScreen;

/**
 * ����˵�������µ�¼ҳ��
 * @author yehongyu.org
 * @version 1.0 2007-11-30 ����02:28:35
 */
public class loginagain extends NoSecureScreen {

    protected void doBuildTemplate(RunData runData, Context context) throws Exception {
        runData.setLayoutTemplate("NoLayout.html");
        context.put("isfirst", runData.getParameters().getString("isfirst", ""));
        context.put("reLoginError", runData.getParameters().getString("reLoginError", ""));
    }
}

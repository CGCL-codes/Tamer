package com.aimluck.eip.schedule;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALEipUser;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.util.ALEipUtils;

/**
 * スケジュールのフォームデータを管理するクラスです。
 * 
 */
public class CellScheduleSelectFormByMemberData extends CellScheduleSelectFormData {

    /** <code>logger</code> logger */
    @SuppressWarnings("unused")
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(CellScheduleSelectFormByMemberData.class.getName());

    /** <code>login_user</code> 表示対象ユーザー */
    private ALEipUser targerUser;

    @Override
    public void initField() {
    }

    @Override
    public void init(ALAction action, RunData rundata, Context context) throws ALPageNotFoundException, ALDBErrorException {
        super.init(action, rundata, context);
        String s = rundata.getParameters().getString("selectedmember");
        if (s != null) {
            targerUser = ALEipUtils.getALEipUser(Integer.parseInt(s));
        }
    }

    public ALEipUser getTargerUser() {
        return targerUser;
    }

    public void setTargerUser(ALEipUser targerUser) {
        this.targerUser = targerUser;
    }
}

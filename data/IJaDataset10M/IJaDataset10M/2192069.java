package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.turbine.util.RunData;
import com.aimluck.eip.schedule.CellAppScheduleSelectData;

public class CellAppScheduleScreen extends ALCSVScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ScheduleScreen.class.getName());

    /** 無効 */
    private static final String DEF_REMOTE_NONE = "none";

    /** 有効（全てのリモートから許可） */
    private static final String DEF_REMOTE_ALL = "all";

    /**
   * 接続元のIPアドレスによる制限（IPアドレス設定） 無効：none 有効（全てのリモートから許可）：all
   * 有効（特定のリモートから許可）；192.168.1 や 192.168.1.10
   * 
   */
    private static final String TRUST_IP_ADDR = JetspeedResources.getString("aipo.httpio.trustip_addr", "");

    @Override
    protected void doOutput(RunData rundata) throws Exception {
        String remoteAddr = rundata.getRemoteAddr();
        if (remoteAddr == null || "".equals(remoteAddr) || "".equals(TRUST_IP_ADDR) || DEF_REMOTE_NONE.equals(TRUST_IP_ADDR)) {
            return;
        }
        if (!DEF_REMOTE_ALL.equals(TRUST_IP_ADDR) && !remoteAddr.startsWith(TRUST_IP_ADDR)) {
            return;
        }
        setCsvEncoding("UTF-8");
        super.doOutput(rundata);
    }

    @Override
    protected String getCSVString(RunData rundata) throws Exception {
        try {
            CellAppScheduleSelectData listData = new CellAppScheduleSelectData();
            listData.init(rundata);
            if (listData.validate()) {
                if (!listData.getViewList(rundata)) {
                    return null;
                }
                listData.doFormatCsv();
                return listData.outPutCsv();
            }
            return null;
        } catch (Exception e) {
            logger.error("[ERROR]" + e);
            return null;
        }
    }

    @Override
    protected String getFileName() {
        return "schedule.csv";
    }
}

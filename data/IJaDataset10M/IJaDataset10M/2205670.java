package com.aimluck.eip.eventlog.action;

import java.util.Calendar;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import com.aimluck.eip.cayenne.om.portlet.EipTEventlog;
import com.aimluck.eip.cayenne.om.security.TurbineUser;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.eventlog.util.ALEventlogUtils;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.services.eventlog.ALEventlogConstants;
import com.aimluck.eip.services.eventlog.ALEventlogFactoryService;
import com.aimluck.eip.services.eventlog.ALEventlogHandler;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ログ保存ハンドラ．
 * 
 */
public class ALActionEventlogHandler extends ALEventlogHandler {

    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(ALActionEventlogHandler.class.getName());

    public ALActionEventlogHandler() {
    }

    public static ALEventlogHandler getInstance() {
        return new ALActionEventlogHandler();
    }

    /**
   * ログ
   */
    @Override
    public void log(int entity_id, int portlet_type, String note) {
        logActionEvent(entity_id, portlet_type, note);
    }

    /**
   * ログ
   */
    @Override
    public void log(int entity_id, int portlet_type, String note, String mode) {
        logActionEvent(entity_id, portlet_type, note, mode);
    }

    private void logActionEvent(int entity_id, int portlet_type, String note) {
        RunData rundata = ALEventlogFactoryService.getInstance().getRunData();
        String mode = rundata.getParameters().getString(ALEipConstants.MODE);
        if (mode == null || "".equals(mode)) {
            String action = rundata.getAction();
            if (action == null || "".equals(action)) {
                return;
            }
        } else {
            logActionEvent(entity_id, portlet_type, note, mode);
        }
    }

    private void logActionEvent(int entity_id, int portlet_type, String note, String mode) {
        RunData rundata = ALEventlogFactoryService.getInstance().getRunData();
        int event_type = ALEventlogUtils.getEventTypeValue(mode);
        int uid = ALEipUtils.getUserId(rundata);
        String ip_addr = rundata.getRemoteAddr();
        saveEvent(event_type, uid, portlet_type, entity_id, ip_addr, note);
    }

    /**
   * Login処理
   * 
   * @param mode
   * @return
   */
    @Override
    public void logLogin(int userid) {
        RunData rundata = ALEventlogFactoryService.getInstance().getRunData();
        int event_type = ALEventlogUtils.getEventTypeValue("Login");
        int p_type = ALEventlogConstants.PORTLET_TYPE_LOGIN;
        String ip_addr = rundata.getRemoteAddr();
        saveEvent(event_type, userid, p_type, 0, ip_addr, null);
    }

    /**
   * Logout処理
   * 
   * @param mode
   * @return
   */
    @Override
    public void logLogout(int userid) {
        RunData rundata = ALEventlogFactoryService.getInstance().getRunData();
        int event_type = ALEventlogUtils.getEventTypeValue("Logout");
        int p_type = ALEventlogConstants.PORTLET_TYPE_LOGOUT;
        String ip_addr = rundata.getRemoteAddr();
        saveEvent(event_type, userid, p_type, 0, ip_addr, null);
    }

    /**
   * XLS出力処理
   * 
   * @param mode
   * @return
   */
    @Override
    public void logXlsScreen(int userid, String Note, int _p_type) {
        RunData rundata = ALEventlogFactoryService.getInstance().getRunData();
        int event_type = ALEventlogUtils.getEventTypeValue("xls_screen");
        String ip_addr = rundata.getRemoteAddr();
        saveEvent(event_type, userid, _p_type, 0, ip_addr, null);
    }

    /**
   * 
   * @param event_type
   *          イベント種別
   * @param uid
   *          ユーザーID
   * @param p_type
   *          ポートレットTYPE
   * @param note
   * @return
   */
    private boolean saveEvent(int event_type, int uid, int p_type, int entity_id, String ip_addr, String note) {
        try {
            EipTEventlog log = Database.create(EipTEventlog.class);
            TurbineUser tuser = Database.get(TurbineUser.class, Integer.valueOf(uid));
            log.setTurbineUser(tuser);
            log.setEventDate(Calendar.getInstance().getTime());
            log.setEventType(Integer.valueOf(event_type));
            log.setPortletType(Integer.valueOf(p_type));
            log.setEntityId(Integer.valueOf(entity_id));
            log.setIpAddr(ip_addr);
            log.setCreateDate(Calendar.getInstance().getTime());
            log.setUpdateDate(Calendar.getInstance().getTime());
            log.setNote(note);
            Database.commit();
            return true;
        } catch (Exception ex) {
            Database.rollback();
            logger.error("Exception", ex);
            return false;
        }
    }
}

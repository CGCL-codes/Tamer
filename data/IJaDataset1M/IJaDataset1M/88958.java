package com.dcivision.framework;

import java.sql.Connection;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.framework.bean.EventMessageCode;
import com.dcivision.framework.bean.MtmEventMessageLog;
import com.dcivision.framework.dao.EventMessageCodeDAObject;
import com.dcivision.framework.dao.MtmEventMessageLogDAObject;

public class EventLogger {

    public static final String EVENT_TYPE_INFO = "I";

    public static final String EVENT_TYPE_WARNING = "W";

    public static final String EVENT_TYPE_ERROE = "E";

    private static final Log log = LogFactory.getLog(EventLogger.class);

    public static void log(HttpServletRequest request, String msgCode, String source, String errorDetail) {
        log(request, msgCode, source, errorDetail, null, null);
    }

    public static void log(HttpServletRequest request, String msgCode, String source, String errorDetail, String message1) {
        log(request, msgCode, source, errorDetail, message1, null);
    }

    public static void log(HttpServletRequest request, String msgCode, String source, String errorDetail, String message1, String message2) {
        SessionContainer sessionContainer = (SessionContainer) request.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        String serverName = request.getServerName();
        Locale locale = null;
        String message = null;
        Integer trigerEventUserID = new Integer(0);
        if (sessionContainer != null) {
            trigerEventUserID = sessionContainer.getUserRecordID();
            locale = sessionContainer.getSessionLocale();
        }
        message = MessageResourcesFactory.getMessage(locale, msgCode, message1, message2);
        log(trigerEventUserID, serverName, message, msgCode, source, errorDetail);
    }

    public static void log(Integer trigerEventUserID, String serverName, String message, String msgCode, String source, String errorDetail) {
        SessionContainer sessionContainer = new SessionContainer();
        Connection conn = null;
        EventMessageCode eventCode = null;
        MtmEventMessageLog mtmEventLog = new MtmEventMessageLog();
        Integer eventCodeID = null;
        String strError = null;
        int errorDetailLen = TextUtility.parseInteger(SystemParameterFactory.getSystemParameter(SystemParameterConstant.SYSTEMLOG_EVENTLOG_ERROR_DETAIL));
        if (errorDetailLen > 0 && (errorDetail != null)) {
            strError = errorDetail.substring(0, errorDetailLen);
        }
        try {
            conn = DataSourceFactory.getConnection();
            EventMessageCodeDAObject eventCodeDAO = new EventMessageCodeDAObject(sessionContainer, conn);
            MtmEventMessageLogDAObject mtmEventLogDAO = new MtmEventMessageLogDAObject(sessionContainer, conn);
            eventCode = (EventMessageCode) eventCodeDAO.getEventCodeByMsgCode(msgCode);
            if (eventCode != null) {
                eventCodeID = eventCode.getID();
                mtmEventLog.setServerName(serverName);
                mtmEventLog.setActionTakerID(trigerEventUserID);
                mtmEventLog.setCreatorID(trigerEventUserID);
                mtmEventLog.setUpdaterID(trigerEventUserID);
                mtmEventLog.setMessage(message);
                mtmEventLog.setErrorDetail(strError);
                mtmEventLog.setSourceFunctionCode(source);
                mtmEventLog.setMessageCodeID(eventCodeID);
                mtmEventLogDAO.insertObject(mtmEventLog);
                conn.commit();
            }
        } catch (Exception ex) {
            log.error(ex, ex);
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
        }
    }

    /**
   * Log the event message.
   * @param trigerEventUserID
   * @param serverName
   * @param message
   * @param msgCode
   * @param source
   * @param errorDetail
   */
    public static void logEventMessage(Integer trigerEventUserID, String serverName, String attachMessage, String message, String msgCode, String source, String errorDetail) {
        SessionContainer sessionContainer = new SessionContainer();
        Locale locale = new Locale(GlobalConstant.LOCALE_STR_EN_US);
        Connection conn = null;
        EventMessageCode eventCode = null;
        MtmEventMessageLog mtmEventLog = new MtmEventMessageLog();
        Integer eventCodeID = null;
        String strError = null;
        int errorDetailLen = TextUtility.parseInteger(SystemParameterFactory.getSystemParameter(SystemParameterConstant.SYSTEMLOG_EVENTLOG_ERROR_DETAIL));
        if (errorDetailLen > 0 && (errorDetail != null)) {
            strError = errorDetail.substring(0, errorDetailLen);
        }
        message = MessageResourcesFactory.getMessage(locale, message);
        message = attachMessage + message;
        try {
            conn = DataSourceFactory.getConnection();
            EventMessageCodeDAObject eventCodeDAO = new EventMessageCodeDAObject(sessionContainer, conn);
            MtmEventMessageLogDAObject mtmEventLogDAO = new MtmEventMessageLogDAObject(sessionContainer, conn);
            eventCode = (EventMessageCode) eventCodeDAO.getEventCodeByErrorCode(msgCode);
            if (eventCode != null) {
                eventCodeID = eventCode.getID();
                mtmEventLog.setServerName(serverName);
                mtmEventLog.setActionTakerID(trigerEventUserID);
                mtmEventLog.setCreatorID(trigerEventUserID);
                mtmEventLog.setUpdaterID(trigerEventUserID);
                mtmEventLog.setMessage(message);
                mtmEventLog.setErrorDetail(strError);
                mtmEventLog.setSourceFunctionCode(source);
                mtmEventLog.setMessageCodeID(eventCodeID);
                mtmEventLogDAO.insertObject(mtmEventLog);
                conn.commit();
            }
        } catch (Exception ex) {
            log.error(ex, ex);
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
        }
    }

    /**
   * Log the event message.
   * @param trigerEventUserID
   * @param serverName
   * @param message
   * @param msgCode
   * @param source
   * @param errorDetail
   */
    public static void logEventMessageForSchedule(Integer trigerEventUserID, String serverName, String message, String msgCode, String source, String errorDetail) {
        SessionContainer sessionContainer = new SessionContainer();
        Locale locale = new Locale(GlobalConstant.LOCALE_STR_EN_US);
        Connection conn = null;
        EventMessageCode eventCode = null;
        MtmEventMessageLog mtmEventLog = new MtmEventMessageLog();
        Integer eventCodeID = null;
        String strError = null;
        int errorDetailLen = TextUtility.parseInteger(SystemParameterFactory.getSystemParameter(SystemParameterConstant.SYSTEMLOG_EVENTLOG_ERROR_DETAIL));
        if (errorDetailLen > 0 && (errorDetail != null)) {
            strError = errorDetail.substring(0, errorDetailLen);
        }
        try {
            conn = DataSourceFactory.getConnection();
            EventMessageCodeDAObject eventCodeDAO = new EventMessageCodeDAObject(sessionContainer, conn);
            MtmEventMessageLogDAObject mtmEventLogDAO = new MtmEventMessageLogDAObject(sessionContainer, conn);
            eventCode = (EventMessageCode) eventCodeDAO.getEventCodeByErrorCode(msgCode);
            if (eventCode != null) {
                eventCodeID = eventCode.getID();
                mtmEventLog.setServerName(serverName);
                mtmEventLog.setActionTakerID(trigerEventUserID);
                mtmEventLog.setCreatorID(trigerEventUserID);
                mtmEventLog.setUpdaterID(trigerEventUserID);
                mtmEventLog.setMessage(message);
                mtmEventLog.setErrorDetail(strError);
                mtmEventLog.setSourceFunctionCode(source);
                mtmEventLog.setMessageCodeID(eventCodeID);
                mtmEventLogDAO.insertObject(mtmEventLog);
                conn.commit();
            }
        } catch (Exception ex) {
            log.error(ex, ex);
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            } finally {
                conn = null;
            }
        }
    }
}

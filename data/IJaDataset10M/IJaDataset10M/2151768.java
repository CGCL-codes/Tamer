package com.dcivision.alert.core;

import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.alert.bean.UpdateAlertLogAction;
import com.dcivision.alert.bean.UpdateAlertSystemLog;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.UserInfoFactory;

/**
 * <p>
 * Class Name: AbstactMessageFormatter.java
 * </p>
 *
 * @author Greatwall shao
 * @company DCIVision Limited
 * @creation date 2006-11-06
 */
public abstract class AbstractMessageFormatter implements MessageFormatter {

    private static final Log log = LogFactory.getLog(AbstractMessageFormatter.class);

    protected Connection dbConn;

    protected SessionContainer sessionContainer;

    protected UpdateAlertSystemLog updateAlertSystemLog;

    private String messageType;

    protected String messageSubject;

    protected String userName;

    private boolean isList = false;

    private AlertManager alertManager;

    private String objectType;

    private String actionType;

    public AbstractMessageFormatter(UpdateAlertSystemLog bean, SessionContainer sessionContainer, Connection dbConn) throws ApplicationException {
        this.updateAlertSystemLog = bean;
        this.dbConn = dbConn;
        this.sessionContainer = sessionContainer;
        this.userName = UserInfoFactory.getUserFullName(updateAlertSystemLog.getActionUserID());
        alertManager = new AlertManager(sessionContainer, dbConn);
        initialize();
    }

    public String getSubjectWithoutLink() {
        return this.messageSubject;
    }

    public String getSubjectAsTitle() {
        String message;
        message = this.messageSubject.replaceAll("\"", "'");
        return message;
    }

    public String getProperLengSubject() {
        return this.messageSubject;
    }

    public String isRead() {
        String isRead = "no";
        try {
            if (alertManager.checkForSystemLogActionByActionType(updateAlertSystemLog.getID(), UpdateAlertLogAction.READ_ACTION)) {
                isRead = "yes";
            } else {
                isRead = "no";
            }
        } catch (ApplicationException e) {
            log.error(e, e);
        }
        return isRead;
    }

    private void initialize() throws ApplicationException {
        this.setObjectType(updateAlertSystemLog.getObjectType().trim());
        this.setActionType(updateAlertSystemLog.getActionType().trim());
        makeMessageType();
        setSystemMakeSubject();
    }

    public String getSubject() {
        String str = "";
        String isList = this.isList() ? "yes" : "";
        str = "<a class=\"strongllink\" href=\"#\" " + " title = \" " + getSubjectAsTitle() + " \" " + "onClick=\"OpenCenteredPopup('./EditMessageDetail.do?" + "messageType=" + this.messageType + "&objectid=" + updateAlertSystemLog.getObjectID().toString() + "&systemLogID=" + updateAlertSystemLog.getID() + "&isList=" + isList + "', 'DCIVisionViewWorkflowProgress', '850', '600', 'status=no,menubar=no,scrollbars=yes,resizable=yes,toolbar=no');\">";
        str += getProperLengSubject();
        str += "</a>";
        return str;
    }

    public String getUrlLink() {
        String str = "";
        String isList = this.isList() ? "yes" : "";
        str = "EditMessageDetail.do?" + "messageType=" + this.messageType + "&objectid=" + updateAlertSystemLog.getObjectID().toString() + "&systemLogID=" + updateAlertSystemLog.getID() + "&isList=" + isList;
        return str;
    }

    protected abstract void makeMessageType();

    protected abstract void setSystemMakeSubject() throws ApplicationException;

    protected String getMessageResource(String key) {
        return MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), key);
    }

    protected String getMessageResource(String key, String arg) {
        return MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), key, arg);
    }

    protected String getMessageResource(String key, String arg0, String arg1) {
        return MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), key, arg0, arg1);
    }

    protected String getMessageResource(String key, String arg0, String arg1, String arg2) {
        return MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), key, arg0, arg1, arg2);
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean isList) {
        this.isList = isList;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public SessionContainer getSessionContainer() {
        return sessionContainer;
    }

    public void setSessionContainer(SessionContainer sessionContainer) {
        this.sessionContainer = sessionContainer;
    }

    public UpdateAlertSystemLog getUpdateAlertSystemLog() {
        return updateAlertSystemLog;
    }

    public void setUpdateAlertSystemLog(UpdateAlertSystemLog updateAlertSystemLog) {
        this.updateAlertSystemLog = updateAlertSystemLog;
    }

    public abstract String getSender();

    public Connection getDbConn() {
        return dbConn;
    }

    public void setDbConn(Connection dbConn) {
        this.dbConn = dbConn;
    }

    public String toString() {
        StringBuffer desc = new StringBuffer();
        desc.append("actionType=" + getActionType());
        desc.append(", objectType=" + getObjectType());
        desc.append(", messageType=" + getMessageType());
        desc.append(", userType=" + userName);
        return desc.toString();
    }
}

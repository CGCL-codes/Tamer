package com.quikj.application.web.talk.plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.quikj.ace.messages.vo.talk.SetupResponseMessage;
import com.quikj.server.app.EndPointInterface;
import com.quikj.server.framework.AceLogger;
import com.quikj.server.framework.AceSQL;
import com.quikj.server.framework.AceSQLMessage;

public class DbUnavailableUserTransfer implements DbOperationInterface {

    private String username;

    private SessionInfo session;

    private EndPointInterface activeCalledParty;

    private SetupResponseMessage originalSetupRespMessage;

    private int originalSetupRespStatus;

    private String originalSetupRespReason;

    private int operationId;

    private String lastError = "";

    private ServiceController parent;

    private AceSQL database;

    public DbUnavailableUserTransfer(String username, SessionInfo session, EndPointInterface active_called_party, SetupResponseMessage original_setupresp_message, int original_setupresp_status, String original_setupresp_reason, ServiceController parent, AceSQL database) {
        this.parent = parent;
        this.database = database;
        this.username = username;
        this.session = session;
        activeCalledParty = active_called_party;
        originalSetupRespMessage = original_setupresp_message;
        originalSetupRespStatus = original_setupresp_status;
        originalSetupRespReason = original_setupresp_reason;
    }

    public void cancel() {
        database.cancelSQL(operationId, parent);
    }

    public boolean checkForTransfer() {
        try {
            Statement statement = UserTable.getTransferInfoQueryStatement(database.getConnection(), username);
            operationId = database.executeSQL(statement, (String[]) null, this);
            if (operationId == -1) {
                lastError = parent.getErrorMessage();
                return false;
            }
            return true;
        } catch (SQLException ex) {
            lastError = "Error creating SQL statement : " + ex.getMessage();
            return false;
        }
    }

    public EndPointInterface getEndPoint() {
        return session.getCallingEndPoint();
    }

    public String getLastError() {
        return lastError;
    }

    public boolean processResponse(AceSQLMessage message) {
        if (message.getStatus() == AceSQLMessage.SQL_ERROR) {
            AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, parent.getName() + "- DbUnavailableUserTransfer.processResponse() -- Database error result querying user name " + username + ".");
            parent.finishSetupResponse(originalSetupRespMessage, originalSetupRespStatus, originalSetupRespReason, activeCalledParty);
            return true;
        }
        UserElement user_data = new UserElement();
        try {
            ResultSet result = message.getResultSet();
            if ((result == null) || (result.next() == false)) {
                parent.finishSetupResponse(originalSetupRespMessage, originalSetupRespStatus, originalSetupRespReason, activeCalledParty);
                return true;
            }
            UserTable.processTransferInfoQueryResult(user_data, result);
        } catch (Exception ex) {
            AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, parent.getName() + "- DbUnavailableUserTransfer.processResponse() -- Exception processing result : " + ex.getMessage());
            parent.finishSetupResponse(originalSetupRespMessage, originalSetupRespStatus, originalSetupRespReason, activeCalledParty);
            return true;
        }
        parent.transferUserUnavailableCall(session, user_data, activeCalledParty, username);
        return true;
    }
}

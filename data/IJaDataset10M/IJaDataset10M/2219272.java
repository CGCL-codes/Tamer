package com.macro10.switchboard.commands;

import com.macro10.switchboard.AbstractCommand;
import com.macro10.switchboard.MessageStatus;
import com.macro10.switchboard.Request;
import com.macro10.switchboard.Response;
import com.macro10.switchboard.ResponseMessage;
import com.macro10.switchboard.entity.Account;
import com.macro10.switchboard.entity.DataObject;
import com.macro10.switchboard.service.AccountService;
import com.macro10.switchboard.service.UserTransaction;
import java.util.Collection;

/**
 * Requires headers: username(optional)
 * Filters: restrict_user
 */
public class GetAccount extends AbstractCommand {

    @Override
    public ExecuteResult execute(Request request, Response response) {
        String username = request.getHeader("username");
        if (username == null) {
            username = (String) request.getSession().getAttribute("username");
        }
        UserTransaction ut = null;
        try {
            ut = new UserTransaction();
            AccountService accountService = new AccountService(ut);
            Account account = accountService.getAccount(username);
            if (account != null) {
                Collection<DataObject> dataObjects = account.getObjects();
                ResponseMessage responseMessage = new ResponseMessage(MessageStatus.OK);
                responseMessage.setHeader("message", "account.details");
                responseMessage.setHeader("user-name", username);
                responseMessage.setHeader("user-language", account.getLanguage());
                responseMessage.setHeader("user-online", String.valueOf(account.isOnline()));
                if (account.isOnline()) {
                    responseMessage.setHeader("user-session", account.getSessionId());
                }
                for (DataObject obj : dataObjects) {
                    responseMessage.setHeader(obj.getName(), obj.getValue().toString());
                }
                response.addMessage(responseMessage);
            } else {
                ResponseMessage responseMessage = new ResponseMessage(MessageStatus.PROCESSING_ERROR);
                responseMessage.setHeader("message", "account.not.found");
                response.addMessage(responseMessage);
            }
            ut.commit();
        } catch (Exception e) {
            response.clearMessages();
            ResponseMessage responseMessage = new ResponseMessage(MessageStatus.PROCESSING_EXCEPTION);
            responseMessage.setHeader("message", "account.retrieval.failed");
            responseMessage.setBody(e.getMessage());
            response.addMessage(responseMessage);
            if (ut != null) {
                ut.rollback();
            }
        }
        return ExecuteResult.START_POST;
    }
}

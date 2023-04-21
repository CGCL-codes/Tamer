package org.openhealthexchange.messagestore.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.openhealthexchange.messagestore.service.MessageStoreService;
import org.openhealthexchange.openpixpdq.ihe.log.MessageStore;
import org.openhealthexchange.messagestore.vo.MessageStoreBean;

/**
 * Action class to call MessageStoreService.
 * 
 * @author Anil kumar
 * @date Nov 25, 2008
 */
public class MessageStoreAction extends Action {

    private static Logger log = Logger.getLogger(MessageStoreAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection<MessageStore> personlist = null;
        ;
        MessageStoreBean messageForm = (MessageStoreBean) form;
        if (messageForm.getAction() == null || messageForm.getAction().equals("")) {
            personlist = new ArrayList<MessageStore>();
            request.setAttribute("personlist", personlist);
            return mapping.findForward("success");
        }
        if (messageForm != null && messageForm.getAction() != null && messageForm.getAction().equalsIgnoreCase("Search")) {
            MessageStore messageLog = new MessageStore();
            if (messageForm.getIp() != null && messageForm.getIp() != "") messageLog.setIp(messageForm.getIp());
            if (messageForm.getErrorMessage() != null && messageForm.getErrorMessage() != "") messageLog.setErrorMessage(messageForm.getErrorMessage());
            if (messageForm.getMessageDate() != null && !messageForm.getMessageDate().equalsIgnoreCase("mm/dd/yyyy")) messageLog.setMessageDate(_convertStringToDate(messageForm.getMessageDate()));
            if (messageForm.getMessageId() != null && messageForm.getMessageId() != "") messageLog.setMessageId(messageForm.getMessageId());
            if (messageForm.getSendingFacility() != null && messageForm.getSendingFacility() != "") messageLog.setSendingFacility(messageForm.getSendingFacility());
            if (messageForm.getSendingApplication() != null && messageForm.getSendingApplication() != "") messageLog.setSendingApplication(messageForm.getSendingApplication());
            if (messageForm.getReceivingFacility() != null && messageForm.getReceivingFacility() != "") messageLog.setReceivingFacility(messageForm.getReceivingFacility());
            if (messageForm.getReceivingApplication() != null && messageForm.getReceivingApplication() != "") messageLog.setReceivingApplication(messageForm.getReceivingApplication());
            MessageStoreService messageservice = new MessageStoreService();
            personlist = messageservice.searchLog(messageLog);
            if (personlist != null) {
                request.setAttribute("personlist", personlist);
            } else {
                personlist = new ArrayList<MessageStore>();
                request.setAttribute("personlist", personlist);
            }
        }
        return mapping.findForward("success");
    }

    /**
		 * This method converts String type to Date type.
		 * @param String date
		 * @return Date
		 */
    private Date _convertStringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (date == null) return null;
        try {
            Date date1 = null;
            date1 = dateFormat.parse(date);
            return date1;
        } catch (ParseException pex) {
            return null;
        }
    }
}

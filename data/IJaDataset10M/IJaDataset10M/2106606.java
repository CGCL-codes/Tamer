package org.openhealthexchange.messagestore.vo;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class MessageStoreBean extends ActionForm {

    /**
	 * 
	 * @author Anil kumar
	 * @date Nov 25, 2008
	 */
    private static final long serialVersionUID = 1L;

    private String ip = null;

    private String messageDate = "DD-MM-YYYY";

    private String messageId = null;

    private String errorMessage = null;

    private String messageType = null;

    private String sendingFacility = null;

    private String sendingApplication = null;

    private String receivingFacility = null;

    private String receivingApplication = null;

    private String action = null;

    /**
	 * 
	 * @return ip
	 */
    public String getIp() {
        return ip;
    }

    /**
	 * 
	 * @param ip
	 */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
	 * 
	 * @return messageDate
	 */
    public String getMessageDate() {
        return messageDate;
    }

    /**
	 * 
	 * @param messageDate
	 */
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    /**
	 * 
	 * @return messageId
	 */
    public String getMessageId() {
        return messageId;
    }

    /**
	 * 
	 * @param messageId
	 */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
	 * 
	 * @return errorMessage
	 */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
	 * 
	 * @param errorMessage
	 */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
	 * 
	 * @return sendingFacility
	 */
    public String getSendingFacility() {
        return sendingFacility;
    }

    /**
	 * 
	 * @param sendingFacility
	 */
    public void setSendingFacility(String sendingFacility) {
        this.sendingFacility = sendingFacility;
    }

    /**
	 * 
	 * @return sendingApplication
	 */
    public String getSendingApplication() {
        return sendingApplication;
    }

    /**
	 * 
	 * @param sendingApplication
	 */
    public void setSendingApplication(String sendingApplication) {
        this.sendingApplication = sendingApplication;
    }

    /**
	 * 
	 * @return receivingFacility
	 */
    public String getReceivingFacility() {
        return receivingFacility;
    }

    /**
	 * 
	 * @param receivingFacility
	 */
    public void setReceivingFacility(String receivingFacility) {
        this.receivingFacility = receivingFacility;
    }

    /**
	 * 
	 * @return receivingApplication
	 */
    public String getReceivingApplication() {
        return receivingApplication;
    }

    /**
	 * 
	 * @param receivingApplication
	 */
    public void setReceivingApplication(String receivingApplication) {
        this.receivingApplication = receivingApplication;
    }

    /**
	 * 
	 * @return action
	 */
    public String getAction() {
        return action;
    }

    /**
	 * 
	 * @param action
	 */
    public void setAction(String action) {
        this.action = action;
    }

    /**
	 * 
	 * @return messageType
	 */
    public String getMessageType() {
        return messageType;
    }

    /**
	 * 
	 * @param messageType
	 */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
	 * Reset all properties to their default values.
	 *
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.ip = null;
        this.messageDate = "DD/MM/YYYY";
        this.messageId = null;
        this.errorMessage = null;
        this.sendingFacility = null;
        this.sendingApplication = null;
        this.receivingFacility = null;
        this.receivingApplication = null;
        this.messageType = null;
        this.action = null;
    }
}

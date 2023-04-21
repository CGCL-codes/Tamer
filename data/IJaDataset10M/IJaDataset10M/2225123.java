package com.vayoodoot.message;

import org.apache.log4j.Logger;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Jan 14, 2007
 * Time: 6:44:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Message {

    protected String messageType;

    private String messageStatus;

    protected String loginName;

    protected String targetUserName;

    protected String sessionToken;

    protected String errorCode;

    protected String errorMessage;

    protected String ip;

    protected int port;

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILURE = "FAILURE";

    public static final int PACKET_LENGTH = 10000;

    public static final int MESSAGE_RECIEVE_TIMEOUT = 10000;

    public static final int MESSAGE_POLL_INTERVAL = 600;

    public static final int MESSAGE_POLL_COUNT = 10;

    private static String messageHeader;

    private static String messageFooter;

    private static Logger logger = Logger.getLogger(Message.class);

    static {
        try {
            messageHeader = MessageFormatter.getMessageString("com.vayoodoot.message.MessageHeader");
            messageFooter = MessageFormatter.getMessageString("com.vayoodoot.message.MessageFooter");
        } catch (MessageException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String getMessageString(String messageName) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("<vayoodoot>");
            sb.append(getMessageHeader());
            sb.append(MessageFormatter.getMessageString(messageName));
            sb.append(getMessageFooter());
            sb.append("</vayoodoot>");
            return (sb.toString());
        } catch (MessageException me) {
            logger.fatal("Exception while loading message: " + messageName + ":" + me, me);
        }
        return null;
    }

    protected Message(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);
        Field[] fields = this.getClass().getDeclaredFields();
        result.append(newLine);
        for (Field field : fields) {
            if (field.getName().equals("messageString")) continue;
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("  Error Code: " + errorCode);
        result.append(newLine);
        result.append("  Error Message: " + errorMessage);
        result.append(newLine);
        result.append("}");
        return result.toString();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * THis method will be invoked by the MessageListener class when a new element has arrived
     *
     * @param elementName
     * @param elementValue
     */
    public void recievedElement(String elementName, String elementValue) {
        if (elementName.equalsIgnoreCase("error_code")) {
            errorCode = elementValue;
        }
        if (elementName.equalsIgnoreCase("error_message")) {
            errorMessage = elementValue;
        }
        if (elementName.equalsIgnoreCase("login_name")) {
            loginName = elementValue;
        }
        if (elementName.equalsIgnoreCase("session_token")) {
            sessionToken = elementValue;
        }
        if (elementName.equalsIgnoreCase("message_status")) {
            messageStatus = elementValue;
        }
    }

    public boolean hasErrors() {
        if (errorCode != null && errorCode.trim().length() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public static String getMessageHeader() {
        return messageHeader;
    }

    public static String getMessageFooter() {
        return messageFooter;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    protected HashMap getValuesMap() {
        HashMap hm = new HashMap();
        hm.put("LOGIN_NAME", loginName);
        hm.put("SESSION_TOKEN", sessionToken);
        hm.put("MESSAGE_STATUS", messageStatus);
        hm.put("MESSAGE_TYPE", messageType);
        hm.put("ERROR_CODE", errorCode);
        hm.put("ERROR_MESSAGE", errorMessage);
        return hm;
    }

    public abstract String getXMLString() throws MessageException;

    public Message[] getSplittedMessages() throws MessageException {
        return new Message[] { this };
    }
}

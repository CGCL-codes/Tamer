package com.gever.exception;

import java.sql.SQLWarning;
import java.util.Properties;

public class DefaultException extends Exception {

    /**
	 * ��̬��������ʾ���󼶱�
	 */
    public static final String INFO = "info";

    public static final String WARN = "warn";

    public static final String DEBUG = "debug";

    public static final String ERROR = "error";

    public static boolean isFileMsg = true;

    private Throwable eThrow;

    private String errorCode;

    private String errorDescription = "";

    private String errorModel = null;

    private String errorLevel = WARN;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorModel() {
        return errorModel;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public String toString() {
        return new StringBuffer().append("Error code:").append(errorCode).append(" Error level:").append(errorLevel).append(" Error model:").append(errorModel).append(" Error description:").append(errorDescription).toString();
    }

    public DefaultException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public DefaultException(String errorCode, String errorLevel) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
    }

    public DefaultException(String errorCode, String errorLevel, String errorModel) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorModel = errorModel;
        this.errorLevel = errorLevel;
    }

    public DefaultException(String errorCode, String errorDescription, String errorModel, String errorLevel) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorModel = errorModel;
        this.errorLevel = errorLevel;
    }

    public DefaultException(String errorCode, Throwable aThrow) {
        super(errorCode, aThrow);
        this.errorCode = errorCode;
        this.errorDescription = aThrow.getMessage();
        this.eThrow = aThrow;
    }

    public DefaultException(String errorCode, String errorLevel, Throwable aThrow) {
        super(errorCode, aThrow);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
        this.errorDescription = aThrow.getMessage();
        this.eThrow = aThrow;
    }

    public DefaultException(Throwable eThrow) {
        super(eThrow);
        String errCode = "";
        String errLevel = "";
        String errMsg = "";
        if (eThrow instanceof DefaultException) {
            DefaultException de = (DefaultException) eThrow;
            errCode = de.errorCode;
            errMsg = de.getErrorDescription();
            errLevel = de.errorLevel;
        } else if (eThrow instanceof SQLWarning) {
            errCode = "err.system.SQLWarning";
            errMsg = eThrow.getMessage();
            errLevel = WARN;
        } else {
            errCode = eThrow.getClass().getName();
            errMsg = eThrow.getMessage();
        }
        this.errorCode = errCode;
        this.errorLevel = errLevel;
        this.errorDescription = errMsg;
    }

    /**
	 * �Ӵ�����Ϣ�����ļ��ж�ȡ������Ϣ
	 * 
	 * @param strErrorCode
	 * @return
	 */
    public String getErrorMessage(String strErrorCode) {
        String strMsg = "";
        try {
            ErrorConfig config = ErrorConfig.getInstance();
            Properties prop = config.getProperties();
            strMsg = prop.getProperty(strErrorCode);
            if (strMsg == null || "".equals(strMsg)) strMsg = strErrorCode;
            this.errorDescription = strMsg;
        } catch (java.io.IOException e) {
            isFileMsg = false;
        }
        return strMsg;
    }

    public void setIsFileMsg(boolean isFileMsg) {
        this.isFileMsg = isFileMsg;
    }

    public boolean getIsFileMsg() {
        return isFileMsg;
    }
}

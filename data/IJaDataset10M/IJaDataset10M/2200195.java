package com.nhncorp.usf.core.call;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class DefaultFault implements Fault
 * 
 * @author Web Platform Development Team
 */
public class DefaultFault implements Fault {

    private static final long serialVersionUID = 581698344218498352L;

    private final String code;

    private final String message;

    private final Object detail;

    /**
	 * DefaultFault constructor
	 * 
	 * @param code 발생한 errorcode 
	 */
    public DefaultFault(String code) {
        this(code, null, null);
    }

    /**
	 * DefaultFault constructor
	 * 
	 * @param code 발생한 errorcode 
	 * @param message 발생한 errorcode에 관한 message
	 */
    public DefaultFault(String code, String message) {
        this(code, message, null);
    }

    /**
	 * DefaultFault constructor
	 * 
	 * @param code 발생한 errorcode 
	 * @param message 발생한 errorcode에 관한 message
	 * @param detail 발생한 errorcode에 관한 자세한 message
	 */
    public DefaultFault(String code, String message, Object detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    /**
	 * DefaultFault constructor
	 * exception에 관련되 정보를 모아서 Fault의 detail값에 Map형태로 저장합니다.
	 * 
	 * @param exception 수행중 일어난 exception
	 */
    public DefaultFault(Throwable exception) {
        if (exception == null) {
            throw new NullPointerException("exception");
        }
        this.code = "";
        this.message = exception.getMessage();
        Map<String, Object> exceptionDetail = new HashMap<String, Object>(2);
        exceptionDetail.put("info", "classname=" + exception.getClass().getName());
        StringWriter detailOut = new StringWriter();
        exception.printStackTrace(new PrintWriter(detailOut));
        exceptionDetail.put("stacktrace", detailOut.getBuffer().toString());
        this.detail = exceptionDetail;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getDetail() {
        return detail;
    }

    /**
	 * {@inheritDoc}
	 */
    public String toString() {
        return "fault: (" + code + ") " + message;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DefaultFault)) {
            return false;
        }
        DefaultFault that = (DefaultFault) object;
        if (code != null ? !code.equals(that.code) : that.code != null) {
            return false;
        }
        if (detail != null ? !detail.equals(that.detail) : that.detail != null) {
            return false;
        }
        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }
}

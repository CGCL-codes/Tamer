package org.ws4d.java.message;

import org.ws4d.java.util.StringUtil;

/**
 * 
 */
public class SOAPException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2359211597196944496L;

    private final FaultMessage fault;

    /**
	 * 
	 */
    public SOAPException() {
        this(null, null);
    }

    /**
	 * @param s the detail message
	 */
    public SOAPException(String s) {
        this(s, null);
    }

    /**
	 * @param fault the SOAP Fault to encapsulate
	 */
    public SOAPException(FaultMessage fault) {
        this(null, fault);
    }

    /**
	 * @param s the detail message
	 * @param fault the SOAP Fault to encapsulate
	 */
    public SOAPException(String s, FaultMessage fault) {
        super(s);
        this.fault = fault;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(StringUtil.formatClassName(getClass()));
        sb.append(": [ fault=").append(fault);
        sb.append(" ]");
        return sb.toString();
    }

    /**
	 * @return the fault
	 */
    public FaultMessage getFault() {
        return fault;
    }
}

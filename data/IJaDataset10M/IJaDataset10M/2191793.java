package org.jabber.xdb;

import java.util.Vector;

public class XDBResponse {

    private String sResponseType;

    private XDBExtension extension;

    public XDBExtension getExtension() {
        return extension;
    }

    public String getResponseType() {
        return sResponseType;
    }

    public void setExtension(XDBExtension xdbe) {
        extension = xdbe;
    }

    public void setResponseType(String sResponseType) {
        this.sResponseType = sResponseType;
    }

    private boolean bErrorMessage;

    private String sErrorCode;

    private String sErrorText;

    /** default constructor comment **/
    public XDBResponse() {
        bErrorMessage = false;
    }

    public String getErrorCode() {
        return sErrorCode;
    }

    public String getErrorText() {
        return sErrorText;
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (5/1/2001 6:36:12 PM)
	 * @return boolean
	 */
    public boolean isErrorMessage() {
        return bErrorMessage;
    }

    public void setErrorCode(String sErrorCode) {
        this.sErrorCode = sErrorCode;
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (5/1/2001 6:36:12 PM)
	 * @param newErrorMessage boolean
	 */
    public void setErrorMessage(boolean newErrorMessage) {
        bErrorMessage = newErrorMessage;
    }

    public void setErrorText(String sErrorText) {
        this.sErrorText = sErrorText;
    }
}

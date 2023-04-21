package com.quikj.server.log;

import java.util.ArrayList;
import java.util.List;
import com.quikj.server.framework.AceLogger;

/**
 * 
 * @author Elizabeth
 */
public class LogEmailInfo {

    private int emailType;

    private List emailToAddress;

    private List emailCcAddress;

    /** Creates a new instance of LogEmailInfo */
    public LogEmailInfo() {
        this.emailType = AceLogger.NUM_MSG_TYPES;
        this.emailToAddress = new ArrayList();
        this.emailCcAddress = new ArrayList();
    }

    public java.util.List getEmailCcAddress() {
        return emailCcAddress;
    }

    String getEmailCcAddress(int indexCcAddresses) {
        return (String) this.emailCcAddress.get(indexCcAddresses);
    }

    /**
	 * Getter for property emailToAddress.
	 * 
	 * @return Value of property emailToAddress.
	 * 
	 */
    public java.util.List getEmailToAddress() {
        return emailToAddress;
    }

    String getEmailToAddress(int indexToAddresses) {
        return (String) this.emailToAddress.get(indexToAddresses);
    }

    public int getEmailType() {
        return this.emailType;
    }

    public boolean getLogEmailInfo(int error_type, LogEmailInfo tempInfo) {
        boolean found = false;
        int index;
        if (this.emailType == error_type) {
            tempInfo.emailToAddress = this.emailToAddress;
            tempInfo.emailCcAddress = this.emailCcAddress;
            found = true;
        }
        return found;
    }

    public void setEmailCcAddress(java.util.List emailCcAddress) {
        this.emailCcAddress = emailCcAddress;
    }

    public void setEmailCcAddress(String ccAddress) {
        this.emailCcAddress.add(ccAddress);
    }

    public void setEmailToAddress(java.util.List emailToAddress) {
        this.emailToAddress = emailToAddress;
    }

    public void setEmailToAddress(String toAddress) {
        this.emailToAddress.add(toAddress);
    }

    public void setEmailType(int emailType) {
        this.emailType = emailType;
    }
}

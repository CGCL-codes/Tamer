package org.softmed.ATComm.sms;

import java.util.ArrayList;
import java.util.List;

public class SMSBatch {

    String prefix;

    List<SMS> smsList = new ArrayList<SMS>();

    public List<SMS> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<SMS> sms) {
        this.smsList = sms;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

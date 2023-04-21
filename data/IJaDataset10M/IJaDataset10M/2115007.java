package com.vangent.hieos.xutil.atna;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bernie Thuman
 */
public class ATNAAuditEventPatientRecord extends ATNAAuditEvent {

    private String messageId;

    private List<String> patientIds = new ArrayList<String>();

    /**
     *
     * @return
     */
    public List<String> getPatientIds() {
        return patientIds;
    }

    /**
     *
     * @param patientId
     */
    public void addPatientId(String patientId) {
        patientIds.add(patientId);
    }

    /**
     *
     * @return
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
}

package com.vinny.xacml.event;

import java.util.ArrayList;
import java.util.EventObject;

public class TwoPhaseTransactionEvent extends EventObject {

    public static final String INIT = "INIT";

    public static final String P1_QUERY_TO_COMMIT = "P1_QUERY_TO_COMMIT";

    public static final String P1_AGREEMENT = "P1_AGREEMENT";

    public static final String P2_PERMIT = "P2_PERMIT";

    public static final String P2_DENY = "P2_DENY";

    public static final String P2_ACKNOWLEDGEMENT = "P2_ACKNOWLEDGEMENT";

    String originatorPdpId;

    String transactionId;

    String type;

    ArrayList<Boolean> statusOfEvaluation;

    public ArrayList<Boolean> getStatusOfEvaluation() {
        return statusOfEvaluation;
    }

    public void setStatusOfEvaluation(ArrayList<Boolean> statusOfEvaluation) {
        this.statusOfEvaluation = statusOfEvaluation;
    }

    public TwoPhaseTransactionEvent(Object source, String originatorPdpId, String transactionId, String type) {
        super(source);
        this.originatorPdpId = originatorPdpId;
        this.transactionId = transactionId;
        this.type = type;
    }

    public String getOriginatorPdpId() {
        return originatorPdpId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{ originator=" + originatorPdpId + ", tId=" + transactionId + " type=" + type + "}";
    }
}

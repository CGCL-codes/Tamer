package org.posterita.core;

import java.util.HashMap;
import org.compiere.process.DocAction;

public class UDIMap {

    public static final HashMap<String, String> docStatusMap;

    static {
        docStatusMap = new HashMap<String, String>();
        docStatusMap.put(DocAction.STATUS_Approved, "Approved");
        docStatusMap.put(DocAction.STATUS_Completed, "Completed");
        docStatusMap.put(DocAction.STATUS_Closed, "Closed");
        docStatusMap.put(DocAction.STATUS_Drafted, "Drafted");
        docStatusMap.put(DocAction.STATUS_InProgress, "InProgress");
        docStatusMap.put(DocAction.STATUS_Invalid, "Invalid");
        docStatusMap.put(DocAction.STATUS_NotApproved, "Not Approved");
        docStatusMap.put(DocAction.STATUS_Reversed, "Reversed");
        docStatusMap.put(DocAction.STATUS_Unknown, "UnKnown");
        docStatusMap.put(DocAction.STATUS_Voided, "Voided");
        docStatusMap.put(DocAction.STATUS_WaitingConfirmation, "Waiting Confirmation");
        docStatusMap.put(DocAction.STATUS_WaitingPayment, "Waiting payment");
    }
}

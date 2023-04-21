package com.k_int.z3950.util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.PrintStream;
import java.io.IOException;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.*;
import java.math.BigInteger;
import com.k_int.util.RPNQueryRep.*;
import com.k_int.util.PrefixLang.*;
import com.k_int.codec.util.*;
import com.k_int.IR.*;
import org.apache.log4j.Category;

public class ZTargetEndpoint extends Thread {

    private Vector apduListeners = new Vector();

    private Socket z_assoc = null;

    private InputStream incoming_data = null;

    private OutputStream outgoing_data = null;

    private PDU_codec codec = PDU_codec.getCodec();

    private OIDRegister reg = OIDRegister.getRegister();

    private boolean running = true;

    private static Category cat = Category.getInstance("ZTargetEndpoint");

    public ZTargetEndpoint(Socket s) {
        z_assoc = s;
        try {
            incoming_data = s.getInputStream();
            outgoing_data = s.getOutputStream();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() {
    }

    public synchronized void addTargetAPDUListener(TargetAPDUListener l) {
        apduListeners.addElement(l);
    }

    public synchronized void removeTargetAPDUListener(TargetAPDUListener l) {
        apduListeners.removeElement(l);
    }

    protected void notifyAPDUEvent(PDU_type pdu) {
        Vector l;
        APDUEvent e = new APDUEvent(this, pdu);
        synchronized (this) {
            l = (Vector) apduListeners.clone();
        }
        for (int i = 0; i < l.size(); i++) {
            TargetAPDUListener zl = (TargetAPDUListener) l.elementAt(i);
            switch(pdu.which) {
                case PDU_type.initrequest_CID:
                    zl.incomingInitRequest(e);
                    break;
                case PDU_type.searchrequest_CID:
                    zl.incomingSearchRequest(e);
                    break;
                case PDU_type.presentrequest_CID:
                    zl.incomingPresentRequest(e);
                    break;
                case PDU_type.deleteresultsetrequest_CID:
                    zl.incomingDeleteResultSetRequest(e);
                    break;
                case PDU_type.accesscontrolresponse_CID:
                    zl.incomingAccessControlResponse(e);
                    break;
                case PDU_type.resourcecontrolrequest_CID:
                    zl.incomingResourceControlRequest(e);
                    break;
                case PDU_type.triggerresourcecontrolrequest_CID:
                    zl.incomingTriggerResourceControlRequest(e);
                    break;
                case PDU_type.resourcereportrequest_CID:
                    zl.incomingResourceReportRequest(e);
                    break;
                case PDU_type.scanrequest_CID:
                    zl.incomingScanRequest(e);
                    break;
                case PDU_type.sortrequest_CID:
                    zl.incomingSortRequest(e);
                    break;
                case PDU_type.segmentrequest_CID:
                    zl.incomingSegmentRequest(e);
                    break;
                case PDU_type.extendedservicesrequest_CID:
                    zl.incomingExtendedServicesRequest(e);
                    break;
                case PDU_type.close_CID:
                    zl.incomingClose(e);
                    break;
                default:
            }
        }
        e = null;
        l = null;
    }

    public void sendInitResponse(String refid, AsnBitString protocol_version, AsnBitString options, long pref_message_size, long exceptional_record_size, boolean result, String implementation_id, String implementation_name, String implementation_version, EXTERNAL_type user_information, Vector other_information) throws java.io.IOException {
        PDU_type pdu = new PDU_type();
        InitializeResponse_type initialize_response = new InitializeResponse_type();
        pdu.o = initialize_response;
        pdu.which = PDU_type.initresponse_CID;
        initialize_response.referenceId = refid;
        initialize_response.protocolVersion = protocol_version;
        initialize_response.options = options;
        initialize_response.preferredMessageSize = BigInteger.valueOf(pref_message_size);
        initialize_response.exceptionalRecordSize = BigInteger.valueOf(exceptional_record_size);
        initialize_response.result = new Boolean(result);
        initialize_response.implementationId = implementation_id;
        initialize_response.implementationName = implementation_name;
        initialize_response.implementationVersion = implementation_version;
        initialize_response.userInformationField = user_information;
        initialize_response.otherInfo = other_information;
        encodeAndSend(pdu);
    }

    public void sendSearchResponse(SearchTask t) throws java.io.IOException {
        PDU_type pdu = new PDU_type();
        SearchResponse_type search_response = new SearchResponse_type();
        pdu.o = search_response;
        pdu.which = PDU_type.searchresponse_CID;
        String refid = (String) t.getUserData();
        search_response.referenceId = refid;
        search_response.resultCount = BigInteger.valueOf(t.getTaskResultSet().getFragmentCount());
        search_response.numberOfRecordsReturned = BigInteger.valueOf(0);
        search_response.nextResultSetPosition = BigInteger.valueOf(1);
        if ((t.getTaskStatusCode() == SearchTask.TASK_COMPLETE)) {
            search_response.searchStatus = Boolean.TRUE;
        } else {
            search_response.searchStatus = Boolean.FALSE;
            switch(t.getTaskStatusCode()) {
                case SearchTask.TASK_EXECUTING_ASYNC:
                case SearchTask.TASK_EXECUTING_SYNC:
                    search_response.resultSetStatus = BigInteger.valueOf(2);
                    break;
                case SearchTask.TASK_FAILURE:
                    search_response.resultSetStatus = BigInteger.valueOf(3);
                    break;
            }
        }
        search_response.presentStatus = BigInteger.valueOf(0);
        search_response.records = new Records_type();
        search_response.records.which = Records_type.responserecords_CID;
        search_response.records.o = new Vector();
        encodeAndSend(pdu);
    }

    public void sendClose(int reason_code, String reason_text) throws java.io.IOException {
        PDU_type pdu = new PDU_type();
        pdu.which = PDU_type.close_CID;
        Close_type close = new Close_type();
        pdu.o = close;
        close.closeReason = BigInteger.valueOf(reason_code);
        close.diagnosticInformation = reason_text;
        encodeAndSend(pdu);
    }

    public void notifyClose() {
        PDU_type pdu = new PDU_type();
        pdu.which = PDU_type.close_CID;
        Close_type close = new Close_type();
        pdu.o = close;
        close.closeReason = BigInteger.valueOf(100);
        close.diagnosticInformation = "Internal close notification";
        notifyAPDUEvent(pdu);
    }

    public void encodeAndSend(PDU_type the_pdu) throws java.io.IOException {
        synchronized (this) {
            BEROutputStream encoder = new BEROutputStream();
            codec.serialize(encoder, the_pdu, false, "PDU");
            encoder.flush();
            encoder.writeTo(outgoing_data);
            outgoing_data.flush();
        }
    }

    public void shutdown() {
        running = false;
        try {
            this.interrupt();
        } catch (java.lang.SecurityException se) {
        }
    }

    public void run() {
        while (running) {
            try {
                cat.debug("Waiting for pdu on input stream");
                BERInputStream bds = new BERInputStream(incoming_data);
                PDU_type pdu = null;
                pdu = (PDU_type) codec.serialize(bds, pdu, false, "PDU");
                notifyAPDUEvent(pdu);
                yield();
            } catch (java.io.InterruptedIOException iioe) {
                cat.debug("Processing java.io.InterruptedIOException, shut down association");
                running = false;
                try {
                    sendClose(0, "Session Timeout");
                } catch (java.io.IOException ioe) {
                }
                notifyClose();
            } catch (Exception e) {
                cat.debug("Processing exception : " + e);
                e.printStackTrace();
                running = false;
            }
        }
        cat.debug("Assoc thread exiting (number of listeners=" + apduListeners.size() + ")");
        try {
            incoming_data.close();
            outgoing_data.close();
            z_assoc.close();
        } catch (Exception e) {
        }
        incoming_data = null;
        outgoing_data = null;
        z_assoc = null;
        apduListeners = null;
    }
}

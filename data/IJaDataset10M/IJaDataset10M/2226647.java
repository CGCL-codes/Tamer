package org.dcm4che2.net;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2005-10-18 20:18:41 -0400 (Tue, 18 Oct 2005) $
 * @since Oct 6, 2005
 *
 */
public class CommandFactory {

    public static final int SUCCESS = 0;

    public static final int PENDING = 0xFF00;

    public static final int NORMAL = 0;

    public static final int HIGH = 1;

    public static final int LOW = 2;

    public static final int C_STORE_RQ = 0x0001;

    public static final int C_STORE_RSP = 0x8001;

    public static final int C_GET_RQ = 0x0010;

    public static final int C_GET_RSP = 0x8010;

    public static final int C_FIND_RQ = 0x0020;

    public static final int C_FIND_RSP = 0x8020;

    public static final int C_MOVE_RQ = 0x0021;

    public static final int C_MOVE_RSP = 0x8021;

    public static final int C_ECHO_RQ = 0x0030;

    public static final int C_ECHO_RSP = 0x8030;

    public static final int N_EVENT_REPORT_RQ = 0x0100;

    public static final int N_EVENT_REPORT_RSP = 0x8100;

    public static final int N_GET_RQ = 0x0110;

    public static final int N_GET_RSP = 0x8110;

    public static final int N_SET_RQ = 0x0120;

    public static final int N_SET_RSP = 0x8120;

    public static final int N_ACTION_RQ = 0x0130;

    public static final int N_ACTION_RSP = 0x8130;

    public static final int N_CREATE_RQ = 0x0140;

    public static final int N_CREATE_RSP = 0x8140;

    public static final int N_DELETE_RQ = 0x0150;

    public static final int N_DELETE_RSP = 0x8150;

    public static final int C_CANCEL_RQ = 0x0FFF;

    private static final int RSP = 0x8000;

    public static final int NO_DATASET = 0x0101;

    private static int withDatasetType = 0x0000;

    private static boolean includeUIDinRSP;

    public static boolean isResponse(DicomObject dcmobj) {
        return (dcmobj.getInt(Tag.CommandField) & RSP) != 0;
    }

    public static boolean isCancelRQ(DicomObject dcmobj) {
        return dcmobj.getInt(Tag.CommandField) == C_CANCEL_RQ;
    }

    public static DicomObject newCEchoRSP(DicomObject dcmobj) {
        DicomObject rsp = newRSP(dcmobj, C_ECHO_RSP, NO_DATASET, SUCCESS);
        if (includeUIDinRSP) rsp.putString(Tag.AffectedSOPClassUID, VR.UI, dcmobj.getString(Tag.AffectedSOPClassUID));
        return rsp;
    }

    public static DicomObject newCEchoRQ(int msgId) {
        return newCEchoRQ(msgId, UID.VerificationSOPClass);
    }

    public static DicomObject newCEchoRQ(int msgId, String cuid) {
        DicomObject rq = newRQ(msgId, C_ECHO_RQ, NO_DATASET);
        rq.putString(Tag.AffectedSOPClassUID, VR.UI, cuid);
        return rq;
    }

    public static DicomObject newCStoreRQ(int msgId, String cuid, String iuid, int priority) {
        DicomObject rq = newRQ(msgId, C_STORE_RQ, withDatasetType);
        rq.putString(Tag.AffectedSOPClassUID, VR.UI, cuid);
        rq.putString(Tag.AffectedSOPInstanceUID, VR.UI, iuid);
        rq.putInt(Tag.Priority, VR.US, priority);
        return rq;
    }

    public static DicomObject newCStoreRQ(int msgId, String cuid, String iuid, int priority, String moveOriginatorAET, int moveOriginatorMsgId) {
        DicomObject rq = newCStoreRQ(msgId, cuid, iuid, priority);
        rq.putString(Tag.MoveOriginatorApplicationEntityTitle, VR.AE, moveOriginatorAET);
        rq.putInt(Tag.MoveOriginatorMessageID, VR.US, moveOriginatorMsgId);
        return rq;
    }

    private static DicomObject newRQ(int msgId, int cmdfield, int datasetType) {
        DicomObject rsp = new BasicDicomObject();
        rsp.putInt(Tag.MessageID, VR.US, msgId);
        rsp.putInt(Tag.CommandField, VR.US, cmdfield);
        rsp.putInt(Tag.DataSetType, VR.US, datasetType);
        return rsp;
    }

    private static DicomObject newRSP(DicomObject rq, int cmdfield, int datasetType, int status) {
        DicomObject rsp = new BasicDicomObject();
        rsp.putInt(Tag.CommandField, VR.US, cmdfield);
        rsp.putInt(Tag.DataSetType, VR.US, datasetType);
        rsp.putInt(Tag.Status, VR.US, status);
        rsp.putInt(Tag.MessageIDBeingRespondedTo, VR.US, rq.getInt(Tag.MessageID));
        return rsp;
    }

    public static boolean isIncludeUIDinRSP() {
        return includeUIDinRSP;
    }

    public static void setIncludeUIDinRSP(boolean includeUIDinRSP) {
        CommandFactory.includeUIDinRSP = includeUIDinRSP;
    }

    public static int getWithDatasetType() {
        return withDatasetType;
    }

    public static void setWithDatasetType(int withDatasetType) {
        if (withDatasetType == NO_DATASET || (withDatasetType & 0xffff0000) != 0) throw new IllegalArgumentException("withDatasetType: " + Integer.toHexString(withDatasetType) + "H");
        CommandFactory.withDatasetType = withDatasetType;
    }

    public static boolean hasDataset(DicomObject dcmobj) {
        return dcmobj.getInt(Tag.DataSetType) != NO_DATASET;
    }

    public static boolean isPending(DicomObject cmd) {
        return (cmd.getInt(Tag.Status) & PENDING) == PENDING;
    }
}

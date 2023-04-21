package com.pcmsolutions.smdi;

/**
 * Created by IntelliJ IDEA.
 * User: paulmeehan
 * Date: 14-Jan-2004
 * Time: 07:43:17
 * To change this template use Options | File Templates.
 */
class SMDI {

    public static final int SMDI_MSG = 0x00000000;

    public static final int SMDI_MSG_REJECT = 0x00020000;

    public static final int SMDI_MSG_MASTER = 0x00010000;

    public static final int SMDI_MSG_SLAVE = 0x00010001;

    public static final int SMDI_MSG_ACK = 0x01000000;

    public static final int SMDI_MSG_NACK = 0x01010000;

    public static final int SMDI_MSG_WAIT = 0x01020000;

    public static final int SMDI_MSG_NEXT = 0x01030000;

    public static final int SMDI_MSG_EOP = 0x01040000;

    public static final int SMDI_MSG_ABORT = 0x01050000;

    public static final int SMDI_MSG_DATA = 0x01100000;

    public static final int SMDI_MSG_HDR_REQ = 0x01200000;

    public static final int SMDI_MSG_HDR = 0x01210000;

    public static final int SMDI_MSG_TRANSFER_BEGIN = 0x01220000;

    public static final int SMDI_MSG_TRANSFER_ACK = 0x01220001;

    public static final int SMDI_MSG_NAME = 0x01230000;

    public static final int SMDI_MSG_DELETE = 0x01240000;

    public static final int SMDI_MSG_MIDI = 0x02000000;

    public static final int SMDI_REJECT_UNSUPPORTED = 0x00020000;

    public static final int SMDI_REJECT_INAPPROPIATE = 0x00020002;

    public static final int SMDI_REJECT_BUSY = 0x00050000;

    public static final int SMDI_REJECT_PACKET_MISMATCH = 0x00110000;

    public static final int SMDI_REJECT_OUTOFRANGE = 0x00200000;

    public static final int SMDI_REJECT_EMPTY = 0x00200002;

    public static final int SMDI_REJECT_MEMORY = 0x00200004;

    public static final int SMDI_REJECT_PARAM_MEMORY = 0x00200005;

    public static final int SMDI_REJECT_BPW = 0x00200006;

    public static final int SMDI_REJECT_CHNLS = 0x00200007;

    public static final int SMDI_REJECT_HDR_MISMATCH = 0x00220001;

    public static final int SMDI_REJECT_PACKET_LENGTH = 0x00220002;
}

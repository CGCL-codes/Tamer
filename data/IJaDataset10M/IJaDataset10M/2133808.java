package fi.kvanttisofta.sms;

public class SmsMsgIncoming {

    public final int SMS_MSG_ENCODING_7BIT = 0;

    private int smscAddressLen;

    private int smscAddressType;

    private String smscAddress;

    private int pduType;

    private int senderAddressLength;

    private int senderAddressType;

    private String senderAddress;

    private int pid;

    private int dcs;

    private String scts;

    private int udl;

    private String ud;

    private String pdu;

    private String msg;

    private static boolean debug = false;

    public String getSenderNumber() {
        return senderAddress;
    }

    public String getMessage() {
        return msg;
    }

    public static void setDebug(boolean val) {
        debug = val;
    }

    public SmsMsgIncoming(String smspdu) throws PduParseException {
        pdu = smspdu;
        int pdulen = smspdu.length();
        String tmpstr;
        int i = 0;
        if (debug) System.err.println("printing pdu parsing debug info");
        try {
            boolean hasSmscInfo = false;
            boolean hasSmscLen = false;
            String octet1str = smspdu.substring(i, i + 2);
            if (octet1str.equals("00")) {
                i = i + 2;
                octet1str = smspdu.substring(i, i + 2);
            }
            int octet1 = Integer.parseInt(octet1str, 16);
            i = i + 2;
            String octet2str = smspdu.substring(i, i + 2);
            int octet2 = Integer.parseInt(octet2str, 16);
            i = i + 2;
            if (octet2 >= 127) hasSmscInfo = true;
            if (debug) System.err.println("- hasSmscInfo: " + hasSmscInfo);
            if (hasSmscInfo) {
                smscAddressLen = octet1;
                smscAddressType = octet2;
                int smsAddrLastIndex = i + (smscAddressLen * 2) - 2;
                tmpstr = smspdu.substring(i, smsAddrLastIndex);
                smscAddress = SmsPduCodec.swapDigits(tmpstr);
                if (smscAddress.indexOf('F') != -1) smscAddress = smscAddress.substring(0, smscAddress.length() - 1);
                if ((smscAddressType & 0xf0) == 0x90) smscAddress = '+' + smscAddress;
                i = i + (smscAddressLen * 2) - 2;
                if (debug) {
                    System.err.println("- smscAddrLen: " + smscAddressLen);
                    System.err.println("- smscAddrType: " + smscAddressType);
                    System.err.println("- smscAddress: " + smscAddress);
                }
                String smsDeliverStr = smspdu.substring(i, i + 2);
                pduType = Integer.parseInt(smsDeliverStr, 16);
                i = i + 2;
                String addressLenStr = smspdu.substring(i, i + 2);
                senderAddressLength = Integer.parseInt(addressLenStr, 16);
                i = i + 2;
            } else {
                pduType = octet1;
                senderAddressLength = octet2;
            }
            if (debug) {
                System.err.println("- pduType: " + pduType);
                System.err.println("- senderAddrLen: " + senderAddressLength);
            }
            if ((pduType & 0x03) != 0) throw new PduParseException("not a SMS-DELIVER PDU: " + pduType);
            String addressTypeStr = smspdu.substring(i, i + 2);
            senderAddressType = Integer.parseInt(addressTypeStr, 16);
            i = i + 2;
            int senderLastIndex = i + senderAddressLength + senderAddressLength % 2;
            tmpstr = smspdu.substring(i, senderLastIndex);
            senderAddress = SmsPduCodec.swapDigits(tmpstr);
            senderAddress = senderAddress.substring(0, senderAddressLength);
            if ((senderAddressType & 0xf0) == 0x90) senderAddress = '+' + senderAddress;
            i = i + senderAddressLength + senderAddressLength % 2;
            String protocolStr = smspdu.substring(i, i + 2);
            pid = Integer.parseInt(protocolStr, 16);
            if (pid != 0x00) throw new PduParseException("unknown protocol ID " + pid);
            i = i + 2;
            String dataEncStr = smspdu.substring(i, i + 2);
            dcs = Integer.parseInt(dataEncStr, 16);
            if (dcs != 0x00) throw new PduParseException("unknown data encoding scheme " + dcs);
            i = i + 2;
            tmpstr = smspdu.substring(i, i + 7 * 2);
            scts = SmsPduCodec.swapDigits(tmpstr);
            i = i + 7 * 2;
            String msgLenStr = smspdu.substring(i, i + 2);
            udl = Integer.parseInt(msgLenStr, 16);
            i = i + 2;
            int encMsgLen = 0;
            if (dcs == 0x00) {
                encMsgLen = (udl * 7) / 8;
                if (((udl * 7) % 8) != 0) encMsgLen++;
            } else encMsgLen = udl;
            ud = smspdu.substring(i, i + encMsgLen * 2);
            msg = SmsPduCodec.sevenBitDecode(ud, udl);
        } catch (IndexOutOfBoundsException e) {
            throw new PduParseException("SMS PDU too short: " + e);
        } catch (NumberFormatException e) {
            throw new PduParseException("Hexadecimal number expected: " + e);
        }
        if ((i + ud.length()) < pdulen) throw new PduParseException("PDU too long");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append("\n");
        sb.append("\t smscAddressLen      = 0x" + SmsPduCodec.toHexString(smscAddressLen));
        sb.append("\n\t smscAddressType     = 0x" + SmsPduCodec.toHexString(smscAddressType));
        sb.append("\n\t smscAddress         = " + smscAddress);
        sb.append("\n\t pduType             = 0x" + SmsPduCodec.toHexString(pduType));
        sb.append("\n\t senderAddressLength = 0x" + SmsPduCodec.toHexString(senderAddressLength));
        sb.append("\n\t senderAddressType   = 0x" + SmsPduCodec.toHexString(senderAddressType));
        sb.append("\n\t senderAddress       = " + senderAddress);
        sb.append("\n\t pid                 = 0x" + SmsPduCodec.toHexString(pid));
        sb.append("\n\t dcs                 = 0x" + SmsPduCodec.toHexString(dcs));
        sb.append("\n\t scts                = " + scts);
        sb.append("\n\t udl                 = 0x" + SmsPduCodec.toHexString(udl));
        sb.append("\n\t ud                  = '" + ud + "'");
        sb.append("\n\t msg                 = '" + msg + "'");
        sb.append("\n\t msg hex             = " + SmsPduCodec.hexDump(msg));
        sb.append("\n\t pdu                 = '" + pdu + "'");
        return new String(sb);
    }

    public static void main(String[] args) {
        String smspdu1 = "07917238010010F5040BC87238880900F100009930925161958003C16010";
        SmsMsgIncoming sms1 = null;
        try {
            sms1 = new SmsMsgIncoming(smspdu1);
        } catch (PduParseException e) {
            System.err.println("error in PDU parsing: " + e);
        }
        System.err.println("pdu1: " + smspdu1);
        System.err.println("message1: " + sms1 + "\n\n");
        String smspdu2 = "0791534850020200040C9153483010119600001080229054538017D4F29C0E6A97E7F3F0B90C32CBDF6D101CFD769701";
        SmsMsgIncoming sms2 = null;
        try {
            sms2 = new SmsMsgIncoming(smspdu2);
        } catch (PduParseException e) {
            System.err.println("error in PDU parsing: " + e);
        }
        System.err.println("pdu2: " + smspdu2);
        System.err.println("message2: " + sms2 + "\n\n");
        String smspdu3 = "040C915398581099360000108022914464481DE8329BFD6681EE6F399BEC02D1D1E939283D078541F4F29C1E02";
        SmsMsgIncoming sms3 = null;
        try {
            sms3 = new SmsMsgIncoming(smspdu3);
        } catch (PduParseException e) {
            System.err.println("error in PDU parsing: " + e);
        }
        System.err.println("pdu3: " + smspdu3);
        System.err.println("message3: " + sms3 + "\n\n");
        String smspdu4 = "040C915348700689440000002121717570801FC6301E44AFB3DFF37918E4AEB7CBF2F7DB0D82E57035582C97ABCD00";
        smspdu4 = "07917238010010F8240E9172384104328229000010801221848421276A5F594E2FCBE9657959AEF0498B506616447F83DAE55043E16BD3DDF3F6DC357EB701";
        SmsMsgIncoming sms4 = null;
        try {
            sms4 = new SmsMsgIncoming(smspdu4);
        } catch (PduParseException e) {
            System.err.println("error in PDU parsing: " + e);
        }
        System.err.println("pdu4: " + smspdu4);
        System.err.println("message4: " + sms4 + "\n\n");
    }
}

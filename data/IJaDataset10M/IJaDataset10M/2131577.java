package com.inetmon.jn.decoder.layer5.ipv6;

import java.util.ArrayList;
import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer2.EthernetPacket;
import com.inetmon.jn.decoder.layer3.IPv6Packet;
import com.inetmon.jn.decoder.layer4.IPv6UDPPacket;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;
import com.inetmon.jn.decoder.tools.QuestionDNS;
import com.inetmon.jn.decoder.tools.ResourceRecordDNS;

/**
 * http://www.netfor2.com/dns.htm
 * http://www.netfor2.com/rfc1035.txt
 * The domain system is a mixture of functions and data types which are an
 * official protocol and functions and data types which are still experimental.
 * IPv6udpDNSPacket packet
 *
 * 
 * @author Kamel
 * 
 */
public class IPv6udpDNSPacket extends IPv6UDPPacket {

    /**
	 * A 16 bit identifier assigned by the program that generates any kind of query. This identifier is copied the corresponding reply and can be used by the requester to match up replies to outstanding queries.
	 * @uml.property  name="transactionID"
	 */
    private int transactionID;

    /**
	 * @uml.property  name="flagsDNS"
	 */
    private int FlagsDNS;

    /**
	 * @uml.property  name="flagResponse"
	 */
    private int flagResponse;

    /**
	 * @uml.property  name="flagOPCODE"
	 */
    private int flagOPCODE;

    /**
	 * @uml.property  name="flagAuthoritative"
	 */
    private int flagAuthoritative;

    /**
	 * @uml.property  name="flagTruncated"
	 */
    private int flagTruncated;

    /**
	 * @uml.property  name="flagRecursionDesired"
	 */
    private int flagRecursionDesired;

    /**
	 * @uml.property  name="flagRecursionAvailable"
	 */
    private int flagRecursionAvailable;

    /**
	 * @uml.property  name="flagZ"
	 */
    private int flagZ;

    /**
	 * @uml.property  name="flagAnswerAuthenticated"
	 */
    private int flagAnswerAuthenticated;

    /**
	 * @uml.property  name="flagNonAuthenticatedOk"
	 */
    private int flagNonAuthenticatedOk;

    /**
	 * @uml.property  name="flagReplyCODE"
	 */
    private int flagReplyCODE;

    /**
	 * Carries the query name and other query parameters.
	 * @uml.property  name="questions"
	 */
    private int Questions;

    /**
	 * Carries RRs which directly answer the query.
	 * @uml.property  name="answerRRS"
	 */
    private int AnswerRRS;

    /**
	 * Carries RRs which describe other authoritative servers. May optionally carry the SOA RR for the authoritative data in the answer section.
	 * @uml.property  name="authorityRRS"
	 */
    private int AuthorityRRS;

    /**
	 * Carries RRs which may be helpful in using the RRs in the other sections.
	 * @uml.property  name="additionalRRS"
	 */
    private int AdditionalRRS;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(15);

    int offset = 0;

    int sizeHeader = 0;

    private ArrayList<QuestionDNS> questionList = new ArrayList<QuestionDNS>();

    private ArrayList<ResourceRecordDNS> answerList = new ArrayList<ResourceRecordDNS>();

    private ArrayList<ResourceRecordDNS> authorityList = new ArrayList<ResourceRecordDNS>();

    private ArrayList<ResourceRecordDNS> additionalList = new ArrayList<ResourceRecordDNS>();

    public static final int UDP_PORT_DNS = 53;

    public static final int UDP_PORT_MDNS = 5353;

    public static final int OPCODE_QUERY = 0;

    public static final int OPCODE_IQUERY = 1;

    public static final int OPCODE_STATUS = 2;

    public static final int OPCODE_NOTIFY = 4;

    public static final int OPCODE_UPDATE = 5;

    public static final int RCODE_NOERROR = 0;

    public static final int RCODE_FORMERR = 1;

    public static final int RCODE_SERVFAIL = 2;

    public static final int RCODE_NXDOMAIN = 3;

    public static final int RCODE_NOTIMPL = 4;

    public static final int RCODE_REFUSED = 5;

    public static final int RCODE_YXDOMAIN = 6;

    public static final int RCODE_YXRRSET = 7;

    public static final int RCODE_NXRRSET = 8;

    public static final int RCODE_NOTAUTH = 9;

    public static final int RCODE_NOTZONE = 10;

    public static final int HEADER_LENGTH = 12;

    public IPv6udpDNSPacket(Packet packet) {
        super(packet);
        decodeIPv4udpDNSPacket();
        setProtocol1("DNS v6");
        setInfo("OP:" + getOpName(flagOPCODE) + " , Reply : " + getCodeReplyName(flagReplyCODE));
    }

    public void decodeIPv4udpDNSPacket() {
        offset = EthernetPacket.getEthernetHeaderLength() + ipv6HeaderLength + getUdpHeaderLength();
        sizeHeader = offset;
        transactionID = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        FlagsDNS = DecoderHelper.extractInteger(combined, offset, 2);
        flagResponse = (FlagsDNS & 0x80) >>> 15;
        flagOPCODE = (FlagsDNS & 0x7800) >>> 11;
        flagAuthoritative = (FlagsDNS & 0x400) >>> 10;
        flagTruncated = (FlagsDNS & 0x200) >>> 9;
        flagRecursionDesired = (FlagsDNS & 0x100) >>> 8;
        flagRecursionAvailable = (FlagsDNS & 0x80) >>> 7;
        flagZ = (FlagsDNS & 0x40) >>> 6;
        flagAnswerAuthenticated = (FlagsDNS & 0x10) >>> 5;
        flagNonAuthenticatedOk = (FlagsDNS & 0x08) >>> 4;
        flagReplyCODE = (FlagsDNS & 0x0f);
        offset += 2;
        Questions = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        AnswerRRS = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        AuthorityRRS = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        AdditionalRRS = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        for (int i = 0; i < Questions; i++) {
            QuestionDNS ques = new QuestionDNS();
            ques.setQnameDns(DecoderHelper.readDomain(combined, offset, sizeHeader));
            offset += DecoderHelper.getLengthString();
            ques.setTypeDns(DecoderHelper.extractInteger(combined, offset, 2));
            offset += 2;
            ques.setClassDns(DecoderHelper.extractInteger(combined, offset, 2));
            offset += 2;
            questionList.add(ques);
        }
        readRessource(0);
        readRessource(1);
        readRessource(2);
        DecoderHelper.clearDomain();
    }

    private void readRessource(int typeRessource) {
        int size = 0;
        switch(typeRessource) {
            case 0:
                size = AnswerRRS;
                break;
            case 1:
                size = AuthorityRRS;
                break;
            case 2:
                size = AdditionalRRS;
                break;
            default:
                size = 0;
        }
        for (int i = 0; i < size; i++) {
            ResourceRecordDNS aut = new ResourceRecordDNS();
            aut.setQnameDns(DecoderHelper.readDomain(combined, offset, sizeHeader));
            offset += DecoderHelper.getLengthString();
            aut.setTypeDns(DecoderHelper.extractInteger(combined, offset, 2));
            offset += 2;
            aut.setClassDns(DecoderHelper.extractInteger(combined, offset, 2));
            offset += 2;
            aut.setTtl(DecoderHelper.extractInteger(combined, offset, 4));
            offset += 4;
            aut.setDataLength(DecoderHelper.extractInteger(combined, offset, 2));
            offset += 2;
            if (aut.getTypeDns() == 1) {
                aut.setText("Address: " + DecoderHelper.readIpv4Adress(combined, offset));
                offset += 4;
            } else if (aut.getTypeDns() == 5) {
                aut.setText("Primary name: " + DecoderHelper.readDomain(combined, offset, sizeHeader));
                offset += DecoderHelper.getLengthString();
            } else if (aut.getTypeDns() == 13) {
                String text = "CPU : " + DecoderHelper.readString(combined, offset);
                offset += DecoderHelper.getLengthString();
                text += " OS : " + DecoderHelper.readString(combined, offset);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 15) {
                String text = "Preference: " + DecoderHelper.extractInteger(combined, offset, 2);
                offset += 2;
                text += " Mail Exchange: " + DecoderHelper.readDomain(combined, offset, sizeHeader);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 2) {
                aut.setText(DecoderHelper.readDomain(combined, offset, sizeHeader));
                offset += DecoderHelper.getLengthString();
            } else if (aut.getTypeDns() == 12) {
                aut.setText("Domain name: " + DecoderHelper.readDomain(combined, offset, sizeHeader));
                offset += DecoderHelper.getLengthString();
            } else if (aut.getTypeDns() == 6) {
                String text = "Primary name server: " + DecoderHelper.readDomain(combined, offset, sizeHeader);
                offset += DecoderHelper.getLengthString();
                text += " Responsible authority's mailbox: " + DecoderHelper.readDomain(combined, offset, sizeHeader);
                offset += DecoderHelper.getLengthString();
                text += " Serial number: " + DecoderHelper.extractInteger(combined, offset, 4);
                offset += 4;
                text += " Refrech interval: " + DecoderHelper.extractInteger(combined, offset, 4);
                offset += 4;
                text += " Retry interval: " + DecoderHelper.extractInteger(combined, offset, 4);
                offset += 4;
                text += " Ex[iration Limit: " + DecoderHelper.extractInteger(combined, offset, 4);
                offset += 4;
                text += " Minimum TTL: " + DecoderHelper.extractInteger(combined, offset, 4);
                offset += 4;
                aut.setText(text);
            } else if (aut.getTypeDns() == 16) {
                aut.setText(DecoderHelper.readString(combined, offset));
                offset += DecoderHelper.getLengthString();
            } else if (aut.getTypeDns() == 11) {
                String text = "Address: " + DecoderHelper.readIpv4Adress(combined, offset);
                offset += 4;
                text += " Protocol: " + DecoderHelper.extractInteger(combined, offset, 2);
                offset += 2;
                text += " Bit map: " + DecoderHelper.readString(combined, offset);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 18) {
                String text = "";
                if (DecoderHelper.extractInteger(combined, offset, 4) == 1) {
                    text += "Subtype: AFS cell database server";
                } else if (DecoderHelper.extractInteger(combined, offset, 4) == 2) {
                    text += "Subtype: DCE authentificated name server";
                } else {
                    text += "Subtype: undefined";
                }
                offset += 4;
                text += DecoderHelper.readDomain(combined, offset, sizeHeader);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 20) {
                String text = "ISDN Address: " + DecoderHelper.readString(combined, offset);
                offset += DecoderHelper.getLengthString();
                text += " Subaddress: " + DecoderHelper.readString(combined, offset);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 33) {
                String text = "Priority: " + DecoderHelper.extractInteger(combined, offset, 2);
                offset += 2;
                text += " Weight: " + DecoderHelper.extractInteger(combined, offset, 2);
                offset += 2;
                text += " Port: " + DecoderHelper.extractInteger(combined, offset, 2);
                offset += 2;
                text += " Target: " + DecoderHelper.readDomain(combined, offset, sizeHeader);
                offset += DecoderHelper.getLengthString();
                aut.setText(text);
            } else if (aut.getTypeDns() == 28) {
                aut.setText("Address: " + DecoderHelper.readIpv6Adress(combined, offset));
                offset += 16;
            } else {
                aut.setText(DecoderHelper.readString(combined, offset));
                offset += DecoderHelper.getLengthString();
            }
            switch(typeRessource) {
                case 0:
                    answerList.add(aut);
                    break;
                case 1:
                    authorityList.add(aut);
                    break;
                case 2:
                    additionalList.add(aut);
                    break;
            }
        }
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), "", getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = EthernetPacket.getEthernetHeaderLength() + IPv6Packet.getIpv6HeaderLength() + getUdpHeaderLength();
        TreeItem root = new TreeItem(tree, SWT.NONE);
        root.setText("Domain Name System");
        root.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        root.setData(new ReferenceOnTable(offset, combined.length - offset));
        TreeItem main = new TreeItem(root, SWT.NONE);
        main.setText("DNS Header");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        main.setData(new ReferenceOnTable(offset, 12));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText("Transaction ID: " + transactionID);
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t1.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText("FlagsDNS: 0x" + Integer.toHexString(FlagsDNS));
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t2.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t2_1 = new TreeItem(t2, SWT.NONE);
        t2_1.setText("Response: " + DecoderHelper.getBinaryString(flagResponse, 1) + "(" + getflagResponseName(flagResponse) + ")");
        t2_1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_2 = new TreeItem(t2, SWT.NONE);
        t2_2.setText("OPCODE: " + DecoderHelper.getBinaryString(flagOPCODE, 4) + "(" + getOpName(flagOPCODE) + ")");
        t2_2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_3 = new TreeItem(t2, SWT.NONE);
        t2_3.setText("Authoritative: " + DecoderHelper.getBinaryString(flagAuthoritative, 1) + "(" + getflagAuthoritativeName(flagAuthoritative) + ")");
        t2_3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_4 = new TreeItem(t2, SWT.NONE);
        t2_4.setText("Truncated: " + DecoderHelper.getBinaryString(flagTruncated, 1) + "(" + getflagTruncatedName(flagTruncated) + ")");
        t2_4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_5 = new TreeItem(t2, SWT.NONE);
        t2_5.setText("Recursion Desired: " + DecoderHelper.getBinaryString(flagRecursionDesired, 1) + "(" + getflagRecdesiredName(flagRecursionDesired) + ")");
        t2_5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_6 = new TreeItem(t2, SWT.NONE);
        t2_6.setText("Recursion Available: " + DecoderHelper.getBinaryString(flagRecursionAvailable, 1) + "(" + getflagRecavailName(flagRecursionAvailable) + ")");
        t2_6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_7 = new TreeItem(t2, SWT.NONE);
        t2_7.setText("Z: " + DecoderHelper.getBinaryString(flagZ, 1) + "(" + getflagZName(flagZ) + ")");
        t2_7.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_8 = new TreeItem(t2, SWT.NONE);
        t2_8.setText("Answer Authenticated: " + DecoderHelper.getBinaryString(flagAnswerAuthenticated, 1) + "(" + getflagAuthenticatedName(flagAnswerAuthenticated) + ")");
        t2_8.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_9 = new TreeItem(t2, SWT.NONE);
        t2_9.setText("Non-Authenticated data OK: " + DecoderHelper.getBinaryString(flagNonAuthenticatedOk, 1) + "(" + getNonAuthenticatedName(flagNonAuthenticatedOk) + ")");
        t2_9.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
        TreeItem t2_10 = new TreeItem(t2, SWT.NONE);
        t2_10.setText("flagRCODE: " + DecoderHelper.getBinaryString(flagReplyCODE, 4) + "(" + getCodeReplyName(flagReplyCODE) + ")");
        t2_10.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText("Questions: " + Questions);
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t3.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t4 = new TreeItem(main, SWT.NONE);
        t4.setText("AnswerRRS: " + AnswerRRS);
        t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t4.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t5 = new TreeItem(main, SWT.NONE);
        t5.setText("AuthorityRRS: " + AuthorityRRS);
        t5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t5.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t6 = new TreeItem(main, SWT.NONE);
        t6.setText("AdditionalRRS: " + AdditionalRRS);
        t6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t6.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
    }

    public static boolean isAnalyzable(Packet p) {
        int offset = getEthernetHeaderLength() + getIpv6HeaderLength();
        int srcPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        offset += 2;
        int destPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        if (srcPort == UDP_PORT_DNS || destPort == UDP_PORT_DNS || srcPort == UDP_PORT_MDNS || destPort == UDP_PORT_MDNS) return true;
        return false;
    }

    public String toString() {
        String print = "";
        print += " TID:" + transactionID + " FLAG:" + FlagsDNS + " Q:" + Questions + " ANSRRS:" + AnswerRRS + " AUTHRRS:" + AuthorityRRS + " ADDRRS: " + AdditionalRRS;
        if (Questions != 0) {
            print += "\nQUERIES: ";
            print += questionList.toString();
        }
        if (AnswerRRS != 0) {
            print += "\nANSWERS: ";
            print += answerList.toString();
        }
        if (AuthorityRRS != 0) {
            print += "\nAUTHORITY: ";
            print += authorityList.toString();
        }
        if (AdditionalRRS != 0) {
            print += "\nADDITIONAL: ";
            print += additionalList.toString();
        }
        print += "\n";
        return print;
    }

    public String getflagResponseName(int code) {
        if (code == 1) return "Message is a response"; else return "Message is a query";
    }

    public String getOpName(int code) {
        if (code == OPCODE_QUERY) return "Standard query"; else if (code == OPCODE_IQUERY) return "Inverse query"; else if (code == OPCODE_STATUS) return "Server status request"; else if (code == OPCODE_NOTIFY) return "Zone change notification"; else if (code == OPCODE_UPDATE) return "Dynamic update"; else return "Unknown";
    }

    public String getflagAuthoritativeName(int code) {
        if (code == 1) return "Server is an authority for domain"; else return "Server is not an authority for domain";
    }

    public String getflagTruncatedName(int code) {
        if (code == 1) return "Message is truncated"; else return "Message is not truncated";
    }

    public String getflagRecdesiredName(int code) {
        if (code == 1) return "Do query recursively"; else return "Don't do query recursively";
    }

    public String getflagRecavailName(int code) {
        if (code == 1) return "Server can do recursive queries"; else return "Server can't do recursive queries";
    }

    public String getflagZName(int code) {
        if (code == 1) return "reserved - incorrect!"; else return "reserved (0)";
    }

    public String getflagAuthenticatedName(int code) {
        if (code == 1) return "Answer/authority portion was authenticated by the server"; else return "Answer/authority portion was not authenticated by the server";
    }

    public String getNonAuthenticatedName(int code) {
        if (code == 1) return "Non-authenticated data is acceptable"; else return "Non-authenticated data is unacceptable";
    }

    public String getCodeReplyName(int code) {
        if (code == RCODE_NOERROR) return "No error"; else if (code == RCODE_FORMERR) return "Format error"; else if (code == RCODE_SERVFAIL) return "Server failure"; else if (code == RCODE_NXDOMAIN) return "No such name"; else if (code == RCODE_NOTIMPL) return "Not implemented"; else if (code == RCODE_REFUSED) return "Refused"; else if (code == RCODE_YXDOMAIN) return "Name exists"; else if (code == RCODE_YXRRSET) return "RRset exists"; else if (code == RCODE_NXRRSET) return "RRset does not exist"; else if (code == RCODE_NOTAUTH) return "Not authoritative"; else if (code == RCODE_NOTZONE) return "Name out of zone"; else return "Unknown";
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }
}

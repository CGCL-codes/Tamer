package com.cbsgmbh.xi.af.edifact.util;

/**
 * 
 * This class holds constansts that are used by multiple EDIFACT components
 * @author Jutta Boehme
 */
public class EdifactUtil {

    public static final String XML_PAYLOAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MessageContent>Attachment EDIFACT.Message</MessageContent>";

    public static final String ATTACHMENT_SIGNATURE = "EDIFACT.Signature";

    public static final String ATTACHMENT_CONFIG = "EDIFACT.Configuration";

    public static final String MODE = "Mode";

    public static final String MODE_TEST = "Test";

    public static final String MODE_DEBUG = "Debug";

    public static final String UNB_PARTY_FROM = "UNB.Party.From";

    public static final String UNB_PARTY_TO = "UNB.Party.To";

    public static final String UNB_SENDER_QUALIFIER = "UNB.Sender.Qualifier";

    public static final String UNB_RECEIVER_QUALIFIER = "UNB.Receiver.Qualifier";

    public static final int UNB_QUALIFIER_DUNS = 1;

    public static final int UNB_QUALIFIER_GLN = 14;

    public static final String ADAPTER_NAME = "EDIFACT";

    public static final String ADAPTER_NAMESPACE = "http://cbsgmbh.com/xi/EDIFACT/Adapter";

    public static final String EDIFACT_MSG_NS = ADAPTER_NAMESPACE;

    public static final String FUNACK_MSG_IF = "EDIFACTFunctionalAcknowledgement";

    public static final String CPA_PARTY_AGENCY_DUNS = "016";

    public static final String CPA_PARTY_SCHEMA_DUNS = "DUNS";

    public static final String CPA_PARTY_AGENCY_GLN = "009";

    public static final String CPA_PARTY_SCHEMA_GLN = "GLN";

    public static final String CPA_PARTY_AGENCY_AS2ID = "AS2  ZZZ  117";

    public static final String CPA_PARTY_SCHEMA_AS2ID = "AS2ID";

    public static final String CPA_PARTY_AGENCY_UNBID_DEFAULT_PREFIX = "UNB";

    public static final String CPA_PARTY_AGENCY_UNBID = "  ZZZ  117";

    public static final String CPA_PARTY_AND_AGENCY_WILDCARD = "*";

    public static final String CPA_PARTY_SCHEMA_UNBID = "UNBID";

    public static final String MSG_TYPE = "MessageType";

    public static final String MSG_TYPE_EDIFACT = "EDIFACTMessage";

    public static final String MSG_TYPE_MDN = "MDN";

    public static final String MSG_TYPE_CONTRL = "Contrl";

    public static final String VERSION_ATTR = "Version";

    public static final String FUNCTIONAL_ACK = "AcknowledgementExpected";

    public static final String UNB_CTL_NO_ATTR = "InterchangeControlNumber";

    public static final String UNB_CTRL_NO = "UNBControlNumber";

    public static final String UNG_CTRL_NO = "UNGControlNumber";

    public static final String EDIFACT_MESSAGE_XSD = "EDIFACTMessage";

    public static final String XI_PARTY_TO = "XIPartyTo";

    public static final String XI_PARTY_FROM = "XIPartyFrom";
}

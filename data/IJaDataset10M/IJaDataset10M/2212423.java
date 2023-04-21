/**
 * AlcUpkType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.alcatel.xmlapi.phonesetprogramming.types;

public class AlcUpkType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AlcUpkType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _NOT_ASSIGNED = "NOT_ASSIGNED";
    public static final java.lang.String _PROGRAMMED = "PROGRAMMED";
    public static final java.lang.String _VIDEO = "VIDEO";
    public static final java.lang.String _REMOTE_WATCH = "REMOTE_WATCH";
    public static final java.lang.String _BOSS_MAIL = "BOSS_MAIL";
    public static final java.lang.String _FORWARDING_ON_RING = "FORWARDING_ON_RING";
    public static final java.lang.String _ABSENT_SECRETARY = "ABSENT_SECRETARY";
    public static final java.lang.String _SCREENING_KEY = "SCREENING_KEY";
    public static final java.lang.String _UNSCREENING_KEY = "UNSCREENING_KEY";
    public static final java.lang.String _TRUNK_GROUP_SUPERVISION = "TRUNK_GROUP_SUPERVISION";
    public static final java.lang.String _TRUNK_SUPERVISION = "TRUNK_SUPERVISION";
    public static final java.lang.String _STATION_SUPERVISION = "STATION_SUPERVISION";
    public static final java.lang.String _SECRETARY_CALL = "SECRETARY_CALL";
    public static final java.lang.String _EXECUTIVE_CALL = "EXECUTIVE_CALL";
    public static final java.lang.String _MULTI_DIRECTORY_NUMBER = "MULTI_DIRECTORY_NUMBER";
    public static final java.lang.String _ROUTING_SECRETARY = "ROUTING_SECRETARY";
    public static final java.lang.String _ACD_STATE = "ACD_STATE";
    public static final java.lang.String _ACD_LISTENING = "ACD_LISTENING";
    public static final java.lang.String _ACD_GENERAL_FORWARDING = "ACD_GENERAL_FORWARDING";
    public static final java.lang.String _HEADSET = "HEADSET";
    public static final java.lang.String _DATA = "DATA";
    public static final java.lang.String _ISDN_FILTERING_KEY = "ISDN_FILTERING_KEY";
    public static final java.lang.String _ROUTER_SUPERVISION = "ROUTER_SUPERVISION";
    public static final java.lang.String _SCREENING_SUPERVISION = "SCREENING_SUPERVISION";
    public static final java.lang.String _SPK_ENQUIRY = "SPK_ENQUIRY";
    public static final java.lang.String _SPK_BROKER = "SPK_BROKER";
    public static final java.lang.String _SPK_FORWARD = "SPK_FORWARD";
    public static final java.lang.String _SPK_REDIAL_BIS = "SPK_REDIAL_BIS";
    public static final java.lang.String _SPK_MAIL = "SPK_MAIL";
    public static final java.lang.String _SPK_REDIAL_MEM = "SPK_REDIAL_MEM";
    public static final java.lang.String _SPK_TRANSFER = "SPK_TRANSFER";
    public static final java.lang.String _SPK_ISDN = "SPK_ISDN";
    public static final java.lang.String _SPK_REPERT = "SPK_REPERT";
    public static final java.lang.String _SPK_BOOKING = "SPK_BOOKING";
    public static final java.lang.String _SPK_THREE_PARTY_CONFERENCE = "SPK_THREE_PARTY_CONFERENCE";
    public static final java.lang.String _SPK_INTRUDE = "SPK_INTRUDE";
    public static final java.lang.String _SPK_WAITING_CAMP_ON = "SPK_WAITING_CAMP_ON";
    public static final java.lang.String _SPK_LOUDSPEAKER_PAGING = "SPK_LOUDSPEAKER_PAGING";
    public static final java.lang.String _SPK_CALL_ANNOUNCEMENT = "SPK_CALL_ANNOUNCEMENT";
    public static final java.lang.String _SPK_PAGING_REQUEST = "SPK_PAGING_REQUEST";
    public static final java.lang.String _SPK_PROJECT_NUMBER_SUFFIX = "SPK_PROJECT_NUMBER_SUFFIX";
    public static final java.lang.String _SPK_DECIMAL_END_TO_END_SIGNALING = "SPK_DECIMAL_END_TO_END_SIGNALING";
    public static final java.lang.String _SPK_DTMF_END_TO_END_SIGNALING = "SPK_DTMF_END_TO_END_SIGNALING";
    public static final java.lang.String _SPK_MALICIOUS_CALL_TRACE = "SPK_MALICIOUS_CALL_TRACE";
    public static final java.lang.String _SPK_VOICE_MAIL_MESSAGE_DEPOSIT = "SPK_VOICE_MAIL_MESSAGE_DEPOSIT";
    public static final java.lang.String _SPK_CAMP_ON_CONTROL = "SPK_CAMP_ON_CONTROL";
    public static final java.lang.String _SPK_VOICE_MAIL_CONSULT = "SPK_VOICE_MAIL_CONSULT";
    public static final java.lang.String _NETWORK_EXECUTIVE_CALL = "NETWORK_EXECUTIVE_CALL";
    public static final java.lang.String _NETWORK_SECRETARY_CALL = "NETWORK_SECRETARY_CALL";
    public static final java.lang.String _NETWORK_NOT_ASSIGNED = "NETWORK_NOT_ASSIGNED";
    public static final java.lang.String _NETWORK_RISO = "NETWORK_RISO";
    public static final java.lang.String _ACD_GEN_FORW_PG = "ACD_GEN_FORW_PG";
    public static final java.lang.String _ACD_CLOSURE_PG = "ACD_CLOSURE_PG";
    public static final java.lang.String _OPERATOR_ASSISTANCE = "OPERATOR_ASSISTANCE";
    public static final java.lang.String _ACD_CNX_PERM = "ACD_CNX_PERM";
    public static final java.lang.String _SUP_PARALLEL_SET = "SUP_PARALLEL_SET";
    public static final java.lang.String _MLA = "MLA";
    public static final java.lang.String _SMLA = "SMLA";
    public static final java.lang.String _SUPMEVO = "SUPMEVO";
    public static final java.lang.String _ACD_LINE = "ACD_LINE";
    public static final AlcUpkType NOT_ASSIGNED = new AlcUpkType(_NOT_ASSIGNED);
    public static final AlcUpkType PROGRAMMED = new AlcUpkType(_PROGRAMMED);
    public static final AlcUpkType VIDEO = new AlcUpkType(_VIDEO);
    public static final AlcUpkType REMOTE_WATCH = new AlcUpkType(_REMOTE_WATCH);
    public static final AlcUpkType BOSS_MAIL = new AlcUpkType(_BOSS_MAIL);
    public static final AlcUpkType FORWARDING_ON_RING = new AlcUpkType(_FORWARDING_ON_RING);
    public static final AlcUpkType ABSENT_SECRETARY = new AlcUpkType(_ABSENT_SECRETARY);
    public static final AlcUpkType SCREENING_KEY = new AlcUpkType(_SCREENING_KEY);
    public static final AlcUpkType UNSCREENING_KEY = new AlcUpkType(_UNSCREENING_KEY);
    public static final AlcUpkType TRUNK_GROUP_SUPERVISION = new AlcUpkType(_TRUNK_GROUP_SUPERVISION);
    public static final AlcUpkType TRUNK_SUPERVISION = new AlcUpkType(_TRUNK_SUPERVISION);
    public static final AlcUpkType STATION_SUPERVISION = new AlcUpkType(_STATION_SUPERVISION);
    public static final AlcUpkType SECRETARY_CALL = new AlcUpkType(_SECRETARY_CALL);
    public static final AlcUpkType EXECUTIVE_CALL = new AlcUpkType(_EXECUTIVE_CALL);
    public static final AlcUpkType MULTI_DIRECTORY_NUMBER = new AlcUpkType(_MULTI_DIRECTORY_NUMBER);
    public static final AlcUpkType ROUTING_SECRETARY = new AlcUpkType(_ROUTING_SECRETARY);
    public static final AlcUpkType ACD_STATE = new AlcUpkType(_ACD_STATE);
    public static final AlcUpkType ACD_LISTENING = new AlcUpkType(_ACD_LISTENING);
    public static final AlcUpkType ACD_GENERAL_FORWARDING = new AlcUpkType(_ACD_GENERAL_FORWARDING);
    public static final AlcUpkType HEADSET = new AlcUpkType(_HEADSET);
    public static final AlcUpkType DATA = new AlcUpkType(_DATA);
    public static final AlcUpkType ISDN_FILTERING_KEY = new AlcUpkType(_ISDN_FILTERING_KEY);
    public static final AlcUpkType ROUTER_SUPERVISION = new AlcUpkType(_ROUTER_SUPERVISION);
    public static final AlcUpkType SCREENING_SUPERVISION = new AlcUpkType(_SCREENING_SUPERVISION);
    public static final AlcUpkType SPK_ENQUIRY = new AlcUpkType(_SPK_ENQUIRY);
    public static final AlcUpkType SPK_BROKER = new AlcUpkType(_SPK_BROKER);
    public static final AlcUpkType SPK_FORWARD = new AlcUpkType(_SPK_FORWARD);
    public static final AlcUpkType SPK_REDIAL_BIS = new AlcUpkType(_SPK_REDIAL_BIS);
    public static final AlcUpkType SPK_MAIL = new AlcUpkType(_SPK_MAIL);
    public static final AlcUpkType SPK_REDIAL_MEM = new AlcUpkType(_SPK_REDIAL_MEM);
    public static final AlcUpkType SPK_TRANSFER = new AlcUpkType(_SPK_TRANSFER);
    public static final AlcUpkType SPK_ISDN = new AlcUpkType(_SPK_ISDN);
    public static final AlcUpkType SPK_REPERT = new AlcUpkType(_SPK_REPERT);
    public static final AlcUpkType SPK_BOOKING = new AlcUpkType(_SPK_BOOKING);
    public static final AlcUpkType SPK_THREE_PARTY_CONFERENCE = new AlcUpkType(_SPK_THREE_PARTY_CONFERENCE);
    public static final AlcUpkType SPK_INTRUDE = new AlcUpkType(_SPK_INTRUDE);
    public static final AlcUpkType SPK_WAITING_CAMP_ON = new AlcUpkType(_SPK_WAITING_CAMP_ON);
    public static final AlcUpkType SPK_LOUDSPEAKER_PAGING = new AlcUpkType(_SPK_LOUDSPEAKER_PAGING);
    public static final AlcUpkType SPK_CALL_ANNOUNCEMENT = new AlcUpkType(_SPK_CALL_ANNOUNCEMENT);
    public static final AlcUpkType SPK_PAGING_REQUEST = new AlcUpkType(_SPK_PAGING_REQUEST);
    public static final AlcUpkType SPK_PROJECT_NUMBER_SUFFIX = new AlcUpkType(_SPK_PROJECT_NUMBER_SUFFIX);
    public static final AlcUpkType SPK_DECIMAL_END_TO_END_SIGNALING = new AlcUpkType(_SPK_DECIMAL_END_TO_END_SIGNALING);
    public static final AlcUpkType SPK_DTMF_END_TO_END_SIGNALING = new AlcUpkType(_SPK_DTMF_END_TO_END_SIGNALING);
    public static final AlcUpkType SPK_MALICIOUS_CALL_TRACE = new AlcUpkType(_SPK_MALICIOUS_CALL_TRACE);
    public static final AlcUpkType SPK_VOICE_MAIL_MESSAGE_DEPOSIT = new AlcUpkType(_SPK_VOICE_MAIL_MESSAGE_DEPOSIT);
    public static final AlcUpkType SPK_CAMP_ON_CONTROL = new AlcUpkType(_SPK_CAMP_ON_CONTROL);
    public static final AlcUpkType SPK_VOICE_MAIL_CONSULT = new AlcUpkType(_SPK_VOICE_MAIL_CONSULT);
    public static final AlcUpkType NETWORK_EXECUTIVE_CALL = new AlcUpkType(_NETWORK_EXECUTIVE_CALL);
    public static final AlcUpkType NETWORK_SECRETARY_CALL = new AlcUpkType(_NETWORK_SECRETARY_CALL);
    public static final AlcUpkType NETWORK_NOT_ASSIGNED = new AlcUpkType(_NETWORK_NOT_ASSIGNED);
    public static final AlcUpkType NETWORK_RISO = new AlcUpkType(_NETWORK_RISO);
    public static final AlcUpkType ACD_GEN_FORW_PG = new AlcUpkType(_ACD_GEN_FORW_PG);
    public static final AlcUpkType ACD_CLOSURE_PG = new AlcUpkType(_ACD_CLOSURE_PG);
    public static final AlcUpkType OPERATOR_ASSISTANCE = new AlcUpkType(_OPERATOR_ASSISTANCE);
    public static final AlcUpkType ACD_CNX_PERM = new AlcUpkType(_ACD_CNX_PERM);
    public static final AlcUpkType SUP_PARALLEL_SET = new AlcUpkType(_SUP_PARALLEL_SET);
    public static final AlcUpkType MLA = new AlcUpkType(_MLA);
    public static final AlcUpkType SMLA = new AlcUpkType(_SMLA);
    public static final AlcUpkType SUPMEVO = new AlcUpkType(_SUPMEVO);
    public static final AlcUpkType ACD_LINE = new AlcUpkType(_ACD_LINE);
    public java.lang.String getValue() { return _value_;}
    public static AlcUpkType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AlcUpkType enum = (AlcUpkType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AlcUpkType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}

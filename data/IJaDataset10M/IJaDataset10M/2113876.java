package com.cbsgmbh.xi.af.edifact.module.transform.edifact;

/**
 *	This class holds all constant values regarding the EDIFACT envelope structure. 
 *  @author Jutta Boehme
 */
public class EdifactEnvelopeConstants {

    static final int UNA_FIXED_LENGTH = 10;

    static final int UNB_MIN_LENGTH = 28;

    static final int UNB_MAX_LENGTH = 228;

    static final int UNZ_MIN_LENGTH = 7;

    static final int UNZ_MAX_LENGTH = 25;

    static final int UNG_MIN_LENGTH = 21;

    static final int UNG_MAX_LENGTH = 152;

    static final int UNE_MIN_LENGTH = 7;

    static final int UNE_MAX_LENGTH = 25;

    static final int UNA_INTERCHANGEMARKER_BEGIN = 0;

    static final int UNA_INTERCHANGEMARKER_LEN = 3;

    static final int UNA_COMPONENTELEMENTSEPARATOR_BEGIN = 3;

    static final int UNA_COMPONENTELEMENTSEPARATOR_LEN = 1;

    static final int UNA_ELEMENTSEPARATOR_BEGIN = 4;

    static final int UNA_ELEMENTSEPARATOR_LEN = 1;

    static final int UNA_DECIMALNOTATION_BEGIN = 5;

    static final int UNA_DECIMALNOTATION_LEN = 1;

    static final int UNA_RELEASECHARACTER_BEGIN = 6;

    static final int UNA_RELEASECHARACTER_LEN = 1;

    static final int UNA_RESERVEDFORFUTUREUSE_BEGIN = 7;

    static final int UNA_RESERVEDFORFUTUREUSE_LEN = 1;

    static final int UNA_SEGMENTTERMINATOR_BEGIN = 8;

    static final int UNA_SEGMENTTERMINATOR_LEN = 1;

    static final int UNB_INTERCHANGEMARKER_BEGIN = 0;

    static final int UNB_INTERCHANGEMARKER_LEN = 3;

    static final int UNB_SYNTAXIDENTIFIER_BEGIN = 4;

    static final int UNB_SYNTAXIDENTIFIER_LEN = 4;

    static final int UNB_SYNTAXVERSIONNO_BEGIN = 9;

    static final int UNB_SYNTAXVERSIONNO_LEN = 1;

    static final int UNB_INTERCHANGESENDERID_BEGIN = 11;

    static final int UNB_INTERCHANGESENDERID_MIN_LEN = 1;

    static final int UNB_INTERCHANGESENDERID_MAX_LEN = 35;

    static final int UNB_INTERCHANGESENDERIDQUALIFIER_MIN_LEN = 0;

    static final int UNB_INTERCHANGESENDERIDQUALIFIER_MAX_LEN = 4;

    static final int UNB_INTERCHANGESENDERROUTINGADDRESS_MIN_LEN = 0;

    static final int UNB_INTERCHANGESENDERROUTINGADDRESS_MAX_LEN = 14;

    static final int UNB_INTERCHANGERECIPIENTID_MIN_LEN = 1;

    static final int UNB_INTERCHANGERECIPIENTID_MAX_LEN = 35;

    static final int UNB_INTERCHANGERECIPIENTIDQUALIFIER_MIN_LEN = 0;

    static final int UNB_INTERCHANGERECIPIENTIDQUALIFIER_MAX_LEN = 4;

    static final int UNB_INTERCHANGERECIPIENTIDROUTINGADDRESS_MIN_LEN = 0;

    static final int UNB_INTERCHANGERECIPIENTIDROUTINGADDRESS_MAX_LEN = 14;

    static final int UNB_INTERCHANGEDATE_LEN = 6;

    static final int UNB_INTERCHANGETIME_LEN = 4;

    static final int UNB_INTERCHANGECONTROLNO_MIN_LEN = 1;

    static final int UNB_INTERCHANGECONTROLNO_MAX_LEN = 14;

    static final int UNB_INTERCHANGERECIPIENTREFERENCE_MIN_LEN = 1;

    static final int UNB_INTERCHANGERECIPIENTREFERENCE_MAX_LEN = 14;

    static final int UNB_INTERCHANGERECIPIENTREFERENCEQUALIFIER_LEN = 4;

    static final int UNB_APPLICATIONREFERENCE_MIN_LEN = 0;

    static final int UNB_APPLICATIONREFERENCE_MAX_LEN = 14;

    static final int UNB_PROCESSINGPRIORITYCODE_LEN = 1;

    static final int UNB_ACKNOWLEDGEMENTREQUESTED_LEN = 1;

    static final int UNB_COMMUNICATIONAGREEMENTID_MIN_LEN = 0;

    static final int UNB_COMMUNICATIONAGREEMENTID_MAX_LEN = 35;

    static final int UNB_TESTINDICATOR_LEN = 1;

    static final int UNZ_INTERCHANGEMARKER_BEGIN = 0;

    static final int UNZ_INTERCHANGEMARKER_LEN = 3;

    static final int UNZ_NOINCLUDEDFUNCTIONALGROUPS_BEGIN = 4;

    static final int UNZ_NOINCLUDEDFUNCTIONALGROUPS_MIN_LEN = 1;

    static final int UNZ_NOINCLUDEDFUNCTIONALGROUPS_MAX_LEN = 6;

    static final int UNZ_INTERCHANGECONTROLNO_MIN_LEN = 1;

    static final int UNZ_INTERCHANGECONTROLNO_MAX_LEN = 14;

    static final int UNG_FUNCTIONALGROUPMARKER_BEGIN = 0;

    static final int UNG_FUNCTIONALGROUPMARKER_LEN = 3;

    static final int UNG_FUNCTIONALGROUPIDENTIFICATION_BEGIN = 4;

    static final int UNG_FUNCTIONALGROUPIDENTIFICATION_MIN_LEN = 1;

    static final int UNG_FUNCTIONALGROUPIDENTIFICATION_MAX_LEN = 6;

    static final int UNG_APPLICATIONSENDERCODE_MIN_LEN = 1;

    static final int UNG_APPLICATIONSENDERCODE_MAX_LEN = 35;

    static final int UNG_APPLICATIONSENDERCODEQUALIFIER_MIN_LEN = 0;

    static final int UNG_APPLICATIONSENDERCODEQUALIFIER_MAX_LEN = 4;

    static final int UNG_APPLICATIONRECIPIENTCODE_MIN_LEN = 1;

    static final int UNG_APPLICATIONRECIPIENTCODE_MAX_LEN = 35;

    static final int UNG_APPLICATIONRECIPIENTCODEQUALIFIER_MIN_LEN = 0;

    static final int UNG_APPLICATIONRECIPIENTCODEQUALIFIER_MAX_LEN = 4;

    static final int UNG_DATE_LEN = 6;

    static final int UNG_TIME_LEN = 4;

    static final int UNG_GROUPCONTROLNUMBER_MIN_LEN = 1;

    static final int UNG_GROUPCONTROLNUMBER_MAX_LEN = 14;

    static final int UNG_CONTROLLINGAGENCY_MIN_LEN = 1;

    static final int UNG_CONTROLLINGAGENCY_MAX_LEN = 2;

    static final int UNG_MESSAGETYPEVERSIONNO_MIN_LEN = 1;

    static final int UNG_MESSAGETYPEVERSIONNO_MAX_LEN = 3;

    static final int UNG_MESSAGETYPERELEASENO_MIN_LEN = 1;

    static final int UNG_MESSAGETYPERELEASENO_MAX_LEN = 3;

    static final int UNG_ASSOCIATIONASSIGNEDCODE_MIN_LEN = 0;

    static final int UNG_ASSOCIATIONASSIGNEDCODE_MAX_LEN = 6;

    static final int UNG_APPLICATIONPASSWORD_MIN_LEN = 0;

    static final int UNG_APPLICATIONPASSWORD_MAX_LEN = 14;

    static final int UNE_FUNCTIONALGROUPMARKER_BEGIN = 0;

    static final int UNE_FUNCTIONALGROUPMARKER_LEN = 3;

    static final int UNE_NOTRANSACTIONSETSINCLUDED_BEGIN = 4;

    static final int UNE_NOTRANSACTIONSETSINCLUDED_MIN_LEN = 1;

    static final int UNE_NOTRANSACTIONSETSINCLUDED_MAX_LEN = 6;

    static final int UNE_GROUPCONTROLNO_MIN_LEN = 1;

    static final int UNE_GROUPCONTROLNO_MAX_LEN = 14;
}

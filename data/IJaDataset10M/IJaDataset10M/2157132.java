package com.ibm.ops;

public final class OpstoolConstant {

    public static final String TOOL_VERSION = "1.0";

    public static final int HUB_REPORT_MASTER_NO_START_IX = 0;

    public static final int HUB_REPORT_MASTER_NO_LENGTH = 11;

    public static final int HUB_REPORT_STATUS_START_IX = 11;

    public static final int HUB_REPORT_STATUS_LENGTH = 8;

    public static final int HUB_REPORT_SENDER_START_IX = 19;

    public static final int HUB_REPORT_SENDER_LENGTH = 14;

    public static final int HUB_REPORT_RECIPIENT_START_IX = 33;

    public static final int HUB_REPORT_RECIPIENT_LENGTH = 14;

    public static final int HUB_REPORT_TRANS_TYPE_START_IX = 47;

    public static final int HUB_REPORT_TRANS_TYPE_LENGTH = 14;

    public static final int HUB_REPORT_ENVELOPEID_START_IX = 61;

    public static final int HUB_REPORT_ENVELOPEID_LENGTH = 14;

    public static final int HUB_REPORT_LOGTIME_IN_START_IX = 75;

    public static final int HUB_REPORT_LOGTIME_IN_LENGTH = 27;

    public static final int CIR_REPORT_CNRT_NO_START_IX = 0;

    public static final int CIR_REPORT_CNRT_NO_LENGTH = 11;

    public static final int CIR_REPORT_STATUS_START_IX = 11;

    public static final int CIR_REPORT_STATUS_LENGTH = 8;

    public static final int CIR_REPORT_SENDER_START_IX = 19;

    public static final int CIR_REPORT_SENDER_LENGTH = 14;

    public static final int CIR_REPORT_RECIPIENT_START_IX = 33;

    public static final int CIR_REPORT_RECIPIENT_LENGTH = 14;

    public static final int CIR_REPORT_TRANS_TYPE_START_IX = 47;

    public static final int CIR_REPORT_TRANS_TYPE_LENGTH = 14;

    public static final int CIR_REPORT_LOGTIME_IN_START_IX = 61;

    public static final int CIR_REPORT_LOGTIME_IN_LENGTH = 27;

    public static final int FODS_REJECT_REPORT_SAP_DOC_NUM_START_IX = 0;

    public static final int FODS_REJECT_REPORT_SAP_DOC_NUM_LENGTH = 15;

    public static final int FODS_REJECT_REPORT_STATUS_START_IX = 15;

    public static final int FODS_REJECT_REPORT_STATUS_LENGTH = 5;

    public static final int FODS_REJECT_REPORT_IDOC_NUMBER_START_IX = 20;

    public static final int FODS_REJECT_REPORT_IDOC_NUMBER_LENGTH = 17;

    public static final int FODS_REJECT_REPORT_GEO_KEY_START_IX = 37;

    public static final int FODS_REJECT_REPORT_GEO_KEY_LENGTH = 5;

    public static final int FODS_REJECT_REPORT_MESTYPE_START_IX = 42;

    public static final int FODS_REJECT_REPORT_MESTYPE_LENGTH = 8;

    public static final int FODS_REJECT_REPORT_SERIAL_NUMBER_START_IX = 50;

    public static final int FODS_REJECT_REPORT_SERIAL_NUMBER_LENGTH = 15;

    public static final int FODS_REJECT_REPORT_SNDPRN_START_IX = 65;

    public static final int FODS_REJECT_REPORT_SNDPRN_LENGTH = 11;

    public static final int FODS_REJECT_REPORT_RETURN_CODE_START_IX = 76;

    public static final int FODS_REJECT_REPORT_RETURN_CODE_LENGTH = 7;

    public static final int FODS_REJECT_REPORT_LOGTIME_IN_START_IX = 83;

    public static final int FODS_REJECT_REPORT_LOGTIME_IN_LENGTH = 26;

    public static final int FODS_DONOT_PROCESS_REPORT_SAP_DOC_NUM_START_IX = 0;

    public static final int FODS_DONOT_PROCESS_REPORT_SAP_DOC_NUM_LENGTH = 15;

    public static final int FODS_DONOT_PROCESS_REPORT_STATUS_START_IX = 15;

    public static final int FODS_DONOT_PROCESS_REPORT_STATUS_LENGTH = 5;

    public static final int FODS_DONOT_PROCESS_REPORT_IDOC_NUMBER_START_IX = 20;

    public static final int FODS_DONOT_PROCESS_REPORT_IDOC_NUMBER_LENGTH = 17;

    public static final int FODS_DONOT_PROCESS_REPORT_GEO_KEY_START_IX = 37;

    public static final int FODS_DONOT_PROCESS_REPORT_GEO_KEY_LENGTH = 5;

    public static final int FODS_DONOT_PROCESS_REPORT_MESTYPE_START_IX = 42;

    public static final int FODS_DONOT_PROCESS_REPORT_MESTYPE_LENGTH = 8;

    public static final int FODS_DONOT_PROCESS_REPORT_SERIAL_NUMBER_START_IX = 50;

    public static final int FODS_DONOT_PROCESS_REPORT_SERIAL_NUMBER_LENGTH = 15;

    public static final int FODS_DONOT_PROCESS_REPORT_SNDPRN_START_IX = 65;

    public static final int FODS_DONOT_PROCESS_REPORT_SNDPRN_LENGTH = 11;

    public static final int FODS_EOD_REPORT_SAP_DOC_NUM_START_IX = 0;

    public static final int FODS_EOD_REPORT_SAP_DOC_NUM_LENGTH = 15;

    public static final int FODS_EOD_REPORT_SNDPRN_START_IX = 15;

    public static final int FODS_EOD_REPORT_SNDPRN_LENGTH = 18;

    public static final int FODS_EOD_REPORT_SERIAL_NUMBER_START_IX = 33;

    public static final int FODS_EOD_REPORT_SERIAL_NUMBER_LENGTH = 17;

    public static final int FODS_EOD_REPORT_TIME_STAMP_START_IX = 50;

    public static final int FODS_EOD_REPORT_TIME_STAMP_LENGTH = 27;

    public static final String HUB_REPORT_SPLIT_TAG = "----------------------------------------------------------------------------------------------------------";

    public static final String CIR_REPORT_SPLIT_TAG = "----------------------------------------------------------------------------------------------------------";

    public static final String FODS_REJECT_REPORT_SPLIT_TAG = "----------------------------------------------------------------------------------------------------------";

    public static final String FODS_DONOT_PROCESS_REPORT_SPLIT_TAG = "----------------------------------------------------------------------------------------------------------";

    public static final String FODS_EOD_REPORT_SPLIT_TAG = "----------------------------------------------------------------------------------------------------------";

    public static final String INFOSOURCE_REPORT_SPLIT_TAG = "------------------------------------------------------------------------------------------------------------";

    public static final String INFOSOURCE_REPORT_META_DATA_FIRST_LINE_SPLIT_TAG = "     ";

    public static final String INFOSOURCE_REPORT_META_DATA_FIRST_LINE_ElEMENT_SPLIT_TAG = ":";

    public static final String COMPONET_START_TAG = "########################################################################################################";

    public static final String COMPONET_END_TAG = "=========================================================================================================";

    public static final String HUB_START_TAG = "Failed transactions in Transaction Hub : PROD";

    public static final String INFOSOURCE_START_TAG = "InfoSource generated Messages for the system : Unicode : PROD";

    public static final String CIR_START_TAG = "Failed transactions in CIR : PROD";

    public static final String FODS_START_TAG = "FODS : PROD Status Report";

    public static final String FODS_REJECT_START_TAG = "IDOCs rejected in FODS : PROD";

    public static final String FODS_OUTOF_SEN_START_TAG = "IDOCs arrived out of sequence and sent to DONT.PROCESS queue FODS : PROD";

    public static final String FODS_NOORDER_START_TAG = "Invoice/Deliveries that do not have corresponding Orders sent to DONT.PROCESS queue FODS : PROD";

    public static final String FODS_EOD_START_TAG = "END OF DAYS  FODS : PROD";

    public static final String FOOT_START_TAG = "FOOT : PROD Status Report";

    public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String CSV_SUFFIX = ".csv";
}

package org.openfast.session;

import org.openfast.error.ErrorCode;
import org.openfast.error.ErrorType;
import org.openfast.error.FastAlertSeverity;

public interface SessionConstants {

    ErrorType SESSION = new ErrorType("Session");

    ErrorCode TEMPLATE_NOT_SUPPORTED = new ErrorCode(SESSION, 11, "TNOTSUPP", "Template not supported", FastAlertSeverity.ERROR);

    ErrorCode TEMPLATE_UNKNOWN = new ErrorCode(SESSION, 12, "TUNKNOWN", "Template unknown", FastAlertSeverity.ERROR);

    ErrorCode UNAUTHORIZED = new ErrorCode(SESSION, 13, "EAUTH", "Unauthorized", FastAlertSeverity.FATAL);

    ErrorCode PROTCOL_ERROR = new ErrorCode(SESSION, 14, "EPROTO", "Protocol Error", FastAlertSeverity.ERROR);

    ErrorCode CLOSE = new ErrorCode(SESSION, 15, "CLOSE", "Session Closed", FastAlertSeverity.INFO);

    ErrorCode UNDEFINED = new ErrorCode(SESSION, -1, "UNDEFINED", "Undefined Alert Code", FastAlertSeverity.ERROR);

    SessionProtocol SCP_1_0 = new SessionControlProtocol_1_0();

    SessionProtocol SCP_1_1 = new SessionControlProtocol_1_1();

    String VENDOR_ID = "http://openfast.org/OpenFAST/1.1";
}

package com.netime.commons.standard;

import java.awt.Color;
import net.java.balloontip.BalloonTip.AttachLocation;
import net.java.balloontip.BalloonTip.Orientation;

public final class Constants {

    /** Constants for gui validation using balloon tip */
    public static final Color COLOR_FILL_VALIDATION = new Color(169, 205, 221, 220);

    public static final int PADDING_VALIDATION = 5;

    public static final int OFFSET_HORIZONTAL = 30;

    public static final int OFFSET_VERTICAL = 10;

    public static final boolean CLOSABLE_VALIDATION = false;

    public static final Orientation RIGHT_ABOVE_BALOONTIP = Orientation.RIGHT_ABOVE;

    public static final AttachLocation ALIGNED_BALOONTIP = AttachLocation.ALIGNED;

    /** Access action bits */
    public static final char ACCESS_BY_ATTACH = 'a';

    public static final char ACCESS_BY_ADD = 'A';

    public static final char ACCESS_BY_BROWSE = 'b';

    public static final char ACCESS_BY_POP = 'B';

    public static final char ACCESS_BY_CONTROL = 'c';

    public static final char ACCESS_BY_DELETE = 'd';

    public static final char ACCESS_BY_DELEGATION = 'g';

    public static final char ACCESS_BY_LIST = 'l';

    public static final char ACCESS_BY_MODIFY = 'm';

    public static final char ACCESS_BY_CREATE = 'N';

    public static final char ACCESS_BY_BYPASS_RULE = 'R';

    public static final char ACCESS_BY_READ = 'r';

    public static final char ACCESS_BY_SERVER_ADMIN = 's';

    public static final char ACCESS_BY_TRACE = 't';

    public static final char ACCESS_BY_TRAVERSE = 'T';

    public static final char ACCESS_BY_VIEW = 'v';

    public static final char ACCESS_BY_PASSWORD = 'W';

    public static final char ACCESS_BY_EXECUTE = 'x';

    /** Login type */
    public static final char RICH_INTERNET_APPLICATION = 'I';

    public static final char RICH_CLIENT_PLATFORM = 'C';

    /** User type */
    public static final char ADMINISTRATOR = 'A';

    public static final char CUSTOMER = 'C';

    public static final char USER = 'U';

    /** System error code: 100 ~ 999 */
    public static final int SPRING_CONTEXT_INITIATION_ERROR = 101;

    public static final int PROPERTY_NOT_FOUND = 102;

    public static final int NO_OUTPUT_TYPE_SPECIFIED = 103;

    public static final int TRANSFER_CONFIGURATION_ERROR = 104;

    public static final int RESOURCE_NOT_FOUND = 110;

    /** Application error code: 1000 ~ 9999 */
    public static final int USER_NOT_FOUND = 1000;

    public static final int PASSWORD_NOT_CORRECT = 1001;

    public static final int COMPANY_CODE_DUPLICATED = 1020;

    public static final int DEPARTMENT_CODE_DUPLICATED = 1021;

    public static final int ROLE_CODE_DUPLICATED = 1022;

    public static final int RESPONSIBILITY_CODE_DUPLICATED = 1023;

    public static final int EMAIL_ADDRESS_DUPLICATED = 1024;

    /** Common validation error code: 10000+ */
    public static final int DNI_ERROR = 10000;

    public static final int NOMBRE_ERROR = 10001;

    public static final int TELEPHONE_NO_ERROR = 10002;

    public static final int NUMERO_ERROR = 10003;

    public static final int PISO_ERROR = 10004;

    public static final int CP_ERROR = 10005;

    public static final int EMAIL_ERROR = 10006;

    /** Default value of primitive java type */
    public static final int DEFAULT_INT = Integer.MIN_VALUE;

    public static final long DEFAULT_LONG = Long.MIN_VALUE;

    public static final float DEFAULT_FLOAT = Float.MIN_VALUE;

    public static final double DEFAULT_DOUBLE = Double.MIN_VALUE;

    public static final byte DEFAULT_BYTE = Byte.MIN_VALUE;

    public static final String FALSE = "false";

    public static final String TRUE = "true";

    /** Cache keys */
    public static String COLLECTION_OF_PRIVIEGES;

    /** Constant values of XPATH express */
    public static final int XPATH_NODESET = 0;

    public static final int XPATH_DOCUMENT = 1;

    /** Constant values of edition */
    public static final int EXPRESS_EDITION = 1;

    public static final int STANDARD_EDITION = 2;

    public static final int ENTERPRISE_EDITION = 3;

    /** Constant values of application */
    public static final String APPLICATION_NAME = "Autoffice(TM) Suite, v1.0";
}

package shellkk.qiq.jdm;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public static final String JDM_GENERIC_ERROR = "JDM_GENERIC_ERROR";

    public static final String JDM_CONNECTION_FAILURE = "JDM_CONNECTION_FAILURE";

    public static final String JDM_CONNECTION_OPEN_FAILED = "JDM_CONNECTION_OPEN_FAILED";

    public static final String JDM_CONNECTION_CLOSE_FAILED = "JDM_CONNECTION_CLOSE_FAILED";

    public static final String JDM_ENTRY_NOT_FOUND = "JDM_ENTRY_NOT_FOUND";

    public static final String JDM_RESERVED_1005 = "JDM_RESERVED_1005";

    public static final String JDM_DUPLICATE_ENTRY = "JDM_DUPLICATE_ENTRY";

    public static final String JDM_INVALID_URI = "JDM_INVALID_URI";

    public static final String JDM_INACCESSIBLE_URI = "JDM_INACCESSIBLE_URI";

    public static final String JDM_INCOMPATIBLE_ARGUMENT_SPECIFICATION = "JDM_INCOMPATIBLE_ARGUMENT_SPECIFICATION";

    public static final String JDM_INCOMPATIBLE_SPECIFICATION = "JDM_INCOMPATIBLE_SPECIFICATION";

    public static final String JDM_INVALID_USAGE = "JDM_INVALID_USAGE";

    public static final String JDM_INVALID_SETTINGS = "JDM_INVALID_SETTINGS";

    public static final String JDM_OBJECT_NOT_FOUND = "JDM_OBJECT_NOT_FOUND";

    public static final String JDM_OBJECT_EXISTS = "JDM_OBJECT_EXISTS";

    public static final String JDM_TASK_EXECUTING = "JDM_TASK_EXECUTING";

    public static final String JDM_TASK_NOT_EXECUTING = "JDM_TASK_NOT_EXECUTING";

    public static final String JDM_TASK_FAILED = "JDM_TASK_FAILED";

    public static final String JDMR_RUNTIME_GENERIC_ERROR = "JDMR_RUNTIME_GENERIC_ERROR";

    public static final String JDMR_UNSUPPORTED_FEATURE = "JDMR_UNSUPPORTED_FEATURE";

    public static final String JDMR_NULL_ARGUMENT = "JDMR_NULL_ARGUMENT";

    public static final String JDMR_ARRAY_MISMATCH = "JDMR_ARRAY_MISMATCH";

    public static final String JDMR_INVALID_ARGUMENT = "JDMR_INVALID_ARGUMENT";

    public static final String JDMR_INVALID_STRING_ARGUMENT = "JDMR_INVALID_STRING_ARGUMENT";

    public static final String JDMR_STRING_TOO_LONG = "JDMR_STRING_TOO_LONG";

    public static final String JDMR_INVALID_CLASS_NAME = "JDMR_INVALID_CLASS_NAME";

    public static final String JDMR_INVALID_DATA_TYPE = "JDMR_INVALID_DATA_TYPE";

    public static final String JDMR_ARRAY_SIZE_EXCEEDED = "JDMR_ARRAY_SIZE_EXCEEDED";

    public static final String JDMR_INVALID_OBJECT_TYPE = "JDMR_INVALID_OBJECT_TYPE";

    public static final String JDMR_INVALID_OBJECT = "JDMR_INVALID_OBJECT";

    public static final String JDM_UNKNOWN = "JDM_UNKNOWN";

    protected static ResourceBundle resBundle;

    static {
        Locale locale = Locale.getDefault();
        resBundle = ResourceBundle.getBundle("jdm_message", locale);
    }

    public static String getMessage(String key) {
        if (resBundle == null) {
            return null;
        }
        {
            return resBundle.getString(key);
        }
    }
}

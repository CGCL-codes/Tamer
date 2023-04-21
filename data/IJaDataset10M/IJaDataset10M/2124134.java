package messaging;

import org.opensplice.dds.dcps.Utilities;

public final class PP_source_msgDataWriterHelper {

    public static messaging.PP_source_msgDataWriter narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof messaging.PP_source_msgDataWriter) {
            return (messaging.PP_source_msgDataWriter) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static messaging.PP_source_msgDataWriter unchecked_narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof messaging.PP_source_msgDataWriter) {
            return (messaging.PP_source_msgDataWriter) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }
}

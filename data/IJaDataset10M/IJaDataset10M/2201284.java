package DDSLWS;

import org.opensplice.dds.dcps.Utilities;

public final class OperationInvocationTopicDataWriterHelper {

    public static DDSLWS.OperationInvocationTopicDataWriter narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof DDSLWS.OperationInvocationTopicDataWriter) {
            return (DDSLWS.OperationInvocationTopicDataWriter) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static DDSLWS.OperationInvocationTopicDataWriter unchecked_narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof DDSLWS.OperationInvocationTopicDataWriter) {
            return (DDSLWS.OperationInvocationTopicDataWriter) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }
}

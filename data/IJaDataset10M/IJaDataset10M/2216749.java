package DDSLWS;

import org.opensplice.dds.dcps.Utilities;

public final class OperationResultTopicTypeSupportHelper {

    public static DDSLWS.OperationResultTopicTypeSupport narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof DDSLWS.OperationResultTopicTypeSupport) {
            return (DDSLWS.OperationResultTopicTypeSupport) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static DDSLWS.OperationResultTopicTypeSupport unchecked_narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof DDSLWS.OperationResultTopicTypeSupport) {
            return (DDSLWS.OperationResultTopicTypeSupport) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }
}

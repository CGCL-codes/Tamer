package uk.org.ogsadai.client.toolkit.property.convertor;

import org.w3c.dom.Text;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 * Convertor that converts an XML element representing a request execution
 * status into the object.
 *
 * @author The OGSA-DAI Team.
 */
public class RequestExecutionStatusXMLConvertor {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Converts a request execution status XML element into an object.
     * 
     * @param text
     *            the text node containing the execution status
     * @return the request status
     */
    public static RequestExecutionStatus convert(Text text) {
        RequestExecutionStatus result;
        String status = text.getNodeValue();
        result = getExecutionStatus(status);
        return result;
    }

    /**
     * Returns the request execution status corresponding to the given string.
     * 
     * @param status
     *            status of the request
     * @return a status object
     */
    public static RequestExecutionStatus getExecutionStatus(String status) {
        if (RequestExecutionStatus.COMPLETED.getLocalPart().equals(status)) {
            return RequestExecutionStatus.COMPLETED;
        }
        if (RequestExecutionStatus.COMPLETED_WITH_ERROR.getLocalPart().equals(status)) {
            return RequestExecutionStatus.COMPLETED_WITH_ERROR;
        }
        if (RequestExecutionStatus.ERROR.getLocalPart().equals(status)) {
            return RequestExecutionStatus.ERROR;
        }
        if (RequestExecutionStatus.PROCESSING.getLocalPart().equals(status)) {
            return RequestExecutionStatus.PROCESSING;
        }
        if (RequestExecutionStatus.PROCESSING_WITH_ERROR.getLocalPart().equals(status)) {
            return RequestExecutionStatus.PROCESSING_WITH_ERROR;
        }
        if (RequestExecutionStatus.TERMINATED.getLocalPart().equals(status)) {
            return RequestExecutionStatus.TERMINATED;
        }
        if (RequestExecutionStatus.UNSTARTED.getLocalPart().equals(status)) {
            return RequestExecutionStatus.UNSTARTED;
        }
        throw new IllegalArgumentException("Unsupported request execution status!");
    }
}

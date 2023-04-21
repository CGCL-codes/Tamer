package uk.org.ogsadai.client.toolkit.presentation.cxf;

import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.service.cxf.faults.StandardFaultType;

/**
 * Utility methods for handling exceptions.
 *
 * @author The OGSA-DAI Project Team
 */
public class CXFExceptionUtil {

    /**
     * Copyright notice
     */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010.";

    /**
     * Builds a <code>ClientException</code> details received from the
     * server.
     *
     * @param errorID    error ID of top level error.
     * @param parameters parameters associated with top level error.
     * @return the <code>ClientException</code> corresponding to the
     *         given details.
     */
    public static ClientException buildClientException(final String errorID, final String[] parameters) {
        return new ClientException(new ErrorID(errorID), parameters);
    }

    /**
     * Builds a <code>ClientException</code> details received from the
     * server.
     *
     * @param errorID    error ID of top level error.
     * @param parameters parameters associated with top level error.
     * @param causes     array of cause exception chain.
     * @return the <code>ClientException</code> corresponding to the
     *         given details. The exception will include a cause chain if the
     *         details contain a cause chain.
     */
    public static ClientException buildClientException(final String errorID, final String[] parameters, final StandardFaultType[] causes) {
        ClientException result = new ClientException(new ErrorID(errorID), parameters);
        ClientException nextParent = result;
        if (causes != null) {
            for (StandardFaultType cause : causes) {
                ClientException clientException = new ClientException(new ErrorID(cause.getId()), cause.getParameter().toArray());
                nextParent.initCause(clientException);
                nextParent = clientException;
            }
        }
        return result;
    }
}

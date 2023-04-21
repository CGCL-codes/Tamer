package edu.mit.wi.omnigene.omnitide.request;

import edu.mit.wi.omnigene.util.*;
import edu.mit.wi.omnigene.omnidas.*;

/**
 * Common interface for all Request Handlers
 */
public interface OmnigeneRequestHandler {

    /**
     * This takes care of creating <CODE>OmnigeneRequest</CODE> object.
     * @param clientDASRequest DASRequest Object
     *
     * @return Request Object
     * @throws OmnigeneException When unable to create request object,
     * then throws OmnigeneException
     */
    public Object createRequest(DASRequest clientDASRequest) throws OmnigeneException;
}

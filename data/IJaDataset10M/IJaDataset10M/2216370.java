package org.tripcom.api.exceptions;

import java.net.URI;

/**
 * Signals an internal error in the kernel.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class KernelFailureException extends APIException {

    private static final long serialVersionUID = 1L;

    public KernelFailureException(URI kernelURL, String operation, String description, String kernelStackTrace, long kernelTimestamp) {
        super(kernelURL, operation, description, kernelStackTrace, kernelTimestamp);
    }
}

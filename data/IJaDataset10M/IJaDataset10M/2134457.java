package com.volantis.shared.net.http;

import org.apache.commons.httpclient.methods.GetMethod;
import java.io.IOException;

/**
 * Hides the code that actually executes the remote request.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface MethodExecuter {

    /**
     * Execute the method.
     *
     * @param method            The method to execute.
     * @return The status code.
     * @throws IOException If there was a problem executing the method.
     */
    public HttpStatusCode execute(GetMethod method) throws IOException;
}

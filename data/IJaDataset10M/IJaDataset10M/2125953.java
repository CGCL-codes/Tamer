package com.volantis.map.agent;

import com.volantis.map.common.param.Parameters;

/**
 * The callback that will be invoked by the Media Conversion Program when the
 * requested information becomes available. Note that this callback will only
 * ever be executed by the thread that calls into the
 * {@link MediaAgent#waitForComplete()} method. As such it will be thread safe.
 * 
 * @mock.generate
 */
public interface ResponseCallback {

    /**
     * This method will be called when the Media Conversion Program has the
     * requested information and the caller has entered the
     * {@link MediaAgent#waitForComplete()} method.
     *
     * @param params the parameters asked for. This MAY contain more parameters
     * then where requested. It will always contain a value for the generated
     * short URL. The key for this value is
     * {@link MediaAgent.OUTPUT_URL_PARAMETER_NAME}.
     *
     * @throws Exception if any problem is encountered.
     */
    public void execute(Parameters params) throws Exception;
}

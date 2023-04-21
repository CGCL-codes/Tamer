package com.lbf.middlesim.msg;

import com.lbf.middlesim.fom.OCInstance;

/**
 * Instruct the RTI to delete the given object instance (the handle can be fetched from it). If the
 * supplied <code>time</code> is not set to {@link MSRequest#NULL_TIME NULL_TIME}, then it
 * represents the time to send with the request.
 * <p/>
 * <b>NOTE:</b> If the time is NOT supplied, but the proxy federate IS regulating, the message
 * SHOULD be sent with a time equal to federate's LBTS. As obtained from the MSState
 * with {@link com.lbf.middlesim.core.MSState#getLBTS() getLBTS()} method.
 */
public class REQ_Delete extends MSRequest {

    private static final long serialVersionUID = 98121116105109L;

    private OCInstance instance;

    private double time;

    private String tag;

    public REQ_Delete() {
        super();
    }

    public REQ_Delete(OCInstance instance, String tag) {
        this();
        this.instance = instance;
        this.time = NULL_TIME;
        this.tag = tag;
    }

    public REQ_Delete(OCInstance instance, double time, String tag) {
        this(instance, tag);
        this.time = time;
    }

    public OCInstance getInstance() {
        return instance;
    }

    public void setInstance(OCInstance instance) {
        this.instance = instance;
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

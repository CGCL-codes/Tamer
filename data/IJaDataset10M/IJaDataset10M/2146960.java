package com.vangent.hieos.services.xds.bridge.activity;

/**
 * Interface description
 *
 *
 * @version        v1.0, 2011-06-22
 * @author         Vangent    
 */
public interface ISubmitDocumentRequestActivity {

    /**
     * Method description
     *
     *
     * @param context
     *
     * @return
     */
    public abstract boolean execute(SDRActivityContext context);

    /**
     * Method description
     *
     *
     * @return
     */
    public abstract String getName();
}

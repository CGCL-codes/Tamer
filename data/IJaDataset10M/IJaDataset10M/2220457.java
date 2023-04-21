package com.problexi.wicket.component;

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.WebResponse;

/**
 * Flexigrid Request Handler.
 *
 * @author Ahmed Garhy
 * @version 1.0.0
 */
final class FlexiGridRequestHandler implements IRequestHandler {

    /**
     * Grid's Data.
     *
     * @since 1.0.0
     */
    private FlexiGridDataJavaScriptAdapter gridData;

    /**
     * Default Constructor.
     *
     * @param gridData A grid's data.
     * @since 1.0.0
     */
    public FlexiGridRequestHandler(FlexiGridDataJavaScriptAdapter gridData) {
        this.gridData = gridData;
    }

    /**
     * Detach Request.
     *
     * @param requestCycle A request cycle.
     * @since 1.0.0
     */
    @Override
    public void detach(IRequestCycle requestCycle) {
        this.gridData = null;
    }

    /**
     * Respond to Request.
     *
     * @param requestCycle A request cycle.
     * @since 1.0.0
     */
    @Override
    public void respond(IRequestCycle requestCycle) {
        WebResponse webResponse = (WebResponse) requestCycle.getResponse();
        webResponse.disableCaching();
        String gridDataJson = this.gridData.toJSON();
        webResponse.write(gridDataJson);
    }
}

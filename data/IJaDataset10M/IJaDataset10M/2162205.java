package org.wevote.client.chart;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author NorthernDemon
 */
public interface ShowChartAsync {

    /**
     * Request for tab, containing question
     *
     * @param id number of question
     */
    public void myMethod(int id, AsyncCallback<String> callback);
}

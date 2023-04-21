package com.od.jtimeseries.server.servermetrics.jmx;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 06-Feb-2010
 * Time: 11:39:08
 * To change this template use File | Settings | File Templates.
 */
public interface JmxExecutorService {

    void executeTask(JmxExecutorTask task) throws JmxExecutionException;
}

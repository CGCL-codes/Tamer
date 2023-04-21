package org.apache.mina.filter.reqres;

/**
 * TODO Add documentation
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface ResponseInspectorFactory {

    /**
     * Returns a {@link ResponseInspector}.
     */
    ResponseInspector getResponseInspector();
}

package org.toobsframework.transformpipeline.transformer;

import javax.xml.transform.Result;
import org.toobsframework.performance.IPerformanceHandler;
import org.toobsframework.util.Configuration;

/**
 * interface used to denote that certain data can be passed between transformations
 * to include context.
 * <p>
 * In particular, the toops presentation framework will use this interface to keep 
 * context information when transitioning between outer-inner layout to component
 * transformantions so that the calls to java through XALAN extensions can receive
 * the context they need to chain into another transformation.
 * 
 * @author jaimegarza
 * 
 */
public interface IXMLTransformerHelper {

    public abstract Configuration getConfiguration();

    public abstract Result getNewResult();

    public abstract ToobsTransformer getTransformer(String contentType) throws ToobsTransformerException;

    public abstract IPerformanceHandler getPerformanceHandler();
}

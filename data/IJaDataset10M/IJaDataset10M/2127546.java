package uk.org.ogsadai.dqp.presentation.common;

import java.net.MalformedURLException;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.resource.ResourceState;

/**
 * Factory class used to generate EvaluationNode objects.  The factory pattern
 * is used as the actual implementation of EvaluationNode is dependent on 
 * required security properties.
 * <p>
 * Note that this interface is NOT presentation layer neutral.  It works only
 * for presentation layers that are based on URL and resource IDs.   This
 * includes the Axis and GT presentation layers.
 *
 * @author The OGSA-DAI Project Team
 */
public interface EvaluationNodeFactory {

    /**
     * Configures the factory.
     * 
     * @param resourceState  state of the DQP resource
     * 
     * @throws DQPResourceConfigurationException
     *    if there is a configuration error.
     */
    public void configure(ResourceState resourceState) throws DQPResourceConfigurationException;

    /**
     * Creates a new evaluation node.
     * 
     * @param url      
     *    URL of the evaluation node server.
     * @param drerID   
     *    ID of the evaluation node's DERE
     * @param dsos     
     *    name of the evaluation node's data source service
     * @param dsis     
     *    name of the evaluation node's data sink service
     * @param isLocal  
     *    <tt>true</tt> if evaluation node is local (i.e. on the same OGSA-DAI
     *    instance as the DQP resource), <tt>false</tt> otherwise.
     *    
     * @return an instance of <code>EvaluationNode</code>
     * 
     * @throws MalformedURLException  if the URL is malformed.
     */
    EvaluationNode createEvaluationNode(String url, String drerID, String dsos, String dsis, boolean isLocal) throws MalformedURLException;
}

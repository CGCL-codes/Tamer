package uk.org.ogsadai.resource.dataresource.dqp;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.resource.dataresource.DataResourceState;

/**
 * DQP resource state which stores the configuration information.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface DQPResourceState {

    /**
     * Returns the wrapped data resource state.
     * 
     * @return data resource state
     */
    public DataResourceState getState();

    /**
     * Returns the compiler configuration.
     * 
     * @return compiler configuration
     */
    public CompilerConfiguration getCompilerConfiguration();

    /**
     * Returns dqp federation object.
     * 
     * @return dqp federation
     */
    public DQPFederation getFederation();
}

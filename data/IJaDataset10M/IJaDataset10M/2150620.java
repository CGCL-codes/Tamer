package org.iascf.itmm.client.rpc.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.Map;
import org.iascf.itmm.client.configuration.interfaces.ITaxonomy;
import org.iascf.itmm.client.configuration.rules.ChoiceRule;
import org.iascf.itmm.client.configuration.rules.IRuleBase;
import org.iascf.itmm.client.configuration.rules.ProhibitsRule;
import org.iascf.itmm.client.configuration.rules.RequiresRule;

/**
 * Configuration service RPC interface.
 * 
 * @author Daniel Gloeckner (dgloeckner@iasb.org)
 */
public interface ConfigurationService extends RemoteService {

    /**
     * Get taxonomy descriptions (IDs, names)
     * @return List of {@code IdentifiableComponent} objects.
     */
    public Map<String, String> getTaxonomieDescriptions();

    /**
     * Get the Taxonomy with given ID.
     * 
     *
     * @param taxID 
     * @return the ITaxonomy
     */
    public ITaxonomy getTaxonomy(String taxID);

    /**
     * Get the IRuleBase.
     * @param ID taxonomy ID
     * @return {@code IRuleBase} with modules and submodules
     */
    public IRuleBase getRuleBase(String ID);

    /**
     * This is a workaround for SerializationException bug in GWT 1.5 RC1
     * @param r
     * @param r2
     * @param r3 
     */
    public void dummy(ProhibitsRule r, RequiresRule r2, ChoiceRule r3);
}

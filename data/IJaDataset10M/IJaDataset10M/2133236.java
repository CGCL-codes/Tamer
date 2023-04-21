package org.osmius.service;

import org.osmius.model.OsmMasteragentParameter;
import java.util.List;

/**
 * Business Service Interface to handle communication between web and persistence layer.
 * Exposes the neccessary methods to handle a master agent parameter manager
 */
public interface OsmMasteragentParameterManager extends Manager {

    /**
    * Gets the parameters of a master agent
    * @param indMaster The master agent identificator
    * @return The master agent parameters
    */
    public List getOsmMasteragentParameters(String indMaster);

    /**
    * Gets the parameters of master agents
    * @param osmMasteragentParameter A parametrized master agent parameters
    * @return The master agents parameters list
    */
    public List getOsmMasteragentParameters(OsmMasteragentParameter osmMasteragentParameter);

    /**
    * Save the parameters of a master agent
    * @param osmMasteragentParameters The master agent parameters array
    */
    public void saveOsmMasteragentParameters(OsmMasteragentParameter[] osmMasteragentParameters);
}

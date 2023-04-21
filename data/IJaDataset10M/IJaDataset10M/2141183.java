package org.osmius.service.impl;

import org.osmius.service.OsmMasteragentParameterManager;
import org.osmius.model.OsmMasteragentParameter;
import org.osmius.dao.OsmMasteragentParameterDao;
import java.util.List;

/**
 * @see org.osmius.service.OsmMasteragentParameterManager
 */
public class OsmMasteragentParameterManagerImpl extends BaseManager implements OsmMasteragentParameterManager {

    OsmMasteragentParameterDao dao;

    /**
    * Sets a master agent parameter dao - <a href="http://www.springframework.org">Spring</a> IoC
    * @param dao
    */
    public void setOsmMasteragentParameterDao(OsmMasteragentParameterDao dao) {
        this.dao = dao;
    }

    /**
    * @see org.osmius.service.OsmMasteragentParameterManager#getOsmMasteragentParameters(String)
    */
    public List getOsmMasteragentParameters(String indMaster) {
        return dao.getOsmMasteragentParameters(indMaster);
    }

    /**
    * @see org.osmius.service.OsmMasteragentParameterManager#getOsmMasteragentParameters(org.osmius.model.OsmMasteragentParameter)
    */
    public List getOsmMasteragentParameters(OsmMasteragentParameter osmMasteragentParameter) {
        return dao.getOsmMasteragentParameters(osmMasteragentParameter);
    }

    /**
    * @see org.osmius.service.OsmMasteragentParameterManager#saveOsmMasteragentParameters(org.osmius.model.OsmMasteragentParameter[])
    */
    public void saveOsmMasteragentParameters(OsmMasteragentParameter[] osmMasteragentParameters) {
        dao.saveOsmMasteragentParameters(osmMasteragentParameters);
    }
}

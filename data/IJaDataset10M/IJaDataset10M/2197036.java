package org.osmius.dao;

import org.osmius.model.OsmTypplatformTypagent;

public interface OsmTypplatformTypagentDao extends Dao {

    public OsmTypplatformTypagent getOsmTypplatformTypagent(String typAgent, String typPlatform);

    public void saveOrUpdateOsmTypplatformTypagent(OsmTypplatformTypagent osmTypplatformTypagent);
}

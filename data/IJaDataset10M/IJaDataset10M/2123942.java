package org.osmius.service.impl;

import org.osmius.dao.OsmPluginDao;
import org.osmius.model.OsmPlugin;
import org.osmius.service.OsmPluginManager;
import java.util.List;

public class OsmPluginManagerImpl extends BaseManager implements OsmPluginManager {

    private OsmPluginDao dao;

    public void setOsmPluginDao(OsmPluginDao dao) {
        this.dao = dao;
    }

    public void saveOrUpdateOsmPlugin(OsmPlugin osmPlugin) {
        dao.saveOrUpdateOsmPlugin(osmPlugin);
    }

    public Long getSizePlugins(OsmPlugin osmPlugin) {
        return dao.getSizePlugins(osmPlugin);
    }

    public List getOsmPlugins(OsmPlugin osmPlugin, int rowStart, int pageSize, String orderBy) {
        return dao.getOsmPlugins(osmPlugin, rowStart, pageSize, orderBy);
    }

    public OsmPlugin getOsmPlugin(String plugin) {
        return dao.getOsmPlugin(plugin);
    }
}

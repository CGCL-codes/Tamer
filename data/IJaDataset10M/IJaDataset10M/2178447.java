package org.osmius.service.impl;

import org.osmius.dao.OsmUserscriptMasteragentDao;
import org.osmius.service.OsmUserscriptsManager;
import org.osmius.dao.OsmUserscriptsDao;
import org.osmius.dao.OsmUserscriptTypplatformDao;
import org.osmius.model.OsmUserscripts;
import org.osmius.model.OsmUserscriptTypplatform;
import org.osmius.model.OsmUserscriptTypplatformId;
import java.util.List;

public class OsmUserscriptsManagerImpl extends BaseManager implements OsmUserscriptsManager {

    OsmUserscriptsDao dao;

    OsmUserscriptTypplatformDao osmUserscriptTypplatformDao;

    OsmUserscriptMasteragentDao osmUserscriptMasteragentDao;

    public void setOsmUserscriptMasteragentDao(OsmUserscriptMasteragentDao osmUserscriptMasteragentDao) {
        this.osmUserscriptMasteragentDao = osmUserscriptMasteragentDao;
    }

    public void setOsmUserscriptTypplatformDao(OsmUserscriptTypplatformDao osmUserscriptTypplatformDao) {
        this.osmUserscriptTypplatformDao = osmUserscriptTypplatformDao;
    }

    public void setOsmUserscriptsDao(OsmUserscriptsDao dao) {
        this.dao = dao;
    }

    public OsmUserscripts getOsmUserscript(String hTextScript) {
        return dao.getOsmUserscript(hTextScript);
    }

    public void saveOsmUserscripts(OsmUserscripts osmUserscripts, String[] platforms) {
        dao.saveOsmUserscripts(osmUserscripts);
        osmUserscriptTypplatformDao.deleteOsmUserscriptTypplatform(osmUserscripts.getTxtScript());
        for (int i = 0; i < platforms.length; i++) {
            String platform = platforms[i];
            OsmUserscriptTypplatform osmUserscriptTypplatform = new OsmUserscriptTypplatform();
            OsmUserscriptTypplatformId id = new OsmUserscriptTypplatformId(osmUserscripts.getTxtScript(), platform);
            osmUserscriptTypplatform.setId(id);
            osmUserscriptTypplatformDao.saveOsmUserscriptTypplatform(osmUserscriptTypplatform);
            osmUserscriptMasteragentDao.updateDtiFinsent(osmUserscriptTypplatform.getId().getTypPlatform(), osmUserscripts.getTxtScript());
        }
    }

    public List getOsmUserscripts(String txtScript) {
        return dao.getOsmUserscripts(txtScript);
    }

    public boolean testScript(String script) {
        return dao.testScript(script);
    }

    public OsmUserscripts getOsmUserscriptById(String id) {
        return dao.getOsmUserscriptById(id);
    }

    public void renameOsmUserscripts(OsmUserscripts finalScript, String[] platforms) {
        dao.renameOsmUserscripts(finalScript, platforms);
    }

    public void renameOsmUserscripts(OsmUserscripts oldScript, OsmUserscripts finalScript) {
        dao.renameOsmUserscripts(oldScript, finalScript);
    }
}

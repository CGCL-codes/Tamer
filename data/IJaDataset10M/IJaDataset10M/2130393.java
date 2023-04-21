package org.osmius.service.impl;

import org.osmius.service.OsmNTypnotifiwayManager;
import org.osmius.model.OsmNTypnotifiway;
import org.osmius.model.OsmClient;
import org.osmius.dao.OsmNTypnotifiwayDao;
import java.util.List;

public class OsmNTypnotifiwayManagerImpl extends BaseManager implements OsmNTypnotifiwayManager {

    private OsmNTypnotifiwayDao dao;

    public void setOsmNTypnotifiwayDao(OsmNTypnotifiwayDao dao) {
        this.dao = dao;
    }

    public List getOsmNTypnotifiways(OsmNTypnotifiway osmNTypnotifiway) {
        return dao.getOsmNTypnotifiways(osmNTypnotifiway);
    }

    public OsmNTypnotifiway getOsmNTypnotifiway(String text) {
        return dao.getOsmNTypnotifiway(text);
    }
}

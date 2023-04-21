package org.osmius.service.impl;

import org.osmius.service.OsmDwhInstancecapacityDayManager;
import org.osmius.dao.OsmDwhInstancecapacityDayDao;
import java.util.List;

public class OsmDwhInstancecapacityDayManagerImpl extends BaseManager implements OsmDwhInstancecapacityDayManager {

    private OsmDwhInstancecapacityDayDao dao;

    public void setOsmDwhInstancecapacityDayDao(OsmDwhInstancecapacityDayDao dao) {
        this.dao = dao;
    }

    public List getEvents(String idnInstance, String typEvent, String dtiIni, String dtiFin) {
        return dao.getEvents(idnInstance, typEvent, dtiIni, dtiFin);
    }

    public List getTop5EventsVariation(String dtiIni, String dtiFin) {
        return dao.getTop5EventsVariation(dtiIni, dtiFin);
    }
}

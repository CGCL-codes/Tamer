package org.osmius.service.impl;

import org.osmius.service.OsmNNotificationWayManager;
import org.osmius.dao.OsmNNotificationWayDao;
import org.osmius.model.OsmNNotificationWay;
import java.util.List;

public class OsmNNotificationWayManagerImpl extends BaseManager implements OsmNNotificationWayManager {

    private OsmNNotificationWayDao osmNNotificationWayDao;

    public void setOsmNNotificationWayDao(OsmNNotificationWayDao osmNNotificationWayDao) {
        this.osmNNotificationWayDao = osmNNotificationWayDao;
    }

    public Long getSizeOsmNNotificationWay(OsmNNotificationWay osmNNotificationWay) {
        return osmNNotificationWayDao.getSizeOsmNNotificationWay(osmNNotificationWay);
    }

    public List getOsmNNotificationWay(OsmNNotificationWay osmNNotificationWay, int rowStart, int pageSize, String orderBy) {
        return osmNNotificationWayDao.getOsmNNotificationWay(osmNNotificationWay, rowStart, pageSize, orderBy);
    }
}

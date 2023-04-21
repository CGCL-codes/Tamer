package org.osmius.dao;

import org.osmius.model.OsmNHistnotifications;
import java.util.List;

public interface OsmNHistnotificationsDao extends Dao {

    public Long getSizeOsmNHistnotifications(OsmNHistnotifications osmNHistnotifications, int days);

    public List getOsmNHistnotifications(OsmNHistnotifications osmNHistnotifications, int rowStart, int pageSize, String orderBy, int days);

    public void removeByInstance(String instance);

    public void removeByService(String service);

    public OsmNHistnotifications getOsmNHistnotificationById(Long idnNotifi);
}

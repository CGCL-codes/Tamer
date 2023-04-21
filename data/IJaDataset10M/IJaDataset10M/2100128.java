package org.opennms.web.svclayer;

import org.opennms.web.command.LocationMonitorIdCommand;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

/**
 * 
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
@Transactional(readOnly = true)
public interface DistributedPollerService {

    public LocationMonitorListModel getLocationMonitorList();

    public LocationMonitorListModel getLocationMonitorDetails(LocationMonitorIdCommand command, BindException errors);

    @Transactional(readOnly = false)
    public void pauseLocationMonitor(LocationMonitorIdCommand command, BindException errors);

    @Transactional(readOnly = false)
    public void resumeLocationMonitor(LocationMonitorIdCommand command, BindException errors);

    @Transactional(readOnly = false)
    public void deleteLocationMonitor(LocationMonitorIdCommand command, BindException errors);
}

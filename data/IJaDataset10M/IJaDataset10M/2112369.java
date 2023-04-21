package hambo.svc.log;

import hambo.svc.ClientService;

/**
 * @deprecated replaced by the EventMonitor
 */
public interface EventLogService extends ClientService {

    /**
   * Returns a event log object.
   */
    EventLog getEventLog(String userID, String sessionKey);
}

package hambo.svc.log.standard;

import hambo.svc.*;
import hambo.svc.log.*;

public class StandardEventLogService implements EventLogService {

    private ClientIdentity cid;

    private static StandardEventLogger logger = null;

    public StandardEventLogService(ClientIdentity cid, ServiceContext context) {
        this.cid = cid;
        synchronized (StandardEventLogService.class) {
            if (logger == null) {
                logger = new StandardEventLogger(context);
            }
        }
    }

    /**
   * 
   */
    public EventLog getEventLog(String userID, String sessionKey) {
        return new StandardEventLog(userID, sessionKey, cid.toString(), logger);
    }
}

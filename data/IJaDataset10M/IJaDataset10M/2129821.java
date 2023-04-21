package net.teqlo.runtime.memory;

import java.io.PrintWriter;
import java.util.UUID;
import net.teqlo.TeqloException;
import net.teqlo.components.Activity;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.ServiceLookup;
import net.teqlo.db.User;
import net.teqlo.runtime.ContextMonitor;

/**
 * A dry run context dishes out dry run activities, and is monitored so that progress can be reported back to the user.
 * 
 * @author jthwaites
 * 
 */
public class DryrunContext extends BaseContext {

    protected ContextMonitor monitor = null;

    protected PrintWriter printWriter = null;

    public DryrunContext(User user, BaseService service, UUID id, String tpFqn) {
        super(user, service, id, tpFqn);
        try {
            this.monitor = new ContextMonitor(this);
            this.monitor.open();
        } catch (TeqloException e) {
            this.exception = e;
        }
    }

    public Activity createActivityInstance(Short activitySerial) throws TeqloException {
        ServiceLookup sl = this.getServiceLookup();
        ActivityLookup al = sl.getActivityLookup(activitySerial);
        Activity activity = new DryrunActivity(this.user, al);
        return activity;
    }

    /**
	 * Returns the dry run activity monitor
	 * @return
	 */
    public ContextMonitor getContextMonitor() {
        return this.monitor;
    }
}

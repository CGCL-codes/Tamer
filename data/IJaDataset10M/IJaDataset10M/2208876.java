package org.openmobster.core.mobileCloud.android.module.sync.daemon;

import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import org.openmobster.core.mobileCloud.android.errors.ErrorHandler;
import org.openmobster.core.mobileCloud.android.errors.SystemException;
import org.openmobster.core.mobileCloud.android.module.sync.SyncService;
import org.openmobster.core.mobileCloud.android.module.sync.engine.ChangeLogEntry;
import org.openmobster.core.mobileCloud.android.module.sync.engine.SyncDataSource;

/**
 * Initiate Sync Task is run by the Daemon to initiate sync session from the device with the server based on appropriate
 * environmental state. Such state would be a modified device changelog indicating change in the state of data on the device, etc
 * 
 * @author openmobster@gmail.com
 *
 */
final class InitiateSyncTask extends TimerTask {

    boolean inProgress;

    public InitiateSyncTask() {
    }

    public void run() {
        inProgress = true;
        try {
            List<String> servicesToSync = new ArrayList<String>();
            List<ChangeLogEntry> changelog = SyncDataSource.getInstance().readChangeLog();
            if (changelog != null) {
                for (ChangeLogEntry entry : changelog) {
                    String service = entry.getNodeId();
                    if (!servicesToSync.contains(service)) {
                        servicesToSync.add(service);
                    }
                }
            }
            for (String service : servicesToSync) {
                SyncService.getInstance().performTwoWaySync(service, service, true);
            }
        } catch (Exception e) {
            SystemException syse = new SystemException(this.getClass().getName(), "InitiateSyncTask", new Object[] { "Exception=" + e.toString(), "Message=" + e.getMessage() });
            ErrorHandler.getInstance().handle(syse);
        } finally {
            inProgress = false;
        }
    }
}

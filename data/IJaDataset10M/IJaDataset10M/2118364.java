package javax.jcr.tools.backup.impl.systemview;

import javax.jcr.tools.backup.BackupWorker;
import javax.jcr.tools.backup.Context;
import java.io.OutputStream;

/**
 * @author Alex Karshakevich
 * Date:    Aug 13, 2007
 * Time:    2:31:55 AM
 */
public class SystemViewBackupWorkerImpl implements BackupWorker {

    public static final String REPOSITORY_DATA_RESOURCENAME = "data.xml";

    /**
   * Path to node to start backup at
   */
    private String exportPath;

    /**
   * Default is to backup the entire repository
   */
    public static final String DEFAULT_EXPORT_PATH = "/";

    /**
   * Default constructor with required configuration
   */
    public SystemViewBackupWorkerImpl() {
        exportPath = DEFAULT_EXPORT_PATH;
    }

    /**
   * Backup repository data using a system view export
   * @param context execution context
   * @throws Exception when unable to perform an operation.
   */
    public void execute(Context context) throws Exception {
        OutputStream out = null;
        try {
            out = context.getPersistenceManager().getOutResource(REPOSITORY_DATA_RESOURCENAME, true);
            context.getSession().exportSystemView(exportPath, out, false, false);
        } finally {
            if (null != out) out.close();
        }
    }

    /**
   * Set absolute path to start backup at. If left at default it will back up the root node including the /jcr:system
   * branch which will likely not be restoreable. Try setting it to the first child node that is your own data.
   * @see SystemViewBackupWorkerImpl#DEFAULT_EXPORT_PATH
   * @param exportPath absolute path to node to start backup at
   */
    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
}

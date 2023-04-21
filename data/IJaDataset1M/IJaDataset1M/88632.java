package fr.itris.glips.svgeditor.io.managers.export.monitor;

import java.awt.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the monitor used for an open action
 * @author Jordi SUC
 */
public class ExportMonitor extends Monitor {

    /**
	 * the prefix for the labels
	 */
    private String exportPrefix = "";

    /**
	 * the labels
	 */
    private String titleLabel = "", messageLabel = "";

    /**
	 * the constructor of the class
	 * @param parent the parent component used to 
	 * display the progress dialog
	 * @param min the min value of the progress
	 * @param max the max value of the progress
	 * @param exportPrefix the export prefix
	 */
    public ExportMonitor(Component parent, int min, int max, String exportPrefix) {
        super(parent, null, min, max);
        this.exportPrefix = exportPrefix;
        initialize();
    }

    @Override
    protected void initialize() {
        titleLabel = ResourcesManager.bundle.getString("FileExportMonitorTitle");
        messageLabel = ResourcesManager.bundle.getString(exportPrefix + "ExportMonitorMessage");
        waitDialog.setTitleMessage(titleLabel);
        waitDialog.setMessage(messageLabel);
    }
}

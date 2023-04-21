package ces.platform.infoplat.service.ftpserver.ftp.gui;

import javax.swing.JPanel;
import ces.platform.infoplat.service.ftpserver.ftp.FtpConfig;

/**
 * All ftp UI right side panels are derived from this class.
 */
public abstract class PluginPanel extends JPanel {

    protected FtpTree mTree;

    /**
     * Constructor - set the ftp tree object.
     */
    public PluginPanel(FtpTree tree) {
        mTree = tree;
    }

    /**
     * Get ftp tree object.
     */
    public FtpTree getTree() {
        return mTree;
    }

    /**
     * Reload new configuration. Config object can be null
     */
    public abstract void refresh(FtpConfig config);

    /**
     * Is displayable in the root pane.
     */
    public abstract boolean isDisplayable();
}

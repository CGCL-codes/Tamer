package org.silicolife.util.xls2sql.gui.sql;

import java.awt.event.MouseEvent;
import org.silicolife.data.SLDatabaseConnection;
import org.silicolife.gui.img.Images;
import org.silicolife.util.xls2sql.application.XLS2SQLApplication;
import org.silicolife.util.xls2sql.gui.DesktopPane;
import org.silicolife.util.xls2sql.gui.LabeledIconPanel;

public class DatabaseConnectionPanel extends LabeledIconPanel {

    private static final long serialVersionUID = 1L;

    public DatabaseConnectionPanel(DesktopPane desktop, String text, String toolTip, String key) {
        super(desktop, text, toolTip, key, Images.databaseConnectionImage);
    }

    public static DatabaseConnectionPanel createDatabaseConnectionPanel(DesktopPane desktop, String text, String toolTip, String key) {
        DatabaseConnectionPanel panel = new DatabaseConnectionPanel(desktop, text, toolTip, key);
        return panel;
    }

    public void mouseEntered(MouseEvent event) {
        super.mouseEntered(event);
    }

    public void mouseExited(MouseEvent event) {
        super.mouseExited(event);
    }

    public SLDatabaseConnection getDatabaseConnection() {
        XLS2SQLApplication application = getDesktop().getApplication();
        return application.getDatabaseConnection(this.getKey());
    }
}

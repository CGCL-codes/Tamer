package org.silicolife.darwin.gui.desktop;

import java.awt.Color;
import java.awt.Component;
import org.silicolife.gui.SLFrame;
import org.silicolife.gui.desktop.SLDesktop;
import org.silicolife.darwin.application.DarwinApplication;
import org.silicolife.darwin.gui.DarwinFrame;
import org.silicolife.darwin.gui.sql.DatabaseConnectionPanel;
import org.silicolife.darwin.gui.sql.DatabaseFrame;
import org.silicolife.data.SLDatabaseConnection;

public class Desktop extends SLDesktop {

    public static int ICON_VIEW = 1;

    public static int OBJECT_COUNT_VIEW = 2;

    public static int TABLE_COUNT_VIEW = 3;

    public static int TABLE_ROW_COUNT_VIEW = 4;

    private static final long serialVersionUID = 1L;

    public Desktop(SLFrame frame) {
        super(frame);
    }

    public static Desktop createDesktop(DarwinFrame frame) {
        Desktop desktop = new Desktop(frame);
        desktop.getContentPane().setBackground(Color.WHITE);
        frame.addSLFrameListener(desktop);
        return desktop;
    }

    public DesktopMouseListener createDesktopMouseListener() {
        return DesktopMouseListener.createDesktopMouseListener(this);
    }

    public DesktopObserver createDesktopObserver() {
        return DesktopObserver.createDesktopObserver(this);
    }

    public void addDatabaseConnection(SLDatabaseConnection connection) {
        DatabaseConnectionPanel panel = DatabaseConnectionPanel.createDatabaseConnectionPanel(this, connection.getShortName(), connection.getKey(), connection.getKey());
        addLabeledIconPanel(panel);
        int x = (getWidth() / 2) - (panel.getWidth() / 2);
        int y = (getHeight() / 3) - (panel.getHeight() / 2);
        panel.setLocation(x, y);
    }

    public void addDatabaseFrame(SLDatabaseConnection connection) {
        DatabaseFrame frame = DatabaseFrame.createDatabaseFrame(this, connection.getShortName(), connection.getKey(), connection.getKey());
        addFrame(frame);
        int x = (getWidth() / 2) - (frame.getWidth() / 2);
        int y = (getHeight() / 3) - (frame.getHeight() / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public void removeDatabaseConnection(String key) {
        for (Component component : getLayeredPane().getComponents()) {
            if (component instanceof DatabaseConnectionPanel) {
                if (((DatabaseConnectionPanel) component).getKey() == key) {
                    removeLabeledIconPanel((DatabaseConnectionPanel) component);
                }
            }
        }
    }

    public void removeDatabaseFrame(String key) {
        for (Component component : getLayeredPane().getComponents()) {
            if (component instanceof DatabaseFrame) {
                if (((DatabaseFrame) component).getKey() == key) {
                    removeFrame((DatabaseFrame) component);
                }
            }
        }
    }

    public void setView(int view) {
        ((DesktopObserver) getDesktopObserver()).fireDesktopViewChanged(view);
    }

    public DarwinApplication getApplication() {
        return (DarwinApplication) getFrame().getApplication();
    }
}

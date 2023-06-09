package core;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Action;
import javax.swing.SwingUtilities;

public final class MacUtils extends Application {

    private static MacUtils instance;

    private MacUtils() {
    }

    public static boolean isMac() {
        return System.getProperty("os.name").startsWith("Mac OS X");
    }

    static void init(final Action exitAction, final Action prefsAction, final Action aboutAction) {
        instance = new MacUtils();
        instance.addApplicationListener(new ApplicationAdapter() {

            public void handleAbout(ApplicationEvent e) {
                final ActionEvent event = new ActionEvent(e.getSource(), 0, "About");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            aboutAction.actionPerformed(event);
                        } catch (Exception e) {
                            ErrorMsg.reportStatus(e);
                        }
                    }
                });
                e.setHandled(true);
            }

            public void handleOpenFile(ApplicationEvent e) {
                final File file = new File(e.getFilename());
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            Actions.openFrame(file);
                        } catch (Exception e) {
                            ErrorMsg.reportStatus(e);
                        }
                    }
                });
                e.setHandled(true);
            }

            public void handlePreferences(ApplicationEvent e) {
                e.setHandled(true);
                final ActionEvent event = new ActionEvent(e.getSource(), 0, "Preferences");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            prefsAction.actionPerformed(event);
                        } catch (Exception e) {
                            ErrorMsg.reportStatus(e);
                        }
                    }
                });
            }

            public void handleQuit(ApplicationEvent e) {
                exitAction.actionPerformed(new ActionEvent(e.getSource(), 0, "Exit"));
                if (PatchEdit.getDesktop().isReadyToExit()) {
                    e.setHandled(true);
                } else {
                    e.setHandled(false);
                }
            }
        });
        instance.setEnabledPreferencesMenu(true);
    }
}

package jmodnews.gui;

import javax.swing.JOptionPane;
import jmodnews.gui.view.ViewPanelContainer;

/**
 * An internal (never visible) {@link ModulePanel} that is
 * used to deliver events and user actions to the main window
 * containing all the other panels.
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class WindowControlPanel extends ModulePanel {

    public WindowControlPanel(ModuleGroup group) {
        super(group, null, "HiddenWindowControlPanel", true);
    }

    protected void getNotification(ModulePanel source, String eventType, Object data) {
    }

    protected int handleUserAction(UserAction ua) {
        String name = ua.getActionName();
        if (name.equals("Crash")) {
            throw new RuntimeException("Crash intended");
        } else if (name.equals("HeavyCrash")) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.exit(99);
                }
            }).start();
            throw new RuntimeException("Heavy crash intended");
        } else if (name.startsWith("NewView-")) {
            String view = name.substring(8);
            ModuleGroup group = new ModuleGroup(null, myGroup.getModuleManager());
            ViewPanelContainer vpc = myGroup.getViewPanelContainer();
            group.setViewPanelContainer(vpc);
            new WindowControlPanel(group);
            vpc.createViewPanel(group, view, true);
        } else if (name.startsWith("DependentView-")) {
            String view = name.substring(14);
            ViewPanelContainer vpc = myGroup.getViewPanelContainer();
            vpc.createViewPanel(myGroup, view, true);
        } else if (name.startsWith("NewWindow-")) {
            String view = name.substring(10);
            new MainWindow(myGroup.getModuleManager(), view);
        } else if (name.equals("CloseView")) {
            ViewPanelContainer vpc = myGroup.getViewPanelContainer();
            vpc.closeCurrentView();
        } else if (name.equals("RunCommand")) {
            String cmd = JOptionPane.showInputDialog(this, "Command to run:");
            if (cmd != null) {
                int result = myGroup.fireUserAction(new UserAction(cmd));
                if (result == UserAction.NOT_INTERESTED) {
                    JOptionPane.showMessageDialog(this, "Command not found");
                } else {
                    JOptionPane.showMessageDialog(this, "Return value: " + result);
                }
                return 0;
            }
            return 1;
        } else {
            UserActionHandler uah = myGroup.getModuleManager().getGlobalActionHandler(name);
            if (uah != null) {
                return uah.handleUserAction(myGroup, ua);
            }
        }
        return UserAction.NOT_INTERESTED;
    }
}

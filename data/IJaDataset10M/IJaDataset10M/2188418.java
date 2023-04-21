package admin.astor;

import admin.astor.tools.PollingProfiler;
import admin.astor.tools.DeviceHierarchyDialog;
import admin.astor.tools.PopupTable;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ServerPopupMenu extends JPopupMenu implements AstorDefs {

    private TangoHost host;

    private TangoServer server;

    private LevelTree tree;

    private JFrame frame;

    private HostInfoDialog parent;

    private int mode;

    static final int SERVERS = 0;

    static final int LEVELS = 1;

    static final int NOTIFD = 2;

    private static String[] serverMenuLabels = { "Start server", "Restart server", "Set startup level", "Uptime", "Polling Manager", "Polling Profiler", "Pool Threads Manager", "Configure (Wizard)", "DB Attribute Properties", "Server Info", "Class  Info", "Device Dependencies", "Test   Device", "Check  States", "Black Box", "Starter logs", "Standard Error" };

    private static String[] levelMenuLabels = { "Start servers", "Stop  servers", "Change level number", "Set startup level for each server", "Uptime", "Expand Tree" };

    private static String[] notifdMenuLabels = { "Start daemon" };

    private static final int OFFSET = 2;

    private static final int START_STOP = 0;

    private static final int RESTART = 1;

    private static final int STARTUP_LEVEL = 2;

    private static final int UPTIME = 3;

    private static final int POLLING_MANAGER = 4;

    private static final int POLLING_PROFILER = 5;

    private static final int POOL_THREAD_MAN = 6;

    private static final int CONFIGURE = 7;

    private static final int DB_ATTRIBUTES = 8;

    private static final int SERVER_INFO = 9;

    private static final int CLASS_INFO = 10;

    private static final int DEPENDENCIES = 11;

    private static final int TEST_DEVICE = 12;

    private static final int CHECK_STATES = 13;

    private static final int BLACK_BOX = 14;

    private static final int STARTER_LOGS = 15;

    private static final int STD_ERROR = 16;

    private static final int START = 0;

    private static final int STOP = 1;

    private static final int CHANGE_LEVEL = 2;

    private static final int SERVER_LEVELS = 3;

    private static final int UPTIME_LEVEL = 4;

    private static final int EXPAND = 5;

    private static final boolean TANGO_7 = true;

    public ServerPopupMenu(JFrame frame, HostInfoDialog parent, TangoHost host, int mode) {
        super();
        this.frame = frame;
        this.parent = parent;
        this.host = host;
        this.mode = mode;
        buildBtnPopupMenu();
    }

    private void buildBtnPopupMenu() {
        JLabel title = new JLabel("Server Control :");
        title.setFont(new java.awt.Font("Dialog", 1, 16));
        add(title);
        add(new JPopupMenu.Separator());
        String[] pMenuLabels;
        switch(mode) {
            case SERVERS:
                pMenuLabels = serverMenuLabels;
                break;
            case LEVELS:
                pMenuLabels = levelMenuLabels;
                break;
            default:
                title.setText("Event Notify Daemon");
                pMenuLabels = notifdMenuLabels;
        }
        for (String lbl : pMenuLabels) {
            JMenuItem btn = new JMenuItem(lbl);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    cmdActionPerformed(evt);
                }
            });
            add(btn);
        }
    }

    public void showMenu(MouseEvent evt, JTree tree, TangoServer server) {
        TreePath selectedPath = tree.getPathForLocation(evt.getX(), evt.getY());
        if (selectedPath == null) return;
        tree.setSelectionPath(selectedPath);
        String name = server.getName();
        JLabel lbl = (JLabel) getComponent(0);
        lbl.setText("  " + name + "  :");
        this.server = server;
        JMenuItem mi = (JMenuItem) getComponent(START_STOP + OFFSET);
        boolean running = (server.getState() == DevState.ON);
        if (running || server.getState() == DevState.MOVING) mi.setText("Kill  Server"); else mi.setText("Start Server");
        getComponent(RESTART + OFFSET).setEnabled(running);
        getComponent(POLLING_PROFILER + OFFSET).setEnabled(running);
        getComponent(TEST_DEVICE + OFFSET).setEnabled(running);
        getComponent(CHECK_STATES + OFFSET).setEnabled(running);
        getComponent(BLACK_BOX + OFFSET).setEnabled(running);
        getComponent(CONFIGURE + OFFSET).setEnabled(running);
        getComponent(DB_ATTRIBUTES + OFFSET).setVisible(!running);
        getComponent(POOL_THREAD_MAN + OFFSET).setVisible(TANGO_7);
        getComponent(DEPENDENCIES + OFFSET).setVisible(TANGO_7);
        location = tree.getLocationOnScreen();
        location.x += evt.getX();
        location.y += evt.getY();
        show(tree, evt.getX(), evt.getY());
    }

    public void showMenu(MouseEvent evt, LevelTree tree, boolean expanded) {
        this.tree = tree;
        JLabel lbl = (JLabel) getComponent(0);
        lbl.setText("  " + tree + "  :");
        JMenuItem mi = (JMenuItem) getComponent(EXPAND + OFFSET);
        mi.setText((expanded) ? "Collapse Tree" : levelMenuLabels[EXPAND]);
        getComponent(SERVER_LEVELS + OFFSET).setVisible(false);
        location = tree.getLocationOnScreen();
        location.x += evt.getX();
        location.y += evt.getY();
        show(tree, evt.getX(), evt.getY());
    }

    public void showMenu(MouseEvent evt) {
        JLabel lbl = (JLabel) evt.getSource();
        boolean running = (host.notifyd_state == all_ok);
        getComponent(START + OFFSET).setEnabled(!running);
        location = lbl.getLocationOnScreen();
        show(lbl, evt.getX(), evt.getY());
    }

    private Point location;

    private void cmdActionPerformed(ActionEvent evt) {
        switch(mode) {
            case SERVERS:
                serverCmdActionPerformed(evt);
                break;
            case LEVELS:
                levelCmdActionPerformed(evt);
                break;
            case NOTIFD:
                notifdCmdActionPerformed(evt);
                break;
        }
    }

    private void notifdCmdActionPerformed(ActionEvent evt) {
        Object obj = evt.getSource();
        int cmdidx = -1;
        for (int i = 0; i < notifdMenuLabels.length; i++) if (getComponent(OFFSET + i) == obj) cmdidx = i;
        switch(cmdidx) {
            case START:
                host.startServer(parent, notifyd_prg + "/" + host.getName());
                break;
        }
    }

    private void levelCmdActionPerformed(ActionEvent evt) {
        Object obj = evt.getSource();
        int cmdidx = -1;
        for (int i = 0; i < levelMenuLabels.length; i++) if (getComponent(OFFSET + i) == obj) cmdidx = i;
        switch(cmdidx) {
            case START:
                parent.startLevel(tree.getLevelRow());
                break;
            case STOP:
                parent.stopLevel(tree.getLevelRow());
                break;
            case CHANGE_LEVEL:
                tree.changeChangeLevel(tree.getLevelRow());
                break;
            case SERVER_LEVELS:
                tree.changeServerLevels();
                break;
            case UPTIME_LEVEL:
                tree.displayUptime();
                break;
            case EXPAND:
                tree.toggleExpandCollapse();
                break;
        }
    }

    private void serverCmdActionPerformed(ActionEvent evt) {
        Object obj = evt.getSource();
        int cmdidx = -1;
        for (int i = 0; i < serverMenuLabels.length; i++) if (getComponent(OFFSET + i) == obj) cmdidx = i;
        switch(cmdidx) {
            case STARTUP_LEVEL:
                if (server.startupLevel(parent, host.getName(), location)) parent.updateData();
                break;
            case UPTIME:
                try {
                    String[] exportedStr = server.getServerUptime();
                    String[] columns = new String[] { "Last   exported", "Last unexported" };
                    PopupTable ppt = new PopupTable(parent, server.getName(), columns, new String[][] { exportedStr }, new Dimension(450, 50));
                    ppt.setVisible(true);
                } catch (DevFailed e) {
                    ErrorPane.showErrorMessage(parent, null, e);
                }
                break;
            case POLLING_MANAGER:
                if (server.getState() == DevState.ON) new ManagePollingDialog(parent, server).setVisible(true); else try {
                    new DbPollPanel(parent, server.getName()).setVisible(true);
                } catch (DevFailed e) {
                    ErrorPane.showErrorMessage(parent, null, e);
                }
                break;
            case POOL_THREAD_MAN:
                server.poolThreadManager(parent, host);
                break;
            case POLLING_PROFILER:
                startPollingProfiler();
                break;
            case TEST_DEVICE:
                server.testDevice(parent);
                break;
            case CHECK_STATES:
                server.checkStates(parent);
                break;
            case BLACK_BOX:
                server.displayBlackBox(parent);
                break;
            case STARTER_LOGS:
                host.displayLogging(parent, server.toString());
                break;
            case CONFIGURE:
                server.configureWithWizard(parent);
                break;
            case SERVER_INFO:
                server.displayServerInfo(parent);
                break;
            case DB_ATTRIBUTES:
                server.manageMemorizedAttributes(parent);
                break;
            case CLASS_INFO:
                server.displayClassInfo(frame);
                break;
            case DEPENDENCIES:
                try {
                    new DeviceHierarchyDialog(parent, server.getName()).setVisible(true);
                } catch (DevFailed e) {
                    ErrorPane.showErrorMessage(parent, null, e);
                }
                break;
            case STD_ERROR:
                if (server != null) host.readStdErrorFile(frame, server.getName()); else host.readStdErrorFile(frame, notifyd_prg + "/" + host.getName());
                break;
            case RESTART:
                server.restart(parent, host, true);
                break;
            case START_STOP:
                if (server.getState() == DevState.ON || server.getState() == DevState.MOVING) {
                    try {
                        if (JOptionPane.showConfirmDialog(parent, "Are you sure to want to kill " + server.getName(), "Confirm Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) return;
                        host.stopServer(server.getName());
                    } catch (DevFailed e) {
                        if (e.errors[0].reason.equals("SERVER_NOT_RESPONDING")) {
                            try {
                                if (JOptionPane.showConfirmDialog(parent, e.errors[0].desc + "\n" + "Do you even want to kill it ?", "Confirm Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) return;
                                host.hardKillServer(server.getName());
                            } catch (DevFailed e2) {
                                ErrorPane.showErrorMessage(parent, null, e2);
                            }
                        } else ErrorPane.showErrorMessage(parent, null, e);
                    }
                } else host.startServer(parent, server.getName());
                break;
        }
    }

    private void startPollingProfiler() {
        try {
            String[] devnames = server.queryDevice();
            new PollingProfiler(parent, devnames).setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
    }
}

package co.edu.unal.ungrid.services.client.applet.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import co.edu.unal.ungrid.services.client.applet.document.AbstractDocument;
import co.edu.unal.ungrid.services.client.applet.util.resource.ResourceHelper;
import co.edu.unal.ungrid.services.client.applet.view.action.SuperAction;
import co.edu.unal.ungrid.services.client.applet.view.action.status.NetStatus;
import co.edu.unal.ungrid.services.client.service.ServiceFactory;
import co.edu.unal.ungrid.services.proxy.ProxyEventListener;
import co.edu.unal.ungrid.services.proxy.ProxyServer;
import co.edu.unal.ungrid.services.proxy.SynchronizeListener;

public abstract class AbstractMenuHelper implements ProxyEventListener, SynchronizeListener {

    protected abstract MenuEntry[] getMainEntries();

    protected abstract SuperAction[][] getMenuActions();

    protected abstract int getNumButtonGroups();

    protected abstract Color getForeground();

    protected abstract Font getFont();

    protected abstract int getNetEntryIdx();

    protected abstract int getAutoEntryIdx();

    protected abstract String getOnlineAutoToolTipText();

    protected abstract String getOfflineAutoToolTipText();

    protected abstract int getSelectedIdx();

    public abstract void setUndoEnabled(String sText, boolean b);

    public abstract void setRedoEnabled(String sText, boolean b);

    public static class MenuEntry {

        public MenuEntry(String sLabel, Class<?> cls, String sIconUrlNormal, String sIconUrlPressed, String sToolTip, SuperAction buttonAction) {
            this.sLabel = sLabel;
            this.cls = cls;
            this.sIconUrlNormal = sIconUrlNormal;
            this.sIconUrlPressed = sIconUrlPressed;
            this.sToolTip = sToolTip;
            this.buttonAction = buttonAction;
        }

        public String sLabel;

        public Class<?> cls;

        public String sIconUrlNormal;

        public String sIconUrlPressed;

        public String sToolTip;

        public SuperAction buttonAction;
    }

    public AbstractMenuHelper() {
        MenuEntry[] me = getMainEntries();
        if (me != null) {
            m_caMainMenu = new Component[me.length];
            m_aaMainAction = getMenuActions();
        }
        m_butGroup = new ButtonGroup[getNumButtonGroups()];
        m_iNetOn = ResourceHelper.loadIcon(getClass(), NET_ON_ICON);
        if (m_iNetOn == null) {
            System.err.println(NET_ON_ICON + " not found!");
        }
        m_iNetOff = ResourceHelper.loadIcon(getClass(), NET_OFF_ICON);
        if (m_iNetOff == null) {
            System.err.println(NET_OFF_ICON + " not found!");
        }
        m_iNetUsr = ResourceHelper.loadIcon(getClass(), NET_USR_ICON);
        if (m_iNetUsr == null) {
            System.err.println(NET_USR_ICON + " not found!");
        }
        m_iJobEnabled = ResourceHelper.loadIcon(getClass(), JOB_ENABLED_ICON);
        if (m_iJobEnabled == null) {
            System.err.println(JOB_ENABLED_ICON + " not found!");
        }
        m_iJobDisabled = ResourceHelper.loadIcon(getClass(), JOB_DISABLED_ICON);
        if (m_iJobDisabled == null) {
            System.err.println(JOB_DISABLED_ICON + " not found!");
        }
        m_iJobExecuting = ResourceHelper.loadIcon(getClass(), JOB_EXECUTING_ICON);
        if (m_iJobExecuting == null) {
            System.err.println(JOB_EXECUTING_ICON + " not found!");
        }
    }

    public void init() {
        setUndoEnabled("Can't Undo", false);
        setRedoEnabled("Can't Redo", false);
    }

    protected Component createMenuEntry(int idx) {
        Component c = null;
        try {
            MenuEntry[] me = getMainEntries();
            Class<?> cls = me[idx].cls;
            if (cls != null) {
                c = (Component) cls.newInstance();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return c;
    }

    protected void fillStdMenuEntry(final JMenu menu, final String sLabel, final SuperAction[] actions) {
        menu.setText(sLabel);
        menu.setForeground(getForeground());
        menu.setFont(getFont());
        if (actions != null) {
            for (int a = 0; a < actions.length; a++) {
                if (actions[a] == null) {
                    menu.add(new JSeparator());
                } else {
                    JMenuItem mi = actions[a].getMenuItem();
                    mi.setFont(getFont());
                    menu.add(mi);
                }
            }
        }
    }

    protected void fillButtonMenuEntry(final AbstractButton button, final SuperAction action, final String sIconUrlNormal, final String sIconUrlPressed, final String sToolTip) {
        if (action != null) {
            button.setAction(action);
        }
        if (sIconUrlNormal != null) {
            Icon icon = ResourceHelper.loadIcon(getClass(), sIconUrlNormal);
            if (icon != null) {
                button.setIcon(icon);
            } else {
                System.err.println(sIconUrlNormal + " not found!");
            }
        }
        if (sIconUrlPressed != null) {
            Icon icon = ResourceHelper.loadIcon(getClass(), sIconUrlPressed);
            if (icon != null) {
                button.setSelectedIcon(icon);
            } else {
                System.err.println(sIconUrlPressed + " not found!");
            }
        }
        if (sToolTip != null) {
            button.setToolTipText(sToolTip);
        }
        if (button instanceof JToggleButton) {
            if (m_butGroup[m_bgIdx] == null) {
                m_butGroup[m_bgIdx] = new ButtonGroup();
            }
            m_butGroup[m_bgIdx].add(button);
        }
    }

    protected void fillMenuEntry(int idx) {
        Component c = m_caMainMenu[idx];
        MenuEntry[] me = getMainEntries();
        if (c instanceof JMenu) {
            String sLabel = me[idx].sLabel;
            SuperAction[] actions = m_aaMainAction[idx];
            if (sLabel != null) {
                fillStdMenuEntry((JMenu) c, sLabel, actions);
            }
        } else if (c instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) c;
            SuperAction action = me[idx].buttonAction;
            String sIconUrlNormal = me[idx].sIconUrlNormal;
            String sIconUrlPressed = me[idx].sIconUrlPressed;
            String sToolTip = me[idx].sToolTip;
            fillButtonMenuEntry(button, action, sIconUrlNormal, sIconUrlPressed, sToolTip);
            if (idx == getSelectedIdx()) {
                button.setSelected(true);
            }
        }
    }

    public JMenuBar createMenuBar() {
        if (m_caMainMenu != null) {
            MenuEntry[] me = getMainEntries();
            for (int idx = 0; idx < m_caMainMenu.length; idx++) {
                if (me[idx].sLabel != null) {
                    m_caMainMenu[idx] = createMenuEntry(idx);
                    if (m_caMainMenu[idx] != null) {
                        fillMenuEntry(idx);
                    }
                } else {
                    if (m_bgIdx < m_butGroup.length - 1 && m_butGroup[m_bgIdx] != null) {
                        m_bgIdx++;
                    }
                }
            }
            m_menuBar = new JMenuBar();
            for (int i = 0; i < m_caMainMenu.length; i++) {
                if (m_caMainMenu[i] != null) {
                    m_menuBar.add(m_caMainMenu[i]);
                } else {
                    m_menuBar.add(Box.createHorizontalGlue());
                }
            }
            ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        }
        return m_menuBar;
    }

    protected String getFileNameNoExt(String sPathName) {
        String sFileName = sPathName;
        int n = sPathName.lastIndexOf(File.separator);
        if (n > 0) {
            sFileName = sPathName.substring(n + 1);
            n = sFileName.lastIndexOf('.');
            if (n > 0) {
                sFileName = sFileName.substring(0, n);
            }
        }
        return sFileName;
    }

    private void processNetworkLost() {
        JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
        if (m_actNetwork[NET_STATUS].getMenuItem().getText().equals(NetStatus.GO_OFF_LINE)) {
            netStat.setIcon(m_iNetOff);
            netStat.setToolTipText(NetStatus.NETWORK_OFF);
        }
        m_actNetwork[NET_STATUS].getMenuItem().setText(NetStatus.NETWORK_OFF);
        m_actNetwork[NET_STATUS].setEnabled(false);
        updateAutomatic();
    }

    private void processNetworkRecovered() {
        JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
        if (m_actNetwork[NET_STATUS].getMenuItem().getText().equals(NetStatus.NETWORK_OFF)) {
            netStat.setIcon(m_iNetOn);
            netStat.setToolTipText(getNetworkOnToolTip());
        }
        m_actNetwork[NET_STATUS].getMenuItem().setText(NetStatus.GO_OFF_LINE);
        m_actNetwork[NET_STATUS].setEnabled(true);
        updateAutomatic();
    }

    protected String getNetworkOnToolTip() {
        return (NetStatus.NETWORK_ON + " (" + ServiceFactory.getInstance().getAuthority() + ")");
    }

    private void processUserOffLine() {
        JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
        netStat.setIcon(m_iNetUsr);
        String sToolTip = (ServiceFactory.getInstance().isNetOn() ? getNetworkOnToolTip() : NetStatus.NETWORK_OFF);
        netStat.setToolTipText(sToolTip + " : " + NetStatus.USER_OFF);
        m_actNetwork[NET_STATUS].getMenuItem().setText(ServiceFactory.getInstance().isNetOn() ? NetStatus.GO_ON_LINE : NetStatus.NETWORK_OFF);
        updateAutomatic();
    }

    private void processUserOnLine() {
        JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
        netStat.setIcon(ServiceFactory.getInstance().isNetOn() ? m_iNetOn : m_iNetOff);
        netStat.setToolTipText(ServiceFactory.getInstance().isNetOn() ? getNetworkOnToolTip() : NetStatus.NETWORK_OFF);
        m_actNetwork[NET_STATUS].getMenuItem().setText(NetStatus.GO_OFF_LINE);
        updateAutomatic();
    }

    public void processProxyEvent(final ProxyEvent pe) {
        switch(pe) {
            case NET_LOST:
                processNetworkLost();
                break;
            case NET_RECOVERED:
                processNetworkRecovered();
                break;
            case USER_OFF_LINE:
                processUserOffLine();
                break;
            case USER_ON_LINE:
                processUserOnLine();
                break;
        }
    }

    private class SyncTimerListener implements ActionListener {

        public void actionPerformed(final ActionEvent evt) {
            JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
            if (m_bToggle) {
                if (m_bRetrying) {
                    netStat.setIcon(m_iNetOff);
                } else {
                    netStat.setIcon(m_iNetOn);
                }
            } else {
                netStat.setIcon(m_iNetUsr);
            }
            m_bToggle = !m_bToggle;
        }

        private boolean m_bToggle;
    }

    private void processSyncStarted() {
        if (m_syncTimer == null) {
            JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
            netStat.setToolTipText("Synchronizing ...");
            m_syncTimer = new Timer(1000, new SyncTimerListener());
            m_syncTimer.start();
        }
        m_nSyncTimerCount++;
    }

    private void processSyncRetrying() {
        m_bRetrying = true;
    }

    private void processSyncRecovered() {
        m_bRetrying = false;
    }

    private void processSyncFinished() {
        m_nSyncTimerCount--;
        if (m_nSyncTimerCount == 0) {
            if (m_syncTimer != null) {
                JMenu netStat = m_menuBar.getMenu(getNetEntryIdx());
                netStat.setIcon(m_iNetOn);
                netStat.setToolTipText(getNetworkOnToolTip());
                m_syncTimer.stop();
                m_syncTimer = null;
            }
        }
    }

    public void processSynchronizeEvent(final SynchronizeEvent se) {
        switch(se) {
            case STARTED:
                processSyncStarted();
                break;
            case RETRYING:
                processSyncRetrying();
                break;
            case RECOVERED:
                processSyncRecovered();
                break;
            case FINISHED:
                processSyncFinished();
                break;
        }
    }

    protected void updateAutomatic() {
        if (!isJobSubmitted()) {
            MenuEntry[] me = getMainEntries();
            int idx = getAutoEntryIdx();
            if (0 <= idx && idx < me.length) {
                SuperAction auto = me[idx].buttonAction;
                if (auto != null) {
                    JMenuItem mi = auto.getMenuItem();
                    ProxyServer proxy = ServiceFactory.getInstance().getProxy();
                    AbstractDocument doc = ServiceFactory.getInstance().getDocument();
                    if (proxy.isOnLine()) {
                        if (doc.canExecuteRemotely()) {
                            mi.setToolTipText(getOnlineAutoToolTipText());
                            auto.setEnabled(true);
                        } else {
                            if (doc.canExecuteLocally() == false) {
                                mi.setToolTipText("Offline mode: " + getOfflineAutoToolTipText());
                                auto.setEnabled(false);
                            }
                        }
                    } else {
                        if (doc.canExecuteLocally() == false) {
                            mi.setToolTipText("Offline mode: " + getOfflineAutoToolTipText());
                            auto.setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    private class JobTimerListener implements ActionListener {

        public void actionPerformed(final ActionEvent evt) {
            int idx = getAutoEntryIdx();
            if (idx >= 0) {
                Component c = m_menuBar.getComponent(idx);
                if (c instanceof JButton) {
                    JButton auto = (JButton) c;
                    if (m_bToggle) {
                        auto.setDisabledIcon(m_iJobDisabled);
                    } else {
                        auto.setDisabledIcon(m_iJobExecuting);
                    }
                }
                m_bToggle = !m_bToggle;
            }
        }

        private boolean m_bToggle;
    }

    protected void startJobTimer() {
        m_jobTimer = new Timer(1000, new JobTimerListener());
        m_jobTimer.start();
    }

    protected void stopJobTimer() {
        if (m_jobTimer != null) {
            m_jobTimer.stop();
            m_jobTimer = null;
        }
    }

    protected void processJobSubmitted() {
        MenuEntry[] me = getMainEntries();
        int idx = getAutoEntryIdx();
        if (0 <= idx && idx < me.length) {
            SuperAction auto = me[idx].buttonAction;
            if (auto != null) {
                auto.setEnabled(false);
            }
        }
        setJobSubmitted(true);
    }

    protected void processJobStarted() {
        MenuEntry[] me = getMainEntries();
        int idx = getAutoEntryIdx();
        if (0 <= idx && idx < me.length) {
            startJobTimer();
        }
    }

    protected void processJobFinished() {
        MenuEntry[] me = getMainEntries();
        int idx = getAutoEntryIdx();
        if (0 <= idx && idx < me.length) {
            stopJobTimer();
            SuperAction auto = me[idx].buttonAction;
            if (auto != null) {
                auto.setEnabled(true);
            }
            Component c = m_menuBar.getComponent(idx);
            if (c instanceof JButton) {
                ((JButton) c).setDisabledIcon(m_iJobDisabled);
            }
        }
        setJobSubmitted(false);
    }

    protected boolean isJobSubmitted() {
        return m_bJobSubmitted;
    }

    protected void setJobSubmitted(boolean bInJob) {
        m_bJobSubmitted = bInJob;
    }

    protected static final SuperAction[] m_actNetwork = { new NetStatus() };

    public static final int NET_STATUS = 0;

    public static final String NET_ON_ICON = "net-on.gif";

    public static final String NET_OFF_ICON = "net-off.gif";

    public static final String NET_USR_ICON = "net-usr.gif";

    public static final String JOB_ENABLED_ICON = "auto.gif";

    public static final String JOB_DISABLED_ICON = "auto-d.gif";

    public static final String JOB_EXECUTING_ICON = "auto-e.gif";

    protected JMenuBar m_menuBar;

    protected Component[] m_caMainMenu;

    protected SuperAction[][] m_aaMainAction;

    protected ButtonGroup[] m_butGroup;

    protected int m_bgIdx = 0;

    protected Icon m_iNetOn;

    protected Icon m_iNetOff;

    protected Icon m_iNetUsr;

    protected Icon m_iJobEnabled;

    protected Icon m_iJobDisabled;

    protected Icon m_iJobExecuting;

    protected Timer m_jobTimer;

    protected Timer m_syncTimer;

    protected int m_nSyncTimerCount;

    protected boolean m_bRetrying;

    protected boolean m_bJobSubmitted;
}

package org.jdamico.ircivelaclient.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdamico.ircivelaclient.config.Constants;
import org.jdamico.ircivelaclient.listener.ListenConversation;
import org.jdamico.ircivelaclient.observer.IRCResponseHandler;
import org.jdamico.ircivelaclient.util.ChatPrinter;
import org.jdamico.ircivelaclient.util.IRCIvelaClientStringUtils;

public class HandleApplet extends JApplet {

    private static final long serialVersionUID = -97950894125721726L;

    private JTabbedPane mainTabbedPane;

    private ListenConversation chatter = null;

    private ChatPanel chatPanel;

    private FrDrw2FS drawPanel;

    private HashMap<String, PvtChatPanel> pvtTabs;

    private int actualTabIndex;

    public void init() {
        ChatPrinter.print("init(): begin");
        StaticData.server = getParameter(Constants.PARAM_SERVER);
        StaticData.teacher = getParameter(Constants.PARAM_TEACHER);
        StaticData.channel = getParameter(Constants.PARAM_CHANNEL);
        StaticData.nick = getParameter(Constants.PARAM_NICK);
        StaticData.remoteFile = getParameter(Constants.PARAM_REMOTE_FILE);
        StaticData.remoteServlet = getParameter(Constants.PARAM_REMOTE_SERVLET);
        if (StaticData.teacher.trim().equals(StaticData.nick.trim())) {
            StaticData.isTeacher = true;
        }
        String tempFolder = getParameter(Constants.CLASS_FOLDER);
        if (tempFolder != null && !tempFolder.equals("")) {
            StaticData.classFolder = tempFolder;
        }
        setLayout(new BorderLayout());
        String hexColor = getParameter(Constants.PARAM_BGCOLOR);
        Color bColor = IRCIvelaClientStringUtils.singleton().getColorParameter(hexColor);
        this.setBackground(bColor);
        this.setSize(920, 500);
        this.chatter = new ListenConversation(this);
        Thread tC = new Thread(chatter);
        tC.start();
        this.mainTabbedPane = new JTabbedPane();
        this.mainTabbedPane.setBorder(BorderFactory.createLineBorder(Color.black));
        this.chatPanel = new ChatPanel(this);
        this.drawPanel = new FrDrw2FS(this.chatPanel);
        this.mainTabbedPane.addTab("Chat", this.chatPanel);
        this.mainTabbedPane.addTab("BlackBoard", this.drawPanel);
        this.add(this.mainTabbedPane, BorderLayout.CENTER);
        IRCResponseHandler irResponseHandler = new IRCResponseHandler();
        irResponseHandler.setChatPanel(chatPanel);
        irResponseHandler.setDrawPanel(drawPanel);
        irResponseHandler.setHandleApplet(this);
        this.chatter.addObserver(irResponseHandler);
        mainTabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int index = ((JTabbedPane) e.getSource()).getSelectedIndex();
                mainTabbedPane.setForegroundAt(index, Color.black);
                actualTabIndex = index;
            }
        });
        this.pvtTabs = new HashMap<String, PvtChatPanel>();
        ChatPrinter.print("init(): end");
    }

    public void destroy() {
        chatPanel.getSession().close("Applet destroyed by user");
        ChatPrinter.print("destroy()");
    }

    public void stop() {
        ChatPrinter.print("stop(): begin");
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    public void setLoadingFlag(boolean loadingFlag) {
        if (!loadingFlag) {
            this.chatPanel.stopLoadingPanel();
            this.repaint();
        }
    }

    public PvtChatPanel addTab(String title) {
        if (this.pvtTabs.containsKey(title)) return this.pvtTabs.get(title);
        PvtChatPanel pvtChatPanel = new PvtChatPanel(this, title, this.chatPanel.getSession());
        this.mainTabbedPane.add(pvtChatPanel, title);
        this.pvtTabs.put(title, pvtChatPanel);
        return pvtChatPanel;
    }

    public void removeTab(String title) {
        PvtChatPanel pvtTemp = this.pvtTabs.get(title);
        this.pvtTabs.remove(title);
        this.mainTabbedPane.remove(pvtTemp);
        this.mainTabbedPane.updateUI();
    }

    public void updatePvt(String whom, String what) {
        PvtChatPanel pvtChatPanel = this.pvtTabs.get(whom);
        if (pvtChatPanel == null) {
            pvtChatPanel = this.addTab(whom);
        }
        if (whom.equalsIgnoreCase(StaticData.teacher)) pvtChatPanel.updateMainContentArea(what, "red", true); else pvtChatPanel.updateMainContentArea(what, "red", false);
    }

    public void setTabForegroundColor(String title, int colorID) {
        for (int i = 0; i < this.mainTabbedPane.getTabCount(); i++) {
            if (this.mainTabbedPane.getTitleAt(i).equals(title)) {
                if (i == actualTabIndex && colorID == 2) return;
                if (colorID == 1) this.mainTabbedPane.setForegroundAt(i, Color.black); else if (colorID == 2) this.mainTabbedPane.setForegroundAt(i, Color.red);
                break;
            }
        }
    }
}

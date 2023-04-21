package com.elibera.ccs.app;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.elibera.ccs.dialog.DialogExpertPanel;
import com.elibera.ccs.dialog.HelperDialog;
import com.elibera.ccs.panel.ActionButtonPanel;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.util.HelperStd;
import com.elibera.preloader.Preloader;

/**
 * @author meisi
 *
 * Ist das Root Panel, dass von den Anwendungen initzialisiert wird
 * lädt alles weitere
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditorPanel extends JPanel implements KeyListener, MouseListener, DocumentListener {

    public com.elibera.ccs.app.MLEConfig conf;

    public JScrollPane textPanescrollPane;

    public JPanel panelEd;

    public ActionButtonPanel actionButtonPanel;

    private boolean docHasChanged = false;

    private JLabel statusLabel;

    private int[] keys = { KeyEvent.KEY_LOCATION_LEFT, KeyEvent.VK_LEFT, KeyEvent.VK_KP_LEFT, KeyEvent.KEY_LOCATION_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_KP_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_KP_UP, KeyEvent.VK_DOWN, KeyEvent.VK_KP_DOWN };

    public String curEditorSize = null;

    boolean isApplicationMode = false;

    public EditorPanel(ApplicationParams params, JFrame application) {
        new Preloader().start();
        this.conf = new com.elibera.ccs.app.MLEConfig(this, params, application);
        isApplicationMode = params.getParameter("applicationmode", "applet").compareTo("app") == 0;
        setJComponentSize(this, isApplicationMode ? 650 : 550, 450);
        HelperPanel.formatPanel(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        actionButtonPanel = new ActionButtonPanel(conf, isApplicationMode);
        add(actionButtonPanel);
        setJComponentSize(actionButtonPanel, this.getWidth(), 65);
        panelEd = new JPanel();
        HelperPanel.formatPanel(panelEd);
        javax.swing.JTextPane textPane = conf.init();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(1, 1, 1, 1));
        textPanescrollPane = new JScrollPane(textPane);
        textPanescrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setEditorSize(176, 208);
        panelEd.add(textPanescrollPane);
        add(panelEd);
        panelEd.setPreferredSize(new Dimension(300, 330));
        statusLabel = new JLabel((isApplicationMode ? "<html>  <br>" : "") + Msg.getMsg("STATUS_MSG_NO_SAVE_NECESSARY") + " Ver: " + Msg.getConfSetting("VERSION"));
        JPanel pStatus = new JPanel();
        pStatus.add(statusLabel);
        add(pStatus);
        updateUI();
        conf.loadLOInit(false);
    }

    public void preload(SplashWindow splash) {
        DialogExpertPanel.showDialog(conf, true);
        splash.increment();
        HelperDialog.doPreloadDialogs();
    }

    public void setEditorSize(int w, int h) {
        setJComponentSize(conf.textPane, w, h);
        setJComponentSize(textPanescrollPane, w, h);
        curEditorSize = w + "x" + h;
        this.revalidate();
    }

    public int getEditorWidth() {
        return HelperStd.splitToInt(curEditorSize, 'x')[0];
    }

    public int getEditorHeight() {
        return HelperStd.splitToInt(curEditorSize, 'x')[1];
    }

    public void setJComponentSize(JComponent c, int w, int h) {
        Dimension d = new Dimension(w, h);
        c.setSize(d);
        c.setMaximumSize(d);
        c.setMinimumSize(d);
        c.setPreferredSize(d);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < keys.length; i++) if (e.getKeyCode() == keys[i]) {
            this.actionButtonPanel.checkStyleStateOfElements();
            break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        this.actionButtonPanel.checkStyleStateOfElements();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        if (!docHasChanged) {
            this.actionButtonPanel.setSaveState(true);
            docHasChanged = true;
            setStausMsg();
        }
        docHasChanged = true;
    }

    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    /**
	 * setzt den Changed Status --> also wie der Speicher Button ausschauen soll
	 * @param changedStatus
	 */
    public void setDocChangedStatus(boolean changedStatus) {
        this.docHasChanged = changedStatus;
        this.actionButtonPanel.setSaveState(docHasChanged);
        setStausMsg();
    }

    /**
	 * gibt true zurück wenn der Inhalt geändert wurde
	 * @return
	 */
    public boolean isDocChanged() {
        return docHasChanged;
    }

    private void setStausMsg() {
        String info = "";
        if (isApplicationMode) {
            info = conf.serverUrlSaveLernobjekt;
            if (HelperStd.isEmpty(info)) info = conf.serverUrlOpenLernobjekt;
            if (info == null) info = "";
            if (info.length() == 0) info = Msg.getMsg("STATUS_MSG_NO_FILENAME");
            info = "<html>File: " + info + "<br>";
        }
        if (docHasChanged) statusLabel.setText(info + Msg.getMsg("STATUS_MSG_SAVE_NECESSARY")); else statusLabel.setText(info + Msg.getMsg("STATUS_MSG_NO_SAVE_NECESSARY") + " Ver: " + Msg.getConfSetting("VERSION"));
    }
}

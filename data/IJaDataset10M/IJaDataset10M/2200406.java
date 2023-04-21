package net.jfellow.tail.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * @author @author@
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JFrameHelp extends JFrame {

    private static String FRAME_X_HELP = "dialog_help_tail_x";

    private static String FRAME_Y_HELP = "dialog_help_tail_y";

    private static String FRAME_HEIGHT_HELP = "dialog_help_tail_h";

    private static String FRAME_WITH_HELP = "dialog_help_tail_w";

    private static String FRAME_X_ABOUT = "dialog_about_tail_x";

    private static String FRAME_Y_ABOUT = "dialog_about_tail_y";

    private static String FRAME_HEIGHT_ABOUT = "dialog_about_tail_h";

    private static String FRAME_WITH_ABOUT = "dialog_about_tail_w";

    private static JFrameHelp instance;

    private JButton jButtonOK;

    private JScrollPane jScrollPaneMain;

    private JEditorPane jEditorPane;

    private String url;

    private JFrameHelp(String url) {
        this.url = url;
        this.initGui();
        this.restoreUserSettings();
        this.jScrollPaneMain.scrollRectToVisible(new Rectangle(1, 1, 10, 10));
    }

    public static synchronized JFrameHelp getInstance(String url) {
        if (instance == null) {
            instance = new JFrameHelp(url);
        }
        return instance;
    }

    private void initGui() {
        this.setTitle("Manual");
        this.setSize(new Dimension(500, 300));
        this.addWindowListener(new TailWindowClosingAdapter());
        this.getRootPane().setDefaultButton(this.jButtonOK);
        this.jButtonOK = new JButton("OK");
        jButtonOK.setMnemonic('O');
        this.jEditorPane = new JEditorPane();
        this.jEditorPane.setContentType("text/html");
        this.loadText();
        this.jEditorPane.setEditable(false);
        this.jScrollPaneMain = new JScrollPane(this.jEditorPane);
        GridBagLayout gridbagMainPanel = new GridBagLayout();
        this.getContentPane().setLayout(gridbagMainPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        gridbagMainPanel.setConstraints(this.jScrollPaneMain, c);
        this.getContentPane().add(jScrollPaneMain);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(11, 12, 12, 12);
        gridbagMainPanel.setConstraints(this.jButtonOK, c);
        this.getContentPane().add(jButtonOK);
        this.jButtonOK.addActionListener(new ActionListenerOK());
    }

    private void loadText() {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(url)));
            String s = null;
            while ((s = in.readLine()) != null) {
                buf.append(s).append("\n");
            }
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception eio) {
                }
            }
        }
        this.jEditorPane.setText(buf.toString());
        this.jEditorPane.setCaretPosition(0);
    }

    private void saveFramePosition() {
        int x = this.getX();
        int y = this.getY();
        Dimension d = this.getSize();
        int h = (int) d.getHeight();
        int w = (int) d.getWidth();
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
        if (this.url.toLowerCase().indexOf("help.html") != -1) {
            prefs.putInt(FRAME_X_HELP, x);
            prefs.putInt(FRAME_Y_HELP, y);
            prefs.putInt(FRAME_HEIGHT_HELP, h);
            prefs.putInt(FRAME_WITH_HELP, w);
        } else {
            prefs.putInt(FRAME_X_ABOUT, x);
            prefs.putInt(FRAME_Y_ABOUT, y);
            prefs.putInt(FRAME_HEIGHT_ABOUT, h);
            prefs.putInt(FRAME_WITH_ABOUT, w);
        }
    }

    private void restoreUserSettings() {
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
        int x = 0;
        int y = 0;
        int h = 0;
        int w = 0;
        if (this.url.toLowerCase().indexOf("help.html") != -1) {
            x = prefs.getInt(FRAME_X_HELP, 10);
            y = prefs.getInt(FRAME_Y_HELP, 10);
            h = prefs.getInt(FRAME_HEIGHT_HELP, 500);
            w = prefs.getInt(FRAME_WITH_HELP, 600);
        } else {
            x = prefs.getInt(FRAME_X_ABOUT, 10);
            y = prefs.getInt(FRAME_Y_ABOUT, 10);
            h = prefs.getInt(FRAME_HEIGHT_ABOUT, 500);
            w = prefs.getInt(FRAME_WITH_ABOUT, 400);
        }
        this.setSize(new Dimension(w, h));
        if ((x == 10) && (y == 10)) {
            this.setLocationRelativeTo(null);
        } else {
            this.setLocation(x, y);
        }
    }

    private void closeMySelf() {
        this.saveFramePosition();
        this.setVisible(false);
        this.dispose();
    }

    class TailWindowClosingAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            closeMySelf();
        }
    }

    class ActionListenerOK implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            closeMySelf();
        }
    }
}

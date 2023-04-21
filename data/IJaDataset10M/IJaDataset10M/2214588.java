package net.jfellow.regexassistant.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;
import javax.swing.*;

/**
 * @author @author@
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDialogPattern extends JFrame {

    private static String FRAME_X = "dialog_regexp_help_pattern_x";

    private static String FRAME_Y = "dialog_regexp_help_pattern_y";

    private static String FRAME_HEIGHT = "dialog_regexp_help_pattern_h";

    private static String FRAME_WITH = "dialog_regexp_help_pattern_w";

    private static JDialogPattern instance;

    private JButton jButtonOK;

    private JScrollPane jScrollPaneMain;

    private JEditorPane jEditorPane;

    private String url;

    private JDialogPattern(JFrame parent, String url) {
        this.url = url;
        this.initGui();
        this.loadUserPrefs();
        this.jScrollPaneMain.scrollRectToVisible(new Rectangle(1, 1, 10, 10));
    }

    private void initGui() {
        this.setTitle("Parts of the ApiDoc of the Pattern Class");
        this.setIconImage(JFrameRegExpAssistant.getJFellowIcon());
        this.setSize(new Dimension(500, 300));
        this.addWindowListener(new TailWindowClosingAdapter());
        this.getRootPane().setDefaultButton(this.jButtonOK);
        this.jButtonOK = new JButton("OK");
        jButtonOK.setMnemonic('O');
        this.jEditorPane = new JEditorPane();
        this.jEditorPane.setContentType("text/html");
        this.loadText();
        this.jEditorPane.setCaretPosition(0);
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
    }

    private void storeUserPrefs() {
        int x = this.getX();
        int y = this.getY();
        Dimension d = this.getSize();
        int h = (int) d.getHeight();
        int w = (int) d.getWidth();
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.putInt(FRAME_X, x);
        prefs.putInt(FRAME_Y, y);
        prefs.putInt(FRAME_HEIGHT, h);
        prefs.putInt(FRAME_WITH, w);
    }

    private void loadUserPrefs() {
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        int x = prefs.getInt(FRAME_X, 155);
        int y = prefs.getInt(FRAME_Y, 36);
        int h = prefs.getInt(FRAME_HEIGHT, 901);
        int w = prefs.getInt(FRAME_WITH, 786);
        this.setSize(new Dimension(w, h));
        if ((x == 10) && (y == 10)) {
            this.setLocationRelativeTo(null);
        } else {
            this.setLocation(x, y);
        }
    }

    private void closeMySelf() {
        this.storeUserPrefs();
        this.setVisible(false);
        this.dispose();
    }

    public static JDialogPattern getInstance(JFrame parent, String url) {
        if (JDialogPattern.instance != null) {
            return JDialogPattern.instance;
        } else {
            JDialogPattern.instance = new JDialogPattern(parent, url);
            return JDialogPattern.instance;
        }
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

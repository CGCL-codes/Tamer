package com.googlecode.progobots.ui.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.miginfocom.swing.MigLayout;
import com.googlecode.progobots.util.ResourceBundle;

/**
 * A help frame for progobots.
 */
public class HelpFrame extends JFrame {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("progobots/ProgobotsUI");

    private JEditorPane editorPane;

    private Action closeAction;

    public HelpFrame() {
        setTitle(bundle.getString("Progobots_Manual"));
        setLayout(new MigLayout("fill"));
        try {
            editorPane = new JEditorPane(getClass().getResource("/progobots/manual.html"));
        } catch (IOException e) {
            editorPane = new JEditorPane("text/html", bundle.getString("Cannot_read_the_manual"));
        }
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                HelpFrame.this.hyperlinkUpdate(e);
            }
        });
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(800, 768));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, "grow, wrap");
        closeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        };
        bundle.inject(closeAction, "helpClose");
        JButton close = new JButton(closeAction);
        add(close, "right");
        pack();
        getRootPane().setDefaultButton(close);
        close.requestFocusInWindow();
    }

    public void close() {
        setVisible(false);
    }

    private void hyperlinkUpdate(HyperlinkEvent e) {
        try {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                editorPane.setPage(e.getURL());
            }
        } catch (IOException e1) {
        }
    }

    public void fireAction(String action) {
        if ("close".equals(action)) {
            closeAction.actionPerformed(null);
        }
    }
}

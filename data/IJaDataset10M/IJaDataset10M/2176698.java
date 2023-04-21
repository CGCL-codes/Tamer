package org.jmeld.ui;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import org.jmeld.ui.action.Actions;

/**
 *
 * @author kees
 */
public class JMeldComponent extends Container {

    private JMeldPanel meldPanel;

    public JMeldComponent() {
        meldPanel = new JMeldPanel();
        meldPanel.SHOW_TABBEDPANE_OPTION.disable();
        meldPanel.SHOW_TOOLBAR_OPTION.disable();
        meldPanel.SHOW_STATUSBAR_OPTION.disable();
        meldPanel.SHOW_FILE_TOOLBAR_OPTION.disable();
        meldPanel.SHOW_FILE_STATUSBAR_OPTION.disable();
        setLayout(new BorderLayout());
        add(meldPanel, BorderLayout.CENTER);
    }

    public void openComparison(File fileLeft, File fileRight) {
        meldPanel.openFileComparison(fileRight, fileRight, false);
    }

    public Actions getActions() {
        return meldPanel.actions;
    }

    public Action getAction(Actions.Action action) {
        return meldPanel.getAction(action);
    }

    public static void main(String args[]) {
        JFrame frame;
        JMeldComponent jmc;
        JPanel panel;
        JButton button;
        Actions actions;
        Actions.Action action;
        jmc = new JMeldComponent();
        jmc.openComparison(new File(args[0]), new File(args[1]));
        panel = new JPanel(new BorderLayout());
        panel.add(jmc, BorderLayout.CENTER);
        actions = jmc.getActions();
        actions.SAVE.option.disable();
        button = new JButton();
        frame = new JFrame("Standalone JMeld");
        frame.add(jmc);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}

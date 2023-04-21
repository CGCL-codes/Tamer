package de.knup.jedi.jayshare.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import de.knup.jedi.jayshare.ui.*;

public class MainPanel extends JPanel {

    public static boolean dbg = false;

    private static void p(String s) {
        if (dbg) pa(s);
    }

    private static void pa(String s) {
        System.out.println(s);
    }

    private GridBagLayout gbl;

    private GridBagConstraints gbc;

    private Preferences prefs;

    private ConnectionPreferencesPanel pan_connection;

    private SearchPanel pan_search;

    private ExportListPanel pan_export;

    private Component pan_queue;

    private JTabbedPane tabbedPane = new JTabbedPane();

    public MainPanel(Preferences prefs) {
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        this.prefs = prefs;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = gbc.weighty = 1;
        set_panels();
        setLayout(new GridBagLayout());
        add_panels("Connection", "Search", "Download-Queue", "Upload-List");
        p("MainPanel: " + getPreferredSize());
    }

    protected Component makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    private void set_panels() {
        pan_connection = new ConnectionPreferencesPanel(prefs);
        pan_search = new SearchPanel();
        pan_queue = makeTextPanel("Files i am currently downloading");
        pan_export = new ExportListPanel(prefs);
    }

    private void add_panels(String pan1, String pan2, String pan3, String pan4) {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(pan1, pan_connection);
        tabbedPane.addTab(pan2, pan_search);
        tabbedPane.addTab(pan3, pan_queue);
        tabbedPane.addTab(pan4, pan_export);
        tabbedPane.setSelectedIndex(0);
        add(tabbedPane, gbc);
    }

    public int getWidth() {
        return tabbedPane.getWidth();
    }

    public ConnectionPreferencesPanel getConnectionPreferencesPanel() {
        return pan_connection;
    }
}

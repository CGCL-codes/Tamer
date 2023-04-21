package org.moltools.apps.probemaker.swingui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.moltools.apps.probemaker.SwingUIMessages;
import org.moltools.apps.probemaker.seq.Probe;
import org.moltools.lib.seq.db.IndexedSequenceDB;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * 
 * @author Johan Stenberg
 * @version 1.0
 */
public class ProbeTagsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected JScrollPane scrollPane;

    protected JTable resultsTable;

    protected JLabel resultsLabel;

    public ProbeTagsPanel(IndexedSequenceDB<? extends Probe> db) {
        resultsLabel = new JLabel();
        resultsLabel.setText(db.size() + SwingUIMessages.getString("SearchResultPanel.MESSAGE_SEQUENCES_FOUND"));
        resultsTable = new JTable(new ProbeTagsTableModel(db));
        scrollPane = new JScrollPane(resultsTable);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(resultsLabel, BorderLayout.SOUTH);
    }
}

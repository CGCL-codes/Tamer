package de.glossmaker.gui.gloss.importGloss;

import java.awt.BorderLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import de.glossmaker.gloss.datastructure.EOperation;
import de.glossmaker.gloss.datastructure.GlossItems;
import de.glossmaker.gloss.language.Translation;
import de.glossmaker.gloss.observer.GlossItemsChangePublisher;
import de.glossmaker.gui.gloss.help.HelpSystem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author Markus Flingelli
 *
 */
public class ImportGlossTablePanel extends JPanel {

    private static final long serialVersionUID = -7716356035213972865L;

    private JTable mTable = null;

    private JScrollPane mScrollPane = null;

    private GlossItems mGlossItems = null;

    private GlossItems mImportItems = null;

    private JButton mOk;

    private JButton mCancel;

    private JButton mHelp;

    private Translation mTranslation = null;

    public ImportGlossTablePanel(GlossItems items, GlossItems importItems) {
        mGlossItems = items;
        mImportItems = importItems;
        mTranslation = Translation.getInstance();
        initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        this.add(getScrollPane(), BorderLayout.CENTER);
        JPanel ButtonPanel = new JPanel();
        add(ButtonPanel, BorderLayout.SOUTH);
        ButtonPanel.add(getMOk());
        ButtonPanel.add(getMCancel());
        ButtonPanel.add(getMHelp());
    }

    private JScrollPane getScrollPane() {
        if (mScrollPane == null) {
            mScrollPane = new JScrollPane();
            mScrollPane.setViewportView(getTable());
        }
        return mScrollPane;
    }

    private JComboBox getOperationComboBox() {
        JComboBox result = new JComboBox();
        for (EOperation operation : EOperation.values()) {
            result.addItem(operation);
        }
        return result;
    }

    private JTable getTable() {
        if (mTable == null) {
            mTable = new JTable(new ImportGlossTableModel(mGlossItems, mImportItems));
            mTable.setRowHeight(20);
            mTable.setAutoCreateRowSorter(true);
            mTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(getOperationComboBox()));
            for (int i = 0; i < mTable.getColumnCount(); i++) {
                mTable.getColumnModel().getColumn(i).setCellRenderer(new ImportGlossCellRenderer(false));
            }
        }
        return mTable;
    }

    private JButton getMOk() {
        if (mOk == null) {
            mOk = new JButton(mTranslation.getValue("importgloss.button.ok.text"));
            mOk.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    GlossItemsChangePublisher.getInstance(mGlossItems).importGlossItems(mImportItems);
                }
            });
            mOk.setToolTipText(mTranslation.getValue("importgloss.button.ok.tooltip"));
        }
        return mOk;
    }

    private JButton getMCancel() {
        if (mCancel == null) {
            mCancel = new JButton(mTranslation.getValue("importgloss.button.cancel.text"));
            mCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    GlossItemsChangePublisher.getInstance(mGlossItems).closeImportFileDialog();
                }
            });
            mCancel.setToolTipText(mTranslation.getValue("importgloss.button.cancel.tooltip"));
        }
        return mCancel;
    }

    private JButton getMHelp() {
        if (mHelp == null) {
            mHelp = new JButton(mTranslation.getValue("importgloss.button.help.text"));
            HelpSystem.getInstance().enableHelpOnButton(mHelp, "section.import.glossary_file", null);
            mHelp.setToolTipText(mTranslation.getValue("importgloss.button.help.tooltip"));
        }
        return mHelp;
    }
}

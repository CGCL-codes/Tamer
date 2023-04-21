package org.hironico.gui.table.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.hironico.gui.table.editor.FileCellEditor;

/**
 * Cette classe affiche le renderer pour un fichier dans une cellule de table.
 * @author hironico
 * @since 2.1.0
 * @see org.hironico.gui.table.editor.FileCellEditor
 */
public class FileCellRenderer extends JPanel implements TableCellRenderer {

    private JLabel lblFileName = null;

    private JButton btnBrowse = null;

    /**
     * This method initializes the FileCellRenderer
     * 
     */
    public FileCellRenderer() {
        super();
        initialize();
    }

    /**
     * This method initializes the GUI rendering
     * 
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        lblFileName = new JLabel();
        lblFileName.setText("file name ???");
        lblFileName.setOpaque(false);
        this.setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(100, 18));
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.add(lblFileName, gridBagConstraints);
        this.add(getBtnBrowse(), gridBagConstraints1);
    }

    /**
     * This method initializes btnBrowse	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnBrowse() {
        if (btnBrowse == null) {
            btnBrowse = new JButton();
            btnBrowse.setText("...");
            btnBrowse.setPreferredSize(new java.awt.Dimension(43, 10));
            btnBrowse.setToolTipText("Browse...");
        }
        return btnBrowse;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) lblFileName.setText("NULL"); else {
            lblFileName.setText(((File) value).getName());
            lblFileName.setToolTipText(((File) value).getPath());
        }
        btnBrowse.setSize(btnBrowse.getWidth(), table.getRowHeight());
        if (isSelected) setBackground(table.getSelectionBackground()); else setBackground(table.getBackground());
        if (hasFocus) this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption, 1)); else this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return this;
    }
}

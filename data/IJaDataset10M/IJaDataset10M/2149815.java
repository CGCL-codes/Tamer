package org.hironico.gui.log;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.hironico.gui.dialog.OpenDialogAction;
import org.hironico.gui.font.FontChooserPanel;

/**
 * Adaptation du color editor depuis le java tutorial. Ca marche "straight out of the box"
 * Depuis la v2.0.0 refactor du nom en "FontCellEditor" pour etre cohérent avec le renderer.
 */
class FontCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    protected static Logger logger = Logger.getLogger("org.hironico.gui.log");

    protected static final String EDIT = "edit";

    private Font currentFont;

    private JButton btnBrowseFonts = new JButton();

    private JLabel lblExample = new JLabel();

    private JPanel pnlEditor = new JPanel();

    private FontChooserPanel pnlFontChooser = new FontChooserPanel();

    public FontCellEditor() {
        btnBrowseFonts.setActionCommand(EDIT);
        btnBrowseFonts.addActionListener(this);
        lblExample.setOpaque(false);
        initPanel();
    }

    protected void initPanel() {
        pnlEditor.setLayout(new java.awt.GridBagLayout());
        pnlEditor.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        pnlEditor.setBounds(new java.awt.Rectangle(0, 0, 102, 34));
        pnlEditor.setPreferredSize(new java.awt.Dimension(70, 18));
        pnlEditor.setMinimumSize(new java.awt.Dimension(93, 18));
        pnlEditor.setSize(new java.awt.Dimension(120, 18));
        pnlEditor.add(btnBrowseFonts, new java.awt.GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, java.awt.GridBagConstraints.CENTER, java.awt.GridBagConstraints.BOTH, new java.awt.Insets(0, 5, 0, 0), 0, 0));
        pnlEditor.add(lblExample, new java.awt.GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, java.awt.GridBagConstraints.CENTER, java.awt.GridBagConstraints.HORIZONTAL, new java.awt.Insets(0, 5, 0, 0), 0, 0));
        lblExample.setText("Example");
        lblExample.setPreferredSize(new java.awt.Dimension(75, 16));
        lblExample.setSize(new java.awt.Dimension(75, 16));
        btnBrowseFonts.setText("...");
        btnBrowseFonts.setPreferredSize(new java.awt.Dimension(20, 18));
        btnBrowseFonts.setToolTipText("Click to choose a font");
        btnBrowseFonts.setMinimumSize(new java.awt.Dimension(20, 18));
        btnBrowseFonts.setMaximumSize(new java.awt.Dimension(20, 18));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            pnlFontChooser.setCurrentFont(currentFont);
            OpenDialogAction openAction = new OpenDialogAction(pnlEditor, pnlFontChooser) {

                @Override
                protected void setupDialog() {
                    super.setupDialog();
                    setDialogTitle("Font chooser...");
                    dialog.pack();
                }
            };
            openAction.getDialog().addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent we) {
                    fontChooserClosed(pnlFontChooser.getCurrentFont());
                }
            });
            openAction.actionPerformed(e);
        }
    }

    /**
     * Cette méthode est appelée par le windows listener de la dialog
     * de selection de Font.
     * @param fontChoosed l'objet Font sélectionné par l'utilisateur.
     */
    protected void fontChooserClosed(Font fontChoosed) {
        logger.debug("Font editor closed with current font: " + fontChoosed.getName());
        currentFont = fontChoosed;
        if (currentFont == null) {
            fireEditingCanceled();
        } else {
            fireEditingStopped();
        }
    }

    @Override
    public Object getCellEditorValue() {
        return currentFont;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentFont = (Font) value;
        if (currentFont != null) {
            lblExample.setFont(currentFont);
        }
        return pnlEditor;
    }
}

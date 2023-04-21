package org.plazmaforge.framework.client.swing.forms;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.border.Border;
import org.plazmaforge.framework.client.swing.dialogs.OptionDialog;
import org.plazmaforge.framework.client.swing.gui.table.TableHelper;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * @author Oleh Hapon
 * Date: 26.07.2004
 * Time: 9:27:56
 * $Id: TableFindDialog.java,v 1.2 2010/04/28 06:36:10 ohapon Exp $
 */
public class TableFindDialog extends OptionDialog {

    private JTable table;

    private java.util.List fields;

    private int findRow;

    public TableFindDialog(Frame frame, JTable table) {
        super(frame);
        this.table = table;
        setModal(true);
        registerKeyEnter();
    }

    public TableFindDialog(Frame frame, JTable table, boolean modal) {
        super(frame, modal);
        this.table = table;
        registerKeyEnter();
    }

    protected JPanel createDialogPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        Border border = createDialogPanelBorder();
        if (border != null) {
            p.setBorder(border);
        }
        TableModel model = table.getModel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        JLabel label = null;
        JTextField field = null;
        fields = new ArrayList();
        for (int i = 0; i < model.getColumnCount(); i++) {
            gbc.gridy = i;
            label = new JLabel(model.getColumnName(i));
            gbc.gridx = 0;
            p.add(label, gbc);
            field = new JTextField(20);
            gbc.gridx = 1;
            p.add(field, gbc);
            fields.add(field);
        }
        return p;
    }

    protected void doOkAction() {
        if (table == null || fields == null) return;
        TableHelper helper = new TableHelper();
        findRow = helper.findRowByTextFields(table.getModel(), fields);
        close();
    }

    public int getFindRow() {
        return findRow;
    }

    public static void main(String[] args) {
        Double d = new Double(10);
        System.out.println(d.getClass().isAssignableFrom(Number.class));
        System.out.println(d.getClass().isAssignableFrom(Double.class));
        Class[] kl = d.getClass().getClasses();
        for (int i = 0; i < kl.length; i++) {
            System.out.println(kl[i].getName());
        }
    }

    protected void registerKeyEnter() {
        getRootPane().registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireOkAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JTable getTable() {
        return table;
    }

    public java.util.List getFields() {
        return fields;
    }
}

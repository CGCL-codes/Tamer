package br.com.mackenzie.fuzzy.bellmanzadeh.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.EditVariablePanel;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.table.MackTable;

public class ShowEditVariableDialogAction extends BaseAction {

    private JDialog dialog_;

    private EditVariablePanel panel_;

    public ShowEditVariableDialogAction(Component owner, JDialog dialog) {
        super(owner);
        putValue(AbstractAction.NAME, "Editar");
        this.dialog_ = dialog;
        this.panel_ = (EditVariablePanel) owner;
    }

    public void actionPerformed(ActionEvent e) {
        MackTable table = panel_.getTable();
        if (table.getSelectedRow() >= 0) {
            ArrayList row = table.getRow(table.getSelectedRow());
            panel_.getVariabletextField().setText(row.get(0).toString());
            String selectedItem = row.get(1).toString();
            int count = panel_.getTypeComboBox().getItemCount();
            for (int i = 0; i < count; ++i) {
                if (panel_.getTypeComboBox().getItemAt(i).toString().equalsIgnoreCase(selectedItem)) {
                    panel_.getTypeComboBox().setSelectedIndex(i);
                    break;
                }
            }
            this.dialog_.pack();
            this.dialog_.setSize(350, 120);
            this.dialog_.setLocationRelativeTo(this.getOwner());
            this.dialog_.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this.getOwner(), "N�o h� linhas selecionadas para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

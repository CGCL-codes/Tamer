package br.com.mackenzie.fuzzy.bellmanzadeh.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.AddAlternativeDialogPanel;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.table.AlternativesTable;

public class ShowEditAlternativeAction extends BaseAction {

    private JDialog dialog_;

    private AddAlternativeDialogPanel panel_;

    private AlternativesTable table_;

    private EditAlternativeAction editAction_;

    public ShowEditAlternativeAction(Component owner, JDialog dialog, AddAlternativeDialogPanel panel, AlternativesTable table) {
        super(owner);
        putValue(AbstractAction.NAME, "Editar");
        this.dialog_ = dialog;
        this.panel_ = panel;
        this.table_ = table;
    }

    public void actionPerformed(ActionEvent e) {
        if (table_.getSelectedRow() >= 0) {
            panel_.getAddButton().setAction(getEditAction());
            ArrayList row = table_.getRow(table_.getSelectedRow());
            LinkedHashMap fields = panel_.getAddAlternativePanel().getFields();
            Set set = fields.entrySet();
            Entry entry = null;
            Iterator iter = set.iterator();
            Iterator iter2 = row.iterator();
            for (; iter.hasNext() && iter2.hasNext(); ) {
                entry = (Entry) iter.next();
                Object component = entry.getValue();
                String value = null;
                value = iter2.next().toString();
                if (component instanceof JComboBox) {
                    ((JComboBox) component).setSelectedItem(component);
                } else {
                    ((JTextField) component).setText(value);
                }
            }
            this.dialog_.setLocationRelativeTo(this.getOwner());
            this.dialog_.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this.getOwner(), "N�o h� linhas selecionadas para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private EditAlternativeAction getEditAction() {
        if (editAction_ == null) {
            editAction_ = new EditAlternativeAction(this.panel_.getAddAlternativePanel(), this.dialog_, this.table_);
        }
        return editAction_;
    }
}

package net.sf.refactorit.ui.module.common;

import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.ui.TypeChooser;
import net.sf.refactorit.ui.module.RefactorItContext;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 *
 * @author Igor Malinin
 */
public final class TypeCellEditor extends DefaultCellEditor {

    private final JPanel panel = new JPanel(new BorderLayout());

    final JButton button = new JButton("...");

    {
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    final RefactorItContext context;

    public TypeCellEditor(RefactorItContext ctx) {
        super(getTextField());
        this.context = ctx;
        panel.add(editorComponent);
        panel.add(button, BorderLayout.EAST);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TypeChooser tc = new TypeChooser(context, true, null, null, false);
                tc.setIncludePrimitives(true);
                tc.show();
                BinTypeRef ref = tc.getTypeRef();
                if (ref != null) {
                    setValue(ref.getQualifiedName());
                }
            }
        });
    }

    void setValue(Object value) {
        delegate.setValue(value);
    }

    private static JTextField getTextField() {
        JTextField field = new JTextField();
        field.setMargin(new Insets(0, 0, 0, 0));
        field.setBorder(BorderFactory.createLineBorder(Color.black));
        return field;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        super.getTableCellEditorComponent(table, value, isSelected, row, column);
        return panel;
    }
}

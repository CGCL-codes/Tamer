package rra.swingGUI;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import rra.reader.RRAMediator;

public class HPSCheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

    public static final long serialVersionUID = 0L;

    private JCheckBox cb = null;

    private String combatName = null;

    private String combatParticipantName = null;

    private RRAMediator mediator = null;

    public HPSCheckBoxCellEditor(String name, RRAMediator mediator) {
        this.combatName = name;
        this.mediator = mediator;
    }

    @Override
    public Object getCellEditorValue() {
        boolean b = cb.isSelected();
        cb = null;
        return new Boolean(b);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
        combatParticipantName = (String) table.getModel().getValueAt(rowIndex, 1);
        cb = new JCheckBox();
        if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            cb.setSelected(b.booleanValue());
        } else {
            cb.setSelected(false);
        }
        cb.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                List<Object> l = new ArrayList<Object>();
                l.add(combatName);
                l.add(combatParticipantName);
                l.add(((JCheckBox) e.getSource()).isSelected());
                mediator.processEvent(RRAMediator.EventHPSCombatParticipantChange, l);
            }
        });
        cb.setHorizontalAlignment(SwingConstants.CENTER);
        return cb;
    }
}

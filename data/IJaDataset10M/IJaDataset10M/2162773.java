package perun.client.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import perun.isle.virtualmachine.DebuggableCPU;

/**
 * This panel displays a table containing register names and values.
 */
public class RegisterViewer extends OrganismViewerPanel {

    private class RegisterTM extends AbstractTableModel {

        private static final String unkVal = "????????";

        private int registerCount;

        private String registerNames[];

        private List<Long> registerValues;

        public void setData(String names[], List<Long> values) {
            registerCount = (names != null) ? names.length : 0;
            registerNames = names;
            registerValues = values;
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Name";
                case 1:
                    return "Value";
                default:
                    return super.getColumnName(column);
            }
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) return Long.class;
            return String.class;
        }

        public int getRowCount() {
            return registerCount;
        }

        public Object getValueAt(int row, int col) {
            switch(col) {
                case 0:
                    return registerNames[row];
                case 1:
                    if (row >= registerValues.size()) return unkVal;
                    Long val = registerValues.get(row);
                    return hexDisplay() ? String.format("%1$08X", val) : val.toString();
            }
            return unkVal;
        }
    }

    ;

    private final RegisterTM registerTM = new RegisterTM();

    /**
	 * Creates new RegisterViewer panel.
	 * @param orgView The associated OrganismViewer panel.
	 */
    public RegisterViewer(OrganismViewer orgView) {
        super(orgView, "Registers");
        setLayout(new BorderLayout());
        JTable registerTable = new JTable(registerTM);
        registerTable.setBorder(LineBorder.createBlackLineBorder());
        registerTable.setFont(new Font("monospaced", Font.PLAIN, 12));
        registerTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                showPopup(evt);
            }
        });
        JScrollPane scroll = new JScrollPane(registerTable);
        scroll.setBorder(LineBorder.createBlackLineBorder());
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        if (orgView.org == null) {
            registerTM.setData(null, null);
            return;
        }
        String[] names = ((DebuggableCPU) orgView.org.getVM()).getRegisterNames();
        List<Long> values = orgView.org.getRegisterValues();
        registerTM.setData(names, values);
    }
}

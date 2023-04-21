package kr.ac.ssu.imc.whitehole.report.designer.dialogs;

import javax.swing.*;
import javax.swing.table.*;

public class RDQBRowHeader extends JTable {

    protected JTable mainTable;

    protected String sRowHeaders[] = { "      ǥ    ��", "      ��    ��", "      ���̺�", "      ��    ��", "      ��    ��", "      ��    ��", "          ��", "      ��    ��", "  ������ ����", "          Ű" };

    public RDQBRowHeader(JTable table) {
        super();
        mainTable = table;
        setModel(new RowTableModel());
        setPreferredScrollableViewportSize(getPreferredSize());
        setEnabled(false);
        JComponent renderer = (JComponent) getDefaultRenderer(Object.class);
        LookAndFeel.installColorsAndFont(renderer, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
    }

    public int getRowHeight(int row) {
        return mainTable.getRowHeight();
    }

    class RowTableModel extends AbstractTableModel {

        public int getRowCount() {
            return mainTable.getModel().getRowCount();
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int column) {
            return sRowHeaders[row];
        }
    }
}

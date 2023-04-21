package carrancao.util;

import carrancao.entidades.Mesa;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Lubnnia
 */
public class MesaTableModel extends AbstractTableModel {

    private List<Mesa> mesas;

    public MesaTableModel(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    public int getRowCount() {
        return mesas.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Mesa mesa = getMesa(rowIndex);
        if (mesa != null) {
            switch(columnIndex) {
                case 0:
                    return mesa.getNumMesa();
                case 1:
                    return mesa.getStatus();
            }
        }
        return null;
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Nº das Mesas";
            case 1:
                return "Status";
        }
        return null;
    }

    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
        }
        return null;
    }

    public Mesa getMesa(int row) {
        if (row >= 0) {
            return mesas.get(row);
        }
        return null;
    }
}

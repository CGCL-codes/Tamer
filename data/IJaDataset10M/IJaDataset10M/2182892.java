package visSim.modelosTablas;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/** Clase creada como modelo para la tabla de eventos de la simulacion*/
public class modeloTablaLeyenda extends AbstractTableModel {

    final String columnNames[] = { "Descripcion", "Color" };

    final Object[][] data;

    public modeloTablaLeyenda(Vector datos) {
        data = new Object[datos.size() / 2][2];
        for (int i = 0; i < datos.size(); i += 2) {
            data[i / 2][0] = (String) datos.elementAt(i);
            data[i / 2][1] = (String) datos.elementAt(i + 1);
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /** Las celdas de la tabla no se van a poder modificar */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object valor, int row, int col) {
        data[row][col] = valor;
    }
}

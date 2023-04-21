package visSim.modelosTablas;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import visSim.interfazVisual;

/** Clase creada como modelo para la tabla de propiedades */
public class modeloTablaInterfaces extends AbstractTableModel {

    final String columnNames[] = { "Interfaz", "IP", "Mascara", "MAC", "Conecta con" };

    final Object[][] data;

    public modeloTablaInterfaces(Vector interfaces) {
        data = new Object[interfaces.size()][5];
        for (int i = 0; i < interfaces.size(); i++) {
            data[i][0] = ((interfazVisual) (interfaces.elementAt(i))).getNombre();
            data[i][1] = ((interfazVisual) (interfaces.elementAt(i))).getIP();
            data[i][2] = ((interfazVisual) (interfaces.elementAt(i))).getMascara();
            data[i][3] = ((interfazVisual) (interfaces.elementAt(i))).getDirEnlace();
            data[i][4] = ((interfazVisual) (interfaces.elementAt(i))).getconecta();
        }
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
}

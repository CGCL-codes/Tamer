package Model.View;

import Model.Util.DataBase;
import Model.Util.PolizaSdo;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author PaO
 */
public class PorDepositarMT extends AbstractTableModel {

    double sub = 0;

    ArrayList elementos = null;

    String[] encabezados = { "Cta", "Fecha", "Ref.", "Concepto", "Monto", "Sub-Tot" };

    Object[][] datos;

    PolizaSdo poliza = null;

    DateFormat formatoFecha = DateFormat.getDateInstance();

    DecimalFormat formatoNumero = (DecimalFormat) DecimalFormat.getNumberInstance();

    public PorDepositarMT(ArrayList elementos) {
        boolean misma = false;
        this.elementos = elementos;
        formatoNumero.setMinimumFractionDigits(2);
        formatoNumero.setMaximumIntegerDigits(2);
        formatoNumero.applyPattern("#,##0.00");
        datos = new Object[elementos.size()][6];
        System.out.println("Dentro del constructor del modelo, tamaño =" + elementos.size());
        for (int i = 0; i < elementos.size(); i++) {
            poliza = (PolizaSdo) elementos.get(i);
            if (i == 0) misma = true; else if (i < elementos.size() - 1) {
                if (poliza.getCuenta() == ((PolizaSdo) elementos.get(i + 1)).getCuenta()) misma = true; else misma = false;
            }
            this.sub += poliza.getTotal();
            datos[i][0] = DataBase.consultaCuentasXid(poliza.getCuenta());
            datos[i][1] = formatoFecha.format(poliza.getFecha());
            datos[i][2] = "I - " + String.valueOf(poliza.getId());
            datos[i][3] = new String(" " + poliza.getConcepto().trim());
            datos[i][4] = formatoNumero.format(poliza.getTotal());
            if (!misma) {
                System.out.println("Dentro de misma en i= " + i);
                datos[i][5] = formatoNumero.format(this.sub);
                this.sub = 0;
            } else {
                if (i == elementos.size() - 1) datos[i][5] = formatoNumero.format(this.sub);
            }
        }
    }

    public int getColumnCount() {
        return encabezados.length;
    }

    public int getRowCount() {
        return datos.length;
    }

    @Override
    public String getColumnName(int col) {
        return encabezados[col];
    }

    public Object getValueAt(int row, int col) {
        return datos[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        datos[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}

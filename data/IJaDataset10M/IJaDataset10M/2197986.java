package estructuras;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SimpleTableModelFechas extends DefaultTableModel implements Serializable {

    public Estrategia estr;

    public SimpleTableModelFechas(Estrategia estr) {
        this.estr = estr;
        this.addColumn("Fechas");
    }

    public SimpleTableModelFechas(Estrategia estr, Vector<String> colname, Vector<Vector<Object>> data) {
        super(data, colname);
        this.estr = estr;
    }

    private String valor(int i) {
        if (i == 0) return "Fecha inicial"; else if (i == 1) return "Fecha de compra"; else return "Fecha de venta";
    }

    public void addInstrumento(Instrumento instr) {
        this.addColumn(instr.name);
        this.addColumn("Porcentaje");
        for (int i = 0; i < estr.fechas_default.size(); i++) {
            instr.setFecha(estr.ID, i, estr.fechas_default.elementAt(i));
            instr.getAlfas(estr.ID).add(new Double(0));
            instr.getP(estr.ID).add(new Double(0));
            instr.getQ(estr.ID).add(new Double(0));
            instr.getU(estr.ID).add(new Double(0));
            instr.getVolP(estr.ID).add(new Double(0));
            this.setValueAt(valor(i), i, this.getColumnCount() - 2);
            if (i > 0) this.setValueAt(new Double(0), i, this.getColumnCount() - 1);
        }
        this.fireTableDataChanged();
    }

    public void removeInstrumento(int index, Instrumento instr) {
        this.removeColumn(index + 1);
        this.removeColumn(index + 1);
    }

    public void removeColumn(int c) {
        Vector<Vector> data = this.getDataVector();
        for (int i = 0; i < data.size(); i++) data.elementAt(i).remove(c);
        Vector<String> names = new Vector<String>();
        for (int i = 0; i < this.getColumnCount(); i++) names.add(this.getColumnName(i));
        names.remove(c);
        this.setDataVector(data, names);
        this.fireTableDataChanged();
    }

    public void clear() {
        this.getDataVector().clear();
        this.fireTableDataChanged();
    }

    public void addFecha() {
        Fecha sig = new Fecha();
        sig.setTime(this.estr.fechas_default.elementAt(this.getRowCount() - 1).julday());
        sig.setTipo("Fecha inicial");
        for (int i = 0; i < this.estr.instrumentos.size(); i++) {
            this.estr.instrumentos.elementAt(i).addFecha(this.estr.ID, sig);
        }
        this.estr.fechas_default.add(sig);
        Vector<Object> fila = new Vector<Object>();
        fila.add(sig.toString());
        if (this.getRowCount() >= 1) {
            for (int i = 1; i < this.getColumnCount(); i++) {
                Object obj = this.getValueAt(this.getRowCount() - 1, i);
                fila.add(obj);
            }
        } else {
            for (int i = 1; i < this.getColumnCount(); i++) {
                Object obj = null;
                if (i % 2 == 0) obj = "0"; else if (i % 2 == 1) obj = "Fecha inicial";
                fila.add(obj);
            }
        }
        this.addRow(fila);
    }

    public void removeFecha(int index) {
        for (int i = 0; i < this.estr.instrumentos.size(); i++) {
            this.estr.instrumentos.elementAt(i).removeFecha(this.estr.ID, index);
        }
        this.estr.fechas_default.remove(index);
        this.removeRow(index);
    }

    public void addFecha(Fecha sig) {
        sig.setTipo("Fecha de venta");
        for (int i = 0; i < this.estr.instrumentos.size(); i++) {
            this.estr.instrumentos.elementAt(i).addFecha(this.estr.ID, sig);
        }
        this.estr.fechas_default.add(sig);
        Vector<Object> fila = new Vector<Object>();
        fila.add(sig.toString());
        if (this.getRowCount() >= 1) {
            for (int i = 1; i < this.getColumnCount(); i++) {
                if (i % 2 == 0) {
                    Object obj = null;
                    if (this.getRowCount() == 1) obj = new Double(1); else obj = this.getValueAt(this.getRowCount() - 1, i);
                    fila.add(obj);
                } else fila.add(valor(this.getRowCount()));
            }
        } else {
            for (int i = 1; i < this.getColumnCount(); i++) {
                Object obj = null;
                if (i % 2 == 0) obj = "NaN"; else if (i % 2 == 1) obj = "Fecha inicial";
                fila.add(obj);
            }
        }
        this.addRow(fila);
    }

    public Class<?> getColumnClass(int col) {
        if (col >= 1 && col % 2 == 0) return Double.class;
        if (col == 0) return Punto.class;
        return String.class;
    }
}

package radiadores.igu.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import radiadores.entidades.ItemIndice;
import radiadores.persistencia.FachadaPersistencia;

/**
 *
 * @author franco
 */
public class IndiceTableModel extends AbstractTableModel implements IModeloReiniciable {

    private static final long serialVersionUID = 1L;

    private static final String[] NOMBRE_COLUMNAS = { "Concepto", "Valor" };

    private static final boolean[] COLUMNAS_EDITABLES = { false, true };

    private static final Class[] CLASE_COLUMNAS = { String.class, Double.class };

    private List<ItemIndice> items;

    /**
     * Constructor
     *
     * @param filas Cantidad de filas iniciales
     */
    public IndiceTableModel(int filas) {
        this.items = new ArrayList<ItemIndice>(filas > 0 ? filas : 0);
    }

    public double getValor(String concepto) {
        double resultado = Double.NaN;
        for (ItemIndice item : items) {
            if (item.getConcepto().equals(concepto)) {
                resultado = item.getValor();
            }
        }
        return resultado;
    }

    /**
     * Devuelve el nombre de la columna
     *
     * @param columna Indice de la columna
     * @return El nombre de la columna
     */
    @Override
    public String getColumnName(int columna) {
        return NOMBRE_COLUMNAS[columna];
    }

    /**
     * Devuelve la clase de la columna indicada
     *
     * @param columna Indice de la columna de la que se quiere averiguar la clase
     * @return La clase de la columna indicada
     */
    @Override
    public Class<?> getColumnClass(int columna) {
        return CLASE_COLUMNAS[columna];
    }

    /**
     * Devuelve si la celda es o no editable
     *
     * @param fila Indice de la fila
     * @param columna Indice de la columna
     * @return True si la celda es editable
     */
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return COLUMNAS_EDITABLES[columna];
    }

    /**
     * Devuelve la cantidad de filas
     *
     * @return La cantidad de filas
     */
    @Override
    public int getRowCount() {
        return items.size();
    }

    /**
     * Devuelve la cantidad de columnas
     *
     * @return La cantidad de columnas
     */
    @Override
    public int getColumnCount() {
        return NOMBRE_COLUMNAS.length;
    }

    /**
     * Devuelve el valor de la celda indicada
     *
     * @param fila Indice de la fila
     * @param columna Indice de la columna
     * @return Devuelve el valor de la celda indicada
     */
    @Override
    public Object getValueAt(int fila, int columna) {
        Object resultado = null;
        switch(columna) {
            case 0:
                resultado = items.get(fila).getConcepto();
                break;
            case 1:
                resultado = items.get(fila).getValor();
                break;
        }
        return resultado;
    }

    @Override
    public void setValueAt(Object valor, int fila, int columna) {
        switch(columna) {
            case 1:
                items.get(fila).setValor((Double) valor);
                FachadaPersistencia.getInstancia().actualizar(items.get(fila), true);
                break;
        }
        fireTableRowsUpdated(fila, fila);
    }

    /**
     * Agrega Proveedor al modelo
     *
     * @param proveedor Proveedor a agregar

     */
    public void agregarFila(ItemIndice item) {
        items.add(item);
        fireTableRowsInserted(items.size(), items.size());
    }

    public void agregarFilas(List<ItemIndice> itemsNuevos) {
        if (itemsNuevos != null) {
            items.addAll(itemsNuevos);
            fireTableRowsInserted(items.size() - itemsNuevos.size(), items.size());
        }
    }

    /**
     * Limita la cantidad de elementos del modelo al indicado
     *
     * @param cantidad Cantidad a la que se quiere limitar el numero de filas
     */
    public void limitarCantidad(int cantidad) {
        int cantidadAnterior = items.size();
        items = items.subList(0, cantidad);
        fireTableRowsDeleted(cantidad, cantidadAnterior);
    }

    /**
     * Devuelve todas las filas del modelo
     *
     * @return Todas las filas del modelo
     */
    public List<ItemIndice> getFilas() {
        return items;
    }

    public ItemIndice getFila(int indice) {
        return items.get(indice);
    }

    public void eliminarFila(int indice) {
        items.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }

    public void limpiarTableModel() {
        int tamanio = items.size();
        items.clear();
        fireTableRowsDeleted(0, tamanio);
    }

    @Override
    public void reiniciar() {
        limpiarTableModel();
    }
}

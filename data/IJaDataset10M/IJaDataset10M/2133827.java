package servando.communications.model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Esta clase sirve como clase de utilidad para enviar y recibir "matrices" de
 * números double.
 * @author Tomás Teijeiro Campo
 */
@XmlRootElement(name = "doubleMatrix")
@XmlAccessorType(XmlAccessType.FIELD)
public class DoubleMatrix {

    /**
     * Conjunto de cadenas de caracteres para transportar metadatos.
     */
    @XmlElement(name = "metaData")
    private ArrayList<String> metaData;

    /**
     * Conjunto de filas de la matriz que transporta este objeto
     */
    @XmlElement(name = "row")
    private ArrayList<DoubleList> rows;

    /**
     * Crea una nueva instancia de DoubleMatrix de tamaño 0x0
     */
    public DoubleMatrix() {
    }

    /**
     * Crea una nueva instancia de DoubleMatrix del tamaño especificado
     * @param rows
     * @param columns
     */
    public DoubleMatrix(int rows, int columns) {
        metaData = new ArrayList<String>();
        this.rows = new ArrayList<DoubleList>(rows);
        for (int i = 0; i < rows; i++) {
            this.rows.add(i, new DoubleList(columns));
        }
    }

    /**
     * @return Número de filas de la matriz
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     *
     * @return Número de columnas de la matriz
     */
    public int getColumnCount() {
        if (rows != null && rows.size() > 0) {
            return rows.get(0).getValues().length;
        }
        return 0;
    }

    /**
     * Obtiene el elemento situado en la coordenada especificada.
     * @param row Fila
     * @param column columna
     * @return Valor en la coordenada (fila, columna)
     */
    public double getElementAt(int row, int column) {
        return rows.get(row).getValues()[column];
    }

    /**
     * Establece el elemento situado en la coordenada especificada
     * @param row fila
     * @param column columna
     * @param value Valor a establecer en la coordenada (fila,columna)
     */
    public void setElementAt(int row, int column, double value) {
        rows.get(row).getValues()[column] = value;
    }

    /**
     * Conjunto de cadenas de caracteres para transportar metadatos.
     * @return the metaData
     */
    public ArrayList<String> getMetaData() {
        return metaData;
    }

    /**
     * Conjunto de cadenas de caracteres para transportar.
     * @param metaData the metaData to set
     */
    public void setMetaData(ArrayList<String> metaData) {
        this.metaData = metaData;
    }

    /**
     * Conjunto de filas de la matriz que transporta este objeto
     * @return the rows
     */
    public ArrayList<DoubleList> getRows() {
        return rows;
    }

    /**
     * Conjunto de filas de la matriz que transporta este objeto
     * @param rows the rows to set
     */
    public void setRows(ArrayList<DoubleList> rows) {
        this.rows = rows;
    }
}

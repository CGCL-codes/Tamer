package org.mushroomdb.catalog;

import java.util.Iterator;

/**
 * Interface para los registros.
 * 
 * @author Matias
 *
 */
public interface Register {

    /**
	 * Devuelve las columnas de este registro.
	 * @return el iterador de columnas de este registro.
	 */
    public Iterator getColumns();

    /**
	 * Devuelve el �ndice de una columna en el registro. Los �ndices de las columnas
	 * de los registros est�n comprendidos en el siguiente rango: 1 &lt;= i &lt;= #columnas
	 * @param column la columna de la cual se desea saber su �ndice.
	 * @return un entero indicando el �ndice de la columna solicitada.
	 */
    public int getColumnIndex(Column column);

    /**
	 * Devuelve el valor de la columna solicitada.
	 * @param columnIndex el �nidce de la columna deseada.
	 * @return el objeto de la columna elegida.
	 */
    public Object getValue(int columnIndex);

    /**
	 * Establece el valor de una columna.
	 * @param columnIndex el �ndice de la columna deseada.
	 * @param value el nuevo valor para la columna elegida.
	 */
    public void setValue(int columnIndex, Object value);

    /**
	 * Devuelve la cantidad de columnas de este registro.
	 * @return un int representando la cantidad de columnas de este registro.
	 */
    public int getColumnCount();

    /**
	 * Removes this register from it's source (i.e. a Table) 
	 * @throws UnsupportedOperationException if this register
	 * 		does not support the operation.
	 */
    public void remove();
}

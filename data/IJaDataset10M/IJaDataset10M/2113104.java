package jaxlib.col.table;

import javax.swing.table.TableModel;

/** 
 * An observable table.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: TableModelTable.java 2650 2008-09-04 10:36:43Z joerg_wassmer $
 */
public interface TableModelTable<E> extends Table<E>, TableModel {

    @Override
    public Class getColumnClass(int columnIndex);

    @Override
    public E getValueAt(int rowIndex, int columnIndex);

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex);
}

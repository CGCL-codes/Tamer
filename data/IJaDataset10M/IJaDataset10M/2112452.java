package org.columba.mail.gui.table;

import javax.swing.event.TableColumnModelListener;
import org.columba.mail.message.IHeaderList;

/**
 * @author fdietz
 *
 */
public interface IHeaderTableModel extends TableColumnModelListener {

    /**
	 * ***************************** implements TableModelModifier
	 * ******************
	 */
    void modify(Object[] uids);

    void remove(Object[] uids);

    void update();

    void clear();

    void set(IHeaderList headerList);

    /** ********************** getter/setter methods *************************** */
    void enableThreadedView(boolean b);

    String getColumnName(int column);

    void clearColumns();

    void addColumn(String c);
}

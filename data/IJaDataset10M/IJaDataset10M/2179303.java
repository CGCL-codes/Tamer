package root;

import java.util.Hashtable;
import javax.swing.table.TableCellEditor;

/**
 * Inspired by http://www.javaworld.com/javaworld/javatips/jw-javatip102.html
 * Well, more like ripped, but this is for educational/nonprofit purposes,
 * which is fine under the site's TOS.
 *
 * This gets matched up with the CasterAlarmClockTable, and manages the special
 * editors that the 3rd column of the table instantiates.
 */
public class CasterAlarmClockRowEditor {

    private Hashtable data;

    public CasterAlarmClockRowEditor() {
        data = new Hashtable();
    }

    public void addEditorOnRow(int row, TableCellEditor tce) {
        data.put(row, tce);
    }

    public TableCellEditor getEditorOnRow(int row) {
        return (TableCellEditor) data.get(row);
    }

    public void removeEditorOnRow(int row) {
        data.remove(row);
    }
}

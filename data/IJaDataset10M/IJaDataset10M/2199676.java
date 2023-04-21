package org.sqlsplatter.tinyhorror.actions;

import java.util.List;
import org.sqlsplatter.tinyhorror.jdbc.ThsResultSet;
import org.sqlsplatter.tinyhorror.objects.ThsDatabase;
import org.sqlsplatter.tinyhorror.objects.ThsTable;
import org.sqlsplatter.tinyhorror.other.ConnectionProperties;
import org.sqlsplatter.tinyhorror.other.TableFieldsMapper;
import org.sqlsplatter.tinyhorror.other.exceptions.THSException;
import org.sqlsplatter.tinyhorror.values.Value;

public class Insert implements IAction {

    private final String _tableName;

    private final List<String> _columns;

    private final List<? extends Value>[] _valueLists;

    /**
	 * INSERT constructor.
	 */
    public Insert(String tableName, List<String> columns, List<? extends Value>... valueLists) {
        _tableName = tableName;
        _valueLists = valueLists;
        _columns = columns;
    }

    public Insert(String tableName, List<String> columns, Select select) {
        throw new UnsupportedOperationException();
    }

    public ThsResultSet execute(ConnectionProperties connProps, ThsDatabase database) throws THSException {
        ThsTable table = database.getTable(_tableName);
        TableFieldsMapper fm = new TableFieldsMapper(table);
        for (List<? extends Value> valuesList : _valueLists) {
            for (Value value : valuesList) value.link(fm);
        }
        for (List<? extends Value> valuesList : _valueLists) {
            table.insert(_columns, valuesList, connProps);
        }
        return null;
    }
}

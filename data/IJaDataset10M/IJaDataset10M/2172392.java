package org.dbunit.dataset;

import com.mockobjects.ExpectationList;
import com.mockobjects.Verifiable;
import java.util.Arrays;

/**
 * @author Manuel Laflamme
 * @since Apr 29, 2003
 * @version $Revision: 284 $
 */
public class MockDataSetConsumer implements Verifiable, IDataSetConsumer {

    private static final ProducerEvent START_DATASET_EVENT = new ProducerEvent("startDataSet()");

    private static final ProducerEvent END_DATASET_EVENT = new ProducerEvent("endDataSet()");

    private final ExpectationList _expectedList = new ExpectationList("");

    private String _actualTableName;

    public void addExpectedStartDataSet() throws Exception {
        _expectedList.addExpected(START_DATASET_EVENT);
    }

    public void addExpectedEndDataSet() throws Exception {
        _expectedList.addExpected(END_DATASET_EVENT);
    }

    public void addExpectedStartTable(ITableMetaData metaData) throws Exception {
        _expectedList.addExpected(new StartTableEvent(metaData, false));
    }

    public void addExpectedStartTable(String tableName, Column[] columns) throws Exception {
        addExpectedStartTable(new DefaultTableMetaData(tableName, columns));
    }

    public void addExpectedStartTableIgnoreColumns(String tableName) throws Exception {
        _expectedList.addExpected(new StartTableEvent(tableName, true));
    }

    public void addExpectedEmptyTable(String tableName, Column[] columns) throws Exception {
        addExpectedStartTable(tableName, columns);
        addExpectedEndTable(tableName);
    }

    public void addExpectedEmptyTableIgnoreColumns(String tableName) throws Exception {
        addExpectedStartTableIgnoreColumns(tableName);
        addExpectedEndTable(tableName);
    }

    public void addExpectedEndTable(String tableName) throws Exception {
        _expectedList.addExpected(new EndTableEvent(tableName));
    }

    public void addExpectedRow(String tableName, Object[] values) throws Exception {
        _expectedList.addExpected(new RowEvent(tableName, values));
    }

    public void verify() {
        _expectedList.verify();
    }

    public void startDataSet() throws DataSetException {
        _expectedList.addActual(START_DATASET_EVENT);
    }

    public void endDataSet() throws DataSetException {
        _expectedList.addActual(END_DATASET_EVENT);
    }

    public void startTable(ITableMetaData metaData) throws DataSetException {
        _expectedList.addActual(new StartTableEvent(metaData, false));
        _actualTableName = metaData.getTableName();
    }

    public void endTable() throws DataSetException {
        _expectedList.addActual(new EndTableEvent(_actualTableName));
        _actualTableName = null;
    }

    public void row(Object[] values) throws DataSetException {
        _expectedList.addActual(new RowEvent(_actualTableName, values));
    }

    private static class ProducerEvent {

        protected final String _name;

        public ProducerEvent(String name) {
            _name = name;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProducerEvent)) return false;
            final ProducerEvent item = (ProducerEvent) o;
            if (!_name.equals(item._name)) return false;
            return true;
        }

        public int hashCode() {
            return _name.hashCode();
        }

        public String toString() {
            return _name;
        }
    }

    private static class StartTableEvent extends ProducerEvent {

        private final String _tableName;

        private final Column[] _columns;

        private final boolean _ignoreColumns;

        public StartTableEvent(ITableMetaData metaData, boolean ignoreColumns) throws DataSetException {
            super("startTable()");
            _tableName = metaData.getTableName();
            _columns = metaData.getColumns();
            _ignoreColumns = ignoreColumns;
        }

        public StartTableEvent(String tableName, boolean ignoreColumns) throws DataSetException {
            super("startTable()");
            _tableName = tableName;
            _columns = new Column[0];
            _ignoreColumns = ignoreColumns;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StartTableEvent)) return false;
            if (!super.equals(o)) return false;
            final StartTableEvent startTableItem = (StartTableEvent) o;
            if (!_tableName.equals(startTableItem._tableName)) return false;
            if (!_ignoreColumns) {
                if (!Arrays.equals(_columns, startTableItem._columns)) return false;
            }
            return true;
        }

        public int hashCode() {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            return result;
        }

        public String toString() {
            String string = _name + ": table=" + _tableName;
            if (!_ignoreColumns) {
                string += ", columns=" + Arrays.asList(_columns);
            }
            return string;
        }
    }

    private static class EndTableEvent extends ProducerEvent {

        private final String _tableName;

        public EndTableEvent(String tableName) {
            super("endTable()");
            _tableName = tableName;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EndTableEvent)) return false;
            if (!super.equals(o)) return false;
            final EndTableEvent endTableItem = (EndTableEvent) o;
            if (!_tableName.equals(endTableItem._tableName)) return false;
            return true;
        }

        public int hashCode() {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            return result;
        }

        public String toString() {
            return _name + ": table=" + _tableName;
        }
    }

    private static class RowEvent extends ProducerEvent {

        private final String _tableName;

        private final Object[] _values;

        public RowEvent(String tableName, Object[] values) {
            super("row()");
            _tableName = tableName;
            _values = values;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RowEvent)) return false;
            if (!super.equals(o)) return false;
            final RowEvent rowItem = (RowEvent) o;
            if (!_tableName.equals(rowItem._tableName)) return false;
            if (!Arrays.equals(_values, rowItem._values)) return false;
            return true;
        }

        public int hashCode() {
            int result = super.hashCode();
            result = 29 * result + _tableName.hashCode();
            return result;
        }

        public String toString() {
            return _name + ": table=" + _tableName + ", values=" + Arrays.asList(_values);
        }
    }
}

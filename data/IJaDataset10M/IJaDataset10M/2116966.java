package org.dbunit.dataset;

import org.dbunit.dataset.IDataSetProducer;
import org.dbunit.dataset.IDataSetConsumer;
import org.dbunit.dataset.DefaultConsumer;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.datatype.DataType;

/**
 * @author Manuel Laflamme
 * @since Jun 12, 2003
 * @version $Revision: 291 $
 */
public class MockDataSetProducer implements IDataSetProducer {

    private int _tableCount;

    private int _columnCount;

    private int _rowCount;

    private IDataSetConsumer _consumer = new DefaultConsumer();

    public void setupTableCount(int tableCount) {
        _tableCount = tableCount;
    }

    public void setupColumnCount(int columnCount) {
        _columnCount = columnCount;
    }

    public void setupRowCount(int rowCount) {
        _rowCount = rowCount;
    }

    public void setConsumer(IDataSetConsumer consumer) throws DataSetException {
        _consumer = consumer;
    }

    public void produce() throws DataSetException {
        _consumer.startDataSet();
        for (int i = 0; i < _tableCount; i++) {
            String tableName = "TABLE" + i;
            Column[] columns = new Column[_columnCount];
            for (int j = 0; j < columns.length; j++) {
                columns[j] = new Column("COLUMN" + j, DataType.UNKNOWN);
            }
            ITableMetaData metaData = new DefaultTableMetaData(tableName, columns);
            _consumer.startTable(metaData);
            for (int j = 0; j < _rowCount; j++) {
                Object[] values = new Object[_columnCount];
                for (int k = 0; k < values.length; k++) {
                    values[k] = j + "," + k;
                }
                _consumer.row(values);
            }
            _consumer.endTable();
        }
        _consumer.endDataSet();
    }
}

package org.broadleafcommerce.openadmin.client.callback;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;

/**
 * 
 * @author krosenberg
 *
 */
public class TileGridItemSelected {

    private Record record;

    private DataSource dataSource;

    public TileGridItemSelected(Record record, DataSource dataSource) {
        this.record = record;
        this.dataSource = dataSource;
    }

    public Record getRecord() {
        return record;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}

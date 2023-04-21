package net.mlw.vlh.adapter.jdbc.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.mlw.vlh.ValueListInfo;
import net.mlw.vlh.adapter.jdbc.AbstractJdbcAdapter;
import net.mlw.vlh.adapter.jdbc.spring.util.SpringConnectionCreator;
import org.springframework.jdbc.core.RowMapper;

/**
 * net.mlw.vlh.adapter.jdbc.spring.SpringDaoValueListAdapter
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2005/10/17 11:42:36 $
 */
public class SpringDaoValueListAdapter extends AbstractJdbcAdapter {

    private RowMapper rowMapper;

    public SpringDaoValueListAdapter() {
        setConnectionCreator(new SpringConnectionCreator());
    }

    /**
    * @see net.mlw.vlh.adapter.jdbc.AbstractJdbcAdapter#processResultSet(java.lang.String, java.sql.ResultSet, int, net.mlw.vlh.ValueListInfo)
    */
    public List processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info) throws SQLException {
        List list = new ArrayList();
        for (int rowIndex = 0; result.next() && rowIndex < numberPerPage; rowIndex++) {
            list.add(rowMapper.mapRow(result, rowIndex));
        }
        return list;
    }

    /**
    * @return Returns the rowMapper.
    */
    public RowMapper getRowMapper() {
        return rowMapper;
    }

    /**
    * @param rowMapper The rowMapper to set.
    */
    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }
}

package org.opoo.oqs.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.opoo.oqs.jdbc.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link java.sql.ResultSet}�����ȡ��������{@link List}��
 * ��ResultSet��ÿһ����¼ʹ��ָ����RowReader������ɶ���Ȼ������List�С�
 *
 * @author Alex Lin(alex@opoo.org)
 * @version 1.0
 * @since 1.0
 */
public class ListResultSetExtractor implements ResultSetExtractor {

    private RowMapper rowMapper;

    /**
     *
     * @param rowMapper RowMapper
     */
    public ListResultSetExtractor(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    /**
     * ResultSet��ÿ����¼��ӦList��һ�
     *
     * @param rs ResultSet
     * @return Object
     * @throws SQLException
     * @throws DataAccessException
     */
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        List list = new ArrayList();
        int i = 0;
        while (rs.next()) {
            Object result = rowMapper.mapRow(rs, i++);
            if (result != null) {
                list.add(result);
            }
        }
        return list;
    }
}

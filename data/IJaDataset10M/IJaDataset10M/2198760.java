package purej.dao.processor.crud.statement;

import java.sql.Connection;
import purej.dao.domain.JDBCModel;

/**
 * �ڵ� CRUD ������Ʈ��Ʈ
 * 
 * @author leesangboo
 * 
 */
public interface CRUDStatement {

    /**
     * �Է¹��� ������ �ڵ� CRUD Statement���� ��ȯ�Ѵ�.
     * 
     * @param conn
     * @param columnNames
     * @param columnTypes
     * @param jdbcModel
     * @param pk
     * @return
     * @throws Exception
     */
    public CRUDStatementModel makePreparedStatement(Connection conn, String[] columnNames, String[] columnTypes, JDBCModel jdbcModel, String[] pk) throws Exception;
}

package ces.coffice.common.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ces.coral.dbo.DBOperation;
import ces.coral.dbo.DBOperationFactory;
import ces.coral.dbo.ERDBOperationFactory;
import ces.coral.file.CesGlobals;
import ces.platform.infoplat.core.base.ConfigInfo;
import ces.platform.system.common.Constant;

public class DbBase {

    public DbBase() {
    }

    /**
     * �õ�һ��dbo����
     * @return
     * @throws java.lang.Exception
     */
    protected DBOperation createDBOperation() throws Exception {
        new CesGlobals().setConfigFile(Constant.DB_CONFIGE_FILE);
        DBOperationFactory factory = new ERDBOperationFactory();
        return factory.createDBOperation(ConfigInfo.getInstance().getPoolName());
    }

    /**
     * �õ�һ��dbo����
     * @return
     * @throws java.lang.Exception
     */
    protected DBOperation createDBOperation(String poolName) throws Exception {
        new CesGlobals().setConfigFile(Constant.DB_CONFIGE_FILE);
        DBOperationFactory factory = new ERDBOperationFactory();
        return factory.createDBOperation(poolName);
    }

    /**
	 * �رս��
	 * @return
	 */
    protected void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
        }
    }

    /**
	 * �ر���ݿ��Statement
	 * @return
	 */
    protected void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
        }
    }

    /**
	 * �ر���ݿ��Statement
	 * @return
	 */
    protected void close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException ex) {
        }
    }

    /**
	 * �ر���ݿ������
	 * @return
	 */
    protected void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
        }
    }

    /**
	 * �ر���ݿ�ĵ���
	 * @return
	 */
    protected void close(DBOperation dbo) {
        try {
            if (dbo != null) {
                dbo.close();
            }
        } catch (SQLException ex) {
        }
    }

    /**
	 * �ر���ݿ����Դ
	 * @return
	 */
    protected void close(ResultSet resultSet, Statement statement, PreparedStatement preparedStatement, Connection connection, DBOperation dbo) {
        close(resultSet);
        close(statement);
        close(preparedStatement);
        close(connection);
        close(dbo);
    }
}

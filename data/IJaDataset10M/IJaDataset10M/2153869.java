package it.eg.sloth.bean.packagebean;

import it.eg.sloth.db.manager.DataConnectionException;
import it.eg.sloth.db.manager.DataConnectionManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Lib_tdeBean {

    private static String CifraStatement0 = "{ ? = call LIB_TDE.Cifra(?) }";

    private static String ClosewalletStatement0 = "{ call LIB_TDE.Closewallet() }";

    private static String DecifraStatement0 = "{ ? = call LIB_TDE.Decifra(?) }";

    private static String OpenwalletStatement0 = "{ call LIB_TDE.Openwallet(?) }";

    public static String Cifra(Connection connection, String intesto) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(CifraStatement0);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setObject(2, intesto, Types.VARCHAR);
        callableStatement.execute();
        String value = callableStatement.getString(1);
        callableStatement.close();
        return value;
    }

    public static String Cifra(String intesto) throws SQLException, DataConnectionException {
        Connection connection = null;
        try {
            connection = DataConnectionManager.getInstance().getConnection();
            return Cifra(connection, intesto);
        } finally {
            DataConnectionManager.release(connection);
        }
    }

    public static void Closewallet(Connection connection) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(ClosewalletStatement0);
        callableStatement.execute();
        callableStatement.close();
    }

    public static void Closewallet() throws SQLException, DataConnectionException {
        Connection connection = null;
        try {
            connection = DataConnectionManager.getInstance().getConnection();
            Closewallet(connection);
        } finally {
            DataConnectionManager.release(connection);
        }
    }

    public static String Decifra(Connection connection, String intesto) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(DecifraStatement0);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setObject(2, intesto, Types.VARCHAR);
        callableStatement.execute();
        String value = callableStatement.getString(1);
        callableStatement.close();
        return value;
    }

    public static String Decifra(String intesto) throws SQLException, DataConnectionException {
        Connection connection = null;
        try {
            connection = DataConnectionManager.getInstance().getConnection();
            return Decifra(connection, intesto);
        } finally {
            DataConnectionManager.release(connection);
        }
    }

    public static void Openwallet(Connection connection, String inwallet) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(OpenwalletStatement0);
        callableStatement.setObject(1, inwallet, Types.VARCHAR);
        callableStatement.execute();
        callableStatement.close();
    }

    public static void Openwallet(String inwallet) throws SQLException, DataConnectionException {
        Connection connection = null;
        try {
            connection = DataConnectionManager.getInstance().getConnection();
            Openwallet(connection, inwallet);
        } finally {
            DataConnectionManager.release(connection);
        }
    }
}

package baseDeDatos.sqlDB;

import java.sql.Connection;
import java.sql.DriverManager;
import validaciones.Validar;

public class SqlServerDB extends AbstractSqlDB {

    public SqlServerDB(String hostName, String nombreBaseDeDatos, String user, String password) {
        super(hostName, nombreBaseDeDatos, user, password);
    }

    Connection conexion(String hostName, String nombreBaseDeDatos, String user, String password) {
        Connection conexion;
        String pathConexion;
        pathConexion = "jdbc:microsoft:sqlserver://" + hostName + "/" + nombreBaseDeDatos;
        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
            conexion = DriverManager.getConnection(pathConexion, user, password);
        } catch (Exception e) {
            Validar.error("Error conectando con base de datos" + e);
            conexion = null;
        }
        return conexion;
    }
}

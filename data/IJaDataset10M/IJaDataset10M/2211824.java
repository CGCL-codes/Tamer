package Datos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Usuario
 */
public class ListaTipodocIdentidadDAO extends DAO {

    static Connection con;

    public ListaTipodocIdentidadDAO() {
        super();
    }

    public static Statement sta(Statement st) throws SQLException {
        con = DAO.getConexion();
        st = con.createStatement();
        return st;
    }

    public static ResultSet results(ResultSet rs) throws SQLException {
        st = sta(st);
        rs = st.executeQuery("SELECT * FROM `tipodedocumentodeidentidad`");
        return rs;
    }
}

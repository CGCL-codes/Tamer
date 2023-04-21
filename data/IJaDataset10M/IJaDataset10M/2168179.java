package Datos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Usuario
 */
public class ListaPeriocidadRemunDAO extends DAO {

    static Connection con;

    public ListaPeriocidadRemunDAO() {
        super();
    }

    public static Statement sta(Statement st) throws SQLException {
        con = DAO.getConexion();
        st = con.createStatement();
        return st;
    }

    public static ResultSet results(ResultSet rs) throws SQLException {
        st = sta(st);
        rs = st.executeQuery("SELECT * FROM `periododeremuneracion`");
        return rs;
    }
}

package it.cspnet.sutri.core.src.sutri.spike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class UtenteDAO {

    public Utente leggi(String id) throws SQLException {
        Utente u = null;
        Connection conn = getDataSource().getConnection();
        try {
            PreparedStatement pstm = conn.prepareStatement("...");
            try {
                ResultSet rslt = pstm.executeQuery();
                try {
                    if (rslt.first()) u = new Utente(id, rslt.getString(1), rslt.getInt(2));
                } finally {
                    rslt.close();
                }
            } finally {
                pstm.close();
            }
        } finally {
            conn.close();
        }
        return u;
    }

    private DataSource getDataSource() {
        return null;
    }
}

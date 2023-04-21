package src.DAO;

import src.jdbc.PostgreSQL;
import src.Entities.Idioma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Patty
 */
public class IdiomaDAO {

    public String deleteIdioma(int idiomaId) {
        try {
            String sql = "DELETE FROM idioma WHERE idioma_id = ?;";
            PreparedStatement delete = PostgreSQL.getConnection().prepareStatement(sql);
            delete.setInt(1, idiomaId);
            return String.valueOf(delete.execute());
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String insertIdioma(Idioma idioma) {
        try {
            String sql = "INSERT " + "       INTO idioma(idioma_id, idioma)" + "     VALUES ( ?, ?)";
            PreparedStatement insert = PostgreSQL.getConnection().prepareStatement(sql);
            insert.setInt(1, idioma.getIdiomaId());
            insert.setString(2, idioma.getIdioma());
            return String.valueOf(insert.execute());
        } catch (Exception e) {
            return e.toString();
        }
    }

    public ArrayList selectIdioma(String sql) {
        ArrayList results = new ArrayList();
        try {
            Connection con = PostgreSQL.getConnection();
            Statement select = con.createStatement();
            ResultSet query = select.executeQuery(sql);
            while (query.next()) {
                Idioma idioma = new Idioma(query.getInt("idioma_id"), query.getString("idioma"));
                results.add(idioma);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return results;
    }

    public String updateIdioma(Idioma idioma) {
        try {
            String sql = "UPDATE idioma" + "        SET idioma = ?" + "      WHERE idioma_id = ?";
            PreparedStatement update = PostgreSQL.getConnection().prepareStatement(sql);
            update.setInt(1, idioma.getIdiomaId());
            update.setString(2, idioma.getIdioma());
            return String.valueOf(update.execute());
        } catch (Exception e) {
            return e.toString();
        }
    }
}

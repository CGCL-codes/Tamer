package magazyn;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import commons.DbConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class UsunAutoraManager {

    private String imie = "a", nazwisko = "a";

    PrzegladajAutoraData autor = new PrzegladajAutoraData();

    public String usun() {
        try {
            Connection conn = (Connection) DbConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement("DELETE FROM `biblioteka`.`autorzy` WHERE imie=? and nazwisko =?");
            ps.setString(1, this.imie);
            ps.setString(2, this.nazwisko);
            ps.executeUpdate();
            System.out.println(ps);
            return "success";
        } catch (SQLException ex) {
            Logger.getLogger(UsunAutoraManager.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public PrzegladajAutoraData getAutor() {
        return autor;
    }

    public void setAutor(PrzegladajAutoraData autor) {
        this.autor = autor;
        this.imie = autor.getImie();
        this.nazwisko = autor.getNazwisko();
        System.out.println(imie);
        System.out.println(nazwisko);
    }
}

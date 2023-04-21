package magazyn;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import commons.DbConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdytujAutoraManager {

    PrzegladajAutoraData autor = new PrzegladajAutoraData();

    private String imie;

    private String nazwisko;

    public EdytujAutoraManager() {
    }

    ;

    public String edytuj() {
        try {
            Connection conn = (Connection) DbConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) conn.prepareStatement("UPDATE `biblioteka`.`autorzy` SET imie=?, nazwisko=? WHERE imie = ? and nazwisko = ?");
            ps.setString(1, imie);
            ps.setString(2, nazwisko);
            ps.setString(3, this.autor.getImie());
            ps.setString(4, this.autor.getNazwisko());
            ps.executeUpdate();
            System.out.println(ps);
            return "success";
        } catch (SQLException ex) {
            Logger.getLogger(EdytujAutoraManager.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println(this.autor.getImie());
        System.out.println(this.autor.getNazwisko());
    }
}

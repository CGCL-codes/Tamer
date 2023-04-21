package adbus.models;

import adbus.libraries.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *gluwna kalas Przewoznik
 * @author Kasia
 */
public class Przewoznik extends ORM {

    /** jednoargumentowy konstruktor klasy Przewoznik*/
    public Przewoznik(String string) {
        super();
        nazwa = string;
    }

    /**konstruktor klasy Przewoznik*/
    public Przewoznik() {
        super();
    }

    /**metoda pobierająca przewoznikow i dodająca je do listy*/
    public LinkedList<Przewoznik> pobierz() {
        LinkedList<Przewoznik> przewoznicy = new LinkedList<Przewoznik>();
        try {
            ResultSet krs = Database.querySelect(basicQuery() + getWhere(), filtersval);
            while (krs.next()) {
                Przewoznik k = new Przewoznik();
                k.zaladuj(krs);
                przewoznicy.add(k);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return przewoznicy;
    }

    public void pobierzWgId(int id) {
        try {
            zaladuj(Database.querySelect("SELECT * FROM Przewoźnicy WHERE id_przewoźnik = " + id));
        } catch (SQLException ex) {
            Logger.getLogger(Przewoznik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zaladuj(ResultSet set) {
        try {
            if (set.isBeforeFirst()) {
                set.first();
            }
            nazwa = set.getString("nazwa");
            data_dodania = set.getDate("data_dodania");
            id_przewoźnik = set.getInt("id_przewoźnik");
            adres = set.getString("adres");
            miasto = set.getString("miasto");
            telefon = set.getString("telefon");
            mail = set.getString("mail");
            ok_imie = set.getString("ok_imie");
            ok_nazwisko = set.getString("ok_nazwisko");
            ok_telefon = set.getString("ok_telefon");
            ok_mail = set.getString("ok_mail");
            uwagi = set.getString("uwagi");
        } catch (SQLException ex) {
            Logger.getLogger(Przewoznik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer id_przewoźnik = null;

    public String nazwa;

    public String nip;

    public String adres;

    public String miasto;

    public String telefon;

    public String mail;

    public Date data_dodania;

    public String ok_imie;

    public String ok_nazwisko;

    public String ok_telefon;

    public String ok_mail;

    public String uwagi;

    @Override
    public String basicQuery() {
        return "SELECT * FROM Przewoźnicy prz";
    }

    public void setAvailableFilter(String from, String to) {
        String condition = "EXISTS ( " + "SELECT p.id_pojazd  " + "FROM Pojazdy p  " + "JOIN Linie l ON p.id_linia = l.id_linia  " + "LEFT JOIN Reklamy r ON p.id_pojazd = r.id_pojazdu  " + "LEFT JOIN Kampanie k ON k.id_kampania = r.id_kampania   " + "WHERE (NOT( " + "(data_rozpoczecia <= ? AND data_zakonczenia > ?) " + "OR  (data_rozpoczecia > ? AND data_rozpoczecia <= ?)) " + "OR data_rozpoczecia IS NULL) " + "AND l.id_przewoźnik = prz.id_przewoźnik" + ")";
        filters.put("available", condition);
        filtersval.put("a1", from);
        filtersval.put("a2", from);
        filtersval.put("a3", from);
        filtersval.put("a4", to);
    }

    public void zapisz() {
        String q = null;
        if (id_przewoźnik == null) {
            q = "INSERT INTO Przewoźnicy(id_przewoźnik,nazwa,adres,miasto,telefon,mail,ok_imie,ok_nazwisko,ok_telefon,ok_mail,uwagi,data_dodania) " + "VALUES(NULL,?,?,?,?,?,?,?,?,?,?,CURDATE());";
        } else {
            q = "UPDATE Przewoźnicy SET nazwa = ?," + "adres = ?,miasto = ?,telefon = ?," + "mail=?,ok_imie=?,ok_nazwisko=?,ok_telefon=?," + "ok_mail=?,uwagi=? WHERE id_przewoźnik = ?";
        }
        LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        hm.put("1", nazwa);
        hm.put("2", adres);
        hm.put("3", miasto);
        hm.put("4", telefon);
        hm.put("5", mail);
        hm.put("6", ok_imie);
        hm.put("7", ok_nazwisko);
        hm.put("8", ok_telefon);
        hm.put("9", ok_mail);
        hm.put("10", uwagi);
        if (id_przewoźnik != null) {
            hm.put("11", id_przewoźnik);
        }
        ResultSet res = super.zapisz(q, "id_przewoźnik", hm);
        if (id_przewoźnik == null) {
            try {
                id_przewoźnik = res.getInt(1);
                System.out.println("ID Przewoźnika: " + id_przewoźnik);
            } catch (SQLException ex) {
                Logger.getLogger(Przewoznik.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setCampaignFilter(Kampania k) {
        filters.put("kampania", "id_przewoźnik in (SELECT l.id_przewoźnik FROM " + "Linie l JOIN Pojazdy p ON p.id_linia = l.id_linia " + "JOIN Reklamy r ON r.id_pojazdu = p.id_pojazd " + "WHERE r.id_kampania = ?)");
        filtersval.put("k1", k.id_kampania);
    }

    @Override
    public String toString() {
        return nazwa;
    }

    public void delete() {
        try {
            Database.queryUpdate("DELETE FROM Przewoźnicy WHERE id_przewoźnik = " + id_przewoźnik, "id_przewoźnik");
        } catch (SQLException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

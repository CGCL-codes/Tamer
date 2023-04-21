package adbus.models;

import adbus.libraries.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**głowna klasa Klient*/
public class Klient extends ORM {

    /**konstruktor klasy Klient z argumentem string*/
    Klient(String string) {
        super();
        nazwa = string;
    }

    /**konstruktor klasy Klient bezargumentowy*/
    public Klient() {
        super();
    }

    @Override
    public String basicQuery() {
        return "SELECT * FROM Klienci";
    }

    /**metoda pobierająca klientow o określonym id*/
    public void pobierzWgId(int id) {
        setIDFilter(id);
        zaladuj();
    }

    @Override
    public void zaladuj() {
        super.zaladuj();
        zaladuj(result);
    }

    /**metoda wstawiająca lub aktualizująca tabele Klienci*/
    public void zapisz() {
        String q = null;
        if (id_klient == null) {
            q = "INSERT INTO Klienci(nazwa,nip,adres,miasto,telefon,mail,ok_imie,ok_nazwisko,ok_telefon,ok_mail,uwagi,data_dodania) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,CURDATE());";
        } else {
            q = "UPDATE Klienci SET nazwa = ?," + "nip = ?,adres = ?,miasto = ?,telefon = ?," + "mail=?,ok_imie=?,ok_nazwisko=?,ok_telefon=?," + "ok_mail=?,uwagi=? WHERE id_klient = ?";
        }
        LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        hm.put("1", nazwa);
        hm.put("2", nip);
        hm.put("3", adres);
        hm.put("4", miasto);
        hm.put("5", telefon);
        hm.put("6", mail);
        hm.put("8", ok_imie);
        hm.put("9", ok_nazwisko);
        hm.put("10", ok_telefon);
        hm.put("11", ok_mail);
        hm.put("7", uwagi);
        if (id_klient != null) {
            hm.put("12", id_klient);
        }
        ResultSet res = super.zapisz(q, "id_klient", hm);
        if (id_klient == null) {
            try {
                id_klient = res.getInt(1);
                System.out.println("ID Klienta: " + id_klient);
            } catch (SQLException ex) {
                Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public LinkedList<Klient> pobierz() {
        LinkedList<Klient> klienci = new LinkedList<Klient>();
        try {
            ResultSet krs = Database.querySelect(basicQuery() + getWhere(), filtersval);
            while (krs.next()) {
                Klient k = new Klient();
                k.zaladuj(krs);
                klienci.add(k);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return klienci;
    }

    public static LinkedList<Klient> pobierzWszystkich() {
        Klient temp = new Klient();
        return temp.pobierz();
    }

    /**metoda ladująca dane z tabeli do odpowiednich zmiennych*/
    public void zaladuj(ResultSet set) {
        try {
            if (set.isBeforeFirst()) set.first();
            nazwa = set.getString("nazwa");
            data_dodania = set.getDate("data_dodania");
            id_klient = set.getInt("id_klient");
            nip = set.getString("nip");
            adres = set.getString("adres");
            miasto = set.getString("miasto");
            telefon = set.getString("telefon");
            mail = set.getString("mail");
            ok_imie = set.getString("ok_imie");
            ok_nazwisko = set.getString("ok_nazwisko");
            ok_telefon = set.getString("ok_telefon");
            ok_mail = set.getString("ok_mail");
        } catch (SQLException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po id*/
    public void setIDFilter(Integer id) {
        filters.put("id", "id_klient = ?");
        filtersval.put("id", id);
    }

    /**metoda odpowiedzialna za filtrowanie klientów po dacie dodania*/
    public void setDateFilter(String from, String to) {
        String condition = "";
        if (from.equals("") && to.equals("")) {
            filters.remove("data_dodania");
            filtersval.remove("data_dodania");
            filtersval.remove("data_dodania2");
            return;
        } else if (from.equals("")) {
            condition = "data_dodania <= ?";
            filtersval.put("data_dodania", to);
        } else if (to.equals("")) {
            condition = "data_dodania >= ?";
            filtersval.put("data_dodania", from);
        } else {
            condition = "data_dodania BETWEEN ? and ?";
            filtersval.put("data_dodania", from);
            filtersval.put("data_dodania2", to);
        }
        filters.put("data_dodania", condition);
    }

    /**metoda odpowiedzialna za filtrowanie klientów po nazwie*/
    public void setNameFilter(String name) {
        if (name.equals("")) {
            filters.remove("name");
        } else {
            filters.put("name", "nazwa LIKE ?");
            filtersval.put("name", "%" + name + "%");
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po numerze nip*/
    public void setNipFilter(String nip) {
        if (nip.equals("")) {
            filters.remove("nip");
            filtersval.remove("nip");
        } else {
            filters.put("nip", "nip = ?");
            filtersval.put("nip", nip);
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po adresie*/
    public void setAddressFilter(String address) {
        if (address.equals("")) {
            filters.remove("address");
            filtersval.remove("address");
            filtersval.remove("address2");
        } else {
            filters.put("address", "(adres LIKE ? OR miasto LIKE ?)");
            filtersval.put("address", "%" + address + "%");
            filtersval.put("address2", "%" + address + "%");
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po numerze telefonu*/
    public void setPhoneFilter(String phone) {
        if (phone.equals("")) {
            filters.remove("phone");
            filtersval.remove("phone");
        } else {
            filters.put("phone", "telefon LIKE ?");
            filtersval.put("phone", "%" + phone + "%");
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po adresie mailowym*/
    public void setMailFilter(String mail) {
        if (mail.equals("")) {
            filters.remove("mail");
            filtersval.remove("mail");
        } else {
            filters.put("mail", "mail LIKE ?");
            filtersval.put("mail", "%" + mail + "%");
        }
    }

    /**metoda odpowiedzialna za filtrowanie klientów po osobie kontaktowej*/
    public void setContactPersonFilter(String contact) {
        if (contact.equals("")) {
            filters.remove("contact");
            filtersval.remove("contact");
            filtersval.remove("contact2");
            filtersval.remove("contact3");
        } else {
            filters.put("contact", "(ok_imie LIKE ? OR ok_nazwisko LIKE ? OR ok_mail LIKE ?)");
            filtersval.put("contact", "%" + contact + "%");
            filtersval.put("contact2", "%" + contact + "%");
            filtersval.put("contact3", "%" + contact + "%");
        }
    }

    @Override
    public String toString() {
        return nazwa;
    }

    /**metoda usuwająca klienta o podanym id z tabeli Klienci*/
    public void delete() {
        try {
            Database.queryUpdate("DELETE FROM Klienci WHERE id_klient = " + id_klient, "id_klient");
        } catch (SQLException ex) {
            Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**zmienna przechowująca id klienta*/
    public Integer id_klient = null;

    /**zmienna przechowująca nazwe klienta*/
    public String nazwa;

    /**zmienna przechowująca nip klienta*/
    public String nip;

    /**zmienna przechowująca ulice i numer domu klienta*/
    public String adres;

    /**zmienna przechowująca miasto klienta*/
    public String miasto;

    /**zmienna przechowująca telefon klienta*/
    public String telefon;

    /**zmienna przechowująca mail klienta*/
    public String mail;

    /**zmienna przechowująca date dodania klienta*/
    public Date data_dodania;

    /**zmienna przechowująca imie osoby kontaktowej klienta*/
    public String ok_imie;

    /**zmienna przechowująca nazwisko osoby kontaktowej klienta*/
    public String ok_nazwisko;

    /**zmienna przechowująca numer telefonu osoby kontaktowej klienta*/
    public String ok_telefon;

    /**zmienna przechowująca mail osoby kontaktowej klienta*/
    public String ok_mail;

    /**zmienna przechowująca uwagi dotyczace klienta*/
    public String uwagi;
}

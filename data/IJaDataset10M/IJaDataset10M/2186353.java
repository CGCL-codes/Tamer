package podsustav_ekstrakcija;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

/**
 * Klasa koja služi za rad sa izvorom MySQL
 * @author Matija Novak
 */
public class Izvor_MySQL implements Ekstrakcija_I {

    private String konekcija_string;

    private String user;

    private String pass;

    private String upit;

    private String baza;

    private final String driver = "com.mysql.jdbc.Driver";

    /**
     * metoda na razini klase koja daje instancu svoje klase
     * potrebno za dinamičko učitavnje klasa
     * @param args -argumenti
     * @return vraća instancu same sebe
     */
    public static Izvor_MySQL daj_instancu(String args[]) {
        Izvor_MySQL instanca = new Izvor_MySQL();
        return instanca;
    }

    @Override
    public boolean ucitaj_parametre(String adresa, String naziv, int port, String username, String password) {
        if (port == 0) {
            konekcija_string = "jdbc:mysql://" + adresa + "/" + naziv;
        } else {
            konekcija_string = "jdbc:mysql://" + adresa + ":" + port + "/" + naziv;
        }
        user = username;
        pass = password;
        baza = naziv;
        return true;
    }

    @Override
    public Vector daj_tablice_kolone() {
        Vector podaci = new Vector();
        Izvrsavanje_upita_baza izvrsi_upit = new Izvrsavanje_upita_baza(konekcija_string, user, pass, driver);
        upit = "SELECT table_name, column_name, data_type, column_key FROM information_schema.columns WHERE table_schema='" + baza + "';";
        ResultSet rezultat = izvrsi_upit.obradi_select_upit(upit);
        try {
            while (rezultat.next()) {
                Properties red = new Properties();
                red.setProperty("tablica", rezultat.getString("table_name"));
                red.setProperty("kolona", rezultat.getString("column_name"));
                red.setProperty("tip", rezultat.getString("data_type"));
                if (rezultat.getString("column_key").contains("PRI")) {
                    red.setProperty("kljuc", "true");
                } else {
                    red.setProperty("kljuc", "false");
                }
                podaci.add(red);
            }
            izvrsi_upit.zatvori_globalnu_konekciju_select_upita();
            return podaci;
        } catch (SQLException ex) {
            System.err.println("Greška: " + ex.toString());
            izvrsi_upit.zatvori_globalnu_konekciju_select_upita();
            return null;
        }
    }

    @Override
    public Vector izvrsi_upit(String upit, Vector info) {
        Vector podaci = new Vector();
        Izvrsavanje_upita_baza izvrsi_upit = new Izvrsavanje_upita_baza(konekcija_string, user, pass, driver);
        ResultSet rezultat = izvrsi_upit.obradi_select_upit(upit);
        try {
            while (rezultat.next()) {
                Properties red = new Properties();
                for (java.util.Iterator l = info.iterator(); l.hasNext(); ) {
                    java.util.Properties red_info;
                    red_info = (java.util.Properties) l.next();
                    if (red_info.getProperty("tablica_i").contains("TRG_")) {
                        continue;
                    }
                    String podatak = "";
                    if (red_info.getProperty("tip_i").contains("int")) {
                        podatak = String.valueOf(rezultat.getInt(red_info.getProperty("kolona_i").trim()));
                    } else if (red_info.getProperty("tip_i").contains("char") || red_info.getProperty("tip_i").contains("text") || red_info.getProperty("tip_i").contains("ARRAY")) {
                        podatak = rezultat.getString(red_info.getProperty("kolona_i").trim());
                    } else if (red_info.getProperty("tip_i").contains("timestamp")) {
                        podatak = String.valueOf(rezultat.getTimestamp(red_info.getProperty("kolona_i").trim()));
                    } else if (red_info.getProperty("tip_i").contains("time")) {
                        podatak = String.valueOf(rezultat.getTime(red_info.getProperty("kolona_i").trim()));
                    } else if (red_info.getProperty("tip_i").contains("date")) {
                        podatak = String.valueOf(rezultat.getDate(red_info.getProperty("kolona_i").trim()));
                    }
                    red.setProperty(red_info.getProperty("kolona_i").trim(), podatak);
                }
                podaci.add(red);
            }
            izvrsi_upit.zatvori_globalnu_konekciju_select_upita();
            return podaci;
        } catch (SQLException ex) {
            System.err.println("Greška: " + ex.toString() + ex.getMessage() + ex.getSQLState() + ex.getStackTrace());
            izvrsi_upit.zatvori_globalnu_konekciju_select_upita();
            return null;
        }
    }
}

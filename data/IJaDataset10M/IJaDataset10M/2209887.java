package sesija;

import clanovi.Clan;
import db.Menager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student1
 */
public class Sesija {

    private ArrayList<Clan> listaClanova;

    static Sesija sesija;

    public ArrayList<Clan> getListaClanova() {
        return listaClanova;
    }

    private Sesija() {
        listaClanova = ucitajUListuIzBaze();
    }

    public static Sesija vratiInstancu() {
        if (sesija == null) sesija = new Sesija();
        return sesija;
    }

    public void ubaciClana(Clan c) {
        getListaClanova().add(c);
        Menager db = new Menager();
        try {
            db.sacuvajClavaSK(c);
        } catch (SQLException ex) {
            Logger.getLogger(Sesija.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<Clan> ucitajUListuIzBaze() {
        try {
            Menager db = new Menager();
            listaClanova = db.vratiSveClanove();
        } catch (SQLException ex) {
            Logger.getLogger(Sesija.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaClanova;
    }

    public void izmeniClana(Clan noviClan) {
        Menager db = new Menager();
        try {
            db.izmeniClanaSK(noviClan);
            listaClanova.add(noviClan);
        } catch (Exception ex) {
            Logger.getLogger(Sesija.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void izbrisiCeluBazu() {
        Menager db = new Menager();
        try {
            db.izbrisiBazuSK();
            listaClanova.removeAll(listaClanova);
        } catch (Exception ex) {
            Logger.getLogger(Sesija.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void izbrisiClana(Clan cl) {
        Menager db = new Menager();
        try {
            db.izbrisiPodatkeOOsobiSK(cl);
            listaClanova.remove(cl);
        } catch (Exception ex) {
            Logger.getLogger(Sesija.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

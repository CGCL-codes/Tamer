package Modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mathieu PASSENAUD
 * @version 0.1
 * charge le driver postgre (non testé)
 */
public class BaseDeDonneesPostgre {

    public BaseDeDonneesPostgre() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            return;
        }
        String url = Parametres.url, mdp = Parametres.motdepasse, user = Parametres.nom;
        try {
            BaseDeDonnees.con = (Connection) DriverManager.getConnection("jdbc:postgresql://" + url, user, mdp);
        } catch (SQLException ex) {
            return;
        }
    }
}

package Dao;

import Modele.Tache;
import Modele.Technicien;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Didier
 */
public class DaoTache {

    private Connexion connexion;

    public DaoTache(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     * Insère la tache tacheCritere dans la base de donnée
     * @param tacheCritere
     */
    public void creerTache(Tache tacheCritere, int idTechnicien) {
        String requete = "INSERT INTO tache (TAC_ID,TAC_TEC_ID,TAC_TYPE,TAC_DATEDEBUT,TAC_DATEFIN,TAC_PRISE_EN_COMPTE) " + "VALUES (0," + idTechnicien + ",";
        requete += (tacheCritere.getType() != null) ? "'" + tacheCritere.getType() + "'," : "'',";
        requete += (tacheCritere.getDateDebut() != null) ? "'" + this.connexion.dateToString(tacheCritere.getDateDebut()) + "'," : "'',";
        requete += (tacheCritere.getDateFin() != null) ? "'" + this.connexion.dateToString(tacheCritere.getDateFin()) + "'," : "'',";
        requete += "0);";
        System.out.println(requete);
        try {
            this.connexion.executeUpdate(requete);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Modifie la tache tacheCritere dans la base de donnée
     * Ne pas oublier de renseigner l'id de la tache
     * Pour ne pas modifier le technicien, passer un technicien null
     * @param clientCritere
     */
    public void modifierTache(Tache tacheCritere, Technicien techncien) {
        if (tacheCritere.getIdTache() != 0) {
            String requete = "UPDATE tache SET ";
            if (techncien != null) requete += "TAC_TEC_ID=" + techncien.getIdTechnicien() + ",";
            requete += "TAC_TYPE='" + tacheCritere.getType() + "'" + ", TAC_DATEDEBUT='" + this.connexion.dateToString(tacheCritere.getDateDebut()) + "'" + ", TAC_DATEFIN='" + this.connexion.dateToString(tacheCritere.getDateFin()) + "'" + ", TAC_PRISE_EN_COMPTE=" + (tacheCritere.isPriseEnCompte() ? "1" : "0");
            requete += " WHERE TAC_ID=" + tacheCritere.getIdTache() + ";";
            try {
                System.out.println(requete);
                this.connexion.executeUpdate(requete);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Retourne la liste de toutes les tâches
     * @param tacheCritere
     * @return
     */
    public List<Tache> lister() {
        String requete = "SELECT * FROM tache;";
        System.out.println(requete);
        List<Tache> liste = new ArrayList<Tache>();
        try {
            ResultSet results = this.connexion.executeRequete(requete);
            while (results.next()) {
                Tache tache = new Tache();
                tache.setIdTache(results.getInt("TAC_ID"));
                tache.setDateDebut(this.connexion.stringToDate(results.getString("TAC_DATEDEBUT")));
                tache.setDateFin(this.connexion.stringToDate(results.getString("TAC_DATEFIN")));
                tache.setType(results.getString("TAC_TYPE"));
                tache.setPriseEnCompte(results.getInt("TAC_PRISE_EN_COMPTE") == 0 ? false : true);
                liste.add(tache);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return liste;
    }

    /**
     * Retourne la liste de toutes les tâches suivant le techncien
     * @param tacheCritere
     * @return
     */
    public List<Tache> listerParTechnicien(int idTechncien) {
        String requete = "SELECT * FROM tache WHERE TAC_TEC_ID=" + idTechncien + ";";
        System.out.println(requete);
        List<Tache> liste = new ArrayList<Tache>();
        try {
            ResultSet results = this.connexion.executeRequete(requete);
            while (results.next()) {
                Tache tache = new Tache();
                tache.setIdTache(results.getInt("TAC_ID"));
                tache.setDateDebut(this.connexion.stringToDate(results.getString("TAC_DATEDEBUT")));
                tache.setDateFin(this.connexion.stringToDate(results.getString("TAC_DATEFIN")));
                tache.setType(results.getString("TAC_TYPE"));
                tache.setPriseEnCompte(results.getInt("TAC_PRISE_EN_COMPTE") == 0 ? false : true);
                liste.add(tache);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return liste;
    }

    /**
     * Supprime la tache avec l'identifiant renseigné dans tacheCritere
     * Ne pas oublier de renseigner l'identifiant
     * @param tacheCritere
     */
    public void supprimerTache(Tache tacheCritere) {
        try {
            String requete = "DELETE FROM tache WHERE TAC_ID=";
            requete += tacheCritere.getIdTache() + ";";
            System.out.println(requete);
            this.connexion.executeUpdate(requete);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

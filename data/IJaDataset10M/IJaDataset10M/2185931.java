package Dao;

import Modele.TarifHoraire;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Didier
 */
public class DaoTarifHoraire {

    private Connexion connexion;

    public DaoTarifHoraire(Connexion connexion) {
        this.connexion = connexion;
    }

    /**
     * Insère le tarif horaire dans la base de donnée
     * Retourne l'identifiant du tarif créé
     * @param tarifHoraire
     */
    public int creerTarifHoraire(TarifHoraire tarifHoraire) {
        String requete = "INSERT INTO tarifhoraire " + "(THO_ID,THO_DUREE_GRATUITE,THO_DUREE_INTER,THO_TARIF_DUREEINTER,THO_TARIF_HORAIRE,THO_ACTIF)" + " VALUES (0, ";
        requete += (tarifHoraire.getDureeGratuite() != 0) ? +tarifHoraire.getDureeGratuite() + "," : ",";
        requete += (tarifHoraire.getDureeIntermediaire() != 0) ? +tarifHoraire.getDureeIntermediaire() + "," : ",";
        requete += (tarifHoraire.getTarifDureeIntermediaire() != 0) ? +tarifHoraire.getTarifDureeIntermediaire() + "," : ",";
        requete += (tarifHoraire.getTarifHoraire() != 0) ? +tarifHoraire.getTarifHoraire() + "," : ",";
        requete += (tarifHoraire.getActif()) ? "1" : "0";
        requete += ");";
        System.out.println(requete);
        try {
            this.connexion.executeUpdate(requete);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        List<TarifHoraire> tarifs = this.lister();
        int idTarif = 0;
        for (TarifHoraire tarif : tarifs) {
            if (tarif.getActif() == tarifHoraire.getActif() && tarif.getDureeGratuite() == tarifHoraire.getDureeGratuite() && tarif.getDureeIntermediaire() == tarifHoraire.getDureeIntermediaire() && tarif.getTarifDureeIntermediaire() == tarifHoraire.getTarifDureeIntermediaire() && tarif.getTarifHoraire() == tarifHoraire.getTarifHoraire()) idTarif = tarif.getIdTarifHoraire();
        }
        return idTarif;
    }

    /**
     * Modifie le tarif horaire de la base de donnée
     * Ne pas oublier de renseigner l'id du tarif horaire
     * @param tarifHoraire
     */
    public void modifierTarifHoraire(TarifHoraire tarifHoraire) {
        if (tarifHoraire.getIdTarifHoraire() != 0) {
            String requete = "UPDATE tarifhoraire SET ";
            requete += "THO_DUREE_GRATUITE=" + tarifHoraire.getDureeGratuite() + ",";
            requete += "THO_DUREE_INTER=" + tarifHoraire.getDureeIntermediaire() + ",";
            requete += "THO_TARIF_DUREEINTER=" + tarifHoraire.getTarifDureeIntermediaire() + ",";
            requete += "THO_TARIF_HORAIRE=" + tarifHoraire.getTarifHoraire() + ",";
            requete += "THO_ACTIF=" + ((tarifHoraire.getActif()) ? "1" : "0");
            requete += " WHERE THO_ID=" + tarifHoraire.getIdTarifHoraire() + ";";
            try {
                System.out.println(requete);
                this.connexion.executeUpdate(requete);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Recherche de tarif horaire suivant l'id
     * @param tarifHoraire
     * @return
     */
    public TarifHoraire findById(TarifHoraire tarifHoraire) {
        String requete = "SELECT * FROM tarifhoraire WHERE THO_ID=" + tarifHoraire.getIdTarifHoraire() + ";";
        ResultSet results = this.connexion.executeRequete(requete);
        TarifHoraire tarifResult = new TarifHoraire();
        try {
            tarifResult.setIdTarifHoraire(results.getInt("THO_ID"));
            tarifResult.setActif(results.getInt("THO_ACTIF") == 1 ? true : false);
            tarifResult.setDureeGratuite(results.getInt("THO_DUREE_GRATUITE"));
            tarifResult.setDureeIntermediaire(results.getInt("THO_DUREE_INTER"));
            tarifResult.setTarifDureeIntermediaire(results.getFloat("THO_TARIF_DUREEINTER"));
            tarifResult.setTarifHoraire(results.getFloat("THO_TARIF_HORAIRE"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tarifResult;
    }

    /**
     * Recherche tous les tarifs horaire correspondants aux critères
     * @param userCritere
     * @return
     */
    public List<TarifHoraire> lister() {
        String requete = "SELECT * FROM tarifhoraire;";
        List<TarifHoraire> liste = new ArrayList<TarifHoraire>();
        try {
            ResultSet results = this.connexion.executeRequete(requete);
            while (results.next()) {
                TarifHoraire tarifResult = new TarifHoraire();
                tarifResult.setIdTarifHoraire(results.getInt("THO_ID"));
                tarifResult.setActif(results.getInt("THO_ACTIF") == 1 ? true : false);
                tarifResult.setDureeGratuite(results.getInt("THO_DUREE_GRATUITE"));
                tarifResult.setDureeIntermediaire(results.getInt("THO_DUREE_INTER"));
                tarifResult.setTarifDureeIntermediaire(results.getFloat("THO_TARIF_DUREEINTER"));
                tarifResult.setTarifHoraire(results.getFloat("THO_TARIF_HORAIRE"));
                liste.add(tarifResult);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return liste;
    }

    /**
     * Supprime le tarif horaire avec l'identifiant renseigné dans tarifHoraire
     * Ne pas oublier de renseigner l'identifiant
     * @param tarifHoraire
     */
    public boolean supprimerTarifHoraire(TarifHoraire tarifHoraire) {
        boolean reussite = false;
        TarifHoraire tarifHoraireActif = this.getTarifActif();
        if (tarifHoraireActif.getIdTarifHoraire() != tarifHoraire.getIdTarifHoraire()) {
            try {
                String requete = "DELETE FROM tarifhoraire WHERE THO_ID=";
                requete += tarifHoraire.getIdTarifHoraire() + ";";
                System.out.println(requete);
                this.connexion.executeUpdate(requete);
                reussite = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return reussite;
    }

    /**
     * Retourne le tarif horaire actif
     * Un seul tarif horaire est actif en même temps
     * @return
     */
    public TarifHoraire getTarifActif() {
        String requete = "SELECT * FROM tarifhoraire WHERE THO_ACTIF=1;";
        TarifHoraire tarifResult = new TarifHoraire();
        try {
            ResultSet results = this.connexion.executeRequete(requete);
            if (results.next()) {
                tarifResult.setIdTarifHoraire(results.getInt("THO_ID"));
                tarifResult.setActif(results.getInt("THO_ACTIF") == 1 ? true : false);
                tarifResult.setDureeGratuite(results.getInt("THO_DUREE_GRATUITE"));
                tarifResult.setDureeIntermediaire(results.getInt("THO_DUREE_INTER"));
                tarifResult.setTarifDureeIntermediaire(results.getFloat("THO_TARIF_DUREEINTER"));
                tarifResult.setTarifHoraire(results.getFloat("THO_TARIF_HORAIRE"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tarifResult;
    }

    public void setTarifActif(TarifHoraire tarifHoraire) {
        String disable = "UPDATE tarifhoraire SET THO_ACTIF=0 WHERE THO_ACTIF=1;";
        String enable = "UPDATE tarifhoraire SET THO_ACTIF=1 WHERE THO_ID=";
        enable += tarifHoraire.getIdTarifHoraire() + ";";
        try {
            System.out.println(disable);
            System.out.println(enable);
            this.connexion.executeUpdate(disable);
            this.connexion.executeUpdate(enable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

package campingcar.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import algutil.connexion.GestionConnexion;
import algutil.date.ConversionDate;
import campingcar.bo.Commentaire;
import campingcar.bo.Emplacement;

public class CampingCarDAOImpl {

    private static final Logger log = Logger.getLogger(CampingCarDAOImpl.class);

    private static CampingCarDAOImpl instance;

    private int nbEmplacementsAjoutes = 0;

    private int nbEmplacementsMAJ = 0;

    private int nbCommentairesAjoutes;

    public static CampingCarDAOImpl getInstance() {
        if (instance == null) instance = new CampingCarDAOImpl();
        return instance;
    }

    public void ajouter(Emplacement e) throws SQLException {
        Emplacement alreadyExists = getEmplacement(e.getNumero(), e.getTypeEmplacement(), e.getPays());
        int emplacementId;
        if (alreadyExists == null) {
            log.info("Insersion de l'emplacement : " + e);
            Connection c = GestionConnexion.getConnexionCampingCar();
            PreparedStatement p = c.prepareStatement("INSERT INTO emplacement (id, pays, num, localite, GPSN, GPSE, adresse, tarifs, type_de_borne,  eau_potable, vidange_eaux_usees, vidange_wc, v220, wc_publics, autres_services, autres_infos, type_emplacement_id, creation_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, now(), now())");
            emplacementId = getNextId("emplacement", "id");
            p.setInt(1, emplacementId);
            p.setString(2, e.getPays());
            p.setInt(3, e.getNumero());
            p.setString(4, e.getLocalite());
            p.setString(5, e.getGPSN());
            p.setString(6, e.getGPSE());
            p.setString(7, e.getAdresse());
            p.setString(8, e.getTarifs());
            p.setString(9, e.getTypeDeBorne());
            p.setString(10, e.isEauPotable() ? "Y" : "N");
            p.setString(11, e.isVidangeEauxUsees() ? "Y" : "N");
            p.setString(12, e.isVidangeWC() ? "Y" : "N");
            p.setString(13, e.isV220() ? "Y" : "N");
            p.setString(14, e.isWCPublics() ? "Y" : "N");
            p.setString(15, e.getAutresServices());
            p.setString(16, e.getAutresInfos());
            p.setInt(17, e.getTypeEmplacement());
            p.executeUpdate();
            p.close();
            nbEmplacementsAjoutes++;
        } else {
            if (alreadyExists.equals(e)) {
                log.debug("L'emplacement existe deja en BD : " + e);
            } else {
                e.setId(alreadyExists.getId());
                log.info("L'emplacement est MAJ : " + e);
                majEmplacement(e);
                nbEmplacementsMAJ++;
            }
            emplacementId = alreadyExists.getId();
        }
        for (int i = 0; i < e.getCommentaires().size(); i++) {
            ajouter(e.getCommentaires().get(i), emplacementId);
        }
    }

    public void ajouter(Commentaire c, int emplacementId) throws SQLException {
        if (!isCommentaireExists(emplacementId, ConversionDate.getAnnee4(c.getDate()) + "-" + ConversionDate.getMois(c.getDate()) + "-" + ConversionDate.getJour(c.getDate()), c.getUser())) {
            log.info("Insersion du commentaire : " + c);
            Connection con = GestionConnexion.getConnexionCampingCar();
            PreparedStatement p = con.prepareStatement("INSERT INTO commentaire (id, emplacement_id, date_post, user, msg, creation_date, last_update_date) VALUES (?, ?, ?, ?, ?, now(), now())");
            p.setInt(1, getNextId("commentaire", "id"));
            p.setInt(2, emplacementId);
            p.setString(3, ConversionDate.getAnnee4(c.getDate()) + "-" + ConversionDate.getMois(c.getDate()) + "-" + ConversionDate.getJour(c.getDate()));
            p.setString(4, c.getUser());
            p.setString(5, c.getTexte());
            p.executeUpdate();
            p.close();
            nbCommentairesAjoutes++;
        } else {
            log.debug("Le commentaire existe deja en BD : " + c);
        }
    }

    public boolean isCommentaireExists(int emplacementId, String datePost, String user) throws SQLException {
        boolean exists = false;
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("SELECT * FROM commentaire WHERE emplacement_id=? AND DATE_FORMAT( `date_post` , '%Y-%m-%d' )=? AND user=?");
        p.setInt(1, emplacementId);
        p.setString(2, datePost);
        p.setString(3, user);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            exists = true;
        }
        r.close();
        p.close();
        return exists;
    }

    public void majEmplacement(Emplacement e) throws SQLException {
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("UPDATE emplacement set localite=?, GPSN=?, GPSE=?, adresse=?, tarifs=?, type_de_borne=?,  eau_potable=?, vidange_eaux_usees=?, vidange_wc=?, v220=?, wc_publics=?, autres_services=?, autres_infos=? WHERE id=?");
        p.setString(1, e.getLocalite());
        p.setString(2, e.getGPSN());
        p.setString(3, e.getGPSE());
        p.setString(4, e.getAdresse());
        p.setString(5, e.getTarifs());
        p.setString(6, e.getTypeDeBorne());
        p.setString(7, e.isEauPotable() ? "Y" : "N");
        p.setString(8, e.isVidangeEauxUsees() ? "Y" : "N");
        p.setString(9, e.isVidangeWC() ? "Y" : "N");
        p.setString(10, e.isV220() ? "Y" : "N");
        p.setString(11, e.isWCPublics() ? "Y" : "N");
        p.setString(12, e.getAutresServices());
        p.setString(13, e.getAutresInfos());
        p.setInt(14, e.getId());
        p.executeUpdate();
        p.close();
    }

    public Emplacement getEmplacement(int num, int type, String pays) throws SQLException {
        Emplacement e = null;
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("SELECT * FROM emplacement WHERE num=? AND type_emplacement_id=? AND pays=?");
        p.setInt(1, num);
        p.setInt(2, type);
        p.setString(3, pays);
        ResultSet r = p.executeQuery();
        if (r.next()) {
            e = new Emplacement(r);
        }
        r.close();
        p.close();
        return e;
    }

    public int getNextId(String tableName, String idName) throws SQLException {
        int id = 1;
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("SELECT max(" + idName + ")+1 FROM " + tableName);
        ResultSet r = p.executeQuery();
        if (r.next()) id = r.getInt(1);
        r.close();
        p.close();
        return id;
    }

    public void ajouterRecupHisto(String pays, String url) throws SQLException {
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("INSERT INTO recup_histo (pays, url, nb_emplacements_ajoutes, nb_emplacements_maj, nb_commentaires_ajoutes, creation_date) VALUES (?, ?, ?, ?, ?, now())");
        p.setString(1, pays);
        p.setString(2, url);
        p.setInt(3, nbEmplacementsAjoutes);
        p.setInt(4, nbEmplacementsMAJ);
        p.setInt(5, nbCommentairesAjoutes);
        p.executeUpdate();
        p.close();
        log.info("Nombre d'emplacements ajoutes  : " + nbEmplacementsAjoutes);
        log.info("Nombre d'emplacements maj      : " + nbEmplacementsMAJ);
        log.info("Nombre de commentaires ajoutes : " + nbCommentairesAjoutes);
    }

    public List<String> getURLs(String lib) throws SQLException {
        List<String> urls = new ArrayList<String>();
        Connection c = GestionConnexion.getConnexionCampingCar();
        PreparedStatement p = c.prepareStatement("SELECT * FROM url WHERE lib like ?");
        p.setString(1, lib);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            urls.add(r.getString("url"));
        }
        r.close();
        p.close();
        return urls;
    }

    public void RAZCompteur() {
        nbEmplacementsAjoutes = 0;
        nbEmplacementsMAJ = 0;
        nbCommentairesAjoutes = 0;
    }
}

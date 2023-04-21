package mposms.BO.UtilisateurBO;

import mposms.BO.ObjectBO;
import mposms.DAO.UtilisateurTO.R_AssocieTO;

/**
 * @author de Landsheer Thierry
 *
 */
public class R_AssocieBO implements ObjectBO {

    /**
	 * repr�sennte le numero d'identification du profil
	 */
    private int idProfil;

    /**
	 * repr�sente le numero d'identification de la fonction
	 */
    private int idFonction;

    /**
	 * mode lecture?
	 */
    private boolean lecture;

    /**
	 * mode creation?
	 */
    private boolean creation;

    /**
	 * mode modification?
	 */
    private boolean modification;

    /**
	 * mode suppression?
	 */
    private boolean suppression;

    /**
	 * @return retourne l'identifiant de la fonction
	 */
    public int getIdFonction() {
        return idFonction;
    }

    /**
	 * @return retourne l'identifiant du profil utilisateur
	 */
    public int getIdProfil() {
        return idProfil;
    }

    /**
	 * @return retourne un boolean
	 */
    public boolean isCreation() {
        return creation;
    }

    /**
	 * @return retourne un boolean
	 */
    public boolean isLecture() {
        return lecture;
    }

    /**
	 * @return retourne un boolean
	 */
    public boolean isModification() {
        return modification;
    }

    /**
	 * @return retourne un boolean
	 */
    public boolean isSuppression() {
        return suppression;
    }

    /**
	 * @param idFonction est la valeur � enregistrer
	 */
    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    /**
	 * @param idProfil est la valeur � enregistrer
	 */
    public void setIdProfil(int idProfil) {
        this.idProfil = idProfil;
    }

    /**
	 * @param creation est la valeur � enregistrer
	 */
    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    /**
	 * @param lecture est la valeur � enregistrer
	 */
    public void setLecture(boolean lecture) {
        this.lecture = lecture;
    }

    /**
	 * @param modification The modification to set.
	 */
    public void setModification(boolean modification) {
        this.modification = modification;
    }

    /**
	 * @param suppression The suppression to set.
	 */
    public void setSuppression(boolean suppression) {
        this.suppression = suppression;
    }

    public Object parseBO(Object objectTO) {
        setIdFonction(((R_AssocieTO) objectTO).getIdFonction());
        setIdProfil(((R_AssocieTO) objectTO).getIdProfil());
        setLecture(((R_AssocieTO) objectTO).isLecture());
        setCreation(((R_AssocieTO) objectTO).isCreation());
        setModification(((R_AssocieTO) objectTO).isModification());
        setSuppression(((R_AssocieTO) objectTO).isSuppression());
        return null;
    }

    /**
	 * @param choixDroits
	 */
    public void setDroits(boolean[] choixDroits) {
        setLecture(choixDroits[0]);
        setCreation(choixDroits[1]);
        setModification(choixDroits[2]);
        setSuppression(choixDroits[3]);
    }

    /**
	 * @return un tableau de boolean
	 */
    public boolean[] getDroits() {
        boolean[] droits = new boolean[4];
        droits[0] = lecture;
        droits[1] = creation;
        droits[2] = modification;
        droits[3] = suppression;
        return droits;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id profil = " + idProfil + "\t");
        sb.append("id Fonction = " + idFonction + "\t");
        sb.append("le(s) mode(s) possible(s) pour ce profil :\n");
        sb.append((isLecture() ? "-- lecture\n" : ""));
        sb.append((isCreation() ? "-- creation\n" : ""));
        sb.append((isModification() ? "-- modification\n" : ""));
        sb.append((isSuppression() ? "--  suppression\n" : ""));
        return sb.toString();
    }
}

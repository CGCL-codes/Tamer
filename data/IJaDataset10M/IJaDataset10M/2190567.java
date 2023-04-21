package mposms.BO.Four_MagBO;

import java.sql.Date;
import mposms.BO.ObjectBO;
import mposms.DAO.Four_MagTO.R_OperationEntrepriseTO;

/**
 * @author de Landsheer Thierry
 *
 */
public class R_OperationEntrepriseBO implements ObjectBO {

    /**
	 * repr�sente le num�ro d'identification du magasin
	 */
    private int idMagasin;

    /**
	 * repr�sente le num�ro d'identification de l'entreprise
	 */
    private int idEntreprise;

    /**
	 * repr�sente le num�ro d'identification du fournisseur
	 */
    private int idFournisseur;

    /**
	 * repr�sente le num�ro d'identification de l'operation de l'entreprise
	 */
    private int idOperationEntreprise;

    /**
	 * repr�sente la date de l'op�ration
	 */
    private Date dateOperation;

    /**
	 * @return Retourne dateOperation.
	 */
    public Date getDateOperation() {
        return dateOperation;
    }

    /**
	 * @return Retourne idEntreprise.
	 */
    public int getIdEntreprise() {
        return idEntreprise;
    }

    /**
	 * @return Retourne idFournisseur.
	 */
    public int getIdFournisseur() {
        return idFournisseur;
    }

    /**
	 * @return Retourne idMagasin.
	 */
    public int getIdMagasin() {
        return idMagasin;
    }

    /**
	 * @return Retourne idOperationEntreprise.
	 */
    public int getIdOperationEntreprise() {
        return idOperationEntreprise;
    }

    /**
	 * @param dateOperation Est la valeur dateOperation � enregistrer.
	 */
    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    /**
	 * @param idEntreprise Est la valeur idEntreprise � enregistrer.
	 */
    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    /**
	 * @param idFournisseur Est la valeur idFournisseur � enregistrer.
	 */
    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    /**
	 * @param idMagasin Est la valeur idMagasin � enregistrer.
	 */
    public void setIdMagasin(int idMagasin) {
        this.idMagasin = idMagasin;
    }

    /**
	 * @param idOperationEntreprise Est la valeur idOperationEntreprise � enregistrer.
	 */
    public void setIdOperationEntreprise(int idOperationEntreprise) {
        this.idOperationEntreprise = idOperationEntreprise;
    }

    public Object parseBO(Object objectTO) {
        setIdOperationEntreprise(((R_OperationEntrepriseTO) objectTO).getIdOperationEntreprise());
        setIdFournisseur(((R_OperationEntrepriseTO) objectTO).getIdFournisseur());
        setDateOperation(((R_OperationEntrepriseTO) objectTO).getDateOperation());
        setIdMagasin(((R_OperationEntrepriseTO) objectTO).getIdMagasin());
        setIdEntreprise(((R_OperationEntrepriseTO) objectTO).getIdEntreprise());
        return null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id magasin = " + idMagasin + "\t");
        sb.append("id entreprise = " + idEntreprise + "\t");
        sb.append("id fournisseur = " + idFournisseur + "\t");
        sb.append("date de op�ration = " + dateOperation + "\n");
        sb.append("id de l'op�ration = " + idOperationEntreprise + "\n");
        return sb.toString();
    }
}

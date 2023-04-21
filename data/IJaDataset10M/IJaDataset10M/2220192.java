package atg.metier.dao.jdo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.jdo.JDOException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.spi.PersistenceCapable;
import atg.metier.dao.ATGDao;
import atg.metier.dao.exception.ATGDaoAccessSupportException;
import atg.metier.dao.exception.ATGDaoDataNotFoundException;
import atg.metier.dao.jdbc.exception.ATGDaoBaseIndisponibleException;
import atg.metier.entite.ATGIEntite;
import atg.service.constante.AtgConstantes;
import atg.service.log.AtgLogManager;
import atg.util.service.identifiant.ATGCritereValue;
import atg.util.service.identifiant.ATGListCritereValue;
import atg.util.service.list.ATGIListEntity;
import atg.util.service.list.ATGListEntity;
import atg.util.service.list.ATGListException;
import com.versant.core.jdo.VersantPersistenceManager;

public class ATGDaoJdo<K extends ATGIEntite> extends ATGDao<K> {

    /**
	 * r�f�rence le type de connexion (permet de g�rer plusieurs connexion
	 * diff�rente
	 */
    protected String reference = "versant";

    /**
	 * Classe sur laquelle on va effectuer la recherche
	 */
    protected Class laClasse = null;

    /**
	 * Op�rateur : Egalit�
	 */
    private static String OPERATION_EGAL = " == ";

    /**
	 * Op�rateur : Sup�rieur strict
	 */
    private static String OPERATION_SUPERIEUR_STRICT = " > ";

    /**
	 * Op�rateur : Sup�rieur ou �gal
	 */
    private static String OPERATION_SUPERIEUR_OU_EGAL = " >= ";

    /**
	 * Op�rateur : Inf�rieur strict
	 */
    public static String OPERATION_INFERIEUR_STRICT = " < ";

    /**
	 * Op�rateur : Inf�rieur ou �gal
	 */
    public static String OPERATION_INFERIEUR_OU_EGAL = " >= ";

    /**
	 * Op�rateur : Inf�rieur ou �gal
	 */
    public static String OPERATION_COMME = ".sql(\" $1 like ";

    private PersistenceManager lePm = null;

    public ATGDaoJdo(Class uneClasse) {
        this.laClasse = uneClasse;
    }

    /**
	 * Retourne une instance de Persistance Manager provenant du pool .
	 * 
	 * @return javax.jdo.PersistenceMangager
	 */
    public javax.jdo.PersistenceManager getConnection() throws ATGDaoBaseIndisponibleException {
        try {
            if (this.lePm == null) {
                this.lePm = ATGDaoJdoPoolDataSource.getInstance(reference).getPersistenceManager();
                this.lePm.currentTransaction().setNontransactionalWrite(true);
                this.lePm.currentTransaction().setNontransactionalRead(true);
            }
            return lePm;
        } catch (JDOException ex) {
            this.libererRessource();
            logSevere("Impossible de r�cup�rer un gestionnaire de persistance.");
            throw new ATGDaoBaseIndisponibleException("Impossible de r�cup�rer un gestionnaire de persistance : " + ex.getMessage());
        }
    }

    private String getCritere(String critere) {
        if (critere.equals(ATGCritereValue.OPERATION_EGAL)) return OPERATION_EGAL;
        if (critere.equals(ATGCritereValue.OPERATION_SUPERIEUR_OU_EGAL)) return OPERATION_SUPERIEUR_OU_EGAL;
        if (critere.equals(ATGCritereValue.OPERATION_SUPERIEUR_STRICT)) return OPERATION_SUPERIEUR_STRICT;
        if (critere.equals(ATGCritereValue.OPERATION_INFERIEUR_OU_EGAL)) return OPERATION_INFERIEUR_OU_EGAL;
        if (critere.equals(ATGCritereValue.OPERATION_INFERIEUR_STRICT)) return OPERATION_INFERIEUR_STRICT;
        if (critere.equals(ATGCritereValue.OPERATION_COMME)) return OPERATION_COMME;
        return null;
    }

    /**
	 * Ex�cute une requete
	 */
    public ATGIListEntity<K> executeRequete(ATGListCritereValue listeCritere, int niveau) throws ATGDaoBaseIndisponibleException, ATGDaoAccessSupportException, ATGDaoDataNotFoundException {
        try {
            String requete;
            ATGCritereValue critere;
            ATGCritereValue critereSuivant;
            List listeTriee = listeCritere.getSortedListByKey();
            String courant = "";
            String nomVar = "";
            String temp = "";
            String classeCritere = null;
            ATGListEntity laListe = new ATGListEntity();
            Query query = this.getConnection().newQuery(this.laClasse);
            requete = "";
            if (listeTriee.size() > 0) {
                requete = "(";
                for (int i = 0; i < listeTriee.size(); i++) {
                    critere = (ATGCritereValue) listeTriee.get(i);
                    classeCritere = critere.getClasse().toString().substring(critere.getClasse().toString().lastIndexOf(".") + 1);
                    logFinest("Class : " + classeCritere);
                    if (critere.getClasse().equals(laClasse)) {
                        nomVar = "";
                    } else {
                        temp = classeCritere.toLowerCase();
                        if (nomVar.compareTo(temp + ".") != 0) {
                            logFinest("Ajout d'une variable dans la requete : " + classeCritere + " " + temp);
                            query.declareVariables(classeCritere + " " + temp);
                        }
                        nomVar = temp + ".";
                    }
                    courant = nomVar + critere.getAttribut() + this.getCritere(critere.getOperation()) + "\'" + critere.getStringValue() + "\'";
                    if (this.getCritere(critere.getOperation()) == OPERATION_COMME) courant = courant + "\")";
                    logFinest("Crit�re : " + courant);
                    requete += courant;
                    if (i != (listeTriee.size() - 1)) {
                        critereSuivant = (ATGCritereValue) listeTriee.get(i + 1);
                        if (critere.getKey().equals(critereSuivant.getKey())) requete += " || "; else requete += ") && (";
                    }
                }
                requete += ")";
            }
            logFinest("param�tres g�n�r�s : " + requete);
            if (requete != null && requete.length() > 0) query.setFilter(requete);
            Collection resultat = (Collection) query.execute();
            logFinest("On d�tache la collection du gestionnaire de persistance, nivea : " + getFetchGroupByNiveau(niveau));
            ((VersantPersistenceManager) this.getConnection()).versantDetachCopy(resultat, getFetchGroupByNiveau(niveau));
            Vector uneListe = new Vector(resultat);
            logFinest("Nb de resultats correspondant � la requete : " + uneListe.size());
            query.closeAll();
            if (uneListe.size() == 0) {
                logFinest("Aucune donn�es trouv�e.");
                libererRessource();
                throw new ATGDaoDataNotFoundException("Aucune donn�e trouv�e.");
            }
            try {
                laListe.setListeElement(uneListe);
            } catch (ATGListException e) {
                logSevere("Erreur lors de la gestion de la liste de r�sultat : " + e.getMessage());
                throw new ATGDaoAccessSupportException("Impossible de charger l'ATGList");
            }
            libererRessource();
            return laListe;
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public void delete(K entite) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        try {
            logFinest("Suppression d'une entite en JDO (" + entite + ").");
            logFinest("L'identifiant jdo de l'objet : " + ((PersistenceCapable) entite).jdoGetObjectId());
            PersistenceManager pm = this.getConnection();
            if (!pm.currentTransaction().isActive()) pm.currentTransaction().begin();
            logFinest("On r�cup�re l'entite dans le gestionnaire de persistance, par son Id : ");
            Object lObj = pm.getObjectById(((PersistenceCapable) entite).jdoGetObjectId(), true);
            logFinest("Une fois r�cup�r� on le supprime.");
            pm.deletePersistent(lObj);
            pm.currentTransaction().commit();
            this.libererRessource();
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public void delete(Serializable identifiant) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        try {
            logFinest("Suppression d'une entite en JDO depuis son identifiant (" + identifiant + ").");
            PersistenceManager pm = this.getConnection();
            pm.currentTransaction().begin();
            Object lObj = pm.getObjectById(identifiant, true);
            pm.deletePersistent(lObj);
            pm.currentTransaction().commit();
            this.libererRessource();
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public void insert(K valueObject) throws ATGDaoAccessSupportException {
        try {
            logFinest("Insertion d'une entit� en JDO (" + valueObject + ").");
            if (lePm == null) lePm = this.getConnection();
            lePm.currentTransaction().begin();
            lePm.makePersistent(valueObject);
            lePm.currentTransaction().commit();
            this.libererRessource();
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public ATGIListEntity<K> selectAll() throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        try {
            logFinest("Select all en JDO pour la classe " + this.laClasse);
            logFinest("Un selectAll sans niveau, on met un niveau 0, qui correspont on niveau par d�faut");
            return this.executeRequete(new ATGListCritereValue(), 0);
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public ATGIListEntity<K> selectByCritere(ATGListCritereValue critere, int niveau) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        return this.executeRequete(critere, niveau);
    }

    public ATGIListEntity<K> selectByCritere(ATGListCritereValue critere) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        logFinest("Un select By Critere sans niveau, on met un niveau 0, qui correspont on niveau par d�faut");
        return this.executeRequete(critere, 0);
    }

    private static String getFetchGroupByNiveau(int niveau) {
        switch(niveau) {
            case NIVEAU_IDENTIFIANT:
                return "NIVEAU_INDENTIFIANT";
            case NIVEAU_ATTRIBUTS:
                return "NIVEAU_ATTRIBUTS";
            case NIVEAU_ATTRIBUTS_VO:
                return "NIVEAU_ATTRIBUTS_VO";
            default:
                return null;
        }
    }

    public K selectByIdentifiant(Serializable identifiant, int niveau) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        try {
            K obj = (K) this.getConnection().getObjectById(identifiant, true);
            Vector laCollect = new Vector();
            laCollect.add(obj);
            logFinest("on d�tache une copie de l'objet");
            logFinest("on utilise le fetch correspondant au niveau : " + niveau + " : " + getFetchGroupByNiveau(niveau));
            Collection detachees = ((VersantPersistenceManager) this.getConnection()).versantDetachCopy(laCollect, getFetchGroupByNiveau(niveau));
            obj = (K) detachees.toArray()[0];
            this.libererRessource();
            return obj;
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public K selectByIdentifiant(Serializable identifiant) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        return this.selectByIdentifiant(identifiant, NIVEAU_ATTRIBUTS_VO);
    }

    public void update(ATGIEntite valueObject) throws ATGDaoDataNotFoundException, ATGDaoAccessSupportException {
        try {
            this.getConnection().currentTransaction().begin();
            Vector leVect = new Vector();
            leVect.add(valueObject);
            ((VersantPersistenceManager) this.getConnection()).versantAttachCopy(leVect, true);
            this.getConnection().currentTransaction().commit();
            this.libererRessource();
        } catch (JDOObjectNotFoundException e) {
            this.libererRessource();
            logFinest("Aucune donn�es trouv�es : " + e.getMessage());
            throw new ATGDaoDataNotFoundException("Aucune donn�es trouv�es : " + e.getMessage());
        } catch (JDOException ex) {
            this.libererRessource();
            throw new ATGDaoAccessSupportException("Erreur d'access aux donn�es : " + ex.getMessage());
        }
    }

    public int countByCritere(ATGListCritereValue critere) throws ATGDaoAccessSupportException {
        try {
            ATGIListEntity liste = this.executeRequete(critere, 0);
            return liste.getTotalEnregistrement();
        } catch (ATGDaoDataNotFoundException e) {
            return 0;
        } catch (ATGListException e) {
            logSevere("Erreur de parcours de la liste de r�sultat !");
            throw new ATGDaoAccessSupportException("Erreur de parcours de la liste de r�sultat !");
        }
    }

    /**
	 * Ferme le resultSet, le statement et la connexion en cours d'utilisation
	 */
    protected void libererRessource() {
        if (lePm != null) {
            ATGDaoJdoPoolDataSource.getInstance(reference).release(lePm);
            lePm = null;
        }
    }

    /**
	 * @return Renvoie reference.
	 */
    public String getReference() {
        return reference;
    }

    /**
	 * @param reference
	 *            reference � d�finir.
	 */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
	 * r�f�rence vers le log
	 */
    protected static java.util.logging.Logger logger_ = null;

    /**
	 * Retourne le log associ�
	 * 
	 * @return java.util.logging.Logger Trace associ�e
	 */
    protected java.util.logging.Logger getLogger() {
        if (logger_ == null) logger_ = AtgLogManager.getLog(AtgConstantes.ATG_LOG_CATEGORY_METIER_DAO_JDO);
        return logger_;
    }
}

package net.sf.ideoreport.datamanagers.sqlmanager.config;

/**
 * Repr�sente la configuration d'un composant SQLManager. ; la configuration d�finit un
 * ensemble de requ�tes SQL (identifi�es � l'aide d'un ID), chacune pouvant �tre associ�e
 * � un ensemble de param�tres de filtre. La requ�te et la structure des param�ters de 
 * filtre sont d�finis de mani�re statique ; <b>le programme appelant utilise ces interfaces
 * pour conna�tre les param�tres attendus, en aucun cas leur valeur</b>. Les connexions aux
 * diff�rentes bases de donn�es sont �galement d�crites dans le fichier de configuration. Pour
 * chaque connexion, on fournit les param�tres n�cessaires pour la connexion, plus le param�trage
 * de la datasource.
 * 
 * <p>Une configuration est constitu�e des �l�ments suivants :
 * <ol>
 *      <li>Des param�tres g�n�raux.
 *      <li>Des d�finitions de connexions.
 *      <li>Des d�finitions de filtre g�n�raux.
 *      <li>Des d�finitions de requ�tes. Chaque d�finition de requ�te est constitu�e de :
 *          <ol>
 *              <li>un identifiant
 *              <li>une requ�te SQL, ou plut�t un squelette de requ�te SQL
 *              <li>�ventuellement une d�finition de filtre. Une d�finition de filtre est
 *                  compos�e de d�finitions de param�tres. Ces param�tres seront utilis�s
 *                  pour injecter dans la requ�tes des clauses ad�quates.</li>
 *          </ol>
 *      </li>
 * </ol>
 * </p>
 * 
 * @author jbeausseron 
 */
public interface ISQLManagerConfig {

    /**
     * Renvoie un groupe de param�tres identifi�s par un ID donn�.
     * @param pId l'identifiant du groupe de param�tres.
     * @return le groupe de param�tre ou null si non trouv�.
     */
    public ISQLParametersConfig getSQLParametersConfig(String pId);

    /**
     * Ajoute un groupe de param�tres � la configuration courante.
     * @param pConfig groupe de param�tre.
     */
    public void addSQLParametersConfig(ISQLParametersConfig pConfig);

    /**
     * R�cup�re la configuration associ�e � une requ�te
     * @param pQueryId identifiant de la requ�te.
     * @return la configuration associ�e ou null si non trouv�e.
     */
    public ISQLQueryConfig getSQLQueryConfig(String pQueryId);

    /**
     * R�cup�re la configuration associ�e � une connexion.
     *
     * @param pConnectionId Identifiant de la connexion.
     * @return la configuration associ�e.
     */
    ISQLConnectionConfig getSQLConnection(String pConnectionId);

    /**
     * Renvoie la configuration de la connexion par d�faut ou null
     * s'il n'y en a pas.
     *
     * @return une configuration de connexion.
     */
    ISQLConnectionConfig getDefaultSQLConnection();

    /**
     * Ajoute un param�tre g�n�ral.
     * @param pParam le param�tre � ajouter.
     */
    public void addParameter(ISQLParameterConfig pParam);

    /**
     * Ajoute une configuration de requ�te SQL.
     * @param pQueryConfig la configuration � ajouter.
     */
    public void addSQLQueryConfig(ISQLQueryConfig pQueryConfig);

    /**
     * Ajoute une configuration de connexion SQL.
     *
     * @param pConnectionConfig la configuration � ajouter.
     */
    void addSQLConnection(ISQLConnectionConfig pConnectionConfig);

    /**
     * Renvoie la valeur d'un param�tre commun de configuration.
     * @param pName nom du param�tre
     * @return la valeur du param�tre, ou "" si non trouv�.
     */
    public abstract String getParameter(String pName);
}

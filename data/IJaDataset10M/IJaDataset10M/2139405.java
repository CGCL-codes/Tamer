package org.hironico.database.driver;

import java.util.Properties;
import javax.sql.ConnectionPoolDataSource;

/**
 * Cette interface définit comment les intégrations de driver JDBC devront fabriquer des objets PooledConnection
 * à fournir dans l'implémentation de l'objet ConnectionPool. Cela permet d'établir un standard concernant les objets
 * de connexion manipulés et ainsi de profiter des améliorations apportées aux implémentation JDBC standard, comme
 * par exmeple la possibilité de récupérer le code source des procédures stockées ou bien d'utiliser le systéme de cache
 * sur les queries et les résultats produits. De plus étant donné que cette interface étend ConnectionPoolDataSource,
 * il est alors possible de l'utiliser dans un serveur d'application compatible pour établir des pool de connexions.
 * @author hironico
 * @since 0.0.1
 * @version $Rev: 1.1 $
 * @see org.hironico.database.driver.ConnectionPool
 * @see org.hironico.database.driver.PooledConnection
 */
public interface PooledConnectionFactory extends ConnectionPoolDataSource {

    /**
     * 
     * @return le nom de login à utiliser pour la connexion
     */
    public String getUser();

    /**
     * Permet de définir le nom de login à utiliser pour  la connexion
     * @param user nom de login
     */
    public void setUser(String user);

    /**
     * 
     * @return le mot de passe à utiliser associé à la property user.
     */
    public String getPassword();

    /**
     * Permet de définir le mot de passe à utiliser associé à la property user.
     * @param password mot de passe associé à la property user
     */
    public void setPassword(String password);

    /**
     * 
     * @return le nom de la base de données à la quelle il faut sze connecter.
     */
    public String getDatabase();

    /**
     * Permet de définir le nom de la base de donéees à laquelle se connecter.
     * @param database nom de la base de données
     */
    public void setDatabase(String database);

    /**
     * 
     * @return nom de la classe du driver JDBC à utiliser.
     */
    public String getDriverClass();

    /**
     * Permet de définir le nom de la classe du driver JDBC qu'il faut utiliser pour se connecter.
     * @param driverClass nom de classe de driver JDBC (doit etre dans le classpath).
     */
    public void setDriverClass(String driverClass);

    /**
     * Permet de récupérer un nom de classe par défaut pour un type de connection factory donné.
     * Cette méthode peut retourner null s'il n'existe pas de driver par défaut. Cependant il et fortement recommandé
     * d'implémenter cette méthode.
     * @return nom du driver JDBC par défaut pour cette ConnectionFactory
     */
    public String getDefaultDriverClass();

    /**
     * Permet d'associer des Properties à cette connection factory
     * @return Properties comportant toute propriété utile à la connexion.
     */
    public Properties getConnectionProperties();

    /**
     * Permet de définir toute propriété utile à la connexion
     * @param connectionProperties Properties à utiliser avec le driver.
     */
    public void setConnectionProperties(Properties connectionProperties);

    /**
     * 
     * @return l'URL de connexion spéciifique au driver.
     */
    public String getUrl();

    /**
     * Le texte des procédures stockées peut etre récupéré en lisant dans les tables de méta data de chaque SGBD.
     * Aussi, chaque SGBD posséde une query spécifique pour cela. 
     * @return le texte de la query SQL qui va lire dans les meta data le texte des procédures stockées
     */
    public String getStoredProcedureTextSQLQuery();

    /**
     * Permet de définir la query SQL spéciifque permettant de lire les méta données décrivant le texte d'une
     * procédure stockée.
     * @param storedProcedureTextSQLQuery le SQL pour lire le texte des procédures stockées.
     */
    public void setStoredProcedureTextSQLQuery(String storedProcedureTextSQLQuery);

    /**
     * @see #getStoredProcedureTextSQLQuery()
     * @return le code SQL spécifique pour lire le texte ayant servi à la création de vues dans le SGBD.
     */
    public String getViewTextSQLQuery();

    /**
     * Permet de définir la query SQL servant à récupérer le texte ayant servi à la création d'une vue dans le SGBD.
     * @see #getStoredProcedureTextSQLQuery()
     * @param viewTextSQLQuery la query SQL pour récupérer le texte de création des vues.
     */
    public void setViewTextSQLQuery(String viewTextSQLQuery);

    /**
     * 
     * @return la query lancée à chaque ouverture de connexion
     * @since 0.0.9
     */
    public String getAutoExecSQLQuery();

    /**
     * Permet de définir la query à exécuter à chaque ouverture de connexion.
     * @param autoExecSQLQuery query à lancer à l'ouverture de la connexion.
     * @since 0.0.9
     */
    public void setAutoExecSQLQuery(String autoExecSQLQuery);
}

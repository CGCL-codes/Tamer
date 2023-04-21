package fr.gta10.etape1.prototype.persistance.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import fr.gta10.etape1.prototype.exception.PersistanceException;
import fr.gta10.etape1.prototype.metier.Event;
import fr.gta10.etape1.prototype.metier.EventMail;
import fr.gta10.etape1.prototype.metier.Role;
import fr.gta10.etape1.prototype.metier.SimpleUser;
import fr.gta10.etape1.prototype.metier.User;
import fr.gta10.etape1.prototype.persistance.IDAO;

/**
 * Fabrique capable de g�n�rer des accesseurs � la couche persistance <br/>
 * d�di� � l'application MySql
 * 
 * @author COTTET julien
 * @version 0.0.2
 */
public class DAOJdbc implements IDAO {

    private final String DRIVER;

    private final String DBURL;

    private static final Logger logger = Logger.getLogger(DAOJdbc.class);

    private ResourceBundle sqlBundle = null;

    /**
	 * Initialise la factory directement avec les bons param�tres � partir du
	 * fichier properties ad�quat
	 * 
	 * Cela permet de rendre g�n�rique cette classe (autre driver, autre base,
	 * autre administrateur, etc...).
	 * 
	 * @param _sqlBundle
	 *            le fichier de propri�t� (liste des requ�tes et driver)
	 */
    public DAOJdbc(ResourceBundle _sqlBundle) {
        this.sqlBundle = _sqlBundle;
        DRIVER = sqlBundle.getString("driver");
        DBURL = sqlBundle.getString("dbUrl");
    }

    /**
	 * R�alise une connection avec la base de donn�es
	 * 
	 * @return une connection utilisable
	 * @throws PersistanceException
	 */
    public Connection createConnection() throws PersistanceException {
        Connection conn = null;
        try {
            Class.forName(DRIVER).newInstance();
            conn = java.sql.DriverManager.getConnection(DBURL, sqlBundle.getString("user"), sqlBundle.getString("password"));
        } catch (SQLException e) {
            logger.debug(e.getMessage() + "DAO MySql" + "Cr�ation de connection � la base de donn�e" + DBURL);
            throw new PersistanceException("Connection � : " + DBURL);
        } catch (ClassNotFoundException e) {
            logger.debug(e.getMessage() + "DAO MySql" + "Cr�ation de connection � la base de donn�e" + DBURL);
            throw new PersistanceException("Connection � : " + DBURL);
        } catch (IllegalAccessException e) {
            logger.debug(e.getMessage() + "DAO MySql" + "Cr�ation de connection � la base de donn�e" + DBURL);
            throw new PersistanceException("Connection � : " + DBURL);
        } catch (InstantiationException e) {
            logger.debug(e.getMessage() + "DAO MySql" + "Cr�ation de connection � la base de donn�e" + DBURL);
            throw new PersistanceException("Connection � : " + DBURL);
        }
        return conn;
    }

    /**
	 * Ferme la connexion silencieusement.
	 * 
	 * @param connection -
	 *            connexion � fermer
	 */
    public void close(final Connection connection) throws PersistanceException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.debug(e.getMessage() + "DAO MySql" + "Fermeture de la connection � la base de donn�e" + DBURL);
                throw new PersistanceException("Fermeture de la connection � : " + DBURL);
            }
        }
    }

    /**
	 * Ferme le statement silencieusement.
	 * 
	 * @param statement -
	 *            statement � fermer
	 */
    public void close(final PreparedStatement statement) throws PersistanceException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.debug(e.getMessage() + "DAO MySql" + "Fermeture du statement � la base de donn�e" + DBURL);
                throw new PersistanceException("fermeture du statement" + DBURL);
            }
        }
    }

    /**
	 * Ferme le resultSet silencieusement.
	 * 
	 * @param resultSet -
	 *            resultSet � fermer
	 */
    public void close(final ResultSet resultSet) throws PersistanceException {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.debug(e.getMessage() + "DAO MySql" + "Cr�ation de connection � la base de donn�e" + DBURL);
                throw new PersistanceException("fermeture du Resultset" + DBURL);
            }
        }
    }

    /**
	 * V�rifie qu'il y a bien l'utilisateur demand� dans la base Attention cela
	 * ne v�rifie que l'id
	 */
    public boolean isUserPresent(final User user) {
        boolean retour = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String trigramme = user.getTrigramme();
        String requete = this.sqlBundle.getString("isUserPresent");
        try {
            conn = createConnection();
            stmt = conn.prepareStatement(requete);
            stmt.setString(1, trigramme);
            rs = stmt.executeQuery();
            if (rs.isAfterLast()) {
                retour = false;
            } else {
                retour = true;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } catch (PersistanceException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                close(conn);
                close(stmt);
                close(rs);
            } catch (PersistanceException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return retour;
    }

    /**
	 * Insert un utilisateur en base
	 */
    public void insertUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String requeteInsertUser = this.sqlBundle.getString("insertUser");
        try {
            conn = createConnection();
            stmt = conn.prepareStatement(requeteInsertUser);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getTrigramme());
            stmt.setString(4, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } catch (PersistanceException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                close(conn);
                close(stmt);
            } catch (PersistanceException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    public List<User> listUser(User reference) {
        List<User> userList = new ArrayList<User>();
        User userTmp = new SimpleUser();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String requete;
        String pattern = reference.getTrigramme();
        try {
            conn = createConnection();
            if (pattern.equals("*")) {
                requete = sqlBundle.getString("listAllUser");
                stmt = conn.prepareStatement(requete);
            } else {
                requete = sqlBundle.getString("listUser");
                stmt = conn.prepareStatement(requete);
                stmt.setString(1, pattern);
            }
            rs = stmt.executeQuery();
            Role role = new Role();
            while (rs.next()) {
                userTmp.setLastName(rs.getString("u.lastname"));
                userTmp.setFirstName(rs.getString("u.firstname"));
                userTmp.setTrigramme(rs.getString("u.trigramme"));
                role.setLibelle(rs.getString("r.libelle"));
                userTmp.setRole(role);
                userList.add(userTmp);
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } catch (PersistanceException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                close(conn);
                close(stmt);
                close(rs);
            } catch (PersistanceException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return userList;
    }

    public void insertEvent(Event evt) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String reqInsertEvent = sqlBundle.getString("insertEventMail");
        try {
            conn = createConnection();
            stmt = conn.prepareStatement(reqInsertEvent);
            stmt.setString(1, evt.getAuthor());
            stmt.setDate(2, (java.sql.Date) evt.getExecutionDate());
            stmt.setDate(3, (java.sql.Date) evt.getRecordDate());
            stmt.setString(4, ((EventMail) evt).getDest());
            stmt.setString(5, ((EventMail) evt).getSubject());
            stmt.setString(6, ((EventMail) evt).getBody());
            stmt.setString(7, ((EventMail) evt).getType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } catch (PersistanceException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                close(conn);
                close(stmt);
            } catch (PersistanceException e) {
                logger.debug(e.getMessage(), e);
            }
        }
    }

    public boolean isEventPresent(Event event) {
        boolean retour = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        long eventId = event.getIdEvent();
        String requete = this.sqlBundle.getString("isEventPresent");
        try {
            conn = createConnection();
            stmt = conn.prepareStatement(requete);
            stmt.setLong(1, eventId);
            resultSet = stmt.executeQuery();
            if (resultSet.isAfterLast()) {
                retour = false;
            } else {
                retour = true;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        } catch (PersistanceException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                close(resultSet);
                close(stmt);
                close(conn);
            } catch (PersistanceException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return retour;
    }

    public Role findRole(final String role) {
        return null;
    }

    public void insertRole(final Role role) {
    }

    public List<EventMail> listEvent(final Event reference) {
        return null;
    }

    public List<Role> listRole(final Role role) {
        return null;
    }

    public void removeRole(final Role role) {
    }
}

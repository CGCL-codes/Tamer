package ossobook.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import ossobook.client.base.metainfo.Project;
import ossobook.exceptions.StatementNotExecutedException;

/**
 * manages benutzer table (makes inserts, deletes, updates and select statements
 * on the table) manages rights of the users for the different projects
 * 
 * @author j.lamprecht
 * 
 */
public class UserManager extends TableManager {

    public static final int NORIGHTS = 0;

    public static final int READ = 1;

    public static final int WRITE = 2;

    public UserManager(Connection con, String databaseName) {
        super(con, databaseName);
    }

    /**
	 * 
	 * @return the users of the database except the admins and the logged in
	 *         user
	 * @throws StatementNotExecutedException
	 */
    public Vector<String> getUserNames() throws StatementNotExecutedException {
        Vector<String> users = new Vector<String>();
        String query = "SELECT BenutzerName FROM " + databaseName + ".benutzer WHERE " + "BenutzerName!=SUBSTRING(USER(),1, LOCATE('@', USER())-1) AND" + " Admin!='Y' ORDER BY BenutzerName;";
        try {
            ResultSet rs = executeSelect(query);
            while (rs.next()) {
                users.add(rs.getString(1));
            }
            return users;
        } catch (SQLException e) {
            log.error("MySQL Error occurred.", e);
            throw new StatementNotExecutedException(query);
        }
    }

    /**
	 * proofs whether user may do everything
	 * 
	 * @return true if User is administrator, else false
	 * @throws StatementNotExecutedException
	 */
    public boolean getIsAdmin() throws StatementNotExecutedException {
        String query = "SELECT Admin FROM " + databaseName + ".benutzer WHERE BenutzerName=SUBSTRING(USER(),1, LOCATE('@', USER())-1)" + " AND geloescht='N';";
        try {
            ResultSet rs = executeSelect(query);
            return rs.next() && rs.getString(1).equals("Y");
        } catch (SQLException e) {
            log.error("MySQL Error occurred.", e);
            throw new StatementNotExecutedException(query);
        }
    }

    /**
	 * 
	 * @param project
	 *            project number
	 * @return right which the User has for the project - read or read and write
	 *         or nothing
	 * @throws StatementNotExecutedException
	 */
    public int getRight(Project project) throws StatementNotExecutedException {
        String query = "SELECT Recht FROM " + databaseName + ".projektrechte WHERE " + "Benutzer=SUBSTRING(USER(),1, LOCATE('@', USER())-1)" + " and ProjNr=" + project.getNumber() + " AND DBNummerProjekt=" + project.getDatabaseNumber() + " AND geloescht='N';";
        try {
            ResultSet rs = executeSelect(query);
            if (rs.next()) {
                if (rs.getString(1).equals("schreiben")) {
                    return WRITE;
                } else if (rs.getString(1).equals("lesen")) {
                    return READ;
                }
            }
            return NORIGHTS;
        } catch (SQLException e) {
            log.error("MySQL Error occurred.", e);
            throw new StatementNotExecutedException(query);
        }
    }

    /**
	 * @precondition: "changer" is project owner or administrator gives /
	 *                changes /revokes right to/ of / from given user for given
	 *                project
	 * @param project
	 * @param user
	 *            whose right shall be changed
	 * @param right
	 * @throws StatementNotExecutedException
	 */
    public void changeRight(Project project, String user, int right) throws StatementNotExecutedException {
        String query = "";
        if (user == null) {
            user = "SUBSTRING(USER(),1, LOCATE('@', USER())-1)";
        } else {
            user = "'" + user + "'";
        }
        try {
            if (right == NORIGHTS) {
                query = "UPDATE " + databaseName + ".projektrechte SET geloescht='Y' WHERE " + "ProjNr=" + project.getNumber() + " AND Benutzer=" + user + " AND DBNummerProjekt=" + project.getDatabaseNumber() + ";";
                executeUpdate(query);
            } else {
                query = "UPDATE " + databaseName + ".projektrechte SET Recht=" + right + " WHERE " + "ProjNr=" + project.getNumber() + " AND Benutzer=" + user + " AND DBNummerProjekt=" + project.getDatabaseNumber() + ";";
                int alreadyexists = executeUpdate(query);
                if (alreadyexists < 1) {
                    query = "INSERT INTO " + databaseName + ".projektrechte (Benutzer,ProjNr, DBNummerProjekt, Recht) " + "VALUES (" + user + ", " + project.getNumber() + ", " + project.getDatabaseNumber() + ", " + right + ");";
                    executeUpdate(query);
                }
            }
        } catch (SQLException e) {
            log.error("MySQL Error occurred.", e);
            throw new StatementNotExecutedException(query);
        }
    }

    /**
	 * get e-mail address of project owner
	 * 
	 * @param project
	 *            primary key of project
	 * @return
	 * @throws StatementNotExecutedException
	 */
    public String[] getMail(Project project) throws StatementNotExecutedException {
        String query = "SELECT Mail FROM " + databaseName + ".projekt, " + databaseName + ".benutzer WHERE " + "projNr=" + project.getNumber() + " AND Datenbanknummer=" + project.getDatabaseNumber() + " AND (ProjEigentuemer=BenutzerName OR BenutzerName='" + QueryManagerFactory.getUsername() + "') AND benutzer.geloescht='N'";
        Vector<String> mail = new Vector<String>();
        try {
            ResultSet rs = executeSelect(query);
            while (rs.next()) {
                mail.add(rs.getString(1));
            }
            return mail.toArray(new String[mail.size()]);
        } catch (SQLException e) {
            log.error("MySQL Error occurred.", e);
            throw new StatementNotExecutedException(query);
        }
    }
}

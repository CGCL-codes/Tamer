package agenda.DAO.MySQL;

import agenda.DAO.genericDAO;
import java.sql.*;

/**
 * Defines the MySQL implementation for the generic DAO.
 *
 * @author  Christopher Miller
 * @author  Matthew Broadfoot
 * @version 2.0
 */
public class MySQLDAO implements genericDAO {

    /**
     * Terminates the database connection.
     */
    public void cleanup() {
        MySQLDAOFactory.cleanup();
    }

    /**
     * Retrieves strain data from a MySQL database for use in the BL.
     *
     * Business layer function. This method retrieves strain data from a MySQL
     * database.
     *
     * @return  a result set of strain data
     */
    public ResultSet getStrains() {
        String sql = "SELECT * FROM strain ORDER BY strain_bacterium_id";
        try {
            return MySQLDAOFactory.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Saves strain comparison data to a MySQL database for use in the BL.
     *
     * Business layer function. This method saves comparison data to a MySQL
     * database.
     *
     * @param   controlStrainId     the ID of the strain being examined
     * @param   subjectStrainId     the ID of the specific subject strain
     * @param   similarityDistance  the similarity distance of the comparison
     */
    public void saveStrainComparison(String controlStrainId, String subjectStrainId, double similarityDistance) {
        String sql = "INSERT INTO comparison VALUES (null, '" + controlStrainId + "', '" + subjectStrainId + "', " + similarityDistance + ");";
        try {
            MySQLDAOFactory.executeUpdate(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves bacteria data from a MySQL database for use in the UI.
     *
     * Interface function. This method retrieves bacteria data (<code>
     * bacterium_id, bacterium_name</code>) from a MySQL database.
     *
     * @return  a result set of bacteria data
     */
    public ResultSet getBacteriaNames() {
        String sql = "SELECT bacterium_id, bacterium_name FROM bacterium ORDER BY bacterium_id";
        try {
            return MySQLDAOFactory.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves an average distance from a MySQL database for use in the UI.
     *
     * Interface function. This method retrieves a specific bacterium's average
     * similarity distance from a MySQL database.
     *
     * @param   bacteriumID     the ID of the bacterium under study
     * @return  a result set containing the average similarity distance
     */
    public ResultSet getBacteriumAverage(int bacteriumID) {
        String sql = "SELECT AVG(comparison_similarity_distance) AS average_similarity_distance FROM comparison INNER JOIN strain ON comparison_control_strain_id = strain_id WHERE strain_bacterium_id = " + bacteriumID;
        try {
            return MySQLDAOFactory.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves comparison data from a MySQL database for use in the UI.
     *
     * Interface function. This method retrieves the comparison data (<code>
     * subject, comparison_similarity_distance</code>) for all strains of the
     * given strain's bacterium.
     *
     * @param   controlStrainId     the ID of the strain under study
     * @return  a result set of comparison data
     */
    public ResultSet getComparisons(String controlStrainId) {
        String sql = "SELECT s2.strain_name AS subject, comparison_similarity_distance FROM comparison INNER JOIN strain s1 ON comparison_control_strain_id = s1.strain_id INNER JOIN strain s2 ON comparison_subject_strain_id = s2.strain_id WHERE comparison_control_strain_id = '" + controlStrainId + "'";
        try {
            return MySQLDAOFactory.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves strain names from a MySQL database for use in the UI.
     *
     * Interface function. This method retrieves the strain data (<code>
     * strain_id, strain_name</code>) for all strains of the given strain's
     * bacterium.
     *
     * @param   strainBacteriumID     the ID of the bacterium under study
     * @return  a result set of strain data
     */
    public ResultSet getStrainNames(int strainBacteriumID) {
        String sql = "SELECT strain_id, strain_name FROM strain WHERE strain_bacterium_id = '" + strainBacteriumID + "' ORDER BY strain_bacterium_id";
        try {
            return MySQLDAOFactory.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

package decathlon.parser.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import decathlon.calculator.DecathlonPointsCalculator;
import decathlon.generic.Athlete;
import decathlon.generic.DecathlonData;
import decathlon.generic.Result;
import decathlon.main.DecathlonDemo;
import decathlon.main.DecathlonProperties;

/**
 * The {@link DataReader} class provides methods to access
 * a database and return the results for a decathlon competition.
 * 
 * @author Karpz
 *
 */
public class DataReader {

    /**
	 * Get competition data from database. The query is formed this way
	 * that if the competition name parameter can be converted to an
	 * integer value then the 
	 * 
	 * @param competition - Competition name or id
	 * @return - Array with data for the selected competition
	 * @throws ClassNotFoundException 
	 */
    public List<DecathlonData> getDecathlonData(String competition) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        List<DecathlonData> decathlonData = new ArrayList<DecathlonData>();
        int competitionId = 0;
        try {
            competitionId = Integer.parseInt(competition);
        } catch (NumberFormatException e) {
        }
        try {
            conn = openDbConnection();
            String queryBegin = "SELECT competitions.id,competitions.name,athletes.name," + "athletes.dob,athletes.country_code,race_100m,long_jump,shot_put," + "high_jump,race_400m,hurdles_110m,discus_throw,pole_vault,javelin_throw," + "race_1500m FROM results INNER JOIN athletes ON results.athlete_id = " + "athletes.id INNER JOIN competitions ON results.competition_id = " + "competitions.id WHERE ";
            ps = conn.prepareStatement(queryBegin + "competitions.name = ?");
            ps.setString(1, competition);
            rs = ps.executeQuery();
            if (!rs.next()) {
                if (competitionId != 0) {
                    ps = conn.prepareStatement(queryBegin + "competitions.id = ?");
                    ps.setInt(1, competitionId);
                    rs = ps.executeQuery();
                }
            } else {
                rs.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying the database!", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid driver provided for " + "the database connection!", e);
        }
        try {
            while (rs.next()) {
                Athlete athlete = new Athlete();
                Result result = new Result();
                athlete.setName(rs.getString(3));
                try {
                    athlete.setBirthday((Date) rs.getObject(4));
                } catch (SQLException e) {
                    if (e.getMessage().contains("0000-00-00")) athlete.setBirthday(null);
                }
                athlete.setNationality(rs.getString(5));
                result.setRun100m(rs.getFloat(6));
                result.setLongJump(rs.getFloat(7));
                result.setShotPut(rs.getFloat(8));
                result.setHighJump(rs.getFloat(9));
                result.setRun400m(rs.getFloat(10));
                result.setHurdles110m(rs.getFloat(11));
                result.setDiscus(rs.getFloat(12));
                result.setPoleVault(rs.getFloat(13));
                result.setJavelin(rs.getFloat(14));
                result.setRun1500m(rs.getFloat(15));
                decathlonData.add(new DecathlonData(athlete, result, DecathlonPointsCalculator.calculateTotalPoints(result)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database contains erroneous data for the " + "selected competition. Cannot proceed reading values!");
        }
        try {
            endDbCommunication(ps, rs, conn);
        } catch (SQLException e) {
            System.err.println("Error closing the database connection!");
            e.printStackTrace();
        }
        return decathlonData;
    }

    /**
	 * Returns a database connection object according to the parameters 
	 * containing in the database properties file.
	 * 
	 * @return database connection
	 * @throws ClassNotFoundException - if invalid db driver provided
	 * @throws SQLException - if errors creating the connection object
	 * @throws IOException - if errors accessing the db properties file
	 */
    private static Connection openDbConnection() throws SQLException, ClassNotFoundException {
        DecathlonProperties props;
        try {
            props = new DecathlonProperties(DecathlonDemo.DB_PROPERTIES_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Error opening the database properties file!", e);
        }
        Class.forName(props.getProperty("driver"));
        Connection con = DriverManager.getConnection(props.getProperty("server") + ":" + props.getProperty("port") + "/" + props.getProperty("db"), props.getProperty("user"), props.getProperty("password"));
        return con;
    }

    /**
	 * Closes the database connection and the objects used during the communication 
	 * 
	 * @param ps - prepared statement used for generating the query
	 * @param rs - result of the query
	 * @param conn - connection object used for communicating with the database
	 * @throws SQLException - if errors encountered during the closing of the 
	 * database objects
	 */
    private static void endDbCommunication(PreparedStatement ps, ResultSet rs, Connection conn) throws SQLException {
        if (ps != null) {
            ps.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}

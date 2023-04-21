package uk.ac.city.soi.everestplus.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.slaatsoi.prediction.schema.PredictionPolicyType;
import uk.ac.city.soi.database.EntityManagerCommons;
import uk.ac.city.soi.database.EntityManagerInterface;

/**
 * @author Davide Lorenzoli
 * 
 * @date Jun 11, 2011
 */
public class PredictionPolicyEntityManager extends EntityManagerCommons implements EntityManagerInterface<PredictionPolicyType> {

    private static Logger logger = Logger.getLogger(PredictionPolicyEntityManager.class);

    private static final String DATABASE_TABLE_NAME = "prediction_policy";

    /**
	 * @param connection
	 */
    public PredictionPolicyEntityManager(Connection connection) {
        this(connection, DATABASE_TABLE_NAME);
    }

    /**
	 * @param connection
	 * @param databaseTable
	 */
    public PredictionPolicyEntityManager(Connection connection, String databaseTable) {
        super(connection, databaseTable);
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#delete(java.lang.Object)
	 */
    public int delete(PredictionPolicyType predictionPolicy) {
        return deleteByPrimaryKey(predictionPolicy.getPredictionPolicyId());
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#deleteByPrimaryKey(java.lang.String)
	 */
    public int deleteByPrimaryKey(String predictionPolicyId) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int deletedRecords = 0;
        try {
            String query = "DELETE FROM " + getDatabaseTable() + " " + "WHERE prediction_policy_id=?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setString(1, predictionPolicyId);
            deletedRecords = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return deletedRecords;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#insert(java.lang.Object)
	 */
    public int insert(PredictionPolicyType predictionPolicy) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int insertedRecords = 0;
        if (selectByPrimaryKey(predictionPolicy.getPredictionPolicyId()) != null) {
            return 0;
        }
        try {
            String query = "INSERT INTO " + getDatabaseTable() + " " + "SET prediction_policy_id=?, sla_id=?, agreement_term_id=?, guaranteed_id=?, qos_id=?, prediction_policy_object=?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setString(1, predictionPolicy.getPredictionPolicyId());
            pstmt.setString(2, predictionPolicy.getPredictionTarget().getVariable().getSlaId());
            pstmt.setString(3, predictionPolicy.getPredictionTarget().getVariable().getAgreementTermId());
            pstmt.setString(4, predictionPolicy.getPredictionTarget().getVariable().getGuaranteedId());
            pstmt.setString(5, predictionPolicy.getPredictionTarget().getVariable().getQoSId());
            pstmt.setBlob(6, toBlob(predictionPolicy));
            insertedRecords = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return insertedRecords;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#select()
	 */
    public ArrayList<PredictionPolicyType> select() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<PredictionPolicyType> result = new ArrayList<PredictionPolicyType>();
        ;
        try {
            String query = "SELECT prediction_policy_object " + "FROM " + getDatabaseTable();
            pstmt = getConnection().prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.first()) {
                do {
                    result.add((PredictionPolicyType) toObject(rs.getBlob("prediction_policy_object")));
                } while (rs.next());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, rs);
        }
        return result;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#selectByPrimaryKey(java.lang.String)
	 */
    public PredictionPolicyType selectByPrimaryKey(String predictionPolicyId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PredictionPolicyType result = null;
        try {
            String query = "SELECT prediction_policy_object " + "FROM " + getDatabaseTable() + " " + "WHERE prediction_policy_id=?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setString(1, predictionPolicyId);
            rs = pstmt.executeQuery();
            if (rs.first()) {
                result = (PredictionPolicyType) toObject(rs.getBlob("prediction_policy_object"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, rs);
        }
        return result;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#update(java.lang.Object)
	 */
    public int update(PredictionPolicyType predictionPolicy) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int insertedRecords = 0;
        try {
            String query = "UPDATE " + getDatabaseTable() + " " + "SET prediction_policy_object=? " + "WHERE prediction_policy_id=?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setBlob(1, toBlob(predictionPolicy));
            pstmt.setString(2, predictionPolicy.getPredictionPolicyId());
            insertedRecords = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return insertedRecords;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#executeQuery(java.lang.String)
	 */
    public ArrayList<PredictionPolicyType> executeQuery(String query) {
        throw new UnsupportedOperationException("TO DO");
    }
}

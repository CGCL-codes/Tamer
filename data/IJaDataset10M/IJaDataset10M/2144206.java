package uk.ac.city.soi.everestplus.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uk.ac.city.soi.database.EntityManagerCommons;
import uk.ac.city.soi.database.EntityManagerInterface;
import uk.ac.city.soi.everestplus.core.Statistics;

/**
 * @author Davide Lorenzoli
 * 
 * @date Apr 16, 2010
 */
public class StatisticsEntityManager extends EntityManagerCommons implements EntityManagerInterface<StatisticsEntity> {

    private static final String DATABASE_TABLE_NAME = "statistics";

    /**
	 * @param connection
	 */
    public StatisticsEntityManager(Connection connection) {
        this(connection, DATABASE_TABLE_NAME);
    }

    /**
	 * @param connection
	 */
    public StatisticsEntityManager(Connection connection, String databaseTable) {
        super(connection, databaseTable);
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#executeQuery()
	 */
    public ArrayList<StatisticsEntity> executeQuery(String query) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        ArrayList<StatisticsEntity> result = new ArrayList<StatisticsEntity>();
        try {
            pstmt = getConnection().prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            if (rs.first()) {
                do {
                    Statistics statistics = new Statistics();
                    statistics.setK(rs.getDouble("k"));
                    statistics.setModelGoodness(rs.getDouble("modelGoodness"));
                    statistics.setModelHistorySize(rs.getInt("modelHistorySize"));
                    statistics.setMttrc(rs.getDouble("mttrc"));
                    statistics.setMttrf(rs.getDouble("mttrf"));
                    statistics.setMttry(rs.getDouble("mttry"));
                    statistics.setPrediction(rs.getDouble("prediction"));
                    statistics.setPredictionTimestamp(rs.getLong("predictionTimestamp"));
                    statistics.setPredictionWindow(rs.getDouble("predictionWindow"));
                    statistics.setPrMttrc(rs.getDouble("prMttrc"));
                    statistics.setPrMttry(rs.getDouble("prMttry"));
                    statistics.setPrY(rs.getDouble("prY"));
                    statistics.setY(rs.getInt("y"));
                    statistics.setExecutionId(rs.getString("executionId"));
                    statistics.setkType(rs.getInt("kType"));
                    StatisticsEntity statisticsEntity = new StatisticsEntity(statistics);
                    statisticsEntity.setEntityId(rs.getLong("entityId"));
                    result.add(statisticsEntity);
                } while (rs.next());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return result;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#executeUpdate(java.lang.String)
	 */
    public int executeUpdate(String query) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int insertedRecords = 0;
        try {
            pstmt = getConnection().prepareStatement(query);
            insertedRecords = pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return insertedRecords;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#insert(java.lang.Object)
	 */
    public int insert(StatisticsEntity entity) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int insertedRecords = 0;
        try {
            String query = "INSERT INTO " + getDatabaseTable() + " " + "SET k=?, predictionWindow=?, y=?, mttrc=?, mttry=?, prY=?, prMttrc=?, prMttry=?, prediction=?, mttrf=?, modelHistorySize=?, modelGoodness=?, predictionTimestamp=?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setDouble(1, entity.getK());
            pstmt.setDouble(2, entity.getPredictionWindow());
            pstmt.setDouble(3, entity.getY());
            pstmt.setDouble(4, Double.isNaN(entity.getMttrc()) ? -1 : entity.getMttrc());
            pstmt.setDouble(5, Double.isNaN(entity.getMttry()) ? -1 : entity.getMttry());
            pstmt.setDouble(6, Double.isNaN(entity.getPrY()) ? -1 : entity.getPrY());
            pstmt.setDouble(7, Double.isNaN(entity.getPrMttrc()) ? -1 : entity.getPrMttrc());
            pstmt.setDouble(8, Double.isNaN(entity.getPrMttry()) ? -1 : entity.getPrMttry());
            pstmt.setDouble(9, Double.isNaN(entity.getPrediction()) ? -1 : entity.getPrediction());
            pstmt.setDouble(10, Double.isNaN(entity.getMttrf()) ? -1 : entity.getMttrf());
            pstmt.setInt(11, entity.getModelHistorySize());
            pstmt.setDouble(12, entity.getModelGoodness());
            pstmt.setLong(13, entity.getPredictionTimestamp());
            insertedRecords = pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return insertedRecords;
    }

    /**
	 * @return
	 */
    public StatisticsEntity selectLast() {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        StatisticsEntity statisticsEntity = null;
        try {
            String query = "SELECT * FROM " + getDatabaseTable() + " " + "ORDER BY statistics_PK DESC " + "LIMIT 1";
            pstmt = getConnection().prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            if (rs.first()) {
                Statistics statistics = new Statistics();
                statistics.setExecutionId(rs.getString("executionId"));
                statistics.setK(rs.getDouble("k"));
                statistics.setModelGoodness(rs.getDouble("modelGoodness"));
                statistics.setModelHistorySize(rs.getInt("modelHistorySize"));
                statistics.setMttrc(rs.getDouble("mttrc"));
                statistics.setMttrf(rs.getDouble("mttrf"));
                statistics.setMttry(rs.getDouble("mttry"));
                statistics.setPrediction(rs.getDouble("prediction"));
                statistics.setPredictionTimestamp(rs.getLong("predictionTimestamp"));
                statistics.setPredictionWindow(rs.getInt("predictionWindow"));
                statistics.setPrMttrc(rs.getDouble("mttrc"));
                statistics.setPrMttry(rs.getDouble("mttry"));
                statistics.setPrY(rs.getDouble("prY"));
                statistics.setY(rs.getDouble("y"));
                statisticsEntity = new StatisticsEntity(statistics);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return statisticsEntity;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#update(java.lang.Object)
	 */
    public int update(StatisticsEntity entity) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int insertedRecords = 0;
        try {
            String query = "UPDATE " + getDatabaseTable() + " " + "SET k=?, predictionWindow=?, y=?, mttrc=?, mttry=?, prY=?, prMttrc=?, prMttry=?, prediction=?, mttrf=?, modelHistorySize=?, modelGoodness=?, predictionTimestamp=? " + "WHERE statistics_PK = ?";
            pstmt = getConnection().prepareStatement(query);
            pstmt.setDouble(1, entity.getK());
            pstmt.setDouble(2, entity.getPredictionWindow());
            pstmt.setDouble(3, entity.getY());
            pstmt.setDouble(4, Double.isNaN(entity.getMttrc()) ? -1 : entity.getMttrc());
            pstmt.setDouble(5, Double.isNaN(entity.getMttry()) ? -1 : entity.getMttry());
            pstmt.setDouble(6, Double.isNaN(entity.getPrY()) ? -1 : entity.getPrY());
            pstmt.setDouble(7, Double.isNaN(entity.getPrMttrc()) ? -1 : entity.getPrMttrc());
            pstmt.setDouble(8, Double.isNaN(entity.getPrMttry()) ? -1 : entity.getPrMttry());
            pstmt.setDouble(9, Double.isNaN(entity.getPrediction()) ? -1 : entity.getPrediction());
            pstmt.setDouble(10, Double.isNaN(entity.getMttrf()) ? -1 : entity.getMttrf());
            pstmt.setInt(11, entity.getModelHistorySize());
            pstmt.setDouble(12, entity.getModelGoodness());
            pstmt.setLong(13, entity.getPredictionTimestamp());
            pstmt.setLong(14, entity.getEntityId());
            insertedRecords = pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return insertedRecords;
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#delete(java.lang.Object)
	 */
    public int delete(StatisticsEntity entity) {
        throw new UnsupportedOperationException("TO DO");
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#deleteByPrimaryKey(java.lang.String)
	 */
    public int deleteByPrimaryKey(String entityPrimaryKey) {
        throw new UnsupportedOperationException("TO DO");
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#select()
	 */
    public ArrayList<StatisticsEntity> select() {
        throw new UnsupportedOperationException("TO DO");
    }

    /**
	 * @see uk.ac.city.soi.database.EntityManagerInterface#selectByPrimaryKey(java.lang.String)
	 */
    public StatisticsEntity selectByPrimaryKey(String entityPrimaryKey) {
        throw new UnsupportedOperationException("TO DO");
    }
}

package uk.org.ogsadai.performance.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import uk.org.ogsadai.performance.exception.ConfigurationException;
import uk.org.ogsadai.performance.util.DatabaseConnection;

/**
 * Data model for table "measurement". Set appropriate attributes using the 
 * settter methods and then call the <code>save()</code> method. Each call to 
 * <code>save()</code> will insert a new row in the "measurement" table.

 * @author The OGSA-DAI Project Team.
 *
 */
public class Measurement {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009";

    /** Experiment corresponding to this measurement. */
    private Experiment mExperiment;

    /** Start time of experiment. */
    private long mStartTime;

    /** End time of experiment. */
    private long mEndTime;

    /** Status of experiment. */
    private String mStatus;

    /**
     * Default Constructor. 
     * @param experiment
     */
    public Measurement(final Experiment experiment) {
        mExperiment = experiment;
    }

    /**
     * Returns the experiment that this measurement object is associated with.
     * 
     * @return experiment
     */
    public Experiment getExperiment() {
        return mExperiment;
    }

    /**
     * Returns start time for this measurement.
     * 
     * @return start time
     */
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * Sets start time for this measurement.
     * 
     * @param starttime start time
     */
    public void setStartTime(final long starttime) {
        mStartTime = starttime;
    }

    /**
     * Returns end time for this measurement.
     * 
     * @return end time
     */
    public long getEndTime() {
        return mEndTime;
    }

    /**
     * Sets end time for this measurement.
     * 
     * @param endtime end time
     */
    public void setEndTime(final long endtime) {
        mEndTime = endtime;
    }

    public String getStatus() {
        return mStatus;
    }

    /**
     * Sets status for this measurement
     * 
     * @param status status
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * Saves this measurement. All important fields must have already been
     * set using the setter methods. Each call to this method will insert a 
     * new row into the "measurement" table.
     * 
     * @throws ConfigurationException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IOException
     * @throws SQLException
     */
    public void save() throws ConfigurationException, ClassNotFoundException, NoSuchMethodException, IOException, SQLException {
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        Timestamp startTime = new Timestamp(mStartTime);
        Timestamp endTime = new Timestamp(mEndTime);
        String insert = "INSERT INTO measurements(experimentid, starttime, endtime, " + "status) VALUES (" + mExperiment.getID() + ",'" + startTime + "','" + endTime + "','" + mStatus + "')";
        statement.executeUpdate(insert);
        statement.close();
    }
}

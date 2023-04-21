package at.ac.ait.enviro.cascadingsos.core.datapoint;

import at.ac.ait.enviro.tsapi.datastore.CommException;
import at.ac.ait.enviro.tsapi.datastore.StateException;
import at.ac.ait.enviro.tsapi.timeseries.TimeInterval;
import at.ac.ait.enviro.tsapi.timeseries.TimeSeries;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Date;

/**
 * Interface specifying a SOS-X DataPoint
 * @author BonitzA
 */
public interface DataPoint {

    /**
     * The ID of this DataPoint
     * @return DataPoint ID
     */
    public String getID();

    @Override
    boolean equals(Object obj);

    /**
     * Retrieves a property from the underlying DataSource
     * @param key
     * @return The property or <b>null</b>
     * @throws IllegalStateException when DataSource of the DataPoint can't be found
     */
    public Object getProperty(String key);

    /**
     * Retrieves the Geometry form TimeSeries properties
     * @return Returns the Geometry of the DataPoint or a POINT[0.0,0.0] in case of an invalid geometry
     * @throws IllegalStateException when DataSource of the DataPoint can't be found
     */
    public Geometry getValidGeometry();

    /**
     * Retrieves the Geometry form TimeSeries properties
     * @return Returns the Geometry of the DataPoint or <b>null</b> in case of an invalid geometry
     * @throws IllegalStateException when DataSource of the DataPoint can't be found
     */
    public Geometry getGeometry();

    /**
     * Returns a TimeSeries for the specified interval
     * @param interval
     *      a time interval
     * @return A TimeSeries or <b>null</b>
     * @throws at.ac.ait.enviro.tsapi.datastore.CommException
     * @throws at.ac.ait.enviro.tsapi.datastore.StateException
     */
    public TimeSeries getTimeSeries(TimeInterval interval) throws CommException, StateException;

    /**
     * Fetches the max. Date from a DataPoint
     * @param dp
     *      Some DataPoint
     * @return
     *      the max. Date or <b>null</b>
     */
    public Date getEndDate();

    /**
     * Fetches the min. Date from a DataPoint
     * @param dp
     *      Some DataPoint
     * @return
     *      the min. Date or <b>null</b>
     */
    public Date getStartDate();
}

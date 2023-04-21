package at.ac.ait.enviro.dssos;

import at.ac.ait.enviro.dssos.source.ISOSConnector;
import at.ac.ait.enviro.dssos.source.SOSConnectorFactory;
import at.ac.ait.enviro.dssos.exceptions.SosClientException;
import at.ac.ait.enviro.dssos.exceptions.SosExceptionReport;
import at.ac.ait.enviro.dssos.container.ObservationOffering;
import at.ac.ait.enviro.dssos.container.ObservationResult;
import at.ac.ait.enviro.dssos.container.ServiceIdentification;
import at.ac.ait.enviro.dssos.util.digester.TargetProperties;
import at.ac.ait.enviro.tsapi.PropertyException;
import at.ac.ait.enviro.tsapi.datastore.CommException;
import at.ac.ait.enviro.tsapi.datastore.ConnectionException;
import at.ac.ait.enviro.tsapi.datastore.DataSink;
import at.ac.ait.enviro.tsapi.datastore.DataSource;
import at.ac.ait.enviro.tsapi.datastore.StateException;
import at.ac.ait.enviro.tsapi.timeseries.InvalidIndexException;
import at.ac.ait.enviro.tsapi.timeseries.InvalidTsOperationException;
import at.ac.ait.enviro.tsapi.timeseries.TimeInterval;
import at.ac.ait.enviro.tsapi.timeseries.TimeInterval.Openness;
import at.ac.ait.enviro.tsapi.timeseries.TimeSeries;
import at.ac.ait.enviro.tsapi.timeseries.TimeStamp;
import at.ac.ait.enviro.tsapi.timeseries.impl.DefaultTimeSeries;
import at.ac.ait.enviro.tsapi.timeseries.impl.DefaultTimeStamp;
import at.ac.ait.enviro.tsapi.util.text.ISO8601DateFormat;
import com.vividsolutions.jts.geom.Geometry;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import static at.ac.ait.enviro.tsapi.TSAPIConstants.*;

/**
 * A DataSource for accessing observation data from a Sensor Observation
 * Service.
 *
 * @author BonitzA
 */
public class SOSDataSource implements DataSource, SOSDataStore {

    /** Key for "definition" of a observed property*/
    public static final String TS_DEFINITION = "ts:definition";

    /** Logger */
    private static final transient Logger logger = Logger.getLogger(SOSDataSource.class);

    private static final String DELIM = "&";

    private static final String DELIM_ESCAPED = "&#36;";

    private static final List<String> TS_KEYS = new ArrayList<String>();

    static {
        TS_KEYS.add(TS_OFFERING);
        TS_KEYS.add(TS_PROCEDURE);
        TS_KEYS.add(TS_FEATURE_OF_INTEREST);
        TS_KEYS.add(TS_OBSERVEDPROPERTY);
    }

    protected final Map<String, Object> dsProperties;

    protected final Map<String, Map<String, Object>> tsProperties;

    /**
     * SOSConnector instance
     */
    protected transient ISOSConnector connection;

    public SOSDataSource() {
        dsProperties = new HashMap<String, Object>();
        tsProperties = new HashMap<String, Map<String, Object>>();
    }

    @Override
    public void connect(String resource) throws ConnectionException, IllegalStateException {
        dsProperties.put(DS_CONNECT_STRING, resource);
        getConnection();
    }

    @Override
    public void connect(Map<String, String> resourceParameters) throws ConnectionException, IllegalStateException {
        dsProperties.putAll(resourceParameters);
        getConnection();
    }

    @Override
    public void disconnect() throws IllegalStateException {
        checkConnection();
        if (connection != null) {
            connection = null;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public List<String> getSupportedConnectProperties() {
        final List<String> resParams = new ArrayList<String>(2);
        resParams.add(DS_CONNECT_STRING);
        resParams.add(DS_SOS_EASTING);
        return resParams;
    }

    @Override
    public List<String> getTimeSeriesIds() throws CommException, IllegalStateException {
        checkConnection();
        final Set<String> tsKeysSet = tsProperties.keySet();
        return new ArrayList<String>(tsKeysSet);
    }

    @Override
    public List<String> findTimeSeriesIds(Map<String, Object> timeSeriesProperties) throws CommException, IllegalStateException {
        checkConnection();
        if (timeSeriesProperties == null) {
            return getTimeSeriesIds();
        }
        return filterTimeSeries(timeSeriesProperties);
    }

    @Override
    public boolean containsTSProperty(String timeSeriesId, String key) throws CommException, StateException, PropertyException {
        checkConnection();
        return tsProperties.get(timeSeriesId) == null ? false : tsProperties.get(timeSeriesId).containsKey(key);
    }

    @Override
    public Object getTSProperty(String timeSeriesId, String key) throws CommException, StateException, PropertyException {
        checkConnection();
        refreshAvailableData(timeSeriesId);
        if (containsTSProperty(timeSeriesId, key)) {
            return tsProperties.get(timeSeriesId).get(key);
        } else {
            return null;
        }
    }

    @Override
    public void setTSProperty(String timeSeriesId, String key, Object value) throws CommException, StateException, PropertyException {
        checkConnection();
        if (tsProperties.containsKey(timeSeriesId)) {
            tsProperties.get(timeSeriesId).put(key, value);
        }
    }

    @Override
    public List<String> getTSKeys() throws CommException, IllegalStateException {
        checkConnection();
        return TS_KEYS;
    }

    @Override
    public List<Object> getTSPropertyAllowedValues(String key) throws CommException, IllegalStateException {
        checkConnection();
        final List<Object> result = new LinkedList<Object>();
        for (final Map<String, Object> tsProp : tsProperties.values()) {
            final Object value = tsProp.get(key);
            if (value != null) result.add(value);
        }
        return result;
    }

    @Override
    public TimeSeries getTimeSeriesById(String timeSeriesId, TimeInterval interval) throws CommException, IllegalStateException {
        checkConnection();
        final TimeSeries ts = new DefaultTimeSeries(tsProperties.get(timeSeriesId));
        if (ts == null) {
            throw new IllegalStateException("Unknown TimeSeries ID '" + timeSeriesId + "'");
        }
        if (interval != null) {
            updateTimeSeries(ts, interval);
        }
        return ts;
    }

    @Override
    public void updateTimeSeries(TimeSeries destination, TimeInterval interval) throws CommException {
        try {
            if (destination == null && interval == null) {
                throw new IllegalStateException("One or more parameter are null!");
            }
            final String tsID = (String) destination.getTSProperty(TS_ID);
            if (tsID == null || !tsProperties.containsKey(tsID)) {
                final String msg = String.format("The TimeSeries ID '%s' is not valid in this context", tsID);
                logger.error(msg);
                throw new IllegalArgumentException(msg);
            }
            final String oPrID = tsProperties.get(tsID).get(TS_OBSERVEDPROPERTY).toString();
            final String foiID = tsProperties.get(tsID).get(TS_FEATURE_OF_INTEREST).toString();
            final String offID = tsProperties.get(tsID).get(TS_OFFERING).toString();
            final String prcID = tsProperties.get(tsID).get(TS_PROCEDURE).toString();
            final TimeStamp tStart = interval.getStart();
            final TimeStamp tEnd = interval.getEnd();
            final ObservationResult observationResult = connection.getObservations(oPrID, foiID, offID, prcID, tStart.getAsDate().getTime(), tEnd.getAsDate().getTime());
            fillDatatIntoTimeseries(destination, observationResult, interval.getLeft(), interval.getRight());
            destination.fireTimeSeriesChanged(tStart, tEnd);
        } catch (ConnectionException ex) {
            throw new CommException(ex);
        } catch (SosExceptionReport ex) {
            throw new CommException(ex);
        } catch (SosClientException ex) {
            throw new CommException(ex);
        } catch (PropertyException ex) {
            throw new CommException(ex);
        }
    }

    @Override
    public Object getDataStoreProperty(String key) {
        return dsProperties.get(key);
    }

    @Override
    public void setDataStoreProperty(String key, Object value) {
        dsProperties.put(key, value);
    }

    @Override
    public List<String> getDataStorePropertyKeys() {
        final List<String> keys = new ArrayList<String>();
        keys.add(DS_SOS_ABSTRACT);
        keys.add(DS_SOS_ACCESS);
        keys.add(DS_SOS_EASTING);
        keys.add(DS_SOS_FEES);
        keys.add(DS_SOS_SERVTCEYPE);
        keys.add(DS_SOS_TITLE);
        keys.add(DS_SOS_VERSION);
        return keys;
    }

    @Override
    public boolean containsDataStoreProperty(String key) {
        return dsProperties.containsKey(key);
    }

    @Override
    public DataSink getDataSink() {
        final SOSDataSink dataSink = new SOSDataSink();
        dataSink.setDataStoreProperty(DS_CONNECT_STRING, dsProperties.get(DS_CONNECT_STRING));
        return dataSink;
    }

    /**
     * Throws a {@link IllegalStateException} when disconnected
     * @throws java.lang.IllegalStateException
     */
    protected void checkConnection() throws IllegalStateException {
        if (!isConnected()) {
            throw new IllegalStateException("DataStore is not connected");
        }
    }

    /**
     * Generates the IDs for all possible TimeSeries
     * @throws at.ac.ait.enviro.dssos.connector.exceptions.SosClientException
     * @throws at.ac.ait.enviro.dssos.connector.exceptions.SosExceptionReport
     */
    private void generateTSProperties() throws SosClientException, SosExceptionReport {
        final Collection<ObservationOffering> offs = connection.getOfferings().values();
        for (final ObservationOffering obsOff : offs) {
            for (final String foi : obsOff.getFeatures()) {
                for (final String proc : obsOff.getProcedures()) {
                    for (final String obsProp : obsOff.getObservedProperties()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(escape(obsOff.getID())).append(DELIM);
                        sb.append(escape(foi)).append(DELIM);
                        sb.append(escape(proc)).append(DELIM);
                        sb.append(escape(obsProp)).append(DELIM);
                        final Map<String, Object> tsProps = new HashMap<String, Object>();
                        tsProps.put(TS_OFFERING, obsOff.getID());
                        tsProps.put(TS_FEATURE_OF_INTEREST, foi);
                        tsProps.put(TS_PROCEDURE, proc);
                        tsProps.put(TS_OBSERVEDPROPERTY, obsProp);
                        tsProps.put(TS_ID, sb.toString());
                        tsProps.put(TS_DATASTORE, this);
                        tsProps.put(TS_AVAILABLE_DATA_MIN, obsOff.getStartDate());
                        tsProps.put(TS_AVAILABLE_DATA_MAX, obsOff.getEndDate());
                        setFeatureCoords(tsProps, foi);
                        tsProperties.put(sb.toString(), tsProps);
                    }
                }
            }
        }
    }

    /**
     * Refreshes TS_AVAILABLE_DATA_MIN and TS_AVAILABLE_DATA_MAX
     * @param destination
     * @throws at.ac.ait.enviro.dssos.exceptions.SosClientException
     * @throws at.ac.ait.enviro.dssos.exceptions.SosExceptionReport
     */
    protected void refreshAvailableData(String tsID) {
        try {
            final String offID = tsProperties.get(tsID).get(TS_OFFERING).toString();
            final Collection<ObservationOffering> offs = connection.getOfferings().values();
            for (ObservationOffering off : offs) {
                if (off.getID().equals(offID)) {
                    tsProperties.get(tsID).put(TS_AVAILABLE_DATA_MIN, off.getStartDate());
                    tsProperties.get(tsID).put(TS_AVAILABLE_DATA_MAX, off.getEndDate());
                }
            }
        } catch (SosClientException ex) {
            logger.warn("Could not retrieve TS_AVAILABLE_DATA_MIN and TS_AVAILABLE_DATA_MAX", ex);
        } catch (SosExceptionReport ex) {
            logger.warn("Could not retrieve TS_AVAILABLE_DATA_MIN and TS_AVAILABLE_DATA_MAX", ex);
        }
    }

    /**
     * Inserts a property to {@code p} for the coordinates of a feature of interest {@code foi}.
     *
     * @param tsProps the Map to which the property should be added.
     * @param foi the feature of interest for which the coordinates have to be queried.
     */
    protected void setFeatureCoords(final Map tsProps, final String foi) {
        Geometry geom = null;
        try {
            geom = connection.getFOICoordinates(foi);
            tsProps.put(TS_GEOMETRY, geom);
        } catch (SosClientException ex) {
            logger.warn(String.format("could not query coordinates for feature '%s'", foi), ex);
        } catch (SosExceptionReport ex) {
            logger.warn(String.format("could not query coordinates for feature '%s'", foi), ex);
        }
        if (geom == null) logger.warn(String.format("not coordinates for feature '%s' were found!", foi));
    }

    /**
     * Opens a SOS connection by creating a new {@link ISOSConnector}
     * @return Returns a SOSConnector instance
     * @throws at.ac.ait.enviro.dssos.connector.exceptions.SosClientException
     */
    protected ISOSConnector getConnection() throws ConnectionException {
        if (connection != null) throw new IllegalStateException("DataStore is already connected");
        if (connection == null) {
            try {
                final URL sosURL = new URL(dsProperties.get(DS_CONNECT_STRING).toString());
                final Boolean isEasting = dsProperties.containsKey(DS_SOS_EASTING) ? Boolean.parseBoolean(dsProperties.get(DS_SOS_EASTING).toString()) : false;
                connection = SOSConnectorFactory.getInstance().getNewConnector(sosURL, isEasting);
                generateTSProperties();
            } catch (MalformedURLException ex) {
                final String errMsg = String.format("'%s' is not a valid URL.", dsProperties.get(DS_CONNECT_STRING));
                logger.error(errMsg);
                throw new ConnectionException(errMsg, ex);
            } catch (SosExceptionReport ex) {
                throw new ConnectionException(ex);
            } catch (SosClientException ex) {
                throw new ConnectionException(ex);
            } catch (NullPointerException ex) {
                throw new ConnectionException(ex);
            }
        }
        initDataStoreProperties();
        return connection;
    }

    /**
     * Sets some general infos about the source SOS
     */
    protected void initDataStoreProperties() {
        try {
            final ServiceIdentification sid = connection.getServiceIdentification();
            dsProperties.put(DS_SOS_ABSTRACT, sid.getProperty(ServiceIdentification.KEY_ABSTRACT));
            dsProperties.put(DS_SOS_TITLE, sid.getProperty(ServiceIdentification.KEY_TITLE));
            dsProperties.put(DS_SOS_VERSION, sid.getProperty(ServiceIdentification.KEY_VERSION));
            dsProperties.put(DS_SOS_FEES, sid.getProperty(ServiceIdentification.KEY_FEES));
            dsProperties.put(DS_SOS_SERVTCEYPE, sid.getProperty(ServiceIdentification.KEY_SERVTYPE));
            dsProperties.put(DS_SOS_ACCESS, sid.getProperty(ServiceIdentification.KEY_ACCESS));
        } catch (SosClientException ex) {
            logger.warn("Cannot receive SOS Service Identification", ex);
        } catch (SosExceptionReport ex) {
            logger.warn("Cannot receive SOS Service Identification", ex);
        }
    }

    /**
     * Tries to find all TimeSeries that can be described through the {@code filter}
     * @param filter
     *      A HashMap with some properties
     * @return
     *      A List with TS-IDs
     */
    protected List<String> filterTimeSeries(Map<String, Object> filter) {
        final Set<String> idList = new LinkedHashSet<String>();
        for (Map<String, Object> tsP : tsProperties.values()) {
            if (doPropertiesMatch(tsP, filter)) {
                idList.add((String) tsP.get(TS_ID));
            }
        }
        return new ArrayList<String>(idList);
    }

    /**
     * Checks if the the properties defined in {@code
     * @param source
     * @param filter
     * @return
     */
    protected boolean doPropertiesMatch(Map<String, Object> source, Map<String, Object> filter) {
        if (source == null) {
            return false;
        } else if (filter != null) {
            for (String key : TS_KEYS) {
                Object filterVal = filter.get(key);
                Object sourceVal = source.get(key);
                if (filterVal != null && !filterVal.equals(sourceVal)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String escape(String s) {
        return s.replace(DELIM, DELIM_ESCAPED);
    }

    /**
     * Parses the result data strings and fills the data into the {@link TimeSeries}.
     * Every single data is a single String in the string array parameter. This
     * method loops through the array, parses every single string, creates the {@link AValue}
     * object of the correct type and fills the TimeSeries.
     *
     * @param ts the TimeSeries which must be filled
     * @param refresh specifies whether already available data should be reloaded into the TimeSeries
     * @param valueStrings the array of data Strings returned from the SOS endpoint
     * @throws at.ac.arcs.itt.yau.libs.timeseries.api.impl.InternalErrorException if the raw type of the TimeSeries is unexpected
     */
    @SuppressWarnings("unchecked")
    protected void fillDatatIntoTimeseries(TimeSeries ts, ObservationResult obsResult, Openness left, Openness right) throws ConnectionException {
        String rawData = obsResult.getProperty(ObservationResult.KEY_RAWDATA);
        if (rawData == null) {
            logger.info("Empty result set. Timeseries will be empty");
            return;
        }
        final List<TargetProperties> fields = obsResult.getFields();
        final Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
        final List<Integer> posTimes = getFieldPositions(ObservationResult.TYPE_TIME, fields);
        final int posTime = posTimes.get(0);
        final List<Integer> posCategory = getFieldPositions(ObservationResult.TYPE_CATEGORY, fields);
        final List<Integer> posQantity = getFieldPositions(ObservationResult.TYPE_QUANTITY, fields);
        final Pattern sepBlock = Pattern.compile(obsResult.getProperty(ObservationResult.KEY_SEP_BLOCK));
        final Pattern sepToken = Pattern.compile(obsResult.getProperty(ObservationResult.KEY_SEP_TOKEN));
        final ISO8601DateFormat dateFormat = new ISO8601DateFormat();
        final DecimalFormat numberFormat = new DecimalFormat();
        final DecimalFormatSymbols syms = new DecimalFormatSymbols();
        syms.setDecimalSeparator(obsResult.getProperty(ObservationResult.KEY_SEP_DECIMAL).charAt(0));
        numberFormat.setDecimalFormatSymbols(syms);
        if (posTimes.size() == 0) {
            logger.error("Could not find any TimeStamp definitions in the GetObservation result!");
            return;
        }
        if (posCategory.size() + posQantity.size() == 0) {
            logger.error("Could not find any obs. values definitions in the GetObservation result!");
            return;
        }
        final boolean isComposite = (posCategory.size() + posQantity.size() == 1) ? false : true;
        int cPos = 0;
        for (final int pos : posQantity) {
            final String uom = fields.get(pos).getProperty(ObservationResult.KEY_FIELD_UOM);
            positionMap.put(pos, cPos);
            final String unitKey = isComposite ? TS_UNIT + ":" + cPos : TS_UNIT;
            ts.setTSProperty(unitKey, uom);
            final String defKey = isComposite ? TS_DEFINITION + ":" + cPos : TS_DEFINITION;
            final String def = fields.get(pos).getProperty(ObservationResult.KEY_FIELD_DEF);
            ts.setTSProperty(defKey, def);
            cPos++;
        }
        for (final int pos : posCategory) {
            final String defKey = isComposite ? TS_DEFINITION + ":" + cPos : TS_DEFINITION;
            final String def = fields.get(pos).getProperty(ObservationResult.KEY_FIELD_DEF);
            ts.setTSProperty(defKey, def);
            positionMap.put(pos, cPos++);
        }
        final String[] blocks = sepBlock.split(rawData);
        obsResult.removeProperty(ObservationResult.KEY_RAWDATA);
        rawData = null;
        int i, end;
        if (left.equals(Openness.Open)) i = 1; else i = 0;
        if (right.equals(Openness.Open)) end = blocks.length - 1; else end = blocks.length;
        for (; i < end; i++) {
            final String s = blocks[i];
            final String[] tokens = sepToken.split(s, fields.size());
            try {
                final TimeStamp timeStamp = new DefaultTimeStamp(dateFormat.parse(tokens[posTime]).getTime());
                for (final int pos : posCategory) {
                    final String value = tokens[pos];
                    final String key = isComposite ? VL_VALUE + ":" + positionMap.get(pos) : VL_VALUE;
                    ts.setValueByTimeStamp(key, timeStamp, value);
                }
                for (final int pos : posQantity) {
                    final String key = isComposite ? VL_VALUE + ":" + positionMap.get(pos) : VL_VALUE;
                    try {
                        final double value = numberFormat.parse(tokens[pos]).doubleValue();
                        ts.setValueByTimeStamp(key, timeStamp, value);
                    } catch (ParseException pe) {
                        ts.setValueByTimeStamp(key, timeStamp, tokens[pos]);
                    }
                }
            } catch (InvalidTsOperationException ex) {
                logger.warn("Error while accessing TS properties", ex);
                continue;
            } catch (InvalidIndexException ex) {
                logger.warn("Error while accessing TS properties", ex);
                continue;
            } catch (PropertyException ex) {
                logger.warn("Error while accessing TS properties", ex);
                continue;
            } catch (ParseException ex) {
                logger.warn("Error while parsing", ex);
                continue;
            }
        }
    }

    /**
    * Returns a List with the positions of a specific field type in a List of fields
    * @param type
    * @param fields
    * @return a List of positions
    */
    private List<Integer> getFieldPositions(final String type, final List<TargetProperties> fields) {
        final List<Integer> positions = new LinkedList<Integer>();
        int pos = 0;
        for (TargetProperties field : fields) {
            if (type.equals(field.get(ObservationResult.KEY_FIELD_TYPE))) {
                positions.add(pos);
            }
            pos++;
        }
        return positions;
    }
}

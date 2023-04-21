package bt747.model;

import net.sf.bt747.loc.HttpLocationSender;
import net.sf.bt747.loc.LocationSender;
import gps.BT747Constants;
import gps.GPSListener;
import gps.GpsEvent;
import gps.connection.GPSrxtx;
import gps.log.GPSFilter;
import gps.log.GPSFilterAdvanced;
import gps.mvc.DeviceDirector;
import gps.mvc.MtkModel;
import bt747.sys.Generic;
import bt747.sys.JavaLibBridge;

/**
 * The model in the Model-View-Controller pattern. Information regarding the
 * state of the GPS device or settings must use this model interface.
 * 
 * @author Mario De Weerd
 * 
 */
public class Model extends AppSettings implements GPSListener, EventPoster {

    private final PrivateData data = new PrivateData();

    /**
     * The low level communication class with the GPS device. Needed by the
     * gpsModel.
     */
    private GPSrxtx gpsRxTx;

    /**
     * Indicate that the log type does not reference any log type.
     */
    public static final int NO_LOG_LOGTYPE = 0;

    /**
     * CSV log type (Comma Separated Values).
     */
    public static final int CSV_LOGTYPE = 1;

    /**
     * GMAP log type (Google Map - html output).
     */
    public static final int GMAP_LOGTYPE = 2;

    /**
     * GPX log type (gpx format).
     */
    public static final int GPX_LOGTYPE = 3;

    /**
     * KML log type ('Google Earth' format).
     */
    public static final int KML_LOGTYPE = 4;

    /**
     * NMEA log type (NMEA strings - text format - similar to GPS output).
     */
    public static final int NMEA_LOGTYPE = 5;

    /**
     * Compe GPS log type (Writes PLT and WPT files).
     */
    public static final int PLT_LOGTYPE = 6;

    /**
     * log type (Writes TRK and WPT files).
     */
    public static final int TRK_LOGTYPE = 7;

    /**
     * log type (BIN log type).
     */
    public static final int BIN_LOGTYPE = 8;

    /**
     * log type (TRL holux log type).
     */
    public static final int TRL_LOGTYPE = 9;

    /**
     * log type (TRL holux log type).
     */
    public static final int SR_LOGTYPE = 10;

    /**
     * log type (KMZ log type).<br>
     * Not provided by the default controller since not implemented on all
     * systems.<br>
     * Log type provided for application implementation.
     */
    public static final int KMZ_LOGTYPE = 11;

    /**
     * log type (Array log type - must call specific function to get array).<br>
     * Not provided by the default controller since not implemented on all
     * systems.<br>
     * Log type provided for application implementation.
     */
    public static final int ARRAY_LOGTYPE = 12;

    /**
     * log type (Array log type - must call specific function to get array).<br>
     * Not provided by the default controller since not implemented on all
     * systems.<br>
     * Log type provided for application implementation.
     */
    public static final int MULTI_LOGTYPE = 13;

    /**
     * GPX for OSM log type (gpx format) [same as GPX but limited output).
     */
    public static final int OSM_LOGTYPE = 14;

    /**
     * Upload GPX for OSM (upload handled by caller, specific output file).
     */
    public static final int OSM_UPLOAD_LOGTYPE = 15;

    /**
     * Call upon an external tool.
     */
    public static final int EXTERNAL_LOGTYPE = 16;

    /**
     * Get static google map.
     */
    public static final int GOOGLE_MAP_STATIC_URL_LOGTYPE = 17;

    /**
     * Get Sql commands.
     */
    public static final int SQL_LOGTYPE = 18;

    /**
     * Get PostGis commands.
     */
    public static final int POSTGIS_LOGTYPE = 19;

    /**
     * The number of seconds in a day.
     */
    private static final int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * Start date for the date filter. 01/01/1983 is the earliest date that
     * can be logged. This ensures that all logged data points are retrieved
     * by default if the date filter is not changed.
     */
    private int filterStartTime;

    /**
     * End date for the date filter. Defaults to end of current day.
     */
    private int filterEndTime;

    /**
     * Indicate which conversion is ongoing. Helps for GUI interface.
     */
    private int lastConversionOngoing = Model.NO_LOG_LOGTYPE;

    /**
     * True when a log conversion is ongoing.
     */
    private boolean conversionOngoing = false;

    /**
     * The number of filters that are needed. Currently 2 filters: trackpoint
     * filter and waypoint filter.
     */
    private static final int C_NBR_FILTERS = 2;

    /**
     * Standard log filters.
     */
    private GPSFilter[] logFilters = new GPSFilter[Model.C_NBR_FILTERS];

    /**
     * Advanced log filters.
     */
    private GPSFilterAdvanced[] logFiltersAdv = new GPSFilterAdvanced[Model.C_NBR_FILTERS];

    /**
     * The default constructor of the model.
     */
    public Model() {
        super();
        for (int i = 0; i < logFilters.length; i++) {
            logFilters[i] = new GPSFilter();
            logFiltersAdv[i] = new GPSFilterAdvanced();
        }
        filterStartTime = (JavaLibBridge.getDateInstance(1, 1, 1983).dateToUTCepoch1970());
        filterEndTime = (JavaLibBridge.getDateInstance()).dateToUTCepoch1970() + (Model.SECONDS_PER_DAY - 1);
        gpsRxTx = new GPSrxtx();
        data.device = new DeviceDirector();
        final int protocol = getIntOpt(AppSettings.DEVICE_PROTOCOL);
        setProtocol(protocol);
    }

    protected final void setProtocol(int protocol) {
        if (data.device.model != null) {
            data.device.model.removeListener(this);
        }
        data.device.setProtocol(gpsRxTx, protocol);
        data.device.model.addListener(this);
    }

    /**
     * @return The gpsModel instantiation.
     */
    protected final gps.mvc.GpsModel gpsM() {
        return data.gpsM();
    }

    /**
     * @return The gpsController instantiation.
     */
    protected final gps.mvc.GpsController gpsC() {
        return data.gpsC();
    }

    /**
     * @return The Mtk GPS instantiation.
     */
    protected final gps.mvc.MtkController gpsMtkC() {
        return data.device.mtkControl;
    }

    /**
     * @return The gpsRxTx instantiation (low level communication)
     */
    protected final GPSrxtx gpsRxTx() {
        return gpsRxTx;
    }

    /**
     * Determine the connection status
     * 
     * @return true if the connection is made
     */
    public final boolean isConnected() {
        return gpsRxTx.isConnected();
    }

    /**
     * @return Get the number of commands waiting for a response.
     */
    public final int getOutstandingCommandsCount() {
        return gpsM().getOutStandingCmdsCount();
    }

    /**
     * @return The type of the last conversion that was ongoing
     *         lastConversionOngoing
     */
    public final int getLastConversionOngoing() {
        return lastConversionOngoing;
    }

    /**
     * @param pLastConversionOngoing
     *                the lastConversionOngoing to set
     */
    protected final void setLastConversionOngoing(final int pLastConversionOngoing) {
        lastConversionOngoing = pLastConversionOngoing;
    }

    /**
     * @return the conversionOngoing
     */
    public final boolean isConversionOngoing() {
        return conversionOngoing;
    }

    /**
     * Get the log point validity mask.
     * 
     * @param logFilterType
     *                The type of log filter that we request the information
     *                for (index of the log filter).
     * 
     * @return The log point validity filter mask. This corresponds to 'no
     *         fix', '2d fix', '3d fix', 'Estimated', ... filtering.
     */
    public final int getValidMask(final int logFilterType) {
        switch(logFilterType) {
            case GPSFilter.WAYPT:
                return getIntOpt(AppSettings.WAYPT_VALID);
            case GPSFilter.TRKPT:
                return getIntOpt(TRKPT_VALID);
            default:
                return 0;
        }
    }

    /**
     * Get the record reason mask.
     * 
     * @param logFilterType
     *                The type of log filter that we request the information
     *                for (index of the log filter).
     * 
     * @return The record reason filter mask. This corresponds to time, speed,
     *         distance, button, ... record reasons filtering.
     */
    public final int getRcrMask(final int logFilterType) {
        switch(logFilterType) {
            case GPSFilter.WAYPT:
                return getIntOpt(WAYPT_RCR);
            case GPSFilter.TRKPT:
                return getIntOpt(TRKPT_RCR);
            default:
                return 0;
        }
    }

    /**
     * Get the standard log filters.
     * 
     * @return the logFiltersAdv
     */
    protected final GPSFilterAdvanced[] getLogFiltersAdv() {
        setupAdvancedFilters();
        return logFiltersAdv;
    }

    /**
     * Multiplier to convert floating *DOP value to int value for device.
     */
    private static final int XDOP_FLOAT_TO_INT_100 = 100;

    private void setupBasicSettingsFilter(final GPSFilter[] logFilters) {
        logFilters[GPSFilter.TRKPT].setRcrMask(getIntOpt(TRKPT_RCR));
        logFilters[GPSFilter.TRKPT].setValidMask(getIntOpt(TRKPT_VALID));
        logFilters[GPSFilter.WAYPT].setRcrMask(getIntOpt(WAYPT_RCR));
        logFilters[GPSFilter.WAYPT].setValidMask(getIntOpt(WAYPT_VALID));
    }

    ;

    /**
     * @return the logFilters
     */
    public final GPSFilter[] getLogFilters() {
        setupBasicSettingsFilter(logFilters);
        return logFilters;
    }

    /**
     * Sets up the filters based on settings.
     */
    private void setupAdvancedFilters() {
        final GPSFilterAdvanced[] filters = logFiltersAdv;
        setupBasicSettingsFilter(filters);
        for (int i = logFiltersAdv.length - 1; i >= 0; i--) {
            final GPSFilterAdvanced filter = filters[i];
            filter.setMinRecCount(getIntOpt(MIN_RECCOUNT));
            filter.setMaxRecCount(getIntOpt(MAX_RECCOUNT));
            filter.setMinSpeed(getFloatOpt(MIN_SPEED));
            filter.setMaxSpeed(getFloatOpt(MAX_SPEED));
            filter.setMinDist(getFloatOpt(MIN_DISTANCE));
            filter.setMaxDist(getFloatOpt(MAX_DISTANCE));
            filter.setMaxPDOP((int) (getFloatOpt(MAX_PDOP) * Model.XDOP_FLOAT_TO_INT_100));
            filter.setMaxHDOP((int) (getFloatOpt(MAX_HDOP) * Model.XDOP_FLOAT_TO_INT_100));
            filter.setMaxVDOP((int) (getFloatOpt(MAX_VDOP) * Model.XDOP_FLOAT_TO_INT_100));
            filter.setMinNSAT(getIntOpt(MIN_NSAT));
        }
    }

    /**
     * Get the filter start date.
     * 
     * @return the startDate
     */
    public final int getFilterStartTime() {
        return filterStartTime;
    }

    /**
     * Set the start date for the filters.
     * 
     * @param filterStartTime
     *                the startDate to set
     */
    protected final void setFilterStartTime(final int filterStartTime) {
        this.filterStartTime = filterStartTime;
    }

    /**
     * Get the filter end time.
     * 
     * @return the filter end time.
     */
    public final int getFilterEndTime() {
        return filterEndTime;
    }

    /**
     * Set the filter end date.
     * 
     * @param filterEndTime
     *                the endDate to set
     */
    protected final void setFilterEndTime(final int filterEndTime) {
        this.filterEndTime = filterEndTime;
    }

    /**
     * Indicate that the log conversion started for the given log type.
     * 
     * @param outputLogType
     *                The log type for which the log conversion started.
     */
    protected final void logConversionStarted(final int outputLogType) {
        lastConversionOngoing = outputLogType;
        conversionOngoing = true;
        postEvent(ModelEvent.CONVERSION_STARTED);
    }

    /**
     * Indicate that the log conversion ended for the given log type.
     * 
     * @param outputLogType
     *                The log type for which the log conversion started.
     */
    protected final void logConversionEnded(final int outputLogType) {
        conversionOngoing = false;
        postEvent(ModelEvent.CONVERSION_ENDED);
    }

    /**
     * Get the start address for the log download. To be used for the download
     * progress bar.
     * 
     * @return the startAddr
     */
    public final int getStartAddr() {
        return gpsM().getStartAddr();
    }

    /**
     * Get the end address for the log download. To be used for the download
     * progress bar.
     * 
     * @return the endAddr
     */
    public final int getEndAddr() {
        return gpsM().getEndAddr();
    }

    /**
     * Get 'download ongoing' status.
     * 
     * @return true if the download is currently ongoing. This is usefull for
     *         the download progress bar.
     */
    public final boolean isDownloadOnGoing() {
        return gpsM().isLogDownloadOnGoing();
    }

    /**
     * Get the log address that we are now expecting to receive data for. This
     * is usefull for the download progress bar.
     * 
     * @return the nextReadAddr
     */
    public final int getNextReadAddr() {
        return gpsM().getNextReadAddr();
    }

    private int downloadMethod = Model.DOWNLOAD_SMART;

    /**
     * Get the 'incremental download' status.
     * 
     * @deprecated
     * @return true if the Incremental log download is activated.
     */
    public final boolean isIncremental() {
        return downloadMethod == Model.DOWNLOAD_SMART;
    }

    /**
     * Set the 'incremental download' configuration.
     * 
     * @deprecated
     * @param incrementalDownload
     *                true if the log download should be incremental.
     */
    protected final void setIncremental(final boolean incrementalDownload) {
        if (incrementalDownload) {
            downloadMethod = Model.DOWNLOAD_SMART;
        } else {
            downloadMethod = Model.DOWNLOAD_FILLED;
        }
        postEvent(ModelEvent.INCREMENTAL_CHANGE);
    }

    /**
     * Download reported filled memory.
     */
    public static final int DOWNLOAD_FILLED = 0;

    /**
     * Download reported memory - incremental.
     */
    public static final int DOWNLOAD_SMART = 1;

    /**
     * Download full memory.
     */
    public static final int DOWNLOAD_FULL = 2;

    /**
     * Get the download method.
     * 
     * @return The set download method. Possible values:
     *         {@link #DOWNLOAD_FILLED} {@link #DOWNLOAD_FULL}
     *         {@link #DOWNLOAD_SMART}
     */
    public final int getDownloadMethod() {
        return downloadMethod;
    }

    /**
     * Set the download method.<br>
     * Possible values: {@link #DOWNLOAD_FILLED} {@link #DOWNLOAD_FULL}
     * {@link #DOWNLOAD_SMART}
     */
    public final void setDownloadMethod(final int downloadMethod) {
        this.downloadMethod = downloadMethod;
        postEvent(ModelEvent.DOWNLOAD_METHOD_CHANGE);
    }

    /**
     * @return The gpsModel instantiation.
     */
    public final MtkModel mtkModel() {
        return data.device.mtkModel;
    }

    /**
     * Get the 'logging activation' status of the device.
     * 
     * @return true if the device is currently logging positions to memory.
     */
    public final boolean isLoggingActive() {
        return mtkModel().isLoggingActive();
    }

    /**
     * Value provided by the logger indicating that the device needs to be
     * initialised/formatted.
     * 
     * @return true if the logger needs formatting.
     */
    public final boolean isLoggerNeedsFormat() {
        return mtkModel().isLoggerNeedsFormat();
    }

    /**
     * Get memory size.
     * 
     * @return The ammount of memory available in bytes.
     */
    public final int logMemSize() {
        return mtkModel().getLogMemSize();
    }

    /**
     * Get the amount of memory that is filled with data on the device.
     * 
     * @return The amount of memory used in bytes. This includes the memory
     *         used for other information than locations (e.g., headers)
     */
    public final int logMemUsed() {
        return mtkModel().getLogMemUsed();
    }

    /**
     * Get the amount of memory that is filled with data on the device.
     * 
     * @return The amount of memory used in percent. The percentage represents
     *         the fraction of useful memory. The device may have more memory,
     *         but not all memory can be used to log actual points.
     */
    public final int logMemUsedPercent() {
        return mtkModel().logMemUsedPercent;
    }

    /**
     * Get the number of positions that are currently stored in the memory of
     * the GPS device.
     * 
     * @return The number of logged positions.
     */
    public final int logNbrLogPts() {
        return mtkModel().logNbrLogPts;
    }

    /**
     * Get the SW Logger Version of the MTK device.
     * 
     * @return Version of the MTK Logger SW.
     */
    public final String getMtkLogVersion() {
        return mtkModel().getMtkLogVersion();
    }

    public final String getMainVersion() {
        return mtkModel().getMainVersion();
    }

    public final String getFirmwareVersion() {
        return mtkModel().getFirmwareVersion();
    }

    public final String getModelStr() {
        return mtkModel().getModel();
    }

    /**
     * Get the manufacturer id of the flash memory.
     * 
     * @return The letter code of the manufacturer.
     */
    public final int getFlashManuProdID() {
        return mtkModel().getFlashManuProdID();
    }

    public final String getFlashDesc() {
        return mtkModel().getFlashDesc();
    }

    public final int getLogFormat() {
        return mtkModel().getLogFormat();
    }

    public final boolean isHolux() {
        return mtkModel().isHolux();
    }

    public final int logMemUsefullSize() {
        gpsC().setDataNeeded(MtkModel.DATA_FLASH_TYPE);
        return mtkModel().logMemUsefullSize();
    }

    public final int logFreeMemUsefullSize() {
        gpsC().setDataNeeded(MtkModel.DATA_FLASH_TYPE);
        gpsC().setDataNeeded(MtkModel.DATA_MEM_USED);
        return mtkModel().logFreeMemUsefullSize();
    }

    public final int getDtUpdateRate() {
        return mtkModel().getDtUpdateRate();
    }

    public final int getDtGLL_Period() {
        return mtkModel().getDtGLL_Period();
    }

    public final int getDtRMC_Period() {
        return mtkModel().getDtRMC_Period();
    }

    public final int getDtVTG_Period() {
        return mtkModel().getDtVTG_Period();
    }

    public final int getDtGSA_Period() {
        return mtkModel().getDtGSA_Period();
    }

    public final int getDtGSV_Period() {
        return mtkModel().getDtGSV_Period();
    }

    public final int getDtGGA_Period() {
        return mtkModel().getDtGGA_Period();
    }

    public final int getDtZDA_Period() {
        return mtkModel().getDtZDA_Period();
    }

    public final int getDtMCHN_Period() {
        return mtkModel().getDtMCHN_Period();
    }

    public final int getDtBaudRate() {
        return mtkModel().getDtBaudRate();
    }

    public final int getDtUserOptionTimesLeft() {
        return mtkModel().getDtUserOptionTimesLeft();
    }

    public final String getHoluxName() {
        return mtkModel().getHoluxName();
    }

    public final String getBTAddr() {
        return mtkModel().getBtMacAddr();
    }

    public final int getNMEAPeriod(final int i) {
        return mtkModel().getNMEAPeriod(i);
    }

    public final int getLogTimeInterval() {
        return mtkModel().getLogTimeInterval();
    }

    public final int getLogSpeedInterval() {
        return mtkModel().getLogSpeedInterval();
    }

    public final int getLogDistanceInterval() {
        return mtkModel().getLogDistanceInterval();
    }

    public final int getLogFixPeriod() {
        return mtkModel().getLogFixPeriod();
    }

    public final int getDgpsMode() {
        return mtkModel().getDgpsMode();
    }

    /**
     * Get the devices 'log overwrite' or 'log stop when full' status.
     * 
     * @return true - The device will overwrite data when the logger memory is
     *         full.<br>
     *         false - The device will stop logging data when the logger
     *         memory is full.
     */
    public final boolean isLogFullOverwrite() {
        return mtkModel().isLogFullOverwrite();
    }

    public final boolean isInitialLogOverwrite() {
        return mtkModel().isInitialLogOverwrite();
    }

    public final boolean isSBASEnabled() {
        return mtkModel().isSBASEnabled();
    }

    public final boolean isSBASTestEnabled() {
        return mtkModel().isSBASTestEnabled();
    }

    public final boolean isPowerSaveEnabled() {
        return mtkModel().isPowerSaveEnabled();
    }

    public final int getDatum() {
        return mtkModel().getDatum();
    }

    /**
     * Get the maximum number of record that can be logged with the provided
     * log format.
     * 
     * @param logFormat
     *                The log format to use to calculate the number of
     *                records.
     * @return The number of records that can be logged at most.
     */
    public final int getEstimatedNbrRecords(final int logFormat) {
        int count = 0;
        try {
            int size = BT747Constants.logRecordAndChecksumSize(logFormat, getCurrentGpsType(), 12);
            if (size != 0) {
                count = logMemUsefullSize() / size;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private final int getCurrentGpsType() {
        if (isHolux() && gpsRxTx.isConnected()) {
            return BT747Constants.GPS_TYPE_HOLUX_M241;
        }
        return getIntOpt(AppSettings.GPSTYPE);
    }

    public final int getEstimatedNbrRecordsFree(final int logFormat) {
        int count = 0;
        try {
            int size = BT747Constants.logRecordAndChecksumSize(logFormat, getCurrentGpsType(), 12);
            if (size != 0) {
                count = logFreeMemUsefullSize() / size;
            }
        } catch (final Exception e) {
            Generic.debug("EstRecords", e);
        }
        return count;
    }

    /**
     * Get the debug status. This concerns 'high level' debug information.
     * 
     * @return true - Debug is active
     */
    public static final boolean isDebug() {
        return Generic.isDebug();
    }

    /**
     * Indicates if the given data is available. That means that a request was
     * issued to the device and that the device responded with a value.
     * 
     * @param dataType
     *            For example: {@link MtkModel#DATA_MEM_USED}.
     * @return true if data is already available.
     */
    public final boolean isAvailable(final int dataType) {
        return mtkModel().isDataAvailable(dataType);
    }

    /**
     * Get the connection debug status. This concerns the low level debug
     * information regarding the GPS connection.
     * 
     * @return true - connection debug is active.
     */
    public final boolean isDebugConn() {
        return gpsRxTx.isDebugConn();
    }

    public final void gpsEvent(final GpsEvent event) {
        postEvent(new ModelEvent(event));
    }

    /**
     * This private data class enables forcing the use of the getter and the
     * setter.
     * 
     * @author Mario De Weerd
     * 
     */
    private static final class PrivateData {

        private DeviceDirector device;

        protected final gps.mvc.GpsModel gpsM() {
            return device.model;
        }

        protected final gps.mvc.GpsController gpsC() {
            return device.devController;
        }
    }

    /**
	 * Create a LocationSender instance. Currently this is hard coded to be an
	 * HttpLocationSender however this may be changed to create other Subclasses
	 * of LocationSender perhaps based on application settings. If available a
	 * LocationSenderFactory should then be used.
	 * 
	 * @return a new LocationSender instance
	 * @author Florian Unger
	 */
    LocationSender createAndConfigureLocationSender() {
        HttpLocationSender ls = new HttpLocationSender(this);
        ls.setUpdatePeriod(1000 * getIntOpt(AppSettings.POS_SRV_PERIOD));
        ls.setTargetFile(getStringOpt(AppSettings.POS_SRV_FILE));
        ls.setTargetHostname(getStringOpt(AppSettings.POS_SRV_HOSTNAME));
        ls.setTargetPort(getIntOpt(AppSettings.POS_SRV_PORT));
        ls.setUser(getStringOpt(POS_SRV_USER));
        ls.setPassword(getStringOpt(POS_SRV_PASS));
        if (isConnected()) {
            ls.setBluetoothAdress(getBTAddr());
        }
        return ls;
    }
}

package org.matsim.contrib.matsim4opus.constants;

import org.apache.log4j.Logger;

/**
 * @author thomas
 *
 */
public class Constants {

    private static final Logger log = Logger.getLogger(Constants.class);

    /** important system environments */
    public static String OPUS_HOME;

    public static String OPUS_DATA_PATH;

    /** subdirectories in OPUS_HOME */
    public static String MATSIM_4_OPUS;

    public static String MATSIM_4_OPUS_CONFIG;

    public static String MATSIM_4_OPUS_OUTPUT;

    public static String MATSIM_4_OPUS_TEMP;

    public static String MATSIM_4_OPUS_BACKUP;

    /**
	 * Apply a new root path for the OPUS_HOME directory
	 * @param opusHome path to the new OPUS_HOME Directory
	 */
    public static void setOpusHomeDirectory(String opusHome) {
        if (!opusHome.endsWith("/")) opusHome += "/";
        OPUS_HOME = opusHome;
        OPUS_DATA_PATH = opusHome + "data/";
        MATSIM_4_OPUS = opusHome + "opus_matsim/";
        MATSIM_4_OPUS_CONFIG = opusHome + "opus_matsim/matsim_config/";
        MATSIM_4_OPUS_OUTPUT = opusHome + "opus_matsim/output/";
        MATSIM_4_OPUS_TEMP = opusHome + "opus_matsim/tmp/";
        MATSIM_4_OPUS_BACKUP = opusHome + "opus_matsim/backup/";
        log.info("");
        log.info("Set OPUS_HOME to :" + OPUS_HOME);
        log.info("Set OPUS_DATA_PATH to :" + OPUS_DATA_PATH);
        log.info("Set OPUS MATSim directory to :" + MATSIM_4_OPUS);
        log.info("Set MATSim config directory to :" + MATSIM_4_OPUS_CONFIG);
        log.info("Set OPUS MATSim output directory to :" + MATSIM_4_OPUS_OUTPUT);
        log.info("Set OPUS MATSim temp directory to :" + MATSIM_4_OPUS_TEMP);
        log.info("Set OPUS MATSim backup directory to :" + MATSIM_4_OPUS_BACKUP);
        log.info("");
    }

    /** subdirectories in MATSim */
    public static final String MATSIM_WORKING_DIRECTORY = System.getProperty("user.dir");

    /** file names */
    public static final String GENERATED_PLANS_FILE_NAME = "output_plans.xml.gz";

    public static final String SCORESTATS_FILE_NAME = "scorestats.txt";

    public static final String TRAVELDISTANCESSTAT_FILE_NAME = "traveldistancestats.txt";

    public static final String LOG_FILE_NAME = "logfile.log";

    public static final String LOG_FILE_WARNINGS_ERRORS_NAME = "logfileWarningsErrors.log";

    public static final String OUTPUT_CONFIG_FILE_NAME = "output_config.xml.gz";

    public static final String OUTPUT_NETWORK_FILE_NAME = "output_network.xml.gz";

    public static final String GENERATED_MATSIM_CONFIG_FILE_NAME = "MATSimConfigFile.xml.gz";

    public static final String URBANSIM_PARCEL_DATASET_TABLE = "parcel__dataset_table__exported_indicators__";

    public static final String URBANSIM_PERSON_DATASET_TABLE = "person__dataset_table__exported_indicators__";

    public static final String URBANSIM_JOB_DATASET_TABLE = "job__dataset_table__exported_indicators__";

    /** file type */
    public static final String FILE_TYPE_TAB = ".tab";

    public static final String FILE_TYPE_CSV = ".csv";

    public static final String FILE_TYPE_DBL = ".dbl";

    public static final String FILE_TYPE_ESRI = ".esri";

    public static final String FILE_TYPE_GZ = ".gz";

    public static final String FILE_TYPE_XML = ".xml";

    public static final String FILE_TYPE_KMZ = ".kmz";

    public static final String FILE_TYPE_TXT = ".txt";

    /** matsim output files */
    public static final String OUTPUT_PLANS_FILE_GZ = "output_plans.xml.gz";

    public static final String OUTPUT_PLANS_FILE_XML = "output_plans.xml";

    public static final String OUTPUT_PARCEL_FILE_GZ = "parcels.xml.gz";

    public static final String OUTPUT_PARCEL_FILE_XML = "parcels.xml";

    public static final String OUTPUT_ZONES_FILE_GZ = "zones.xml.gz";

    public static final String OUTPUT_ZONES_FILE_XML = "zones.xml";

    public static final String TRAVEL_DATA_FILE_CSV = "travel_data.csv";

    public static final String GRID_DATA_FILE_CSV = "grid_data.csv";

    public static final String ZONES_FILE_CSV = "zones.csv";

    public static final String ZONES_COMPLETE_FILE_CSV = "zones_complete.csv";

    /** parameter for computing urbansim data */
    public static final String TAB_SEPERATOR = "[\t\n]+";

    public static final String TAB = "\t";

    public static final String NEW_LINE = "\r\n";

    public static final String PARCEL_ID = "parcel_id";

    public static final String PERSON_ID = "person_id";

    public static final String JOB_ID = "job_id";

    public static final String PARCEL_ID_HOME = "parcel_id_home";

    public static final String PARCEL_ID_WORK = "parcel_id_work";

    public static final String ZONE_ID_WORK = "zone_id_work";

    public static final String X_COORDINATE = "x_coord_sp";

    public static final String Y_COORDINATE = "y_coord_sp";

    public static final String ZONE_ID = "zone_id";

    public static final String FACILITY_DESCRIPTION = "urbansim location";

    public static final String ACT_HOME = "home";

    public static final String ACT_WORK = "work";

    /** xsd on matsim.org */
    public static final String CURRENT_MATSIM_4_URBANSIM_XSD_MATSIMORG = "http://matsim.org/files/dtd/matsim4urbansim_v1.xsd";

    public static final String CURRENT_MATSIM_4_URBANSIM_XSD_SOURCEFOREGE = "https://matsim.svn.sourceforge.net/svnroot/matsim/matsim/trunk/dtd/matsim4urbansim_v1.xsd";

    public static final String CURRENT_MATSIM_4_URBANSIM_XSD_LOCALJAR = "/dtd/matsim4urbansim_v1.xsd";

    public static final String CURRENT_XSD_FILE_NAME = "matsim4urbansim_v1.xsd";

    public static final String V1_MATSIM_4_URBANSIM_XSD_MATSIMORG = "http://matsim.org/files/dtd/matsim4urbansim_v1.xsd";

    public static final String V1_MATSIM_4_URBANSIM_XSD_SOURCEFOREGE = "https://matsim.svn.sourceforge.net/svnroot/matsim/matsim/trunk/dtd/matsim4urbansim_v1.xsd";

    public static final String V1_MATSIM_4_URBANSIM_XSD_LOCALJAR = "/dtd/matsim4urbansim_v1.xsd";

    public static final String V1_XSD_FILE_NAME = "matsim4urbansim_v1.xsd";

    public static final String V11_MATSIM_4_URBANSIM_XSD_MATSIMORG = "http://matsim.org/files/dtd/matsim4urbansim_v1.1.xsd";

    public static final String V11_MATSIM_4_URBANSIM_XSD_SOURCEFOREGE = "https://matsim.svn.sourceforge.net/svnroot/matsim/matsim/trunk/dtd/matsim4urbansim_v1.1.xsd";

    public static final String V11_MATSIM_4_URBANSIM_XSD_LOCALJAR = "/dtd/matsim4urbansim_v1.1.xsd";

    public static final String V11_XSD_FILE_NAME = "matsim4urbansim_v1.1.xsd";

    public static final String JAXB_PARSER_PACKAGE_NAME = "matsim4urbansim.jaxbconfig";

    /** MATSim properties */
    public static final String MATSIM_PROPERTIES_FILE = "matsim.properties";

    public static final String FIRST_ITERATION = "firstIteration";

    public static final String LAST_ITERATION = "lastIteration";

    public static final String NUMBER_OF_ITERATIONS_PER_RUN = "numberOfIterationPerRun";

    public static final String PLANS_FILE_PATH = "inputPlansFile";

    public static final String RUN_NUMBER = "run";

    public static final String PATH_TO_GENERATED_MATSIM_CONFIG_FILE = "generatedMATSimConfigPath";

    /** MATSim config modules */
    public static final String MATSIM_CONFIG_MODULE_CONTROLLER = "controler";

    public static final String MATSIM_CONFIG_MODULE_PLANS = "plans";

    public static final String MATSIM_CONFIG_MODULE_URBANSIM_PARAMETER = "urbansimParameter";

    public static final String MATSIM_CONFIG_PARAMETER_SAMPLING_RATE = "samplingRate";

    public static final String MATSIM_CONFIG_PARAMETER_YEAR = "year";

    public static final String MATSIM_CONFIG_PARAMETER_TEMP_DIRECTORY = "tempDirectory";

    /** test run */
    public static final int TEST_RUN_SUCCESSFUL = 0;

    public static final int TEST_RUN_FAILD = -1;

    /** exit codes */
    public static final int NOT_VALID_PATH = 0;

    public static final int MATSIM_PROPERTIES_FILE_NOT_FOUND = 1;

    public static final int CONFIG_OBJECT_NOT_INITIALIZED = 2;

    public static final int EXCEPTION_OCCURED = 3;

    public static final int UNMARSCHALLING_FAILED = 4;

    /** MATSim 4 UrbanSim parameter names **/
    public static final String MATSIM_4_URBANSIM_PARAM = "matsim4urbansim";

    public static final String IS_TEST_RUN = "isTestRun";

    public static final String SAMPLING_RATE = "samplingRate";

    public static final String OPUS_HOME_PARAM = "opusHomeParam";

    public static final String OPUS_DATA_PATH_PARAM = "opusDataPathParam";

    public static final String MATSIM_4_OPUS_PARAM = "matsim4OpusParam";

    public static final String MATSIM_4_OPUS_CONFIG_PARAM = "matsim4OpusConfigParam";

    public static final String MATSIM_4_OPUS_OUTPUT_PARAM = "matsim4OpusOutputParam";

    public static final String MATSIM_4_OPUS_TEMP_PARAM = "matsim4OpusTempParam";

    public static final String MATSIM_4_OPUS_BACKUP_PARAM = "matsim4OpusBackupParam";

    public static final String YEAR = "year";

    public static final String BACKUP_RUN_DATA_PARAM = "backupRunDataParam";

    public static final String TEST_PARAMETER_PARAM = "testParameter";

    public static final String COMPUTE_ZONE_2_ZONE_IMPEDANCE = "compute_zone2zone_impedance";

    public static final String COMPUTE_LOGSUM = "compute_logsum";

    public static final String RETURN_RAW_SUM = "return_raw_sum";

    public static final String BETA = "beta";

    public static final String BETA_TRAVEL_TIMES = "beta_travel_times";

    public static final String BETA_LN_TRAVEL_TIMES = "beta_ln_travel_times";

    public static final String BETA_POWER_TRAVEL_TIMES = "beta_power_travel_times";

    public static final String BETA_TRAVEL_COSTS = "beta_travel_costs";

    public static final String BETA_LN_TRAVEL_COSTS = "beta_ln_travel_costs";

    public static final String BETA_POWER_TRAVEL_COSTS = "beta_power_travel_costs";

    public static final String BETA_TRAVEL_DISTANCE = "beta_travel_distance";

    public static final String BETA_LN_TRAVEL_DISTANCE = "beta_ln_travel_distance";

    public static final String BETA_POWER_TRAVEL_DISTANCE = "beta_power_travel_distance";

    public static final String CUSTOM_PARAMETER = "custom_parameter";

    public static final String MEASUREMENT_LOGFILE = "psrc_log.txt";

    public static final String MATSIM_MODE = "matsim_mode";

    public static final String TARGET_LOCATION_HOT_START_PLANS_FILE = "target_location_for_hotstart_plans_file";

    /** ERSA output file header items */
    public static final String ERSA_ZONE_ID = "zone_id";

    public static final String ERSA_ZONE_X_COORD = "x_coord_zonecentroid";

    public static final String ERSA_ZONE_Y_COORD = "y_coord_zonecentroid";

    public static final String ERSA_PARCEL_ID = "parcel_id";

    public static final String ERSA_PERSON_ID = "person_id";

    public static final String ERSA_JOB_ID = "job_id";

    public static final String ERSA_NEARESTNODE_ID = "nearest_node_id";

    public static final String ERSA_NEARESTNODE_X_COORD = "x_coord_nn";

    public static final String ERSA_NEARESTNODE_Y_COORD = "y_coord_nn";

    public static final String ERSA_SQUARE_X_COORD = "x_coord_square";

    public static final String ERSA_SQUARE_Y_COORD = "y_coord_square";

    public static final String ERSA_X_COORDNIATE = "x_coordinate";

    public static final String ERSA_Y_COORDINATE = "y_coordinate";

    public static final String ERSA_TRAVEL_TIME_ACCESSIBILITY = "travel_time_accessibility";

    public static final String ERSA_TRAVEL_COST_ACCESSIBILITY = "travel_cost_accessibility";

    public static final String ERSA_TRAVEL_DISTANCE_ACCESSIBILITY = "travel_distance_accessibility";

    public static final String ERSA_CONGESTED_TRAVEL_TIME_ACCESSIBILITY = "congested_travel_time_accessibility";

    public static final String ERSA_FREESPEED_TRAVEL_TIME_ACCESSIBILITY = "freespeed_travel_time_accessibility";

    public static final String ERSA_WALK_TRAVEL_TIME_ACCESSIBILITY = "walk_travel_time_accessibility";

    public static final String ERSA_CENTROID_ACCESSIBILITY = "centroid_accessibility";

    public static final String ERSA_MEAN_ACCESSIBILITY = "mean_accessibility";

    public static final String ERSA_DERIVATION_ACCESSIBILITY = "derivation_accessibility";

    public static final String ERSA_CENTROID = "centroid";

    public static final String ERSA_MEAN = "mean";

    public static final String ERSA_DERIVATION = "derivation";

    public static final String ERSA_PERSONS_COUNT = "persons";

    public static final String ERSA_WORKPLACES_COUNT = "workplaces";

    /** Spatial IDs and spatial conversion factors */
    public static final int SRID_WASHINGTON_NORTH = 2926;

    public static final int SRID_SWITZERLAND = 21781;

    public static final double FEET_IN_METER_CONVERSION_FACTOR = 0.3048;

    public static final double METER_IN_FEET_CONVERSION_FACTOR = 3.280839895013124;

    /** MATSim 4 UrbanSim parameter values as strings **/
    public static final String TRUE = "true";

    public static final String FALSE = "false";

    /** MATSim Modes **/
    public static final String COLD_START = "cold_start";

    public static final String WARM_START = "warm_start";

    public static final String HOT_START = "hot_start";

    /** MATSim4OPUS TEST data folder structure */
    public static final String MATSIM_TEST_DATA_WARM_START_URBANSIM_OUTPUT = "warmstart/urbanSimOutput";

    public static final String MATSIM_TEST_DATA_WARM_START_INPUT_PLANS = "warmstart/inputPlan";

    public static final String MATSIM_TEST_DATA_WARM_START_NETWORK = "warmstart/network";

    public static final String MATSIM_TEST_DATA_DEFAULT_URBANSIM_OUTPUT = "urbanSimOutput";
}

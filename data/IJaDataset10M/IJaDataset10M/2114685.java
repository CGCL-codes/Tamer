package net.sf.orcc.backends.c.quasistatic.scheduler.util;

import java.io.File;

/**
 * Global constants
 * 
 * @author Victor Martin
 */
public class Constants {

    public static final String ACCODED = "ACCODED";

    public static final String BORDERLINE_ACTOR = "Borderline Actor";

    public static final String CALML_FILE_PATH = "CALML files" + File.separator;

    public static final String CONFIG_PATH = "config";

    public static final int CREATING_DSE_SCHEDULING = 90;

    public static final String CREATING_DSE_SCHEDULING_LABEL = "Creating DSE Scheduling";

    public static final int CREATING_SCHEDULE_FOR_BTYPE = 15;

    public static final String CREATING_SCHEDULE_FOR_BTYPE_LABEL = "Creating schedule";

    public static String CUSTOM_GENERAL_BUFFER_SIZE = "custom_general_buffer_size";

    public static String CUSTOM_INDIVIDUAL_BUFFERS_SIZES = "custom_individual_buffers_sizes";

    public static final String DSE_INPUT_PATH = "DSE Input";

    public static final String FINISHED_LABEL = "Process finished";

    public static final String FOURMV = "FOURMV";

    /**
	 * For parse input XDF file
	 */
    public static String INPUT_FILE_NAME = "QSB_input.xdf";

    public static final String INTER = "INTER";

    public static final String INTRA = "INTRA";

    public static final String INTRA_AND_INTER = "COMBINED";

    public static final String MOTION = "MOTION";

    public static final String ND_ACTOR = "ND Actor";

    public static final String NEWVOP = "NEWVOP";

    /**
	 * Kind of networks files
	 */
    public static final String NL_FILE = "nl";

    public static final String NOT_ASSIGNED = "not assigned";

    public static final int PARSING_CALML_FILES = 9;

    public static final String PARSING_CALML_FILES_LABEL = "Parsing CALML files";

    public static final int PARSING_XML_FILES = 5;

    public static final String PARSING_XML_FILES_LABEL = "Parsing XML files";

    /**
	 * Back-end constants
	 */
    public static final String PATH_NOT_ASSIGNED = "Path not assigned";

    public static final int PROCESS_FINISHED = 100;

    public static final String PROJECT_DEFAULT_CALML_FILES_PATH_PROPERTY_NAME = "DEFAULT_CALML_FILES_PATH";

    public static final String PROJECT_DEFAULT_NL_FILE_PATH_PROPERTY_NAME = "DEFAULT_NL_FILE_PATH";

    public static final String PROJECT_DEFAULT_OUTPUT_FILES_PATH_PROPERTY_NAME = "DEFAULT_OUTPUT_FILES_PATH";

    public static final String PROJECT_DEFAULT_WORKING_DIRECTORY_PATH_PROPERTY_NAME = "DEFAULT_WORKING_DIRECTORY_PATH";

    public static final String PROJECT_LAST_UPDATE_PROPERTY_NAME = "LAST_UPDATE";

    /**
	 * For properties file
	 */
    public static final String PROJECT_NAME_PROPERTY_NAME = "PROJECT_NAME";

    public static final String PROJECT_VERSION_PROPERTY_NAME = "PROJECT_VERSION";

    public static String QS_SCHEDULER = "qs_scheduler";

    public static final String SCHEDULE_FILES_PATH = "schedule files" + File.separator + "generated" + File.separator;

    public static final int SEPARATING_ACTORS = 14;

    public static final String SEPARATING_ACTORS_LABEL = "Separating_Actors";

    public static final String STARTED_LABEL = "Started";

    /**
	 * To represent kind of actors
	 */
    public static final String STATIC_ACTOR = "Static Actor";

    public static final String STOPPED_LABEL = "Stopped";

    /**
	 * For user interface
	 */
    public static final int STOPPED_PERCENT = 0;

    public static String TOKENS_PATTERN = "tokens_pattern";

    public static final String VARIABLE_TOKEN = "VARIABLE_TOKEN";

    public static final String XDF_FILE = "xdf";

    public static final String XDF_FILE_PATH = "XDF files" + File.separator;

    public static final String ZEROMV = "ZEROMV";
}

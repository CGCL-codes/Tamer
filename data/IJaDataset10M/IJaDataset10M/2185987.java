package net.sourceforge.modelintegra.core.configuration;

import org.apache.log4j.Logger;

public class ConfigurationHelper {

    public static enum ReportTypeEnum {

        STANDARD, USERSPECIFIC, QRCLASSDIAGRAM, QRDIAGRAMSONLY
    }

    ;

    public static ReportTypeEnum REPORT_TYPE = null;

    public static String REPORT_OUTPUT_DIRECTORY = null;

    public static boolean REPORT_EXPORT_DIAGRAMS = false;

    public static String REPORT_DEFAULT_WORD_TEMPLATE = null;

    public static boolean REPORT_GLOSSARY = true;

    public static boolean TESTCASE_GENERATION = false;

    public static boolean TESTCASE_GENERATION_EXPORTDIAGRAMS = false;

    public static boolean ITERATION_MNGMT = false;

    public static boolean TRACEABILITY_TESTCASES = false;

    public static String TRACEABILITY_TESTCASES_IMPORT_FILE = null;

    public static boolean TRACEABILITY_REQUIREMENTS = false;

    public static String TRACEABILITY_REQUIREMENTS_IMPORT_FILE = null;

    public static boolean TRACEABILITY_BUSINESSPROCESSFUNCTIONS = false;

    public static String TRACEABILITY_BUSINESSPROCESSFUNCTIONS_IMPORT_FILE = null;

    public static String INTERNAL_REPORT_OUTPUT_DIRECTORY_CONTENT = null;

    public static boolean TRACEABILITY_SRC = false;

    public static String TRACEABILITY_SRC_PATH = null;

    private static final Logger LOGGER = Logger.getLogger(ConfigurationHelper.class.getName());

    private ConfigurationHelper() {
    }

    public static void addConfigurationItem(String name, String value) {
        if (name.equals("REPORT_TYPE") && value != null && (value.equals(ReportTypeEnum.STANDARD.name()) || value.equals(ReportTypeEnum.USERSPECIFIC.name()) || value.equals(ReportTypeEnum.QRCLASSDIAGRAM.name()) || value.equals(ReportTypeEnum.QRDIAGRAMSONLY.name()))) {
            REPORT_TYPE = ReportTypeEnum.valueOf(value);
        } else if (name.equals("REPORT_OUTPUT_DIRECTORY") && value != null) {
            REPORT_OUTPUT_DIRECTORY = value;
            INTERNAL_REPORT_OUTPUT_DIRECTORY_CONTENT = REPORT_OUTPUT_DIRECTORY + "\\content\\";
        } else if (name.equals("REPORT_DEFAULT_WORD_TEMPLATE") && value != null) {
            REPORT_DEFAULT_WORD_TEMPLATE = value;
        } else if (name.equals("TRACEABILITY_TESTCASES_IMPORT_FILE") && value != null) {
            TRACEABILITY_TESTCASES_IMPORT_FILE = value;
        } else if (name.equals("TRACEABILITY_REQUIREMENTS_IMPORT_FILE") && value != null) {
            TRACEABILITY_REQUIREMENTS_IMPORT_FILE = value;
        } else if (name.equals("TRACEABILITY_BUSINESSPROCESSFUNCTIONS_IMPORT_FILE") && value != null) {
            TRACEABILITY_BUSINESSPROCESSFUNCTIONS_IMPORT_FILE = value;
        } else if (name.equals("TRACEABILITY_SRC_PATH") && value != null) {
            TRACEABILITY_SRC_PATH = value;
        } else {
            LOGGER.error("Configuration item: " + name + " not valid!");
            throw new RuntimeException();
        }
        if (value.length() == 0) {
            LOGGER.error("Default value of Configuration item: " + name + " cannot be empty!");
            throw new RuntimeException();
        }
    }

    public static void addConfigurationItem(String name, boolean value) {
        if (name.equals("REPORT_EXPORT_DIAGRAMS")) {
            REPORT_EXPORT_DIAGRAMS = value;
        } else if (name.equals("REPORT_GLOSSARY")) {
            REPORT_GLOSSARY = value;
        } else if (name.equals("TESTCASE_GENERATION")) {
            TESTCASE_GENERATION = value;
        } else if (name.equals("TESTCASE_GENERATION_EXPORTDIAGRAMS")) {
            TESTCASE_GENERATION_EXPORTDIAGRAMS = value;
        } else if (name.equals("ITERATION_MNGMT")) {
            ITERATION_MNGMT = value;
        } else if (name.equals("TRACEABILITY_TESTCASES")) {
            TRACEABILITY_TESTCASES = value;
        } else if (name.equals("TRACEABILITY_REQUIREMENTS")) {
            TRACEABILITY_REQUIREMENTS = value;
        } else if (name.equals("TRACEABILITY_BUSINESSPROCESSFUNCTIONS")) {
            TRACEABILITY_BUSINESSPROCESSFUNCTIONS = value;
        } else if (name.equals("TRACEABILITY_SRC")) {
            TRACEABILITY_SRC = value;
        } else {
            LOGGER.error("Configuration item: " + name + " not valid!");
            throw new RuntimeException();
        }
    }

    public static void check() {
        boolean valid = true;
        if (REPORT_TYPE == null) {
            LOGGER.error("Invalid configuration: REPORT_TYPE");
            throw new RuntimeException();
        }
        if (REPORT_OUTPUT_DIRECTORY == null) {
            LOGGER.error("Invalid configuration: REPORT_OUTPUT_DIRECTORY");
            throw new RuntimeException();
        }
        if (TESTCASE_GENERATION == false) {
            if (TESTCASE_GENERATION_EXPORTDIAGRAMS == true) {
                LOGGER.error("Invalid configuration: TESTCASE_GENERATION");
                throw new RuntimeException();
            }
        }
        if (TRACEABILITY_SRC == true) {
            if (TRACEABILITY_SRC_PATH == null) {
                LOGGER.error("Invalid configuration: TRACEABILITY_SRC");
                throw new RuntimeException();
            }
        }
        if (valid == false) {
            LOGGER.error("Configuration is not valid!");
            throw new RuntimeException();
        }
    }
}

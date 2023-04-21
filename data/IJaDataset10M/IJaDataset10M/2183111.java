package purej.context;

import java.util.Map;
import java.util.HashMap;
import purej.web.context.WebAppContext;

/**
 * EAF ���ø����̼� ���ؽ�Ʈ
 * 
 * @author SangBoo Lee
 * @since 2006.01.01
 * 
 */
public class ApplicationContext implements java.io.Serializable {

    /**
     * <code>serialVersionUID</code>�� ����
     */
    private static final long serialVersionUID = -4232921910165350071L;

    public static final String DEV_MODE = "DEV";

    public static final String PRD_MODE = "PRD";

    private String SYSTEM_OS;

    private String JAVA_HOME;

    private String JAVA_VENDOR;

    private String JAVA_VERSION;

    private String WAS_VENDOR;

    private String APPLICATION_NAME;

    private String APPLICATION_VERSION;

    private String APPLICATION_MODE;

    private String APPLICATION_DESCRIPTION;

    private String APPLICATION_START_DATE;

    private String FRAMEWORK_REPOSITORY_PATH;

    private String FRAMEWORK_CONSOLE_LOGIN_ID;

    private String FRAMEWORK_CONSOLE_LOGIN_PASSWORD;

    private String CONTROLLER_SUFFIX_NAME;

    private boolean CONTROLLER_LOGGING;

    private boolean CONTROLLER_PARAMETER_LOGGING;

    private String VIEW_SUFFIX_NAME;

    private boolean VIEW_LOGGING;

    private String VIEW_DEFAULT_LOCALE;

    private String VIEW_DEFAULT_THEME;

    private boolean VIEW_CLIENT_VALIDATION;

    private String SERVICE_CONFIG_PATH;

    private boolean SERVICE_LOGGING;

    private String DAO_SQL_STORAGE_PATH;

    private String DAO_AUTO_INCREMENT_DATA_GENERATER;

    private String DAO_TRANSACTION_TYPE;

    private boolean DAO_AUTO_TRANSACTION;

    private boolean DAO_TRANSACTION_LOGGING;

    private boolean DAO_CONNECTION_LOGGING;

    private boolean DAO_SQL_LOGGING;

    private String MODULE_JOB_SCHEDULE_CONFIG_PATH;

    private String MODULE_CACHE_CONFIG_PATH;

    private String MODULE_RULE_STORAGE_PATH;

    private String MODULE_LOG_STORAGE_PATH;

    private String WAS_DATASOURCE_JNDI_NAME;

    private String WAS_USER_TRANSACTION_JNDI_NAME;

    private String WAS_WORK_MANAGER_JNDI_NAME;

    private String WAS_TIMER_JNDI_NAME;

    private Map<String, WebAppContext> WEB_CONFIG_MAP = new HashMap<String, WebAppContext>();

    /**
     * @return Returns the aPPLICATION_DESCRIPTION.
     */
    public String getAPPLICATION_DESCRIPTION() {
        return APPLICATION_DESCRIPTION;
    }

    /**
     * @return the aPPLICATION_MODE
     */
    public String getAPPLICATION_MODE() {
        return APPLICATION_MODE;
    }

    /**
     * @return the aPPLICATION_NAME
     */
    public String getAPPLICATION_NAME() {
        return APPLICATION_NAME;
    }

    /**
     * @return the aPPLICATION_START_DATE
     */
    public String getAPPLICATION_START_DATE() {
        return APPLICATION_START_DATE;
    }

    /**
     * @return the aPPLICATION_VERSION
     */
    public String getAPPLICATION_VERSION() {
        return APPLICATION_VERSION;
    }

    /**
     * @return cONTROLLER_SUFFIX_NAME�� �����մϴ�.
     */
    public String getCONTROLLER_SUFFIX_NAME() {
        return CONTROLLER_SUFFIX_NAME;
    }

    /**
     * @return Returns the dAO_AUTO_INCREMENT_DATA_GENERATER.
     */
    public String getDAO_AUTO_INCREMENT_DATA_GENERATER() {
        return DAO_AUTO_INCREMENT_DATA_GENERATER;
    }

    /**
     * @return the sQL_STORAGE_PATH
     */
    public String getDAO_SQL_STORAGE_PATH() {
        return DAO_SQL_STORAGE_PATH;
    }

    /**
     * @return Returns the dAO_TRANSACTION_TYPE.
     */
    public String getDAO_TRANSACTION_TYPE() {
        return DAO_TRANSACTION_TYPE;
    }

    /**
     * @return the dATASOURCE_JNDI_NAME
     */
    public String getWAS_DATASOURCE_JNDI_NAME() {
        return WAS_DATASOURCE_JNDI_NAME;
    }

    /**
     * @return the fRAMEWORK_CONSOLE_LOGIN_ID
     */
    public String getFRAMEWORK_CONSOLE_LOGIN_ID() {
        return FRAMEWORK_CONSOLE_LOGIN_ID;
    }

    /**
     * @return the fRAMEWORK_CONSOLE_LOGIN_PASSWORD
     */
    public String getFRAMEWORK_CONSOLE_LOGIN_PASSWORD() {
        return FRAMEWORK_CONSOLE_LOGIN_PASSWORD;
    }

    /**
     * @return the jAVA_HOME
     */
    public String getJAVA_HOME() {
        return JAVA_HOME;
    }

    /**
     * @return the jAVA_VENDOR
     */
    public String getJAVA_VENDOR() {
        return JAVA_VENDOR;
    }

    /**
     * @return the jAVA_VERSION
     */
    public String getJAVA_VERSION() {
        return JAVA_VERSION;
    }

    /**
     * @return mODULE_JOB_SCHEDULE_CONFIG_PATH�� �����մϴ�.
     */
    public String getMODULE_JOB_SCHEDULE_CONFIG_PATH() {
        return MODULE_JOB_SCHEDULE_CONFIG_PATH;
    }

    /**
     * @return the sERVICE_CONFIG_PATH
     */
    public String getSERVICE_CONFIG_PATH() {
        return SERVICE_CONFIG_PATH;
    }

    /**
     * @return the sYSTEM_OS
     */
    public String getSYSTEM_OS() {
        return SYSTEM_OS;
    }

    /**
     * @return Returns the vIEW_DEFAULT_LOCALE.
     */
    public String getVIEW_DEFAULT_LOCALE() {
        return VIEW_DEFAULT_LOCALE;
    }

    /**
     * @return Returns the vIEW_DEFAULT_THEME.
     */
    public String getVIEW_DEFAULT_THEME() {
        return VIEW_DEFAULT_THEME;
    }

    /**
     * @return the vIEW_SUFFIX_NAME
     */
    public String getVIEW_SUFFIX_NAME() {
        return VIEW_SUFFIX_NAME;
    }

    /**
     * @return Returns the tIMER_JNDI_NAME.
     */
    public String getWAS_TIMER_JNDI_NAME() {
        return WAS_TIMER_JNDI_NAME;
    }

    /**
     * @return the uSER_TRANSACTION_JNDI_NAME
     */
    public String getWAS_USER_TRANSACTION_JNDI_NAME() {
        return WAS_USER_TRANSACTION_JNDI_NAME;
    }

    /**
     * @return the wAS_VENDOR
     */
    public String getWAS_VENDOR() {
        return WAS_VENDOR;
    }

    /**
     * @return Returns the wORK_MANAGER_JNDI_NAME.
     */
    public String getWAS_WORK_MANAGER_JNDI_NAME() {
        return WAS_WORK_MANAGER_JNDI_NAME;
    }

    /**
     * @return cONTROLLER_LOGGING�� �����մϴ�.
     */
    public boolean isCONTROLLER_LOGGING() {
        return CONTROLLER_LOGGING;
    }

    /**
     * @return cONTROLLER_PARAMETER_LOGGING�� �����մϴ�.
     */
    public boolean isCONTROLLER_PARAMETER_LOGGING() {
        return CONTROLLER_PARAMETER_LOGGING;
    }

    /**
     * @return Returns the dAO_AUTO_TRANSACTION.
     */
    public boolean isDAO_AUTO_TRANSACTION() {
        return DAO_AUTO_TRANSACTION;
    }

    /**
     * @return Returns the dAO_CONNECTION_LOGGING.
     */
    public boolean isDAO_CONNECTION_LOGGING() {
        return DAO_CONNECTION_LOGGING;
    }

    /**
     * @return Returns the dAO_SQL_LOGGING.
     */
    public boolean isDAO_SQL_LOGGING() {
        return DAO_SQL_LOGGING;
    }

    /**
     * @return Returns the dAO_TRANSACTION_LOGGING.
     */
    public boolean isDAO_TRANSACTION_LOGGING() {
        return DAO_TRANSACTION_LOGGING;
    }

    /**
     * @return Returns the sERVICE_LOGGING.
     */
    public boolean isSERVICE_LOGGING() {
        return SERVICE_LOGGING;
    }

    /**
     * @return Returns the vIEW_CLIENT_VALIDATION.
     */
    public boolean isVIEW_CLIENT_VALIDATION() {
        return VIEW_CLIENT_VALIDATION;
    }

    /**
     * @return Returns the vIEW_LOGGING.
     */
    public boolean isVIEW_LOGGING() {
        return VIEW_LOGGING;
    }

    /**
     * @param application_description
     *                The aPPLICATION_DESCRIPTION to set.
     */
    public void setAPPLICATION_DESCRIPTION(String application_description) {
        APPLICATION_DESCRIPTION = application_description;
    }

    /**
     * @param application_mode
     *                the aPPLICATION_MODE to set
     */
    public void setAPPLICATION_MODE(String application_mode) {
        APPLICATION_MODE = application_mode;
    }

    /**
     * @param application_name
     *                the aPPLICATION_NAME to set
     */
    public void setAPPLICATION_NAME(String application_name) {
        APPLICATION_NAME = application_name;
    }

    /**
     * @param application_start_date
     *                the aPPLICATION_START_DATE to set
     */
    public void setAPPLICATION_START_DATE(String application_start_date) {
        APPLICATION_START_DATE = application_start_date;
    }

    /**
     * @param application_version
     *                the aPPLICATION_VERSION to set
     */
    public void setAPPLICATION_VERSION(String application_version) {
        APPLICATION_VERSION = application_version;
    }

    /**
     * @param controller_logging
     *                �����Ϸ��� cONTROLLER_LOGGING.
     */
    public void setCONTROLLER_LOGGING(boolean controller_logging) {
        CONTROLLER_LOGGING = controller_logging;
    }

    /**
     * @param controller_parameter_logging
     *                �����Ϸ��� cONTROLLER_PARAMETER_LOGGING.
     */
    public void setCONTROLLER_PARAMETER_LOGGING(boolean controller_parameter_logging) {
        CONTROLLER_PARAMETER_LOGGING = controller_parameter_logging;
    }

    /**
     * @param controller_suffix_name
     *                �����Ϸ��� cONTROLLER_SUFFIX_NAME.
     */
    public void setCONTROLLER_SUFFIX_NAME(String controller_suffix_name) {
        CONTROLLER_SUFFIX_NAME = controller_suffix_name;
    }

    /**
     * @param dao_auto_increment_data_generater
     *                The dAO_AUTO_INCREMENT_DATA_GENERATER to set.
     */
    public void setDAO_AUTO_INCREMENT_DATA_GENERATER(String dao_auto_increment_data_generater) {
        DAO_AUTO_INCREMENT_DATA_GENERATER = dao_auto_increment_data_generater;
    }

    /**
     * @param dao_auto_transaction
     *                The dAO_AUTO_TRANSACTION to set.
     */
    public void setDAO_AUTO_TRANSACTION(boolean dao_auto_transaction) {
        DAO_AUTO_TRANSACTION = dao_auto_transaction;
    }

    /**
     * @param dao_connection_logging
     *                The dAO_CONNECTION_LOGGING to set.
     */
    public void setDAO_CONNECTION_LOGGING(boolean dao_connection_logging) {
        DAO_CONNECTION_LOGGING = dao_connection_logging;
    }

    /**
     * @param dao_sql_logging
     *                The dAO_SQL_LOGGING to set.
     */
    public void setDAO_SQL_LOGGING(boolean dao_sql_logging) {
        DAO_SQL_LOGGING = dao_sql_logging;
    }

    /**
     * @param sql_storage_path
     *                the sQL_STORAGE_PATH to set
     */
    public void setDAO_SQL_STORAGE_PATH(String sql_storage_path) {
        DAO_SQL_STORAGE_PATH = sql_storage_path;
    }

    /**
     * @param dao_transaction_logging
     *                The dAO_TRANSACTION_LOGGING to set.
     */
    public void setDAO_TRANSACTION_LOGGING(boolean dao_transaction_logging) {
        DAO_TRANSACTION_LOGGING = dao_transaction_logging;
    }

    /**
     * @param dao_transaction_type
     *                The dAO_TRANSACTION_TYPE to set.
     */
    public void setDAO_TRANSACTION_TYPE(String dao_transaction_type) {
        DAO_TRANSACTION_TYPE = dao_transaction_type;
    }

    /**
     * @param framework_console_login_id
     *                the fRAMEWORK_CONSOLE_LOGIN_ID to set
     */
    public void setFRAMEWORK_CONSOLE_LOGIN_ID(String framework_console_login_id) {
        FRAMEWORK_CONSOLE_LOGIN_ID = framework_console_login_id;
    }

    /**
     * @param framework_console_login_password
     *                the fRAMEWORK_CONSOLE_LOGIN_PASSWORD to set
     */
    public void setFRAMEWORK_CONSOLE_LOGIN_PASSWORD(String framework_console_login_password) {
        FRAMEWORK_CONSOLE_LOGIN_PASSWORD = framework_console_login_password;
    }

    /**
     * @param java_home
     *                the jAVA_HOME to set
     */
    public void setJAVA_HOME(String java_home) {
        JAVA_HOME = java_home;
    }

    /**
     * @param java_vendor
     *                the jAVA_VENDOR to set
     */
    public void setJAVA_VENDOR(String java_vendor) {
        JAVA_VENDOR = java_vendor;
    }

    /**
     * @param java_version
     *                the jAVA_VERSION to set
     */
    public void setJAVA_VERSION(String java_version) {
        JAVA_VERSION = java_version;
    }

    /**
     * @param module_job_schedule_config_path
     *                �����Ϸ��� mODULE_JOB_SCHEDULE_CONFIG_PATH.
     */
    public void setMODULE_JOB_SCHEDULE_CONFIG_PATH(String module_job_schedule_config_path) {
        MODULE_JOB_SCHEDULE_CONFIG_PATH = module_job_schedule_config_path;
    }

    /**
     * @param service_config_path
     *                the sERVICE_CONFIG_PATH to set
     */
    public void setSERVICE_CONFIG_PATH(String service_config_path) {
        SERVICE_CONFIG_PATH = service_config_path;
    }

    /**
     * @param service_logging
     *                The sERVICE_LOGGING to set.
     */
    public void setSERVICE_LOGGING(boolean service_logging) {
        SERVICE_LOGGING = service_logging;
    }

    /**
     * @param system_os
     *                the sYSTEM_OS to set
     */
    public void setSYSTEM_OS(String system_os) {
        SYSTEM_OS = system_os;
    }

    /**
     * @param view_client_validation
     *                The vIEW_CLIENT_VALIDATION to set.
     */
    public void setVIEW_CLIENT_VALIDATION(boolean view_client_validation) {
        VIEW_CLIENT_VALIDATION = view_client_validation;
    }

    /**
     * @param view_default_locale
     *                The vIEW_DEFAULT_LOCALE to set.
     */
    public void setVIEW_DEFAULT_LOCALE(String view_default_locale) {
        VIEW_DEFAULT_LOCALE = view_default_locale;
    }

    /**
     * @param view_default_theme
     *                The vIEW_DEFAULT_THEME to set.
     */
    public void setVIEW_DEFAULT_THEME(String view_default_theme) {
        VIEW_DEFAULT_THEME = view_default_theme;
    }

    /**
     * @param view_logging
     *                The vIEW_LOGGING to set.
     */
    public void setVIEW_LOGGING(boolean view_logging) {
        VIEW_LOGGING = view_logging;
    }

    /**
     * @param view_SUFFIX_name
     *                the vIEW_SUFFIX_NAME to set
     */
    public void setVIEW_SUFFIX_NAME(String view_SUFFIX_name) {
        VIEW_SUFFIX_NAME = view_SUFFIX_name;
    }

    /**
     * @param datasource_jndi_name
     *                the dATASOURCE_JNDI_NAME to set
     */
    public void setWAS_DATASOURCE_JNDI_NAME(String datasource_jndi_name) {
        WAS_DATASOURCE_JNDI_NAME = datasource_jndi_name;
    }

    /**
     * @param timer_jndi_name
     *                The tIMER_JNDI_NAME to set.
     */
    public void setWAS_TIMER_JNDI_NAME(String timer_jndi_name) {
        WAS_TIMER_JNDI_NAME = timer_jndi_name;
    }

    /**
     * @param user_transaction_jndi_name
     *                the uSER_TRANSACTION_JNDI_NAME to set
     */
    public void setWAS_USER_TRANSACTION_JNDI_NAME(String user_transaction_jndi_name) {
        WAS_USER_TRANSACTION_JNDI_NAME = user_transaction_jndi_name;
    }

    /**
     * @param was_vendor
     *                the wAS_VENDOR to set
     */
    public void setWAS_VENDOR(String was_vendor) {
        WAS_VENDOR = was_vendor;
    }

    /**
     * @param work_manager_jndi_name
     *                The wORK_MANAGER_JNDI_NAME to set.
     */
    public void setWAS_WORK_MANAGER_JNDI_NAME(String work_manager_jndi_name) {
        WAS_WORK_MANAGER_JNDI_NAME = work_manager_jndi_name;
    }

    public String getFRAMEWORK_REPOSITORY_PATH() {
        return FRAMEWORK_REPOSITORY_PATH;
    }

    public void setFRAMEWORK_REPOSITORY_PATH(String config_base_path) {
        FRAMEWORK_REPOSITORY_PATH = config_base_path;
    }

    public Map<String, WebAppContext> getWEB_CONFIG_MAP() {
        return WEB_CONFIG_MAP;
    }

    public void setWEB_CONFIG_MAP(Map<String, WebAppContext> web_config_map) {
        WEB_CONFIG_MAP = web_config_map;
    }

    public String getMODULE_CACHE_CONFIG_PATH() {
        return MODULE_CACHE_CONFIG_PATH;
    }

    public void setMODULE_CACHE_CONFIG_PATH(String module_cache_config_path) {
        MODULE_CACHE_CONFIG_PATH = module_cache_config_path;
    }

    public String getMODULE_RULE_STORAGE_PATH() {
        return MODULE_RULE_STORAGE_PATH;
    }

    public void setMODULE_RULE_STORAGE_PATH(String module_rule_storage_path) {
        MODULE_RULE_STORAGE_PATH = module_rule_storage_path;
    }

    public String getMODULE_LOG_STORAGE_PATH() {
        return MODULE_LOG_STORAGE_PATH;
    }

    public void setMODULE_LOG_STORAGE_PATH(String module_log_storage_path) {
        MODULE_LOG_STORAGE_PATH = module_log_storage_path;
    }
}

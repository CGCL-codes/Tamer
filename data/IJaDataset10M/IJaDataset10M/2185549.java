package eu.vph.predict.vre.in_silico.exception;

/**
 * Class holding static finals for message displays.
 *
 * @author Geoff Williams
 */
public class InSilicoMessageKeys {

    /** Problem with VFS FileType mismatches */
    public static final String VFS_FILE_TYPE_MISMATCH = "vfs.fileTypeMismatch";

    private static final String BUSINESS_IN_SILICO_ERROR_MESSAGES_PREFIX = "iserror.business_";

    /** User needs to provide a(nother) authentication token */
    public static final String BUSINESS_REQUIRE_AUTHENTICATION_TOKEN = BUSINESS_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("require_authentication_token");

    private static final String SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX = "iserror.system_";

    /** system operation problem - failed to run locally */
    public static final String SYSTEM_LOCAL_RUNNER_FAILED = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("local_runner_failed");

    /** system operation problem - failed to read a resource parameter file */
    public static final String SYSTEM_FAILED_PARAMETER_FILE_READ = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("failed_parameter_file_read");

    /** system operation problem - failed to write a parameter file */
    public static final String SYSTEM_FAILED_PARAMETER_FILE_WRITE = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("failed_parameter_file_write");

    /** system operation problem - job count vs. compression count mismatch */
    public static final String SYSTEM_FAILED_JOB_DIRECTORY_COMPRESSION = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("failed_job_directory_compression");

    /** system config problem - application configurations not defined */
    public static final String SYSTEM_CONFIG_APP_CONFIGS = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("config_app_configs");

    /** system config problem - simulation environment version */
    public static final String SYSTEM_CONFIG_SIM_ENV_VER = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("config_sim_env_ver");

    /** system config problem - simulation environmnent */
    public static final String SYSTEM_CONFIG_SIM_ENV = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("config_sim_env");

    /** system config problem - invalid simulation environment parameter use */
    public static final String SYSTEM_CONFIG_SIM_ENV_PARAM = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("invalid_sim_env_parameter");

    /** system config problem - indeterminable compute resource */
    public static final String SYSTEM_CONFIG_SIM_INDETERMINABLE_COMPUTE_RESOURCE = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("indeterminable_compute_resource");

    /** system config problem - no parameter file modifier mediator found for simulation */
    public static final String SYSTEM_CONFIG_SIM_NO_MEDIATOR = SYSTEM_IN_SILICO_ERROR_MESSAGES_PREFIX.concat("no_mediator_found");

    private static final String SYSTEM_IN_SILICO_WARN_MESSAGES_PREFIX = "iswarn.";

    public static final String SIM_FAILED_REMOTE_RESOURCE_MATCH = SYSTEM_IN_SILICO_WARN_MESSAGES_PREFIX.concat("failed_remote_resource_match");

    private static final String SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX = "isinfo.";

    public static final String CHECK_RES_FOR_SIM_RUN = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("check_res_for_sim_run");

    public static final String CREATE_JOB_PARAMETER_FILES = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("create_job_param_files");

    public static final String CREATE_JOB_RUN_ENVIRONMENT = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("create_job_run_env");

    public static final String FIRE_JMS_MESSAGE_TO_RUN_CONTROLLER = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("fire_jms_to_controller");

    public static final String INVOKE_LOCAL_INVOCATION_SCRIPTS = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("invoke_local_invocation_scripts");

    public static final String JOB_INVOCATION_SUCCESSFUL = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("job_invocation_successful");

    public static final String JOB_PROCESS_MONITORING = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("job_process_monitoring");

    public static final String SIM_ALREADY_RUN = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_already_run");

    public static final String SIM_COMPRESSING_JOB_FILES = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_compressing_job_files");

    public static final String SIM_CREATE_JDL = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_create_jdl");

    public static final String SIM_DELETED = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_deleted");

    public static final String SIM_DETERMINING_COMPUTE_RESOURCE = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_determining_compute_resource");

    public static final String SIM_REMOTE_JOB_STATUS = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_job_status");

    public static final String SIM_REMOTE_RUN_FILE_DOWNLOADING = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_file_downloading");

    public static final String SIM_REMOTE_RUN_FILE_UPLOADING = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_file_uploading");

    public static final String SIM_REMOTE_RUN_FILES_DOWNLOADED = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_files_downloaded");

    public static final String SIM_REMOTE_RUN_COMPLETED = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_completed");

    public static final String SIM_REMOTE_RUN_SUBMITTED = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_submitted");

    public static final String SIM_REMOTE_RUN_SUBMITTING = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_remote_run_submitting");

    public static final String SIM_SPLITTING_JOBS_PREPARATION = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_splitting_preparation");

    public static final String SIM_RUN_STARTING = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_run_starting");

    public static final String SIM_RUNNING_LOCALLY = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_running_locally");

    public static final String SIM_RUNNING_REMOTELY = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_running_remotely");

    public static final String SIM_TOO_MANY_JOBS_FOR_LOCAL = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_too_many_jobs_for_local");

    public static final String SIM_TRANSFER_CONTROL_TO_JOB_MGNT = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_transfer_to_job_mgnt");

    public static final String SIM_UPLOADING_COMPLETED = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_uploading_completed");

    public static final String SIM_UPLOADING_RESULTS = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("sim_uploading_results");

    public static final String READ_PARAMETER_FILES = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("read_parameter_files");

    public static final String RETR_RES_FOR_SIM_RUN = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("retr_res_for_sim_run");

    public static final String MESSAGE_ISINFO_NO_RESULTS_REDIRECT = SYSTEM_IN_SILICO_INFO_MESSAGES_PREFIX.concat("no_results_redirect_progress");

    public static final String MESSAGE_APPLICATION_RESOURCE = "application.resource";

    public static final String MESSAGE_RESOURCE_PROVIDER = "compute.resource_provider";

    public static final String MESSAGE_ISGENERAL_IMAGINARY = "isgeneral.imaginary";

    public static final String MESSAGE_ISGENERAL_FILE = "isgeneral.file";

    public static final String MESSAGE_ISGENERAL_FILEORFOLDER = "isgeneral.fileOrFolder";

    public static final String MESSAGE_ISGENERAL_FOLDER = "isgeneral.folder";

    public static final String MESSAGE_SIMULATION = "simulation.simulation";

    public static final String MESSAGE_SIMULATION_ENVIRONMENT = "simulation.simulation_environment";

    public static final String MESSAGE_SIMULATION_JOB = "simulation.job";

    public static final String MESSAGE_SIMULATION_TYPE = "simulation.simulation_type";

    private InSilicoMessageKeys() {
    }
}

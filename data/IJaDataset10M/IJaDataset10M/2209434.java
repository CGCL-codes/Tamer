package eu.vph.predict.vre.in_silico.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service interface for messaging.
 *
 * @author Geoff Williams
 */
public interface MessageService {

    public static final char TRACE_PREFIX = 'T';

    public static final char DEBUG_PREFIX = 'D';

    public static final char INFO_PREFIX = 'I';

    public static final char WARN_PREFIX = 'W';

    public static final char ERROR_PREFIX = 'E';

    public static final char FATAL_PREFIX = 'F';

    public static final String CODE_DEBUG_WAITING_FOR_PROGRESS_FILE = DEBUG_PREFIX + "1";

    public static final String CODE_DEBUG_PROGRESS_FILE_REPORTS_COMPLETED = DEBUG_PREFIX + "2";

    public static final String CODE_DEBUG_PROCESS_STATUS = DEBUG_PREFIX + "10";

    public static final String CODE_DEBUG_PROCESS_ENDED = DEBUG_PREFIX + "11";

    public static final String CODE_DEBUG_INTERNAL = DEBUG_PREFIX + "20";

    public static final String CODE_DEBUG_INTERNAL_STATUS_CREATION = DEBUG_PREFIX + "21";

    public static final String CODE_INFO_INTERNAL = INFO_PREFIX + "1";

    public static final String CODE_INFO_PROGRESS_FILE_STATUS = INFO_PREFIX + "2";

    public static final String CODE_INFO_RESULTS_UPLOADED = INFO_PREFIX + "3";

    public static final String CODE_WARN_INTERNAL = WARN_PREFIX + "1";

    public static final String CODE_WARN_PROGRESS_FILE_OPEN_FAIL = WARN_PREFIX + "2";

    public static final String CODE_WARN_PROGRESS_MONITOR_RUN_FAIL = WARN_PREFIX + "3";

    public static final String CODE_WARN_RESULTS_UPLOAD_FAIL = WARN_PREFIX + "4";

    public static final String CODE_WARN_PROCESSING_TOOL_UPLOAD_FAIL = WARN_PREFIX + "5";

    public static final String CODE_ERROR_SYSTEM = ERROR_PREFIX + "1";

    public static final String CODE_ERROR_LOCAL_RUNNER_RUN_FAIL = ERROR_PREFIX + "2";

    /** Component name for consistent reference */
    public static final String COMPONENT_MESSAGE_SERVICE = "componentMessageService";

    /** Date format */
    public static final String STANDARD_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
   * Assign process status for the simulation/simulationJob, with additional detail, from external
   * operations
   * 
   * @param simulationId Simulation persistence identity
   * @param target Item to set progress status of, e.g. simulation, or simulation job identifier (jobIdentifier).
   * @param status Textual representation of status.
   * @param statusCode Codified status, e.g. I4 (representing Info code 4)
   * @param host Host name of the sender the progress status.
   * @param source Where (e.g. programmatic, remote web service) the request came from to set the progress status.
   * @param processId Process number of simulation.
   */
    public void assignProgressStatusExternal(String simulationId, String target, String status, String statusCode, String host, String source, String processId);

    /**
   * Assign process status for the simulation/simulationJob from internal operations.
   * 
   * @param simulationId Simulation persistence identity
   * @param target Item to set progress status of, e.g. simulation, or simulation job identifier (jobIdentifier).
   * @param status Textual representation of status.
   * @param statusCode Codified status, e.g. I4 (representing Info code 4), defaults to debug if null.
   */
    public void assignProgressStatusInternal(String simulationId, String target, String[] status, String statusCode);

    /**
   * Translate on message and any nested messages for placement into message, according to locale.
   * <p />
   * In some situations the objects substituted into the message will themselves be translatable 
   * message keys, e.g. <tt>animals_fr.properties</tt> has entry <tt>animals.identified_cat=Le chat {0}</tt>,
   * where the <tt>{0}</tt> parameter would be represented in <tt>colours_fr.properties</tt>, i.e.
   * le chat noir.
   *  
   * @param message
   * @param nestedMessages
   * @param locale
   * @return
   */
    public String messageTranslator(final String message, final Object[] nestedMessages, final Locale locale);

    /**
   * Retrieve a progress status.
   * 
   * @param simulationId Simulation persistence identity
   * @param target Item to set progress status of, e.g. simulation, or simulation job identifier (jobIdentifier).
   * @param logLevel Desired minimum log level (debug, info, warn, etc) to retrieve status information.
   * @param timed Time from which status information to be returned, e.g. progress status since time X!, or null for all.
   * @param locale Locale for message translation.
   * @return Progress status map.
   */
    public List<Map<String, String>> retrieveProgressStatuses(String simulationId, String target, String logLevel, String timed, String locale);
}

package au.edu.archer.metadata.mdsr.config;

/**
 * Interface to provide application-wide constants.
 *
 * @author alabri
 */
public interface RepositoryConstants {

    String INIT_STATUS_OK = "status.ok";

    String PROPS_PATH = "properties.path";

    String LOG_PROPS = "log4j.properties";

    String OBJ_TEMP_PATH = "object.template.path";

    String STATUS_CLOSING = "refuse.new";

    String FEDORA_REPOSITORY_URL = "url.fedora.host";

    String MDSR_HOST_URL = "url.mdsr.host";

    String FEDORA_USERNAME = "fedora.username";

    String FEDORA_PASSWORD = "fedora.password";

    String CACHE_PATH = "resource.cache.path";

    String ALLOW_MSS = "allow.mss";

    String OBJECT_NAMESPACE = "object:";

    String MDSR_IDENTIFIER_TEMPLATE = "@MDSR_IDENTIFIER@";

    String PID_TEMPLATE = "_PID_";

    String STATE_TEMPLATE = "@STATE@";

    String LABEL_TEMPLATE = "@LABEL@";

    String CONTENT_MODEL_TEMPLATE = "@CONTENT_MODEL@";

    String OWNER_ID_TEMPLATE = "@OWNER_ID@";

    int MAX_FILE_UPLOAD = 10485760;

    String SORT_BY_TITLE = "TITLE";

    String SORT_BY_DATE_CREATED = "DATE_CREATED";

    String OBJECT_IDENTIFIER_REGEX = "(([A-Za-z0-9])|-|\\.|~|_|(%[0-9A-F]{2}))+";

    String XML_NAME_REGEX = "([A-Za-z0-9_.-])+";

    String OBJECT_NAME_NULL_ERR_MSG = "Object identifier cannot be Empty or NULL";

    String OBJECT_NAME_INVALID_ERR_MSG = "A valid object identifier is required. Check Readme file for allowed characters";

    String OBJECT_NAME_LONG_ERR_MSG = "Object identifier is too long. Only 30 characters allowed";

    String OBJECT_STATE_NULL_ERR_MSG = "State cannot be Empty or NULL";

    String OBJECT_STATE_INVALID_ERR_MSG = "Invalid state value. State value must be [Active | Inactive | Deleted]";

    String LOG_MSG_LONG_ERR_MSG = "Log Message is too long. Only 50 characters allowed";

    String LOG_MSG_NULL_ERR_MSG = "Log Message cannot be NULL";

    String DS_ID_NULL_ERR_MSG = "Datastream identifier cannot be Empty or NULL";

    String DS_ID_INVALID_ERR_MSG = "A valid datastream identifier is required. Allowed characters are Alphanumeric, _, . and -";

    String DS_ID_LONG_ERR_MSG = "Datastream identifier is too long. Only 30 characters allowed";

    String DS_MIME_TYPE_NULL_ERR_MSG = "Mime type cannot be Empty or NULL";

    String LABEL_NULL_ERR_MSG = "Label cannot be NULL";

    String LABEL_LONG_ERR_MSG = "Label is too long, Only 30 characters allowed";
}

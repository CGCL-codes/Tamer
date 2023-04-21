package pl.rmalinowski.sock4log;

/**
 * 
 * @author Rafal M.Malinowski
 *
 */
public interface MappedNames {

    /**
     * Version string.
     */
    static final String VERSION = "Sock 4 Log - version 0.8.0";

    /**
     * Descritpion string.
     */
    static final String DESCRIPTION = VERSION + "\n Rafa�       l M.Malinowski <malinr@wp.pl>";

    /**
     * cos.
     */
    String CONF_PORT = "port";

    /**
     * cos.
     */
    String CONF_MESSAGE_TEMPLATE = "message_template";

    String CONF_DATE_FORMAT = "date_format";

    String CONF_PUBLISHER_TYPE = "publisher_type";
}

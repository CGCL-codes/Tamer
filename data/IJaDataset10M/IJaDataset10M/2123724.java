package name.angoca.db2sa.tools.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class loads the messages in the system language. If there is no
 * Correspondence, it will load the closer.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.0.2 Comments and license.</li>
 * <li>0.0.3 One return.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.0.1 Assert.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.0.1 2009-09-27
 */
public final class Messages {

    /**
     * Name of the message bundle to load.
     */
    private static final String BUNDLE_NAME = "messages/db2sa_messages";

    /**
     * Messages loader.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.BUNDLE_NAME);

    /**
     * Retrieves a message for the given string.
     * 
     * @param key
     *            Name of the message.
     * @return Message in the corresponding language.
     */
    public static String getString(final String key) {
        assert (key != null) && !key.equals("");
        String message = "";
        try {
            message = Messages.RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException e) {
            message = '�' + key + '!';
        }
        assert (message != null) && !message.equals("");
        return message;
    }

    /**
     * Default constructor hidden.
     */
    private Messages() {
    }
}

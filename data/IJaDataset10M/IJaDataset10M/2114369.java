package de.wilanthaou.songbookcreator.parser;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import de.wilanthaou.songbookcreator.model.Song;

/**
 * A message produced during parsing of a {@link Song}.
 * @author Alexander Metzner
 * @version $Revision: 1.3 $
 * @since 1.0
 */
public class ParserMessage {

    /**
	 * The severity of a message.
	 * @author Alexander Metzner
	 * @version $Revision: 1.3 $
	 * @since 1.0
	 */
    public static enum Severity {

        INFO, WARN, ERROR
    }

    private String filename;

    private Severity severity;

    private String messageKey;

    private String[] messageParameters;

    public ParserMessage(String filename, Severity severity, String messageKey, String... messageParameters) {
        this.filename = filename;
        this.severity = severity;
        this.messageKey = messageKey;
        this.messageParameters = messageParameters;
    }

    public String getMessage(ResourceBundle bundle) {
        return MessageFormat.format(bundle.getString(messageKey), (Object[]) messageParameters);
    }

    public String getFilename() {
        return filename;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String[] getMessageParameters() {
        return messageParameters;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return "[Message " + severity + " key=" + messageKey + " file=" + filename + "]";
    }
}

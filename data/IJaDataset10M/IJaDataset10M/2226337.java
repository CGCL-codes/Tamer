package org.equanda.tapestry5.translators;

import org.apache.tapestry5.*;
import org.apache.tapestry5.services.FormSupport;
import java.sql.Timestamp;

/**
 * Tapestry translator for Timestamp values.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class TimestampTranslator implements Translator<Timestamp>, FieldTranslator<Timestamp> {

    /**
     * Parses blank values to null, otherwise parses the client value to a Timestamp
     *
     * @throws ValidationException if the clientValue can not be parsed
     */
    public Timestamp parseClient(Field field, String clientValue, String message) throws ValidationException {
        if (clientValue == null || clientValue.length() == 0) return null;
        clientValue = clientValue.trim();
        if (clientValue.length() == 0) return null;
        try {
            return new Timestamp(Timestamp.parse(clientValue.trim()));
        } catch (NumberFormatException ex) {
            throw new ValidationException(message);
        }
    }

    /**
     * Converts null to the blank string, non-null to a string representation.
     */
    public String toClient(Timestamp value) {
        return value == null ? "" : value.toString();
    }

    public Class<Timestamp> getType() {
        return Timestamp.class;
    }

    public String getName() {
        return "timestamp";
    }

    public String getMessageKey() {
        return "timestamp-format-exception";
    }

    public void render(Field field, String message, MarkupWriter markupWriter, FormSupport formSupport) {
        formSupport.addValidation(field, "timestamp", message, null);
    }

    public Timestamp parse(String clientValue) throws ValidationException {
        return parseClient(null, clientValue, "Not parseable by TimestampTranslator");
    }

    public void render(MarkupWriter markupWriter) {
    }
}

package com.google.inject;

import com.google.inject.spi.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

/**
 * Thrown when errors occur while creating a {@link Injector}. Includes a list
 * of encountered errors. Typically, a client should catch this exception, log
 * it, and stop execution.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public class CreationException extends RuntimeException {

    final List<Message> errorMessages;

    /**
   * Constructs a new exception for the given errors.
   */
    public CreationException(Collection<Message> errorMessages) {
        super();
        this.errorMessages = new ArrayList<Message>(errorMessages);
        Collections.sort(this.errorMessages, new Comparator<Message>() {

            public int compare(Message a, Message b) {
                return a.getSourceString().compareTo(b.getSourceString());
            }
        });
    }

    public String getMessage() {
        return createErrorMessage(errorMessages);
    }

    private static String createErrorMessage(Collection<Message> errorMessages) {
        Formatter fmt = new Formatter().format("Guice configuration errors:%n%n");
        int index = 1;
        for (Message errorMessage : errorMessages) {
            fmt.format("%s) Error at %s:%n", index++, errorMessage.getSourceString()).format(" %s%n%n", errorMessage.getMessage());
        }
        return fmt.format("%s error[s]", errorMessages.size()).toString();
    }

    /**
   * Gets the error messages which resulted in this exception.
   */
    public Collection<Message> getErrorMessages() {
        return Collections.unmodifiableCollection(errorMessages);
    }
}

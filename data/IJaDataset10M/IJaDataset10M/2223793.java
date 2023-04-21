package dk.i2m.converge.plugins.decoders.dailymail;

/**
 * {@link Exception} thrown if a Daily Mail schedule file could not be found
 * in a given archive.
 *
 * @author Allan Lykke Christensen
 */
public class ScheduleNotFoundException extends ScheduleProcessingException {

    private static final long serialVersionUID = 1L;

    public ScheduleNotFoundException(Throwable cause) {
        super(cause);
    }

    public ScheduleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    public ScheduleNotFoundException() {
        super();
    }
}

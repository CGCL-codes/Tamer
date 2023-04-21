package br.edu.ufcg.ccc.javalog.model;

import java.util.Calendar;

/**
 * Represents a single entry in a package tracker report.
 * 
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 08/11/09
 */
public class ReportEntry {

    private Calendar time;

    private String message;

    /**
	 * Constructs a new report entry with the current time and a report message.
	 * 
	 * @param message
	 *            message
	 * @throws IllegalArgumentException
	 *             thrown when an invalid message is passed
	 */
    public ReportEntry(String message) throws IllegalArgumentException {
        this(Calendar.getInstance(), message);
    }

    /**
	 * Constructs a new report entry with an associated time and a report
	 * message.
	 * 
	 * @param time
	 *            time associated with this entry
	 * @param message
	 *            message
	 */
    public ReportEntry(Calendar time, String message) {
        Validator.validateObject(time, new IllegalArgumentException("Calendário inválido."));
        Validator.validatePhrase(message, new IllegalArgumentException("message failed validation"));
        this.time = time;
        this.message = message;
    }

    /**
	 * Returns the time associated with this message.
	 */
    public Calendar getTime() {
        return this.time;
    }

    /**
	 * Returns this entry's message.
	 */
    public String getMessage() {
        return this.message;
    }

    /**
	 * Sets a new time.
	 * 
	 * @param calendar
	 *            new time
	 * @throws IllegalArgumentException
	 *             thrown when an invalid calendar is passed
	 */
    public void setTime(Calendar calendar) throws IllegalArgumentException {
        Validator.validateObject(calendar, new IllegalArgumentException("Calendário inválido."));
        this.time = calendar;
    }

    /**
	 * Sets a new message.
	 * 
	 * @param message
	 *            new message
	 * @throws IllegalArgumentException
	 *             thrown when an invalid message is passed
	 */
    public void setMessage(String message) throws IllegalArgumentException {
        Validator.validatePhrase(message, new IllegalArgumentException("message validation failed"));
        this.message = message;
    }

    /**
	 * Describes the ReportEntry
	 * 
	 * @return description of the ReportEntry
	 */
    @Override
    public String toString() {
        return String.format("Horario de expedimento: %s%nRelatório: %s%n", time.getTime(), message);
    }
}

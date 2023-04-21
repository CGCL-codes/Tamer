package gov.sns.apps.pvlogger;

import gov.sns.tools.data.*;

/**
 * LoggerModelListener is the interface for listeners who want to receive logger model events.
 *
 * @author  tap
 */
interface LoggerModelListener {

    /**
	 * The status of a logger has been updated along with its client side record.
	 * @param source The source of the event
	 * @param record The record that has been updated.
	 */
    public void newLoggerStatus(LoggerModel source, GenericRecord record);

    /**
	 * The list of loggers has changed.
	 * @param model The source of the event
	 * @param records The new logger records.
	 */
    public void loggersChanged(LoggerModel model, java.util.List records);
}

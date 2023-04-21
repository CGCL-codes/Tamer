package org.archive.crawler.datamodel.settings;

/**
 * If a ValueErrorHandler is registered with a {@link SettingsHandler}, only
 * constraints with level {@link java.util.logging.Level#SEVERE} will throw an
 * {@link javax.management.InvalidAttributeValueException}. 
 *
 * The ValueErrorHandler will recieve a notification for all failed checks
 * with level equal or greater than the error reporting level.
 * 
 * @author John Erik Halse
 */
public interface ValueErrorHandler {

    public void handleValueError(Constraint.FailedCheck error);
}

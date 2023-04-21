package com.usoog.commons.network.message;

/**
 * This is a marker interface for Messages that are intended to be logged in
 * some way or an other. To get the String to log just call
 * {@link com.usoog.commons.network.message.Message#getKey() getMessage()}.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 */
public interface LoggableMessage extends Message {
}

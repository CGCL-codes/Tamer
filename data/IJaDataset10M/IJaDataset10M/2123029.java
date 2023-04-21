package com.sun.org.apache.xerces.internal.impl.io;

import java.io.CharConversionException;
import java.util.Locale;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;

/**
 * <p>Signals that a malformed byte sequence was detected
 * by a <code>java.io.Reader</code> that decodes bytes 
 * of a given encoding into characters.</p>
 * 
 * @xerces.internal
 *
 * @author Michael Glavassevich, IBM
 *
 * @version $Id: MalformedByteSequenceException.java,v 1.2.6.1 2005/09/09 07:22:54 neerajbj Exp $
 */
public class MalformedByteSequenceException extends CharConversionException {

    /** Serialization version. */
    static final long serialVersionUID = 8436382245048328739L;

    /** message formatter **/
    private MessageFormatter fFormatter;

    /** locale for error message **/
    private Locale fLocale;

    /** error domain **/
    private String fDomain;

    /** key for the error message **/
    private String fKey;

    /** replacement arguements for the error message **/
    private Object[] fArguments;

    /** message text for this message, initially null **/
    private String fMessage;

    /**
     * Constructs a MalformedByteSequenceException with the given
     * parameters which may be passed to an error reporter to 
     * generate a localized string for this exception.
     * 
     * @param formatter The MessageFormatter used for building the 
     *                  message text for this exception.
     * @param locale    The Locale for which messages are to be reported.
     * @param domain    The error domain.
     * @param key       The key of the error message.
     * @param arguments The replacement arguments for the error message,
     *                  if needed.
     */
    public MalformedByteSequenceException(MessageFormatter formatter, Locale locale, String domain, String key, Object[] arguments) {
        fFormatter = formatter;
        fLocale = locale;
        fDomain = domain;
        fKey = key;
        fArguments = arguments;
    }

    /**
     * <p>Returns the error domain of the error message.</p>
     * 
     * @return the error domain
     */
    public String getDomain() {
        return fDomain;
    }

    /**
     * <p>Returns the key of the error message.</p>
     * 
     * @return the error key of the error message
     */
    public String getKey() {
        return fKey;
    }

    /**
     * <p>Returns the replacement arguments for the error
     * message or <code>null</code> if none exist.</p>
     * 
     * @return the replacement arguments for the error message
     * or <code>null</code> if none exist
     */
    public Object[] getArguments() {
        return fArguments;
    }

    /**
     * <p>Returns the localized message for this exception.</p>
     * 
     * @return the localized message for this exception.
     */
    public String getMessage() {
        if (fMessage == null) {
            fMessage = fFormatter.formatMessage(fLocale, fKey, fArguments);
            fFormatter = null;
            fLocale = null;
        }
        return fMessage;
    }
}

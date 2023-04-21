package org.omg.IOP;

/**
 * <p>Defines the ExceptionDetailMessage (= 14) service context. This context
 * may be included into reply message, having the SYSTEM_EXCEPTION or
 * USER_EXCEPTION reply status, as the alternative to the stack trace
 * that might contain sensitive or unwanted information. The service
 * context contains the CDR-encapsulated wide string, usually
 * returned by {@link Exception#getMessage}.
 * </p><p>
 * The applications may also send the more comprehensive UnknownExceptionInfo
 * ( = 9 ) service context that contains the thrown exception, written
 * as the Value type.
 * </p>
 */
public interface ExceptionDetailMessage {

    /**
    * Specifies the ExceptionDetailMessage value, 14.
    */
    int value = 14;
}

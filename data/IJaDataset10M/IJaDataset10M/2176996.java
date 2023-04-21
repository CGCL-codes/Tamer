package com.beanstalktech.common.context;

/**
 * Error definitions for Beanstalk
 *
 * @author Stuart Sugarman, Widgetry
 * @version 1.0 12/5/1999
 * @since Beanstalk V1.0
 */
public class AppError {

    public static final int ms_noCommandError = 101;

    /**
     * Session ID did not match session ID in the
     * session context.
     */
    public static final int ms_securityError = 102;

    /**
     * No session was found that matched the session ID
     * in a client request.
     */
    public static final int ms_noSessionError = 103;

    /**
     * An error occurred during the parsing of a request.
     */
    public static final int ms_requestParserError = 104;

    /**
     * An error occurred during the execution of a script
     */
    public static final int ms_scriptExecutionError = 105;

    /**
     * An error occurred during the handling of a request
     * by a command.
     */
    public static final int ms_commandError = 201;

    /**
     * An error occurred while a response to a request
     * was being generated.
     */
    public static final int ms_responseError = 301;

    /**
     * An error occurred during an interaction with a
     * server.
     */
    public static final int ms_serverError = 302;

    /**
     * An error occurred while parsing the response from
     * a server.
     */
    public static final int ms_responseParserError = 303;

    /**
     * A client connection timed out waiting for a response.
     */
    public static final int ms_clientTimeoutError = 401;

    /**
     * A connection to a server timed out before it
     * received a response.
     */
    public static final int ms_serverTimeoutError = 402;

    /**
     * A server reported that a login attempt failed.
     */
    public static final int ms_loginError = 501;

    /**
     * Attempt to use a restricted function without prior login.
     */
    public static final int ms_loginRequired = 502;

    /**
     * A required value is missing in a request.
     */
    public static final int ms_valueMissing = 601;

    /**
     * A value is not valid in a request.
     */
    public static final int ms_valueInvalid = 602;
}

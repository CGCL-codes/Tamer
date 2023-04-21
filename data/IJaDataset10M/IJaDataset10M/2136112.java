package org.jwebsocket.plugins.jdbc;

/**
 * Provides a record with the result of a SQL manipulation command like 
 * update, insert delete.
 * Provides constants for the result types.
 * @since 1.0
 * @author Alexander Schulze
 */
public class DBExecResult {

    /**
	 * The type of the result is unknown, 
	 * usually this result should not be returned to the caller.
	 * @since 1.0
	 */
    public static final int RT_UNKNOWN = -1;

    /**
	 * The record contains a result for a previous request. The object field 
	 * optionally can contain additional data.
	 * @since 1.0
	 */
    public static final int RT_RESULT = 0;

    /**
	 * The operation has successfully finished.
	 * @since 1.0
	 */
    public static final int RT_SUCCESS = 1;

    /**
	 * The operation caused a warning.
	 * @since 1.0
	 */
    public static final int RT_WARNING = 2;

    /**
	 * The operation caused an error.
	 * @since 1.0
	 */
    public static final int RT_ERROR = 3;

    /**
	 * The operation caused an exception, but the server is still ok.
	 * @since 1.0
	 */
    public static final int RT_EXCEPTION = 4;

    /**
	 * The operation caused an exception, but the server has a problem that 
	 * requires maintenance by the server admin.
	 * @since 1.0
	 */
    public static final int RT_FATAL = 5;

    /**
	 * The number of rows, that has been affected by the command.
	 * @since 1.0
	 */
    public int affectedRows = 0;

    /**
	 * A reference to any arbitrary object, 
	 * created by a method and to be returned to the caller.
	 * @since 1.0
	 */
    public Object result = null;

    /**
	 * The type of the result, one of the <code><i>RT_XXX</i></code> constants.
	 * @since 1.0
	 */
    public int type = RT_UNKNOWN;

    /**
	 * A (locale-)key to uniquely define the result, independantly 
	 * from the language.
	 * @since 1.0
	 */
    public String key = null;

    /**
	 * Message to the user, dependant on the currently selected language.
	 * @since 1.0
	 */
    public String message = null;
}

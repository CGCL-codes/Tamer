package org.dinopolis.util.debug;

/**
 * This class is the baseclass for all objects responsible for the
 * formatting of the Debug message.
 *
 * As these objects are created by a factory, all subclasses must be
 * named "DebugMessageFORMATSTRING" (formatstring in uppercase).
 *   
*/
public abstract class DebugMessageFormatObject {

    /**
 * Empty default constructor (all subclasses are created by a factory).
 */
    public DebugMessageFormatObject() {
    }

    /**
 * This is the method to override on subclasses. It is called whenever
 * an evaluation of the keyword in the format string of the debug
 * messages is needed. The given arguments can be used by the subclass:
 * e.g. the subclass DebugMessageLETTERCOUNT might return here the
 * number of letters in the debug message (as a String). Other classes
 * may ignore all arguments: e.g. DebugMessageDATE returns the
 * current date/time, no matter what debug message/level is
 * given. Other subclasses again might need the Debug-instance to
 * obtain special information stored there:
 * DebugMessageDIFFMILLISECONDS needs the time the last debug message
 * was printed. This information can be retrieved from the Debug
 * instance. 
 *
 * @param level the debug level for the given debug message.
 * @param debug_message the debug message to be printed.
 * @param debug_instance the debug object this message string belongs
 * to. It can be used in the <code>getMessage</code>-method to
 * retrieve additional information about what should be returned
 * exactly.
 */
    public abstract String getEvaluatedKeyword(String level, String debug_message, Debug debug_instance);
}

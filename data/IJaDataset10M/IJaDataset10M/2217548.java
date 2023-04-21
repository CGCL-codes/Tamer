package org.dinopolis.util.debug;

/**
 * This class is chosen by the DebugMessageFactory depending on the
 * token used in the message format.
 *
 * This implementation returns the name of the current thread.
 *    
*/
public class DebugMessageTHREADNAME extends DebugMessageFormatObject {

    /**
 * This implementation returns the name of the current thread.
 *
 * @param level the debug level for the given debug message.
 * @param debug_message the debug message to be printed.
 * @param debug_instance the debug object this message string belongs to.
 * It can be used in the <code>getMessage</code>-method to retrieve
 * additional information about what should be returned exactly.
 */
    public String getEvaluatedKeyword(String level, String debug_message, Debug debug_instance) {
        return (Thread.currentThread().getName());
    }
}

package pl.edu.pjwstk.p2pp.debug.processor.subsystems.writers;

import pl.edu.pjwstk.p2pp.debug.DebugInformation;

/**
 * Interface used for creating writer subsystems.
 *
 * @author Konrad Adamczyk conrad.adamczyk@gmail.com
 */
public interface IWriter {

    /**
     * Initiates writer instance with given settings.
     *
     * @param settings
     */
    void init(Object[] settings);

    /**
     * Starts writer subsystem thread.
     */
    void start();

    void halt();

    boolean hasRequests();

    /**
     * Orders writer subsystem to write given debug information.
     *
     * @param debugInfo Debug information being an array of Strings which order is defined by
     *      {@link pl.edu.pjwstk.p2pp.debug.DebugFields}.
     */
    void writeDebugInformation(DebugInformation debugInfo);
}

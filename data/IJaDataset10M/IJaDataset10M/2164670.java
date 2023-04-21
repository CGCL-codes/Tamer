package net.sourceforge.basher;

/** Defines time based operations.  Allows operations for determining run-time since intialization. 
 * @author Johan Lindquist
 * @version 1.0
 */
public interface TimeSource {

    /** Returns the elapsed time of the running system.
     * @return The elapsed time since startup.
     */
    public long getElapsedTime();

    /** Returns the time the system was started.
     *
     * @return The start time.
     */
    public long getStartTime();

    /** Returns the current time in the system.
     *
     * @return The current time.
     */
    public long getCurrentTime();
}

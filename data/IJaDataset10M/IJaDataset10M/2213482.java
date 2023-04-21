package remote.control;

/** Basic mote control interface. */
public interface Mote extends MoteInfo {

    /** Start the mote. */
    void start();

    /** Stop the mote. */
    void stop();

    /** Reset the mote. */
    void reset();

    /** Program the mote.
	 *
	 * @param bin	The mote binary.
	 */
    void program(String bin);

    /** Cancel mote programming. */
    void cancelProgramming();

    /** Write data to the mote console.
	 *
	 * @param data	The data to write.
	 */
    void writeData(String data);

    /** Get the mote status.
	 *
	 * @return	The mote status.
	 */
    MoteStatus getStatus();

    /** Get last command.
	 *
	 * @return	The command that was last executed.
	 */
    MoteCommand getLastCommand();

    /** Set mote attribute.
	 *
	 * @param name	Attribute name.
	 * @param value	New attribute value.
	 * @throws java.lang.UnsupportedOperationException
	 */
    void setAttribute(String name, String value) throws UnsupportedOperationException;

    /** Release control of the mote. */
    void release();
}

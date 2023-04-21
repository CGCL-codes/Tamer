package titancommon.tasks;

import java.util.*;
import titancommon.compiler.NodeMetrics;
import titancommon.compiler.TaskMetrics;

public abstract class Task {

    /** Gets the name of the tasks as it appears in the config file.
     *  *CAUTION*: must be lowercase!
     */
    public abstract String getName();

    /** Gets the unique identifier of the task */
    public abstract int getID();

    /** Returns a copy of itself - this function should call a copy constructor, which first calls the super(copy) class! */
    public abstract Object clone();

    /** Returns the number of input ports with the current configuration */
    public abstract int getInPorts();

    /** Returns the number of output ports with the current configuration 
     * @return
     */
    public abstract int getOutPorts();

    /** Enters a configuration string as it appears in the config file. 
     * *CAUTION*: whitespace is removed during the parsing
     * @param strConfig The configuration contents from the configuration file 
     * @return Whether successful
     */
    public abstract boolean setConfiguration(String[] strConfig);

    /** Gets the contribution for a configuration message for Titan 
     * @param maxBytesPerMsg Maximum message size
     */
    public abstract short[][] getConfigBytes(int maxBytesPerMsg);

    /** 
     * Indicates the complexity of the algorithm contained in the task
     * @param nm Indicates on what node the task is supposed to run
     * @return The metrics of this task 
     */
    public TaskMetrics getMetrics(NodeMetrics nm) {
        return null;
    }

    /** 
     * Sets the datarate at the specified input port. This value is set to compute 
     * the datarates appearing at the output ports. 
     * 
     * @param port port number
     * @param pktPerSecond Number of packets coming in per second
     * @param pktSize Average size of packets going out
     * @return Whether this information changed the configuration
     */
    public boolean setInputPortDatarate(int port, float pktPerSecond, int pktSize) {
        return false;
    }

    private Map m_Attributes = new HashMap();

    /** Sets an attribute to the task */
    public boolean addAttribute(String strName, String strValue) {
        m_Attributes.put(strName, strValue);
        return true;
    }

    /** Retrieves an attribute value */
    public String getAttribute(String strName) {
        return (String) m_Attributes.get(strName);
    }

    public Task() {
    }

    public Task(Task t) {
        m_Attributes.putAll(t.m_Attributes);
    }
}

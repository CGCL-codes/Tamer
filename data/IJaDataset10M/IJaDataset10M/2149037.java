package ch.epfl.lsr.adhoc.simulator.config;

/**
 * Stores the configuration file of the network topology
 * 
 * @version $Revision: 1.10 $ $Date: 2004/06/05 17:14:22 $
 * @author Author: Boris Danev and Aurelien Frossard
 */
public class NetworkConfig extends AbstractConfig {

    public static final String codeRevision = "$Revision: 1.10 $ $Date: 2004/06/05 17:14:22 $ Author: Boris Danev and Aurelien Frossard";

    /** Type of the network configuration */
    private boolean m_type;

    /** Stores the size of the network */
    private int m_networkSize;

    /** Stores the global framework configuration to be used */
    private String m_frameworkInFile;

    private String m_frameworkOutFile;

    private String m_errorLog;

    private String m_simulationLog;

    /** Stores the global quality and range parameters */
    private double m_range;

    private double m_quality;

    /** Stores individual node configuration */
    private NodeConfig[] m_nodes;

    public int getNodeCount() {
        return m_nodes.length;
    }

    public NodeConfig getNodeConfig(int p_nodeNumber) {
        return m_nodes[p_nodeNumber];
    }

    public void setNodes(NodeConfig[] p_nodes) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_nodes = p_nodes;
    }

    public void setType(boolean p_type) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_type = p_type;
    }

    public void setNetworkSize(int p_networkSize) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_networkSize = p_networkSize;
    }

    public void setFrameworkInFile(String p_frameworkInFile) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_frameworkInFile = p_frameworkInFile;
    }

    public void setFrameworkOutFile(String p_frameworkOutFile) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_frameworkOutFile = p_frameworkOutFile;
    }

    public void setErrorLog(String p_errorLog) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_errorLog = p_errorLog;
    }

    public void setSimulationLog(String p_simulationLog) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_simulationLog = p_simulationLog;
    }

    public void setRange(double p_range) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_range = p_range;
    }

    public void setQuality(double p_quality) {
        if (configured) throw new IllegalStateException(ERROR_MESG);
        m_quality = p_quality;
    }

    public boolean getType() {
        return m_type;
    }

    public int getNetworkSize() {
        return m_networkSize;
    }

    public String getFrameworkInFile() {
        return m_frameworkInFile;
    }

    public String getFrameworkOutFile() {
        return m_frameworkOutFile;
    }

    public String getErrorLog() {
        return m_errorLog;
    }

    public String getSimulationLog() {
        return m_simulationLog;
    }

    public double getRange() {
        return m_range;
    }

    public double getQuality() {
        return m_quality;
    }

    /** Returns a String representation of this object. */
    public String toString() {
        StringBuffer sb = new StringBuffer("NetworkConfig[");
        sb.append("type=");
        sb.append(m_type);
        sb.append(",size=");
        sb.append(m_networkSize);
        sb.append(",defaultInFile=");
        sb.append(m_frameworkInFile);
        sb.append(",defaultOutFile=");
        sb.append(m_frameworkOutFile);
        sb.append(",defaultRange=");
        sb.append(m_range);
        sb.append(",defaultQuality=");
        sb.append(m_quality);
        sb.append("\n");
        for (int i = 0; i < m_nodes.length; i++) {
            sb.append(m_nodes[i].toString() + "\n");
        }
        sb.append("]");
        return (sb.toString());
    }
}

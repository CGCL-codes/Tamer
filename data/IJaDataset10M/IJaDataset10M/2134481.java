package hypercast.NONE;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xpath.XPath;
import hypercast.HyperCastConfig;
import hypercast.HyperCastStatsException;
import hypercast.I_AddressPair;
import hypercast.I_LogicalAddress;
import hypercast.I_Node;
import hypercast.I_PhysicalAddress;
import hypercast.I_UnicastAdapter;
import hypercast.NotificationHandler;
import hypercast.I_Stats;
import hypercast.StatsElement;
import hypercast.StatsProcessor;
import hypercast.HyperCastConfigException;
import hypercast.SimpleStats;
import hypercast.util.XmlUtil;

/**
 * @author HyperCast Team
 * @author Guangyu Dong
 * @version 2005 (version 3.0)
 */
public class NONE_Node implements I_Node {

    private HyperCastConfig config;

    private I_UnicastAdapter adapter;

    private NONE_AddressPair addressPair;

    /** pre-configured neighbors: TODO: why can't these be dynamic? */
    private NONE_AddressPair[] neighbors;

    private String PROPERTY_PROTO_PREFIX = "/Public/Node/NONE/";

    /**
	 * A StatsProcessor instance which handles the statistics in this object.
	 */
    private StatsProcessor statsPro;

    /**
	 * The statistics name for this object (as an I_Stats instance). Its default
	 * name is defined in the socket configuration schema file. It can be assigned
	 * by any other object through method setStattisticsName defined in the 
	 * I_Stats interface.
	 */
    private String statisticsName;

    public NONE_Node(final HyperCastConfig config, final I_UnicastAdapter adapter) {
        this.config = config;
        this.adapter = adapter;
        this.addressPair = new NONE_AddressPair(new NONE_LogicalAddress(adapter.createPhysicalAddress()));
        String neighborsString = "";
        try {
            neighborsString = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Neighbors"));
        } catch (HyperCastConfigException e) {
            neighborsString = "";
        }
        initNeighbors(neighborsString);
        initStatisticsStructure();
    }

    public NONE_Node(final HyperCastConfig config, final I_UnicastAdapter adapter, final String prefix) {
        this.config = config;
        this.adapter = adapter;
        this.addressPair = new NONE_AddressPair(new NONE_LogicalAddress(adapter.createPhysicalAddress()));
        PROPERTY_PROTO_PREFIX = prefix;
        String neighborsString = "";
        try {
            neighborsString = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Neighbors"));
        } catch (HyperCastConfigException e) {
            neighborsString = "";
        }
        initNeighbors(neighborsString);
        initStatisticsStructure();
    }

    private void initNeighbors(final String neighborsString) {
        final ArrayList neighborsArray = new ArrayList();
        final String SEPARATOR = ";";
        final StringTokenizer tokenizer = new StringTokenizer(neighborsString.trim(), SEPARATOR);
        while (tokenizer.hasMoreTokens()) {
            final String neighborEntryString = tokenizer.nextToken();
            System.err.println("Adding " + neighborEntryString + " into NONE neighborhood table...");
            final I_PhysicalAddress neighborEntryPhysicalAddress = adapter.createPhysicalAddress(neighborEntryString);
            neighborsArray.add(new NONE_AddressPair(new NONE_LogicalAddress(neighborEntryPhysicalAddress)));
        }
        neighbors = (NONE_AddressPair[]) neighborsArray.toArray(new NONE_AddressPair[neighborsArray.size()]);
    }

    /**
     * This is a no-op for the NONE protocol since there is no overlay to join.
     *
     * @see hypercast.I_Node#joinOverlay()
     */
    public void joinOverlay() {
        config.log.println("NONE_Node started: " + addressPair);
        config.log.flush();
    }

    /**
     * This is a no-op for the NONE protocol since there is no overlay to join.
     *
     * @see hypercast.I_Node#leaveOverlay()
     */
    public void leaveOverlay() {
    }

    /**
     * 
     *
     * @see hypercast.I_Node#getParent(hypercast.I_LogicalAddress)
     */
    public I_AddressPair[] getParent(final I_LogicalAddress root) {
        return new I_AddressPair[] { new NONE_AddressPair((NONE_LogicalAddress) root) };
    }

    /**
     * @see hypercast.I_Node#getChildren(hypercast.I_LogicalAddress)
     */
    public I_AddressPair[] getChildren(final I_LogicalAddress root) {
        return root.equals(addressPair.getLogicalAddress()) ? neighbors : null;
    }

    /**
     * @see hypercast.I_Node#getAllNeighbors()
     */
    public I_AddressPair[] getAllNeighbors() {
        return neighbors;
    }

    /**
     * @see hypercast.I_Node#getAddressPair()
     */
    public I_AddressPair getAddressPair() {
        return addressPair;
    }

    /**
     * @see hypercast.I_Node#createLogicalAddress(byte[], int)
     */
    public I_LogicalAddress createLogicalAddress(final byte[] logicalAddressBytes, final int offset) {
        return new NONE_LogicalAddress(adapter.createPhysicalAddress(logicalAddressBytes, offset));
    }

    /**
     * Everybody is considered a neighbor in
     * the NONE protocol. All previous hops are considered to be valid
     * unless the previous hop is this node.
     *
     * @see hypercast.I_Node#previousHopCheck(hypercast.I_LogicalAddress, hypercast.I_LogicalAddress, hypercast.I_LogicalAddress)
     */
    public boolean previousHopCheck(final I_LogicalAddress source, final I_LogicalAddress destination, final I_LogicalAddress previousHop) {
        I_LogicalAddress myLogicalAddress = addressPair.getLogicalAddress();
        return !source.equals(myLogicalAddress);
    }

    /**
     * @see hypercast.I_Node#setNotificationHandler(NotificationHandler)
     */
    public void setNotificationHandler(final NotificationHandler notificationHandler) {
    }

    /**
     * @see hypercast.I_Node#createLogicalAddress(java.lang.String)
     */
    public I_LogicalAddress createLogicalAddress(final String logicalAddressString) {
        return new NONE_LogicalAddress(adapter.createPhysicalAddress(logicalAddressString));
    }

    /**
     * Sets the logical address of this node to the specified one.
     */
    public void setLogicalAddress(final I_LogicalAddress logicalAddress) {
        if (!(logicalAddress instanceof NONE_LogicalAddress)) throw new IllegalArgumentException("NONE_Node.setLogicalAddress: the logical address type of the parameter is incorrect.");
        addressPair = new NONE_AddressPair((NONE_LogicalAddress) logicalAddress);
    }

    private void initStatisticsStructure() {
        statisticsName = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "StatName"));
        statsPro = new StatsProcessor(this, true, true);
        statsPro.addStatsElement("NodeAdapter", adapter, 1, 1);
        statsPro.addStatsElement("Neighbors", new CurrentNeighbors(), 1, 1);
        statsPro.addStatsElement("LogicalAddress", new LogicalAddressOperator(), 1, 1);
        statsPro.addStatsElement("PhysicalAddress", new PhysicalAddressOperator(), 1, 1);
        statsPro.addStatsElement("NumOfNeighbors", new NumOfCurrentNeighbors(), 1, 1);
    }

    /** @see I_Stats#getStatsName() */
    public String getStatsName() {
        return statisticsName;
    }

    /** @see I_Stats#setStatsName(String) */
    public void setStatsName(String name) {
        statisticsName = name;
    }

    /**
     * Gets the information of a scalar statistics from an object which 
     * implements this interface.
     * @throws HyperCastStatsException if the statistics with given name is not
     * supported by the object.
     */
    public String getStatsValue(final String name) throws HyperCastStatsException {
        if (name.equals("Neighbors")) {
            final StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < neighbors.length; i++) {
                buffer.append(neighbors[i].getLogicalAddress().toString());
                if (i < (neighbors.length - 1)) {
                    buffer.append(";");
                }
            }
            return buffer.toString();
        } else if (name.equals("LogicalAddress")) {
            return addressPair.getLogicalAddress().toString();
        } else if (name.equals("PhysicalAddress")) {
            return addressPair.getPhysicalAddress().toString();
        } else if (name.equals("NumOfNeighbors")) {
            return Integer.toString(neighbors.length);
        } else {
            throw new HyperCastStatsException(name, HyperCastStatsException.StatisticNotFound);
        }
    }

    /**
	 * Sets the information of s writable scalar statistics to an object which 
	 * implements this interface.
	 * @throws HyperCastStatsException if the statistics with given name is not
	 * supported by the object or read-only, or if value does not conform with 
	 * expected format.
	 */
    public String setStatsValue(final String name, final String newValue) throws HyperCastStatsException {
        if (name.equals("Neighbors")) {
            try {
                final String result = newValue.trim().equals("none") ? "" : newValue;
                this.initNeighbors(result);
                return result;
            } catch (final Exception e) {
                throw new HyperCastStatsException(name + "Unable to initiate Neighbors with: " + newValue + ": " + e, HyperCastStatsException.InappropriateValue);
            }
        } else {
            throw new HyperCastStatsException(name, HyperCastStatsException.StatisticNotFound);
        }
    }

    /** @see I_Stats#getStats */
    public Element[] getStats(final Document doc, final XPath xpath) throws HyperCastStatsException {
        return statsPro.getStatsResult(doc, xpath);
    }

    /**@see I_Stats#setStats */
    public Element[] setStats(final Document doc, final XPath xpath, final Element newValue) throws HyperCastStatsException {
        return statsPro.setStatsResult(doc, xpath, newValue);
    }

    /** @see I_Stats#getReadSchema */
    public Element[] getReadSchema(final Document doc, final XPath xpath) throws HyperCastStatsException {
        return statsPro.getReadSchemaResult(doc, xpath);
    }

    /** @see I_Stats#getWriteSchema */
    public Element[] getWriteSchema(final Document doc, final XPath xpath) throws HyperCastStatsException {
        return statsPro.getWriteSchemaResult(doc, xpath);
    }

    /**
	 * This class manages simple statistic "Neighbors".
	 */
    class CurrentNeighbors extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            final StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < neighbors.length; i++) {
                buffer.append(neighbors[i].getLogicalAddress().toString());
                if (i < (neighbors.length - 1)) {
                    buffer.append(";");
                }
            }
            return buffer.toString();
        }

        /**
		 * Sets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#setStats(String newvalue)
		 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            try {
                final String result = newValue.trim().equals("none") ? "" : newValue;
                initNeighbors(result);
                return result;
            } catch (final Exception e) {
                throw new HyperCastStatsException(statisticsName + "Unable to initiate Neighbors with: " + newValue + ": " + e, HyperCastStatsException.InappropriateValue);
            }
        }

        /** 
		 * @see I_Stats#getReadSchema(Document, XPath)
		 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:String", null, null);
        }
    }

    /**
	 * This class manages simple statistic "LogicalAddress".
	 */
    class LogicalAddressOperator extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            return addressPair.getLogicalAddress().toString();
        }

        /**
		 * Sets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#setStats(String newvalue)
		 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
		 * @see I_Stats#getReadSchema(Document, XPath)
		 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:String", null, null);
        }
    }

    /**
	 * This class manages simple statistic "PhysicalAddress".
	 */
    class PhysicalAddressOperator extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            return addressPair.getPhysicalAddress().toString();
        }

        /**
		 * Sets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#setStats(String newvalue)
		 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
		 * @see I_Stats#getReadSchema(Document, XPath)
		 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:String", null, null);
        }
    }

    /**
	 * This class manages simple statistic "NumOfNeighbors".
	 */
    class NumOfCurrentNeighbors extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            return Integer.toString(neighbors.length);
        }

        /**
		 * Sets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#setStats(String newvalue)
		 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
		 * @see I_Stats#getReadSchema(Document, XPath)
		 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Integer", null, null);
        }
    }
}

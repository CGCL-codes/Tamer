package hypercast.HC;

import hypercast.HyperCastStatsException;
import hypercast.I_AddressPair;
import hypercast.I_LogicalAddress;
import hypercast.I_PhysicalAddress;
import hypercast.NeighborhoodStats;
import hypercast.StatsElement;
import hypercast.StatsProcessor;
import hypercast.SimpleStats;
import hypercast.util.XmlUtil;
import hypercast.I_Stats;
import org.apache.xpath.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The stats table for HC neighborhood table
 *
 * @author HyperCast Team
 * @author Guangyu Dong
 * @version 2005 (version 3.0)
 */
class HC_NeighborhoodStats extends NeighborhoodStats {

    /** address pair array*/
    HC_AddressPair[] neighbors;

    int index;

    /**
	 * Constructor.
	 */
    public HC_NeighborhoodStats(HC_AddressPair[] neighbors, int index) {
        super(null);
        this.neighbors = neighbors;
        this.index = index;
    }

    /**
	 * Initialize the statistics processor instance contained in this object.
	 */
    protected void InitStatisticsStructure() {
        super.InitStatisticsStructure();
        statsPro.addStatsElement("Age", new AgeQuery(), 1, 1);
    }

    /** 
 * Return the result of query for the statistics specified by the given xpath.
 * 
 * @param doc   Document used for create new elements or nodes.
 * @param xpath XPath instance represents the statistics to be queried.
 * @see I_Stats#getStats(Document, XPath)
 */
    public Element[] getStats(Document doc, XPath xpath) throws HyperCastStatsException {
        if (neighbors != null) addressPair = neighbors[index];
        return statsPro.getStatsResult(doc, xpath);
    }

    /** 
	 * Set the statistics specified by the given xpath. The value actually set is
	 * returned.
	 * 
	 * @param doc   	Document used for create new elements or nodes.
	 * @param xpath 	XPath instance represents the statistics to be queried.
	 * @param newValue	Element representing the value or sub-tree to be set.
	 * @see I_Stats#setStats(Document, XPath, Element)
	 */
    public Element[] setStats(Document doc, XPath xpath, Element newValue) throws HyperCastStatsException {
        if (neighbors != null) addressPair = neighbors[index];
        return statsPro.setStatsResult(doc, xpath, newValue);
    }

    /**
	* This class manages simple statistic "Age".
	*/
    class AgeQuery extends SimpleStats {

        /**
		* Gets the value of a simple statistic managed by the implementation of this class.
		* 
		* @see SimpleStats#getStats()
		*/
        protected String getStats() {
            if (neighbors != null) addressPair = neighbors[index];
            if (addressPair != null) return "" + ((HC_AddressPair) addressPair).getAge(); else return null;
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

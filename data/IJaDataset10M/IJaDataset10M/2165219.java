package hypercast.CT;

import org.apache.xpath.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import hypercast.*;
import hypercast.util.XmlUtil;

/**
 * This class represents the information about a cluster head include:
 * - Head Node Type: the Node Types of head are either Head, Hybrid 
 *     (Head), or Hybrid (Member)
 * - Timestamp: the last time this entry has been updated.
 * - HeadCriteria: the information about property of service provided 
 *     to a member
 * - Attempts: the number of attempts to request from this cluster head 
 *     and it is set to 0 if the request is accepted.
 *     
 * @author HyperCast Team
 * @author Wittawat Tantisiriroj
 * @version 1.0, Aug. 30, 2006
 */
public class OptionEntry implements I_Stats {

    private StatsProcessor statsPro;

    private String statisticsName = "OptionEntry";

    private byte headType;

    private long timestamp;

    private HeadCriteria criteria;

    private long result;

    /** length of byte array required to hold the option */
    public static final int ADDRESS_ARRAY_SIZE = 9 + HeadCriteria.getSize();

    public OptionEntry(OptionEntry opt) {
        init(opt.timestamp, opt.headType, opt.criteria, opt.result);
    }

    public OptionEntry(long timestamp, byte headType, HeadCriteria criteria) {
        init(timestamp, headType, criteria, 0);
    }

    public OptionEntry(long timestamp, byte headType, HeadCriteria criteria, long result) {
        init(timestamp, headType, criteria, result);
    }

    public OptionEntry(byte[] array, int offset) {
        init(ByteArrayUtility.toLong(array, offset + 1), array[offset], new HeadCriteria(array, offset + 9), 0);
    }

    private void init(long timestamp, byte headType, HeadCriteria criteria, long result) {
        this.headType = headType;
        this.timestamp = timestamp;
        this.criteria = criteria;
        this.result = result;
        InitStatisticsStructure();
    }

    public byte[] toByteArray() {
        byte[] array = new byte[ADDRESS_ARRAY_SIZE];
        array[0] = headType;
        System.arraycopy(ByteArrayUtility.toByteArray(timestamp), 0, array, 1, 8);
        System.arraycopy(criteria.toByteArray(), 0, array, 9, HeadCriteria.getSize());
        return array;
    }

    public static int getSize() {
        return ADDRESS_ARRAY_SIZE;
    }

    /**
	 * @return Returns the timestamp.
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * @return Returns the criteria.
	 */
    public HeadCriteria getCriteria() {
        return criteria;
    }

    public String toString() {
        String typeString;
        if (headType == CT_Node.HeadOnly) {
            typeString = "Head";
        } else if (headType == CT_Node.Hybrid) {
            typeString = "Hybrid(Head)";
        } else {
            typeString = "Hybrid(Member)";
        }
        return "Try = [" + result + "], " + "Type = [" + typeString + "], " + "TimeStamp = [" + timestamp + "], " + "HeadCriteria = [" + criteria + "]";
    }

    /**
	 * @return Returns the headType.
	 */
    public byte getHeadType() {
        return headType;
    }

    /**
	 * @return Returns the result.
	 */
    public long getResult() {
        return result;
    }

    /**
	 * @param result The result to set.
	 */
    public void setResult(long result) {
        this.result = result;
    }

    private void InitStatisticsStructure() {
        statsPro = new StatsProcessor(this, true, true);
        statsPro.addStatsElement("HeadType", new HeadType(), 1, 1);
        statsPro.addStatsElement("Timestamp", new Timestamp(), 1, 1);
        if (this.criteria != null) {
            statsPro.addStatsElement("Criteria", this.criteria, 1, 1);
        }
        statsPro.addStatsElement("Result", new Result(), 1, 1);
    }

    /**
	 * Return the result of query for the statistics specified by the given
	 * xpath.
	 * 
	 * @param doc   Document used for create new elements or nodes.
	 * @param xpath XPath instance represents the statistics to be queried.
	 * @see I_Stats#getStats
	 */
    public Element[] getStats(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getStatsResult(doc, xpath);
    }

    /**
	 * Set the statistics specified by the given xpath. The value actually set
	 * is returned.
	 * 
	 * @param doc   	Document used for create new elements or nodes.
	 * @param xpath 	XPath instance represents the statistics to be queried.
	 * @param newValue	Element representing the value or sub-tree to be set.
	 * @see I_Stats#setStats
	 */
    public Element[] setStats(Document doc, XPath xpath, Element newValue) throws HyperCastStatsException {
        return statsPro.setStatsResult(doc, xpath, newValue);
    }

    /**
	 * Return the schema element which represents the root of the sub-tree,
	 * specified by the given xpath, in read schema tree.
	 * 
	 * @param doc   Document used for create new elements or nodes.
	 * @param xpath XPath instance representing the statistics which is the root
	 * 	of the sub-tree to be queried.
	 * @see I_Stats#getReadSchame
	 */
    public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getReadSchemaResult(doc, xpath);
    }

    /**
	 * Return the schema element which represents the root of the sub-tree,
	 * specified by the given xpath, in write schema tree.
	 * 
	 * @param doc   Document used for create new elements or nodes.
	 * @param xpath XPath instance representing the statistics which is the root
	 * 	of the sub-tree to be queried.
	 * @see I_Stats#getWriteSchame
	 */
    public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getWriteSchemaResult(doc, xpath);
    }

    public String getStatsName() {
        return statisticsName;
    }

    public void setStatsName(String name) {
        statisticsName = name;
    }

    /**
	 * This class manages simple statistic "Timestamp".
	 */
    class Timestamp extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            return "" + timestamp;
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
	 * This class manages simple statistic "HeadType".
	 */
    class HeadType extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            if (headType == CT_Node.HeadOnly) {
                return "Head";
            } else if (headType == CT_Node.Hybrid) {
                return "Hybrid(Head)";
            } else {
                return "Hybrid(Member)";
            }
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
	 * This class manages simple statistic "Result".
	 */
    class Result extends SimpleStats {

        /**
		 * Gets the value of a simple statistic managed by the implementation of this class.
		 * 
		 * @see SimpleStats#getStats()
		 */
        protected String getStats() {
            return "" + result;
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
}

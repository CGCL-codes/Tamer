package com.h9labs.xenstats;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A collection of Citrix XenServer 5 statistics.
 * 
 * @author akutz
 * @param <T> The first key. Specify String if the ordering by UUID. Specify
 *        Long if ordering by time.
 * @param <U> The second key. Specify Long if the ordering by UUID. Specify
 *        String if ordering by time.
 * 
 */
public class XenStats<T, U> extends DefaultHandler {

    private final String rrdString;

    private Long start;

    private Integer step;

    private Long end;

    private Integer rows;

    private Integer columns;

    List<String> legendEntries = new ArrayList<String>();

    HashMap<String, List<String>> legendEntriesAndCounterNames = new HashMap<String, List<String>>();

    List<Long> epochValues = new ArrayList<Long>();

    List<Double> counterValues = new ArrayList<Double>();

    private StringBuffer elementText;

    HashMap<T, HashMap<U, HashMap<String, Double>>> stats;

    List<T> statsL1InOrder = new ArrayList<T>();

    HashMap<T, List<U>> statsL2InOrder = new HashMap<T, List<U>>();

    /**
     * Initializes a new instance of the XenStats class.
     * 
     * @param rrdString The RRD data to initialize the class with.
     */
    private XenStats(String rrdString) {
        this.rrdString = rrdString;
    }

    /**
     * Gets the statistics map.
     * 
     * @return The statistics map.
     */
    public HashMap<T, HashMap<U, HashMap<String, Double>>> getStats() {
        return this.stats;
    }

    /**
     * Gets the keys of the outer most statistics map in the order in which they
     * were added.
     * 
     * @return The keys of the outer most statistics map in the order in which
     *         they were added.
     */
    public List<T> getStatsL1InOrder() {
        return this.statsL1InOrder;
    }

    /**
     * Gets the keys of the second outer most statistics map in the order in
     * which they were added.
     * 
     * @param key The outer key.
     * 
     * @return The keys of the second outer most statistics map in the order in
     *         which they were added.
     */
    public List<U> getStatsL2InOrder(T key) {
        return this.statsL2InOrder.get(key);
    }

    @Override
    public String toString() {
        return this.rrdString;
    }

    /**
     * Gets the first epoch from which statistics were gathered.
     * 
     * @return The first epoch from which statistics were gathered.
     */
    public Long getStart() {
        return this.start;
    }

    /**
     * Gets the interval (in seconds) in which statistic were collected.
     * 
     * @return The interval (in seconds) in which statistic were collected.
     */
    public Integer getStep() {
        return this.step;
    }

    /**
     * Gets the last epoch from which statistics were gathered.
     * 
     * @return The last epoch from which statistics were gathered.
     */
    public Long getEnd() {
        return this.end;
    }

    /**
     * Gets the number of rows (intervals) for which statistics were gathered.
     * 
     * @return The number of rows (intervals) for which statistics were
     *         gathered.
     */
    public Integer getRows() {
        return this.rows;
    }

    /**
     * Gets the total number of columns that were gathered for each row.
     * 
     * @return The total number of columns that were gathered for each row.
     */
    public Integer getColumns() {
        return this.columns;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        this.elementText = new StringBuffer();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementText.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (name.equals("start")) {
            this.start = getLong(elementText.toString());
        } else if (name.equals("step")) {
            this.step = getInteger(elementText.toString());
        } else if (name.equals("end")) {
            this.end = getLong(elementText.toString());
        } else if (name.equals("rows")) {
            this.rows = getInteger(elementText.toString());
        } else if (name.equals("columns")) {
            this.columns = getInteger(elementText.toString());
        } else if (name.equals("entry")) {
            String[] parts = elementText.toString().split(":");
            String uuid = parts[2];
            String counterName = parts[3];
            List<String> counterNames;
            if (!this.legendEntries.contains(uuid)) {
                this.legendEntries.add(uuid);
                counterNames = new ArrayList<String>();
                this.legendEntriesAndCounterNames.put(uuid, counterNames);
            } else {
                counterNames = this.legendEntriesAndCounterNames.get(uuid);
            }
            counterNames.add(counterName);
        } else if (name.equals("t")) {
            Long value = getLong(this.elementText.toString());
            this.epochValues.add(value);
        } else if (name.equals("v")) {
            Double value = getDouble(this.elementText.toString());
            this.counterValues.add(value);
        }
    }

    /**
     * Parses an RRD string and creates a XenStats object that is ordered by
     * time.
     * 
     * @param toParse The RRD string to parse.
     * @return A new XenStats object.
     * @throws Exception When there is an error.
     * @remarks The outer-most map uses the statistical epoch as its key, and
     *          its value is another map. The second outer map uses the UUID of
     *          the element the statistics are for as its key, and its value is
     *          another map. The inner most map uses a statistics counter as its
     *          key and its value is the value of the counter.
     */
    public static XenStats<Long, String> getByTime(String toParse) throws Exception {
        XenStats<Long, String> xs = initXenStats(toParse);
        Iterator<Double> cvIter = xs.counterValues.iterator();
        for (Long epoch : xs.epochValues) {
            HashMap<String, HashMap<String, Double>> epochLgdEntryMap = new HashMap<String, HashMap<String, Double>>();
            List<String> statsInL2Order = new ArrayList<String>();
            for (String uuid : xs.legendEntries) {
                HashMap<String, Double> ctrValueMap = new HashMap<String, Double>();
                List<String> counterNames = xs.legendEntriesAndCounterNames.get(uuid);
                for (String ctrName : counterNames) {
                    Double ctrValue = cvIter.next();
                    ctrValueMap.put(ctrName, ctrValue);
                }
                statsInL2Order.add(uuid);
                epochLgdEntryMap.put(uuid, ctrValueMap);
            }
            xs.statsL2InOrder.put(epoch, statsInL2Order);
            xs.statsL1InOrder.add(epoch);
            xs.stats.put(epoch, epochLgdEntryMap);
        }
        return xs;
    }

    /**
     * Parses an RRD string and creates a XenStats object that is ordered by
     * UUID.
     * 
     * @param toParse The RRD string to parse.
     * @return A new XenStats object.
     * @throws Exception When an error occurs.
     * @remarks The outer-most map uses the UUID of the element the statistics
     *          are for as its key, and its value is another map. The second
     *          outer map uses the statistical epoch as its key, and its value
     *          is another map. The inner most map uses a statistics counter as
     *          its key and its value is the value of the counter.
     */
    public static XenStats<String, Long> getByUuid(String toParse) throws Exception {
        XenStats<String, Long> xs = initXenStats(toParse);
        Iterator<Double> cvIter = xs.counterValues.iterator();
        for (String uuid : xs.legendEntries) {
            HashMap<Long, HashMap<String, Double>> uuidEpochEntryMap = new HashMap<Long, HashMap<String, Double>>();
            List<Long> statsInL2Order = new ArrayList<Long>();
            for (Long epoch : xs.epochValues) {
                HashMap<String, Double> ctrValueMap = new HashMap<String, Double>();
                List<String> counterNames = xs.legendEntriesAndCounterNames.get(uuid);
                for (String ctrName : counterNames) {
                    Double ctrValue = cvIter.next();
                    ctrValueMap.put(ctrName, ctrValue);
                }
                statsInL2Order.add(epoch);
                uuidEpochEntryMap.put(epoch, ctrValueMap);
            }
            xs.statsL2InOrder.put(uuid, statsInL2Order);
            xs.statsL1InOrder.add(uuid);
            xs.stats.put(uuid, uuidEpochEntryMap);
        }
        return xs;
    }

    private static <T, U> XenStats<T, U> initXenStats(String toParse) throws Exception {
        XenStats<T, U> xs = new XenStats<T, U>(toParse);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        ByteArrayInputStream is = new ByteArrayInputStream(toParse.getBytes());
        sp.parse(is, xs);
        xs.stats = new HashMap<T, HashMap<U, HashMap<String, Double>>>();
        return xs;
    }

    private static Long getLong(String toParse) {
        if (toParse == null) {
            return null;
        }
        if (toParse.equals("NaN")) {
            return null;
        }
        return Long.parseLong(toParse);
    }

    private static Integer getInteger(String toParse) {
        if (toParse == null) {
            return null;
        }
        if (toParse.equals("NaN")) {
            return null;
        }
        return Integer.parseInt(toParse);
    }

    private static Double getDouble(String toParse) {
        if (toParse == null) {
            return null;
        }
        if (toParse.equals("NaN")) {
            return null;
        }
        return Double.parseDouble(toParse);
    }
}

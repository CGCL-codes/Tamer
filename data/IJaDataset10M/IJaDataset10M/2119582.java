package AccordionLRACDrawer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import AccordionDrawer.SplitAxis;
import AccordionDrawer.SplitLine;
import AccordionLRACDrawer.bundles.Bundle;
import LRACDataService.IDCAlarmAggRecord;

/**
 *	This class represents a monitored 'device'.  A device is a networked appliance
 * (be it computer/router/switch/refridgerator/whatever) that has properties we can
 * monitor.  These properties are called 'metrics'.  The metrics this device supports
 * are stored in an HashTable called metrics.  
 * 
 * @author Peter McLachlan (spark343@cs.ubc.ca)
 */
public class Device implements Comparable<Device> {

    public static final Color foundColor = Color.BLUE;

    static String sortBy = "custName";

    /**
	 * Reference to drawer
	 */
    AccordionLRACDrawer lrd;

    /**
	 * Complete list of metrics that any device could have. Note: 
	 * The order of this ListArray must be the order of the columns
	 * @see allMetricsIndex
	 */
    private static ArrayList<String> allMetrics = new ArrayList<String>(DataStore.metricEstimate);

    /**
	 * This stores index numbers by metric name for rapid lookup.  Any operation that effects one 
	 * must effect the other
	 */
    private static Hashtable<String, Integer> allMetricsIndex = new Hashtable<String, Integer>(DataStore.metricEstimate);

    /**
	 * The list of metrics this particular device has.
	 */
    Hashtable<String, MetricCell> metrics;

    /**
	 * Name of this device
	 */
    String deviceName;

    /**
	 * Business associated with this device
	 */
    String biz;

    /**
	 * Customer associated with this device
	 */
    String cust;

    /**
	 * Split line above me
	 */
    SplitLine topLine;

    public Device(String deviceName, String biz, String cust, AccordionLRACDrawer lrd) {
        metrics = new Hashtable<String, MetricCell>();
        this.deviceName = deviceName;
        this.biz = biz;
        this.cust = cust;
        setAd(lrd);
    }

    /**
	 * Clear all the static arrays, we're getting fresh data.  
	 */
    static void flushAllMetrics() {
        allMetrics = new ArrayList<String>(DataStore.metricEstimate);
        allMetricsIndex = new Hashtable<String, Integer>(DataStore.metricEstimate);
    }

    /**
	 * Are there any metrics in ANY device ?? (eg. has data been loaded at all...) 
	 * @return
	 */
    static boolean hasData() {
        if (allMetrics != null && allMetrics.size() != 0) return true;
        return false;
    }

    /**
	 * Swap the positions of two metrics in the ordered list of metrics
	 * @param firstMetric
	 * @param secondMetric
	 */
    static void swapMetrics(int firstMetric, int secondMetric) {
        String fMetric = allMetrics.get(firstMetric);
        String sMetric = allMetrics.get(secondMetric);
        allMetrics.set(firstMetric, sMetric);
        allMetrics.set(secondMetric, fMetric);
        allMetricsIndex.remove(fMetric);
        allMetricsIndex.remove(sMetric);
        allMetricsIndex.put(sMetric, firstMetric);
        allMetricsIndex.put(fMetric, secondMetric);
    }

    /**
	 * Retrieve the index number of a metric given the name of the metric. 
	 * -1 if the metric could not be found.  
	 * 
	 * @param metricName
	 * @return metric index number
	 */
    public static int getMetricIndex(String metricName) {
        Integer result = allMetricsIndex.get(metricName);
        if (result == null) return -1; else return result.intValue();
    }

    /**
	 * Retrieve the name of a metric from the list of all monitored metrics
	 * 
	 * @param index
	 * @return
	 */
    public static String getMetricName(int index) {
        return allMetrics.get(index);
    }

    /**
	 * Check if the metric exists, if not, append it.  
	 * 
	 * Note: if metric does not exist, also creates a split line.  
	 * 
	 * @param metricName
	 */
    public static void checkAppendMetric(String metricName, SplitAxis metricAxis) {
        if (!allMetricsIndex.containsKey(metricName)) {
            allMetrics.add(metricName);
            Integer pos = new Integer(allMetrics.lastIndexOf(metricName));
            allMetricsIndex.put(metricName, pos);
            SplitLine newLine;
            if (allMetrics.size() == 1) {
                newLine = metricAxis.getMinLine();
            } else {
                newLine = new SplitLine();
                metricAxis.putAt(newLine, metricAxis.getMaxLine());
            }
        }
    }

    /**
	 * Removes a metric column from the list if it exists.  
	 * @param metricName
	 */
    public static boolean removeMetric(String metricName, SplitAxis metricAxis) {
        Integer metricIndex = allMetricsIndex.remove(metricName);
        if (metricIndex == null) return false;
        Hashtable<String, Integer> newMetricsIndex = new Hashtable<String, Integer>();
        Iterator<String> it = allMetricsIndex.keySet().iterator();
        String deviceName;
        Integer cInt;
        while (it.hasNext()) {
            deviceName = it.next();
            cInt = allMetricsIndex.get(deviceName);
            if (cInt > metricIndex) {
                cInt = new Integer(cInt.intValue() - 1);
                newMetricsIndex.put(deviceName, cInt);
            } else {
                newMetricsIndex.put(deviceName, cInt);
            }
        }
        allMetricsIndex = newMetricsIndex;
        allMetrics.remove(metricIndex.intValue());
        SplitLine lastLine = metricAxis.getMaxLine();
        metricAxis.deleteEntry(lastLine);
        return true;
    }

    /**
	 * Retrieves a metric based on its position in the user specified ordering.
	 * 
	 * NOTE: if the specified metric is not monitored for this device, return is
	 * NULL
	 * 
	 * O(1)
	 * 
	 * @param position
	 * @return MetricCell
	 */
    public MetricCell getMetric(int position) {
        if (position >= 0 && position < allMetrics.size()) {
            String metricName = (String) allMetrics.get(position);
            MetricCell result = (MetricCell) metrics.get(metricName);
            return result;
        }
        System.out.println("Out of range metric request: " + position);
        return null;
    }

    /**
	 * Retrieves a metric from the hashtable based on its name. 
	 * 
	 * @param metricName
	 * @return MetricCell
	 */
    public MetricCell getMetric(String metricName) {
        MetricCell result = (MetricCell) metrics.get(metricName);
        return result;
    }

    /**
	 * Add alarm data to a metric.  If the metric is not being monitored
	 * for this device then it creates a metric cell to contain the data.  
	 * 
	 * 
	 * @param entry
	 * @return
	 */
    public boolean addAlarmData(IDCAlarmAggRecord aRecord) {
        Bundle bundle = aRecord.getBundle();
        String metricName = bundle.getBundleName(aRecord.alarmID);
        if (metricName == null) System.err.println("addAlarmData: null metric name! Crash forthwith.");
        MetricCell cell = getMetric(metricName);
        if (cell == null) {
            cell = new MetricCell(this, metricName, bundle);
            metrics.put(metricName, cell);
        }
        int sev = cell.getHighestSeverity();
        int numAlarms = cell.getNumAlarms();
        int newSev = aRecord.worstAlarm;
        if (newSev > sev) sev = newSev;
        numAlarms += aRecord.numAlarms;
        cell.setSeverity(numAlarms, sev);
        cell.checkAddToCritGroup();
        return true;
    }

    /**
	 * Clear all the MetricCell objects.  Note this does NOT delete any split lines,
	 * it just clears all the tracked metrics for this device.  
	 * 
	 * @return
	 */
    public boolean flushCells() {
        metrics.clear();
        return true;
    }

    public String toString() {
        return deviceName + "(" + getNumOfMetrics() + ")@" + lrd.deviceAxis.getSplitIndex(topLine);
    }

    /**
	 * 
	 * @param x World space position
	 * @param splitAxis 
	 * @param pixelSize
	 * @return
	 */
    public MetricCell pickMetricCell(double x, SplitAxis splitAxis, double pixelSize) {
        SplitLine pickedLine = splitAxis.getSplitFromAbsolute(x, pixelSize, getAd().getFrameNum());
        if (pickedLine == null) return null;
        int sitePosition = splitAxis.getSplitIndex(pickedLine) + 1;
        return getMetric(sitePosition);
    }

    public Hashtable<String, MetricCell> getMetrics() {
        return metrics;
    }

    public AccordionLRACDrawer getAd() {
        return lrd;
    }

    public void setAd(AccordionLRACDrawer lrd) {
        this.lrd = lrd;
    }

    /**
	 * Returns the number of metrics this device is monitoring. 
	 * @return
	 */
    public int getNumOfMetrics() {
        return metrics.size();
    }

    /**
	 * Returns the name of this device.
	 * @return
	 */
    public String getName() {
        return deviceName;
    }

    public int getHeight() {
        return 1;
    }

    public int getKey() {
        if (topLine != null) return lrd.deviceAxis.getSplitIndex(topLine);
        System.out.println("Shouldn't be asking for a metricName of a Device not in the tree!");
        return -2;
    }

    /**
	 * @return Returns the topLine.
	 */
    public SplitLine getTopLine() {
        return topLine;
    }

    /**
	 * @param topLine The topLine to set.
	 */
    public void setTopLine(SplitLine topLine) {
        this.topLine = topLine;
    }

    /**
	 * @return Returns the allMetrics.
	 */
    public static ArrayList<String> getAllMetrics() {
        return allMetrics;
    }

    class metricIndex {

        String metricName;

        int metricIndex;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public int compareTo(Device dev) {
        if (sortBy.equals("custName")) {
            int custNameComp = cust.compareTo(dev.cust);
            if (custNameComp != 0) return custNameComp;
            return deviceName.compareTo(dev.deviceName);
        } else {
            return deviceName.compareTo(dev.deviceName);
        }
    }
}

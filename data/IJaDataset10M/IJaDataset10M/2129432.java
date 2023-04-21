package org.ascape.util.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Collects and stores aggregate statistics on some data source for every specified measurement type
 * of that source, and for each succesive measurement of that source.
 * These stats, when added to a scape, are available for observation by charting tools,
 * data export, etc..
 * Statistics can be automatically collect by the framework, or collected manually.
 * While this class is not abstract, it should be considered so when collecting statistics automatically.
 * This class and its subclasses are intended to collaborate with classes that gather
 * statistics on some collection of objects, by calling <code>clear()</code> to set
 * all values to the appropriate 'zero' value, then <code>addValue()</code> on each object.
 * Pick the stat collector class that provides all the measurements you will want to use.
 * StatCollectorCSA provides count, sum and average measures.
 * StatCollectorCSAMM also provides minimum and maximum measures.
 * StatCollectorCSAMMVar also provides variance and standard deviation measures.
 * Let us know if there are other measurements that you might want to use. (Median is being considered, but will be relativly costly.)
 * The following example creates a class that gathers a count, sum of ages, and average age for supplied
 * objects.
 * To use within ascape, you could create a new class that overrides the <code>getValue</code> and <code>getName</code> methods.
 * <pre><code>
 * class AgeStatCollector extends StatCollectorCSA {
 *    public double getValue(Object object) {
 *         return ((AnObjectWithAge) object).getAge();
 *    }
 *    public String getName() {
 *        return "Age";
 *    }
 * }
 * </code></pre>
 * and then add it to your scape.
 * <pre><code>
 * myScape.addStatCollector(new AgeStatCollector());
 * </code></pre>
 * In practice, we usually just do something like:
 * <pre><code>
 * scape.addStatCollector(new StatCollectorCSA("Age") {
 *    public double getValue(Object object) {
 *         return ((AnObjectWithAge) object).getAge();
 *    }
 * }
 * </code></pre>
 * If you only want to collect statistics on a subpopulation of a scape (i.e. "Wealth of all agents over 30"), use a StatCollectorCond.
 * <BR>
 * Often, you will want to collect statistics on some activity (behavior) that occurs during the model run, instead of on
 * individual agents. For example, it would be hard to collect statistics on agent deaths using automatic stat collection.
 * In this case, use a manual stat collector. Manual stat collectors are still cleared automatically every iteration, but
 * but the developer is responsible for adding values to them. The following example creates a new statistic recording
 * a count of the number of times it was invoked (the number of deaths that have occurred), the total wealth of all agents
 * that have died in the current period, and the average wealth of agents who have died during this period.
 * <pre><code>
 * public void scapeCreated() {
 *     ...
 *     myScape.addStatCollector(new StatCollectorCSA("Death Wealth", false));
 *     ...
 * }
 * ...
 * public void die() {
 *     super.die();
 *     scape.getData().getStatCollector("Death Wealth").addValue(wealth);
 *     ...
 * }
 * </code></pre>
 * Performance note: This manual method requires a string comparison in the model to compare the
 * name of the stat collector to the list of stat collectors. We usually try to avoid this kind of thing inside
 * the body of a running model but for typical usage this hit is probably minimal.
 * To avoid it, you could create a static class member for the stat, or make the stat a member of the parent scape,
 * and access it directly.
 * @see org.ascape.util.data.StatCollectorCond
 * @see org.ascape.util.data.StatCollectorCSA
 * @see org.ascape.util.data.StatCollectorCSAMM
 * @see org.ascape.util.data.StatCollectorCSAMMVar
 * @see org.ascape.util.data.StatCollectorCondCSA
 * @see org.ascape.util.data.StatCollectorCondCSAMM
 * @see org.ascape.util.data.StatCollectorCondCSAMMVar
 * @author Miles Parker
 * @version 2.0
 * @history 2.0 1/27/02 Many small changes since 1.2. Added compareTo functionality.
 * @history 1.2 7/7/1999 made name and auto collect variable, instead of requiring them to be specified by overrdinning methods.
 * @history 1.2 3/9/1999 changed name from ValueSeriesSource, easier to understand, even if not as general
 * @history 1.1.5 4/1/1999 added is auto collect value to support manual value collection
 * @history 1.0.1 3/9/1999 changed name from ValueSeriesSource, easier to understand, even if not as general
 * @since 1.0
 */
public class StatCollector implements Comparable, DataPoint, Serializable {

    /**
     * The number of values collected.
     */
    protected int count;

    /**
     * The name of the stat.
     */
    protected String name = "Unnamed";

    /**
     * Should the stat be collected automatically? Default is true.
     */
    protected boolean autoCollect = true;

    /**
     * The series (plural) that contain the actual statistic results for
     * each measure.
     */
    private DataSeries[] dataSeries;

    public static final int SET_BY_DATAGROUP = 0;

    public static final int COLLECTING = 1;

    public static final int NOT_COLLECTING = 2;

    /**
     * Method by which the stat collector determines if it should be collecting data over time.
     * Default is SET_BY_DATAGROUP.
     */
    private int collectingLongitudinalDataMode = SET_BY_DATAGROUP;

    private DataGroup dataGroup;

    /**
     * Constructs a new StatCollector.
     */
    public StatCollector() {
    }

    /**
     * Constructs a new StatCollector.
     * @param name the name of the stat collector.
     * @param autoCollect should the stat be collected automatically?
     */
    public StatCollector(String name, boolean autoCollect) {
        this.name = name;
        this.autoCollect = autoCollect;
    }

    /**
     * Constructs a new StatCollector. (Automatic by default.)
     * @param name the name of the stat collector.
     */
    public StatCollector(String name) {
        this(name, true);
    }

    /**
     * Override to clear all values related to this stat.
     * Sets count to 0.
     */
    public void clear() {
        count = 0;
    }

    /**
     *
     */
    public void addValueFor(Object object) {
        addValue(getValue(object));
    }

    /**
     * Add the value, incrementing count.
     * Override to track additional measurements.
     */
    public void addValue(double value) {
        count++;
    }

    /**
     * Override to return the value being used to calculate this statistic.
     * For base case, returns 0.0, <i>not</i> the value of interest for the supplied object.
     * We return 0.0 since value doesn't matter in the most limited case;
     * it is unneccesary to override this method if you just want to get a count.
     * If you are implementing a subclass of this stat collector, you <i>must</i> override
     * this class unless you will be collecting the statistic manually, or you are only intersted in collecting a count.
     * Overide the method with a call to the getter for the value
     * this stat is tracking. For example:
     * <pre>
     *    public double getValue(Object object) {
     *        return ((MyClass) object).getMyInterestingValue();
     *    }
     * </pre>
     * @param object an object that contains the value of interest, ignored in base case
     */
    public double getValue(Object object) {
        return 0.0;
    }

    /**
     * Calculate the value to be added (once) to the statistic.
     */
    public double calculateValue() {
        return 0.0;
    }

    /**
     * Is value collected automatically by iterating over a scape?
     * Default is true; override to set false;
     */
    public boolean isCalculated() {
        return false;
    }

    public boolean isPhase2() {
        return false;
    }

    /**
     * Is value collected automatically by iterating over a scape?
     * Default is true; override to set false;
     */
    public boolean isAutoCollect() {
        return autoCollect && !isCalculated();
    }

    /**
     * Set whether the value is collected automatically.
     * @param autoCollect true to collect statistic automatically.
     */
    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }

    public boolean isCollectingLongitudinalData() {
        if (collectingLongitudinalDataMode == NOT_COLLECTING) {
            return false;
        }
        if (collectingLongitudinalDataMode == SET_BY_DATAGROUP) {
            return dataGroup.isCollectingLongitudinalData();
        }
        return true;
    }

    public void setCollectingLongitudinalDataMode(int mode) {
        this.collectingLongitudinalDataMode = mode;
    }

    public int getCollectingLongitudinalDataMode() {
        return collectingLongitudinalDataMode;
    }

    /**
     * Returns the current number of values added.
     */
    public int getCount() {
        return count;
    }

    /**
     * Names for all statistical measures implemented.
     */
    protected static String[] allMeasureNames = { "Count", "Minimum", "Maximum", "Variance", "Standard Deviation", "Sum", "Average" };

    /**
     * Returns all names for measures that statistics collectors are capable of generating.
     */
    public static String[] getAllMeasureNames() {
        return allMeasureNames;
    }

    /**
     * Short names for all statistical measures implemented.
     */
    protected static String[] allMeasureNamesShort = { "Count", "Min", "Max", "Var", "StD", "Sum", "Avg" };

    /**
     * Returns all short names for measures that statistics collectors are capable of generating.
     */
    public static String[] getAllMeasureNamesShort() {
        return allMeasureNamesShort;
    }

    /**
     * Returns a data series for every statistical measure this collector is capable of generating.
     * If these data series don't allready exist, they are created.
     * @see #createDataSeries
     */
    public DataSeries[] getAllDataSeries() {
        if (dataSeries == null) {
            createDataSeries(isCollectingLongitudinalData());
        }
        return dataSeries;
    }

    /**
     * Returns a data series for the measure name provided.
     * For example, myStat.getDataSeries("Count") would return the entire series for Count of myStat.
     * If the series doesn't exist, a runtime excpetion will be thrown.
     */
    public DataSeries getDataSeries(String measureName) {
        if (measureName.equals("Total")) {
            measureName = "Sum";
            System.out.print("Warning, using \"Total " + this + "\" to reference statisitic.");
            System.out.print("Usage is deprecated, use \"Sum " + this + "\" instead.");
        }
        for (int i = 0; i < dataSeries.length; i++) {
            if (dataSeries[i].getMeasureName().equals(measureName)) {
                return dataSeries[i];
            }
        }
        throw new RuntimeException("Data series " + measureName + " of " + this + " doesn't exist.");
    }

    /**
     * Calcualtes the statcollector across the provided list.
     * @param collection
     */
    public void calculateCollection(Collection collection) {
        clear();
        calculateIterator(collection.iterator());
    }

    /**
     * Calcualtes the statcollector across the provided iterator.
     * @param iter the list to calcualte statistics on.
     */
    public void calculateIterator(Iterator iter) {
        clear();
        while (iter.hasNext()) {
            addValueFor(iter.next());
        }
    }

    /**
     * Creates a data series for every measure of this statistic collector.
     */
    public void createDataSeries(boolean collectingLongitudinalData) {
        Vector tempSeries = new Vector();
        if (this instanceof StatCollector) {
            DataSeries countSeries;
            if (collectingLongitudinalData) {
                countSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollector) point).getCount();
                    }

                    public String getMeasureName() {
                        return "Count";
                    }
                };
            } else {
                countSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollector) point).getCount();
                    }

                    public String getMeasureName() {
                        return "Count";
                    }
                };
            }
            countSeries.setDataPoint(this);
            tempSeries.addElement(countSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSAMM) {
            DataSeries minSeries;
            if (collectingLongitudinalData) {
                minSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSAMM) point).getMin();
                    }

                    public String getMeasureName() {
                        return "Minimum";
                    }
                };
            } else {
                minSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSAMM) point).getMin();
                    }

                    public String getMeasureName() {
                        return "Minimum";
                    }
                };
            }
            minSeries.setDataPoint(this);
            tempSeries.addElement(minSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSAMM) {
            DataSeries maxSeries;
            if (collectingLongitudinalData) {
                maxSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSAMM) point).getMax();
                    }

                    public String getMeasureName() {
                        return "Maximum";
                    }
                };
            } else {
                maxSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSAMM) point).getMax();
                    }

                    public String getMeasureName() {
                        return "Maximum";
                    }
                };
            }
            maxSeries.setDataPoint(this);
            tempSeries.addElement(maxSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSAMMVar) {
            DataSeries varianceSeries;
            if (collectingLongitudinalData) {
                varianceSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSAMMVar) point).getVar();
                    }

                    public String getMeasureName() {
                        return "Variance";
                    }
                };
            } else {
                varianceSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSAMMVar) point).getVar();
                    }

                    public String getMeasureName() {
                        return "Variance";
                    }
                };
            }
            varianceSeries.setDataPoint(this);
            tempSeries.addElement(varianceSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSAMMVar) {
            DataSeries stdevSeries;
            if (collectingLongitudinalData) {
                stdevSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSAMMVar) point).getStDev();
                    }

                    public String getMeasureName() {
                        return "Standard Deviation";
                    }
                };
            } else {
                stdevSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSAMMVar) point).getStDev();
                    }

                    public String getMeasureName() {
                        return "Standard Deviation";
                    }
                };
            }
            stdevSeries.setDataPoint(this);
            tempSeries.addElement(stdevSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSA) {
            DataSeries sumSeries;
            if (collectingLongitudinalData) {
                sumSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSA) point).getSum();
                    }

                    public String getMeasureName() {
                        return "Sum";
                    }
                };
            } else {
                sumSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSA) point).getSum();
                    }

                    public String getMeasureName() {
                        return "Sum";
                    }
                };
            }
            sumSeries.setDataPoint(this);
            tempSeries.addElement(sumSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (this instanceof StatCollectorCSA) {
            DataSeries avgSeries;
            if (collectingLongitudinalData) {
                avgSeries = new DataSeriesStore() {

                    public double getValue() {
                        return ((StatCollectorCSA) point).getAvg();
                    }

                    public String getMeasureName() {
                        return "Average";
                    }
                };
            } else {
                avgSeries = new DataSeries() {

                    public double getValue() {
                        return ((StatCollectorCSA) point).getAvg();
                    }

                    public String getMeasureName() {
                        return "Average";
                    }
                };
            }
            avgSeries.setDataPoint(this);
            tempSeries.addElement(avgSeries);
        } else {
            tempSeries.addElement(null);
        }
        if (collectingLongitudinalData) {
            dataSeries = new DataSeriesStore[tempSeries.size()];
        } else {
            dataSeries = new DataSeries[tempSeries.size()];
        }
        tempSeries.copyInto(dataSeries);
    }

    public void setDataGroup(DataGroup dataGroup) {
        this.dataGroup = dataGroup;
    }

    public DataGroup getDataGroup() {
        return dataGroup;
    }

    /**
     * Override to provide a short name, or set a name, in the constructor or using this method.
     * By default, returns "Unnamed". You should always provide a name unless
     * the stat is temporary. Otherwise, there will be duplicate stat names, and there will
     * be no way to select statistics by name! For example:
     * <pre>
     *    public double getName(Object object) {
     *        return "Interesting Value";
     *    }
     * </pre>
     * Or simply:
     * myStat.setName("Interesting Value");
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this statistic.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a short desription of this stat collector.
     */
    public String toString() {
        return getName() + " StatCollector";
    }

    /**
     * Compares one stat collector to another by name. Again, it is important to ensure that all stats within a datagroup have different names.
     */
    public int compareTo(Object o) {
        return this.getName().compareTo(((StatCollector) o).getName());
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (dataSeries != null && dataSeries[0] != null && dataSeries[0] instanceof DataSeriesStore) {
            clear();
            for (int i = 0; i < dataSeries.length; i++) {
                DataSeries ds = dataSeries[i];
                if (ds != null) {
                    ds.clear();
                }
            }
        }
        out.defaultWriteObject();
    }
}

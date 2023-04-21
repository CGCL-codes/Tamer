package com.sun.corba.se.spi.monitoring;

import java.util.*;

/**
 * <p>
 * 
 * @author Hemanth Puttaswamy
 * </p>
 * <p>
 * StatisticsMonitoredAttribute is provided as a convenience to collect the
 * Statistics of any entity. The getValue() call will be delegated to the
 * StatisticsAccumulator set by the user.
 * </p>
 */
public class StatisticMonitoredAttribute extends MonitoredAttributeBase {

    private StatisticsAccumulator statisticsAccumulator;

    private Object mutex;

    /**
 * <p>
 * Constructs the StaisticMonitoredAttribute, builds the required
 * MonitoredAttributeInfo with Long as the class type and is always
 * readonly attribute.
 * </p>
 * <p>
 * 
 * @param name Of this attribute
 * </p>
 * <p>
 * @return a StatisticMonitoredAttribute 
 * </p>
 * <p>
 * @param desc should provide a good description on the kind of statistics 
 * collected, a good example is "Connection Response Time Stats will Provide the
 * detailed stats based on the samples provided from every request completion
 * time"
 * </p>
 * <p>
 * @param s is the StatisticsAcumulator that user will use to accumulate the 
 * samples and this Attribute Object will get the computed statistics values
 * from.
 * </p>
 * <p>
 * @param mutex using which clearState() and getValue() calls need to be locked.
 * </p>
 */
    public StatisticMonitoredAttribute(String name, String desc, StatisticsAccumulator s, Object mutex) {
        super(name);
        MonitoredAttributeInfoFactory f = MonitoringFactories.getMonitoredAttributeInfoFactory();
        MonitoredAttributeInfo maInfo = f.createMonitoredAttributeInfo(desc, String.class, false, true);
        this.setMonitoredAttributeInfo(maInfo);
        this.statisticsAccumulator = s;
        this.mutex = mutex;
    }

    /**
 *  Gets the value from the StatisticsAccumulator, the value will be a formatted
 *  String with the computed statistics based on the samples accumulated in the
 *  Statistics Accumulator.
 */
    public Object getValue() {
        synchronized (mutex) {
            return statisticsAccumulator.getValue();
        }
    }

    /**
 *  Clears the state on Statistics Accumulator, After this call all samples are
 *  treated fresh and the old sample computations are disregarded.
 */
    public void clearState() {
        synchronized (mutex) {
            statisticsAccumulator.clearState();
        }
    }

    /**
 *  Gets the statistics accumulator associated with StatisticMonitoredAttribute.
 *  Usually, the user don't need to use this method as they can keep the handle
 *  to Accumulator to collect the samples.
 */
    public StatisticsAccumulator getStatisticsAccumulator() {
        return statisticsAccumulator;
    }
}

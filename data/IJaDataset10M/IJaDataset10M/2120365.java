package net.sourceforge.stat4j;

import java.util.Properties;

/**
 * Name:		Calculator.java
 * Date:		Aug 29, 2004
 * Description:
 * 
 * Calculator contract for all Calculators that calculate/derive
 * statistic values from metrics. Metrics are are collected/scraped 
 * from log messages and applies to calculators to calculate 
 * the statstic value.
 * 
 * @see Metric
 * @see Statistic 
 * 
 * @author Lara D'Abreo
 */
public interface Calculator {

    public String getName();

    public boolean isApplyImmediate();

    public void init(String name, Properties properties);

    public Statistic getStatistic();

    public void setStatistic(Statistic statistc);

    public void applyMetric(Metric metric);

    public void reset();

    public double getResult();

    public long getTimestamp();
}

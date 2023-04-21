package org.xaware.server.management.statistics;

/**
 * The interface operations are exposed as 
 * jmx operations for the BizViewSessionEntry bean.
 *  
 * @author satish
 *
 */
public interface BizViewSessionEntryMBean {

    /**
     * Handles the JMX Get Request for bizDocName
     */
    public String getBizDocName();

    /**
     * Handles the JMX Get Request for bizDocStarts
     */
    public long getBizDocStarts();

    /**
     * Handles the JMX Get Request for bizDocCompletes
     */
    public long getBizDocCompletes();

    /**
     * Handles the JMX Get Request for bizDocErrors
     */
    public long getBizDocErrors();

    /**
     * Handles the JMX Get Request for bizDocMinDuration
     */
    public double getBizDocMinDuration();

    /**
     * Handles the JMX Get Request for bizDocMaxDuration
     */
    public double getBizDocMaxDuration();

    /**
     * Handles the JMX Get Request for bizDocAvgDuration
     */
    public double getBizDocAvgDuration();

    /**
     * Handles the JMX Get Request for bizDocDurationStd
     */
    public double getBizDocDurationStdDev();
}

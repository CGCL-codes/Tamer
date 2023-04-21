package org.opennms.netmgt.statsd;

import java.util.Date;
import java.util.SortedSet;
import org.opennms.netmgt.model.AttributeStatistic;

/**
 * @author <a href="dj@opennms.org">DJ Gregor</a>
 */
public interface ReportInstance {

    public void walk();

    public SortedSet<AttributeStatistic> getResults();

    public String getResourceTypeMatch();

    public void setResourceTypeMatch(String resourceType);

    public String getAttributeMatch();

    public void setAttributeMatch(String attr);

    public long getStartTime();

    public void setStartTime(long start);

    public long getEndTime();

    public void setEndTime(long end);

    public String getConsolidationFunction();

    public void setConsolidationFunction(String cf);

    public int getCount();

    public void setCount(int count);

    public Date getJobStartedDate();

    public Date getJobCompletedDate();

    public String getName();

    public String getDescription();

    public long getRetainInterval();

    public ReportDefinition getReportDefinition();

    public void setReportDefinition(ReportDefinition definition);

    public void setResourceAttributeKey(String resourceAttributeKey);

    public String getResourceAttributeKey();

    public void setResourceAttributeValueMatch(String resourceAttributeValueMatch);

    public String getResourceAttributeValueMatch();
}

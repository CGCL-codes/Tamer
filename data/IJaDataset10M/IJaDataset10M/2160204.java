package edu.ucdavis.genomics.metabolomics.binbase.bdi.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @swt
 * 
 * @hibernate.class table = "TIME_FRAME" dynamic-insert = "true" dynamic-update =
 *                  "true" schema="QC"
 * 
 */
public class TimeFrame implements Serializable, Comparable {

    Integer id;

    Integer days;

    Date from;

    Date to;

    String name;

    private Collection reports;

    /**
	 * @swt.variable visible="true" name="Days" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "day_range" update = "true" insert = "true"
	 *                     not-null = "false"
	 */
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    /**
	 * @swt.variable visible="true" name="From" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "from_date" update = "true" insert = "true"
	 *                     not-null = "false"
	 */
    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    /**
	 * @swt.variable visible="false" name="Id" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.id column = "timeframe_id" generator-class = "native"
	 *
	 * not-null = "true"
	 * @hibernate.generator-param name = "sequence" value = "QC_SEQUENCE"
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @swt.variable visible="true" name="To" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "to_date" update = "true" insert = "true"
	 *                     not-null = "false"
	 */
    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    /**
	 * *
	 * 
	 * @swt.variable visible="true" name="Name" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "name" update = "true" insert = "true"
	 *                     not-null = "true"
	 * @author wohlgemuth
	 * @version Jun 1, 2006
	 * @return
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @hibernate.set lazy="true" cascade = "none" inverse = "true"
	 * @hibernate.collection-one-to-many class =
	 *                                   "edu.ucdavis.genomics.metabolomics.binbase.bdi.report.Report" column="TIMERAME_ID"
	 * @hibernate.collection-key column = "TIMERAME_ID"
	 */
    public Collection getReports() {
        return reports;
    }

    public void setReports(Collection reports) {
        this.reports = reports;
    }

    public void addReport(Report report) {
        if (this.getReports() == null) {
            this.setReports(new HashSet());
        }
        report.setTimeFrame(this);
        this.getReports().add(report);
    }

    public String toString() {
        return this.getName();
    }

    public int compareTo(Object arg0) {
        if (arg0 instanceof TimeFrame) {
            return ((TimeFrame) arg0).getId().compareTo(this.getId());
        }
        return arg0.toString().compareTo(this.toString());
    }
}

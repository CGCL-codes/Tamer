package org.plazmaforge.framework.core.period;

import java.util.Date;

/** 
 * @author Oleh Hapon
 * $Id: DatePeriod.java,v 1.1 2010/12/05 07:51:31 ohapon Exp $
 */
public class DatePeriod implements IDatePeriod {

    private Date startDate;

    private Date endDate;

    private String code;

    private String name;

    public DatePeriod() {
        super();
    }

    public DatePeriod(String code, String name, Date startDate, Date endDate) {
        super();
        this.code = code;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DatePeriod(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setPeriodDate(Date startDate, Date endDate) {
        setStartDate(startDate);
        setEndDate(endDate);
    }
}

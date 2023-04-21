package org.helianto.core.filter.base;

import java.util.Date;
import org.helianto.core.TrunkEntity;
import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.core.criteria.DateCriteriaBuilder;
import org.helianto.core.filter.DateInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Filter base class to be used with date fields.
 * 
 * @author Mauricio Fernandes de Castro
 */
public abstract class AbstractDateIntervalFilterAdapter<T extends TrunkEntity> extends AbstractTrunkFilterAdapter<T> implements DateInterval {

    private static final long serialVersionUID = 1L;

    private int interval = -7;

    /** 
	 * Default constructor
	 * 
	 * @param filter
	 */
    public AbstractDateIntervalFilterAdapter(T filter) {
        super(filter);
    }

    /**
	 * The date field name to be used with this filter.
	 */
    public String getDateFieldName() {
        return "";
    }

    /**
     * Start date
     */
    @DateTimeFormat(style = "S-")
    public Date getFromDate() {
        return null;
    }

    /**
     * End date
     */
    @DateTimeFormat(style = "S-")
    public Date getToDate() {
        return new Date();
    }

    /**
     * Interval to be added.
     */
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendDateInterval(mainCriteriaBuilder);
    }

    /**
	 * Return the date interval as this filter adapter by default.
	 */
    protected DateInterval getDateInterval() {
        return this;
    }

    public void appendDateInterval(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendDateInterval(mainCriteriaBuilder, getDateFieldName(), getDateInterval());
    }

    public void appendDateInterval(OrmCriteriaBuilder mainCriteriaBuilder, String dateFieldName, DateInterval dateInterval) {
        if (getDateFieldName().length() == 0) {
            logger.debug("Date range filter disabled");
        } else {
            DateCriteriaBuilder dateCriteria = new DateCriteriaBuilder(mainCriteriaBuilder.getAlias(), dateFieldName);
            dateCriteria.appendFromDateRange(dateInterval.getFromDate(), dateInterval.getToDate(), dateInterval.getInterval());
            dateCriteria.appendToDateRange(dateInterval.getFromDate(), dateInterval.getToDate(), dateInterval.getInterval());
            if (dateCriteria.getSegmentCount() > 0) {
                mainCriteriaBuilder.appendAnd().append(dateCriteria);
            }
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AbstractDateIntervalFilterAdapter.class);
}

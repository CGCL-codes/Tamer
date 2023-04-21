package org.helianto.message.filter.classic;

import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.document.filter.classic.AbstractRecordFilter;

/**
 * Base class to follow up filters.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated
 */
public abstract class AbstractFollowUpFilter extends AbstractRecordFilter {

    private static final long serialVersionUID = 1L;

    private char notificationOption;

    /** 
     * Default constructor.
     */
    protected AbstractFollowUpFilter() {
        super();
        setNotificationOption(' ');
    }

    /**
     * Notification option.
     */
    public char getNotificationOption() {
        return this.notificationOption;
    }

    public void setNotificationOption(char notificationOption) {
        this.notificationOption = notificationOption;
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        super.doFilter(mainCriteriaBuilder);
        appendEqualFilter("notificationOption", getNotificationOption(), mainCriteriaBuilder);
    }
}

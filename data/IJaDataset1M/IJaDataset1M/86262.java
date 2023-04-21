package org.jmesa.core.filter;

import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.jmesa.web.WebContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The date filter matcher for Joda Time dates.
 *
 * @since 2.4.4
 * @author Jeff Johnston
 */
public class DateTimeFilterMatcher extends DateFilterMatcher {

    private Logger logger = LoggerFactory.getLogger(DateTimeFilterMatcher.class);

    public DateTimeFilterMatcher() {
    }

    public DateTimeFilterMatcher(String pattern) {
        super(pattern);
    }

    public DateTimeFilterMatcher(String pattern, WebContext webContext) {
        super(pattern, webContext);
    }

    @Override
    public boolean evaluate(Object itemValue, String filterValue) {
        if (itemValue == null) {
            return false;
        }
        String pattern = getPattern();
        if (pattern == null) {
            logger.debug("The filter (value " + filterValue + ") is trying to match against a date column using " + "the DateTimeFilterMatcher, but there is no pattern defined. You need to register a " + "DateTimeFilterMatcher to be able to filter against this column.");
            return false;
        }
        Locale locale = null;
        WebContext webContext = getWebContext();
        if (webContext != null) {
            locale = webContext.getLocale();
        }
        DateTime dateTime = (DateTime) itemValue;
        if (locale != null) {
            itemValue = dateTime.toString(pattern, locale);
        } else {
            itemValue = dateTime.toString(pattern);
        }
        String item = String.valueOf(itemValue);
        String filter = String.valueOf(filterValue);
        if (StringUtils.contains(item, filter)) {
            return true;
        }
        return false;
    }
}

package com.netstoke.core.calendar.dao;

import com.netstoke.core.calendar.ICalendarService;
import com.netstoke.persistence.IDataAccessObject;

/**
 * <p>The <code>ICalendarDao</code> is responsible for defining the persistence operations for 
 * the {@link com.netstoke.core.calendar.ICalendarService} interface.</p>
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 * @see com.netstoke.core.calendar.ICalendarService
 */
public interface ICalendarDao extends ICalendarService, IDataAccessObject {

    static final long serialVersionUID = -8905249523480649417L;
}

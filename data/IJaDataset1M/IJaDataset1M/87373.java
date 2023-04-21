package org.rbx.sims.calendar.dao;

import com.rbx.dao.support.CrudDaoSupport;
import org.rbx.sims.model.SimsCalendar;

/**
 * 
 * User: franco
 * Date: 02/02/2009
 * Time: 09:19:16 AM
 */
public interface CalendarDao extends CrudDaoSupport<SimsCalendar> {

    SimsCalendar find(Long calendarId);
}

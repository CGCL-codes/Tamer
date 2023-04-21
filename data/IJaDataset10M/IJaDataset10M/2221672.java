package org.multimedia.jolitorask.extension.calendar;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import javax.ejb.EJBLocalObject;

public interface CalendarWrapperLocal extends EJBLocalObject {

    public void parseAll() throws SQLException, MalformedURLException;

    public void addCalendar(String context, int id_utilisateur, URL url) throws SQLException;

    public void deleteCalendar(int id_utilisateur) throws SQLException;

    public void updateCalendar(int id_calendar, URL url) throws SQLException;
}

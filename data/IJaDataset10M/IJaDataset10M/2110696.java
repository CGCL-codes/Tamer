package org.icehockeymanager.ihm.game.eventlog;

import java.io.*;
import java.util.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * EventLog contains all logged events of a game.
 * 
 * @author Bernhard von Gunten, Arik Dasen
 * @created October, 2004
 */
public class EventLog implements Serializable {

    static final long serialVersionUID = 6600233904830880793L;

    /**
   * Log
   */
    Vector<EventLogEntry> log = null;

    /**
   * EventLog constructor
   */
    public EventLog() {
        log = new Vector<EventLogEntry>();
    }

    /**
   * Add event log entry.
   * 
   * @param logEntry
   *          EventLogEntry
   */
    public void addEventLogEntry(EventLogEntry logEntry) {
        this.log.add(logEntry);
    }

    /**
   * Returns all log entries until specific date.
   * 
   * @param until
   *          Calendar
   * @return EventLogEntry[]
   */
    public EventLogEntry[] getLog(Calendar until) {
        Vector<EventLogEntry> result = new Vector<EventLogEntry>();
        java.util.Collections.sort(log);
        for (int i = 0; i < log.size(); i++) {
            EventLogEntry tmp = log.get(i);
            if (tmp.getDay().before(until) || tmp.getDay().equals(until)) {
                result.add(tmp);
            }
        }
        return result.toArray(new EventLogEntry[result.size()]);
    }

    /** sReturn the log entries of the user, his team, and his players 
   * @param user 
   * @param until 
   * @return EventLogEntry[] */
    public EventLogEntry[] getUserLog(User user, Calendar until) {
        Vector<EventLogEntry> result = new Vector<EventLogEntry>();
        EventLogEntry[] tmpList = getLog(until);
        for (int i = 0; i < tmpList.length; i++) {
            boolean userFlag = false;
            if (tmpList[i].getPlayer() != null) {
                userFlag = user.getTeam().equals(tmpList[i].getPlayer().getTeam());
            }
            if (user.getTeam().equals(tmpList[i].getTeam())) {
                userFlag = true;
            }
            if (user.equals(tmpList[i].getUser())) {
                userFlag = true;
            }
            if (userFlag) {
                result.add(tmpList[i]);
            }
        }
        return result.toArray(new EventLogEntry[result.size()]);
    }

    /** Return the log entries of a team and his players 
   * @param team 
   * @param until 
   * @return EventLogEntry[] */
    public EventLogEntry[] getTeamLog(Team team, Calendar until) {
        Vector<EventLogEntry> result = new Vector<EventLogEntry>();
        EventLogEntry[] tmpList = getLog(until);
        for (int i = 0; i < tmpList.length; i++) {
            boolean userFlag = false;
            if (tmpList[i].getPlayer() != null) {
                userFlag = team.equals(tmpList[i].getPlayer().getTeam());
            }
            if (team.equals(tmpList[i].getTeam())) {
                userFlag = true;
            }
            if (userFlag) {
                result.add(tmpList[i]);
            }
        }
        return result.toArray(new EventLogEntry[result.size()]);
    }
}

package org.promotego.beans;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ScheduledGame extends BeanSupport<ScheduledGame> {

    private User m_offerer;

    private User m_accepter;

    private Date m_startTime;

    private int m_duration;

    private Location m_location;

    private Date m_lastReminder;

    public ScheduledGame() {
    }

    public ScheduledGame(OfferedGame offeredGame) {
        setOfferer(offeredGame.getOfferer());
        setStartTime(offeredGame.getStartTime());
        setDuration(offeredGame.getDuration());
        setLocation(offeredGame.getLocation());
    }

    @ManyToOne
    public User getAccepter() {
        return m_accepter;
    }

    public void setAccepter(User accepter) {
        m_accepter = accepter;
    }

    public int getDuration() {
        return m_duration;
    }

    public void setDuration(int duration) {
        m_duration = duration;
    }

    @ManyToOne
    public Location getLocation() {
        return m_location;
    }

    public void setLocation(Location location) {
        m_location = location;
    }

    @ManyToOne
    public User getOfferer() {
        return m_offerer;
    }

    public void setOfferer(User offerer) {
        m_offerer = offerer;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getStartTime() {
        return m_startTime;
    }

    public void setStartTime(Date startTime) {
        m_startTime = startTime;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getLastReminder() {
        return m_lastReminder;
    }

    public void setLastReminder(Date lastReminder) {
        m_lastReminder = lastReminder;
    }
}

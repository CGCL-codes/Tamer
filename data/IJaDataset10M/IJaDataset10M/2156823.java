package net.sf.brightside.instantevents.metamodel;

import java.io.Serializable;
import java.util.Date;

public interface Rating {

    public String getSubject();

    public void setSubject(String subject);

    public int getPositiveApplicationRating();

    public void setPositiveApplicationRating(int positiveApplicationRating);

    public int getNegativeApplicationRating();

    public void setNegativeApplicationRating(int negativeApplicationRating);

    public int getPositiveOrganizerRating();

    public void setPositiveOrganizerRating(int positiveOrganizerRating);

    public int getNegativeOrganizerRating();

    public void setNegativeOrganizerRating(int negativeOrganizerRating);

    public Date getDate();

    public void setDate(Date date);

    public User getUser();

    public void setUser(User user);

    public User getCreator();

    public void setCreator(User creator);

    public Event getEvent();

    public void setEvent(Event event);

    public Serializable takeId();
}

package mpower_hibernate;

import java.util.*;

/**
 *
 * @author etkivbe
 */
public class Reminder {

    private long ReminderID;

    private User User;

    private String Type;

    private long RefID;

    private Date StartTime;

    private boolean IsProactive;

    private String URL;

    private String ExtraData;

    private String ReminderText;

    public long getReminderID() {
        return ReminderID;
    }

    public void setReminderID(long ReminderID) {
        this.ReminderID = ReminderID;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public long getRefID() {
        return RefID;
    }

    public void setRefID(long RefID) {
        this.RefID = RefID;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date StartTime) {
        this.StartTime = StartTime;
    }

    public boolean isIsProactive() {
        return IsProactive;
    }

    public void setIsProactive(boolean IsProactive) {
        this.IsProactive = IsProactive;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getExtraData() {
        return ExtraData;
    }

    public void setExtraData(String ExtraData) {
        this.ExtraData = ExtraData;
    }

    public String getReminderText() {
        return ReminderText;
    }

    public void setReminderText(String ReminderText) {
        this.ReminderText = ReminderText;
    }
}

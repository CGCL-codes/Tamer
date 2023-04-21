package ru.point.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
import org.joda.time.LocalDate;
import ru.point.utils.Utils;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Mikhail Sedov [09.01.2009]
 */
@Entity
public class User {

    @PrimaryKey(sequence = "id")
    private long id;

    private Profile profile;

    @SecondaryKey(relate = Relationship.ONE_TO_ONE)
    private String login;

    private String password;

    private Set<Activity> activities = new TreeSet<Activity>();

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private long hireDate;

    private boolean isFemale;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getFullName() {
        return profile.getFirstName() + " " + profile.getSecondName();
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Utils.md5(password);
    }

    public long getHireDate() {
        return hireDate;
    }

    public void setHireDate(long hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        this.isFemale = female;
    }

    public Activity getMainActivity() {
        for (Activity activity : activities) {
            if (activity.isMain()) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User: " + id + " / " + profile + " / " + activities;
    }
}

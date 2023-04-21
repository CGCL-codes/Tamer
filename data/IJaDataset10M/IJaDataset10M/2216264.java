package com.redhipps.hips.client.model;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Doctor extends Model {

    @Persistent(serialized = "true")
    private User login;

    @Persistent
    private String name;

    @Persistent
    private DoctorConstraint defaultConstraints;

    @Persistent
    private boolean active;

    public Doctor() {
    }

    @Deprecated
    public Doctor(PythonDatastoreKey key) {
        super(key);
    }

    public User getLogin() {
        return login;
    }

    public void setLogin(User login) {
        this.login = login;
    }

    @Deprecated
    public void setLogin(String login) {
        this.login = new User(login);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DoctorConstraint getDefaultConstraints() {
        return defaultConstraints;
    }

    public void setDefaultConstraints(DoctorConstraint defaultConstraints) {
        this.defaultConstraints = defaultConstraints;
    }

    @Deprecated
    public int getMinShifts() {
        return this.defaultConstraints.getMinShifts();
    }

    @Deprecated
    public void setMinShifts(int minShifts) {
        this.defaultConstraints.setMinShifts(minShifts);
    }

    @Deprecated
    public int getMaxShifts() {
        return this.defaultConstraints.getMaxShifts();
    }

    @Deprecated
    public void setMaxShifts(int maxShifts) {
        this.defaultConstraints.setMaxShifts(maxShifts);
    }

    @Deprecated
    public int getMaxNightShifts() {
        return this.defaultConstraints.getMaxNightShifts();
    }

    @Deprecated
    public void setMaxNightShifts(int maxNightShifts) {
        this.defaultConstraints.setMaxNightShifts(maxNightShifts);
    }

    @Deprecated
    public int getMaxConsecutiveDays() {
        return this.defaultConstraints.getMaxConsecutiveDays();
    }

    @Deprecated
    public void setMaxConsecutiveDays(int maxConsecutiveDays) {
        this.defaultConstraints.setMaxConsecutiveDays(maxConsecutiveDays);
    }
}

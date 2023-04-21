package com.w5hh.login.vo;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Evandro Rosa Santos <evandro.santos@email.com>
 */
@Entity
@Table(name = "tbprofile")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(generator = "seq_profile", strategy = GenerationType.TABLE)
    @SequenceGenerator(sequenceName = "seq_profile", allocationSize = 1, initialValue = 1, name = "gen_profile")
    @Column(name = "idprofile", unique = true, nullable = false)
    protected Long id;

    @Column(name = "profile", nullable = false, length = 35)
    protected String profile;

    @Column(name = "administrator")
    protected boolean administrator;

    @OneToMany
    protected Set<Place> places;

    @OneToMany
    protected Set<Permission> permissions;

    @ManyToOne
    protected Application application;

    public Profile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "com.w5hh.vo.Profile: " + id + " - " + profile;
    }
}

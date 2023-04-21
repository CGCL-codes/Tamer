package es.viewerfree.gwt.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "vf_users")
@NamedQueries({ @NamedQuery(name = "findUserByUserAndPassword", query = "SELECT u from User u where u.user = ? AND u.password = ?"), @NamedQuery(name = "findUserByUser", query = "SELECT u from User u where u.user = ?"), @NamedQuery(name = "findAllUsers", query = "SELECT u from User u") })
public class User {

    @Override
    public String toString() {
        return "User [_id=" + _id + ", user=" + user + ", password=" + password + ", name=" + name + ", surname=" + surname + ", email=" + email + ", profile=" + profile + "]";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_USER")
    private long _id;

    @Column(name = "login", unique = true)
    private String user;

    private String password;

    private String name;

    private String surname;

    private String email;

    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (_id ^ (_id >>> 32));
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((profile == null) ? 0 : profile.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (_id != other._id) return false;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (password == null) {
            if (other.password != null) return false;
        } else if (!password.equals(other.password)) return false;
        if (profile == null) {
            if (other.profile != null) return false;
        } else if (!profile.equals(other.profile)) return false;
        if (surname == null) {
            if (other.surname != null) return false;
        } else if (!surname.equals(other.surname)) return false;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        return true;
    }
}

package com.jsu.hibernate.domains;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class LoginLog implements Serializable {

    private static final long serialVersionUID = -29324283L;

    /**
	 * Fields
	 */
    private String id;

    private User user;

    private String ip;

    private Date date;

    private String role;

    public LoginLog() {
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return the user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @return the ip
	 */
    public String getIp() {
        return ip;
    }

    /**
	 * @return the date
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * @return the role
	 */
    public String getRole() {
        return role;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @param user the user to set
	 */
    public void setUser(User user) {
        this.user = user;
    }

    /**
	 * @param ip the ip to set
	 */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
	 * @param date the date to set
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * @param role the role to set
	 */
    public void setRole(String role) {
        this.role = role;
    }
}

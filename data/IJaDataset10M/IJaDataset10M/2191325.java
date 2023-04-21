package com.ecyrd.jspwiki;

/**
 * User: SimonLei
 * Date: 2004-10-3
 * Time: 9:04:22
 * $Id: WikiUser.java,v 1.1 2004/10/19 07:31:56 echou Exp $
 */
public class WikiUser {

    private long id = -1;

    private String name;

    private String passwd;

    private String theGroup;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTheGroup() {
        return theGroup;
    }

    public void setTheGroup(String theGroup) {
        this.theGroup = theGroup;
    }
}

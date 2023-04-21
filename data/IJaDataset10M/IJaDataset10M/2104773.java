package com.bugfree4j.per.test.hibernatetest.domain;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="sysuser"
 *     
*/
public class Sysuser implements Serializable {

    /** identifier field */
    private String username;

    /** persistent field */
    private String userpassword;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String sex;

    /** nullable persistent field */
    private Date birthday;

    /** nullable persistent field */
    private String notes;

    /** full constructor */
    public Sysuser(String username, String userpassword, String name, String sex, Date birthday, String notes) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.notes = notes;
    }

    /** default constructor */
    public Sysuser() {
    }

    /** minimal constructor */
    public Sysuser(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="username"
     *         
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** 
     *            @hibernate.property
     *             column="userpassword"
     *             length="15"
     *             not-null="true"
     *         
     */
    public String getUserpassword() {
        return this.userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="16"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="sex"
     *             length="2"
     *         
     */
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /** 
     *            @hibernate.property
     *             column="birthday"
     *             length="19"
     *         
     */
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /** 
     *            @hibernate.property
     *             column="notes"
     *             length="100"
     *         
     */
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return new ToStringBuilder(this).append("username", getUsername()).toString();
    }
}

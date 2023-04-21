package com.spring.hibernate.addressbook.model.obj.addressbook;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * Object mapping for hibernate-handled table: users
 * author: auto-generated
 */
@Entity
@Table(name = "users", catalog = "addressbook")
public class Users implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039804L;

    /** Field mapping */
    private String birthname;

    /** Field mapping */
    private Date dateofbirth;

    /** Field mapping */
    private String firstname;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private String lastname;

    /** Field mapping */
    private String pw;

    /** Field mapping */
    private String username;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public Users() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public Users(Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: birthname
	 * @return A String object (this.birthname)
	 */
    @Column(length = 64)
    public String getBirthname() {
        return this.birthname;
    }

    /**  
     * Set the value related to the column: birthname 
	 * @param birthname the birthname value you wish to set
	 */
    public void setBirthname(final String birthname) {
        this.birthname = birthname;
    }

    /**
     * Return the value associated with the column: dateofbirth
	 * @return A Date object (this.dateofbirth)
	 */
    public Date getDateofbirth() {
        return this.dateofbirth;
    }

    /**  
     * Set the value related to the column: dateofbirth 
	 * @param dateofbirth the dateofbirth value you wish to set
	 */
    public void setDateofbirth(final Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    /**
     * Return the value associated with the column: firstname
	 * @return A String object (this.firstname)
	 */
    @Column(length = 64)
    public String getFirstname() {
        return this.firstname;
    }

    /**  
     * Set the value related to the column: firstname 
	 * @param firstname the firstname value you wish to set
	 */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    /**
     * Return the value associated with the column: id
	 * @return A Integer object (this.id)
	 */
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**  
     * Set the value related to the column: id 
	 * @param id the id value you wish to set
	 */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: lastname
	 * @return A String object (this.lastname)
	 */
    @Column(length = 64)
    public String getLastname() {
        return this.lastname;
    }

    /**  
     * Set the value related to the column: lastname 
	 * @param lastname the lastname value you wish to set
	 */
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    /**
     * Return the value associated with the column: pw
	 * @return A String object (this.pw)
	 */
    @Column(length = 64)
    public String getPw() {
        return this.pw;
    }

    /**  
     * Set the value related to the column: pw 
	 * @param pw the pw value you wish to set
	 */
    public void setPw(final String pw) {
        this.pw = pw;
    }

    /**
     * Return the value associated with the column: username
	 * @return A String object (this.username)
	 */
    @Column(length = 64)
    public String getUsername() {
        return this.username;
    }

    /**  
     * Set the value related to the column: username 
	 * @param username the username value you wish to set
	 */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Deep copy
	 * @throws CloneNotSupportedException
     */
    @Override
    public Users clone() throws CloneNotSupportedException {
        super.clone();
        Users copy = new Users();
        copy.setBirthname(this.getBirthname());
        copy.setDateofbirth(this.getDateofbirth());
        copy.setFirstname(this.getFirstname());
        copy.setId(this.getId());
        copy.setLastname(this.getLastname());
        copy.setPw(this.getPw());
        copy.setUsername(this.getUsername());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("birthname: " + this.birthname + ", ");
        sb.append("dateofbirth: " + this.dateofbirth + ", ");
        sb.append("firstname: " + this.firstname + ", ");
        sb.append("id: " + this.id + ", ");
        sb.append("lastname: " + this.lastname + ", ");
        sb.append("pw: " + this.pw + ", ");
        sb.append("username: " + this.username);
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(Users.class)))) return false;
        Users that = (Users) aThat;
        return (((this.birthname == null) && (that.birthname == null)) || (this.birthname != null && this.birthname.equals(that.birthname))) && (((this.dateofbirth == null) && (that.dateofbirth == null)) || (this.dateofbirth != null && this.dateofbirth.equals(that.dateofbirth))) && (((this.firstname == null) && (that.firstname == null)) || (this.firstname != null && this.firstname.equals(that.firstname))) && (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.lastname == null) && (that.lastname == null)) || (this.lastname != null && this.lastname.equals(that.lastname))) && (((this.pw == null) && (that.pw == null)) || (this.pw != null && this.pw.equals(that.pw))) && (((this.username == null) && (that.username == null)) || (this.username != null && this.username.equals(that.username)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.birthname == null ? 0 : this.birthname.hashCode());
        result = 1000003 * result + (this.dateofbirth == null ? 0 : this.dateofbirth.hashCode());
        result = 1000003 * result + (this.firstname == null ? 0 : this.firstname.hashCode());
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.lastname == null ? 0 : this.lastname.hashCode());
        result = 1000003 * result + (this.pw == null ? 0 : this.pw.hashCode());
        result = 1000003 * result + (this.username == null ? 0 : this.username.hashCode());
        return result;
    }
}

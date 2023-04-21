package org.neodatis.odb.test.fromusers.sjoerdkessels;

import java.util.Date;

/**
 * @author olivier
 * 
 */
public class Person {

    private String name;

    private String email;

    private Date birthDate;

    public Person(String name, String email, Date birthDate) {
        super();
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

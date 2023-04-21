package org.uxd.domain;

import java.util.Date;

/** */
public abstract class Person extends PartyComponent {

    /** Describe the person's gender male or female
	 */
    private String gender;

    /** Identify the persons date of birth
	 */
    private Date birthDate;

    /** Empty constructor
	 */
    public Person() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

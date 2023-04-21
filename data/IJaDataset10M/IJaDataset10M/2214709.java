package at.riemers.zero.base.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author tobias
 */
@Entity
@Table(name = "USR_PERSON")
public class ZeroPerson extends ClientablePersistent implements Person {

    private String firstname = "";

    private String lastname = "";

    private String email = "";

    private String phone = "";

    private String gender = "na";

    private int age = -1;

    /** Creates a new instance of ZeroPerson */
    public ZeroPerson() {
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return firstname + ", " + lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

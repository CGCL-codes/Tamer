package fr.cdm.model.resources;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Desboys
 *
 */
@Entity
public class Employee {

    public enum Sexe {

        Male, Female
    }

    @Id
    private long socialNumber;

    private String firstName;

    private String lastName;

    private Sexe sexe;

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public long getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(long socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

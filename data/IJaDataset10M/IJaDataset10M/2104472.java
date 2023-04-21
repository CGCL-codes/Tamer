package uk.ac.lkl.migen.system.server;

/**
 * A user in the MiGen system. 
 * 
 * A user is defined by their username (i.e. login or nick). 
 * 
 * Usernames are unique. It is the responsibility of the application to ensure this.  
 * 
 * A user also has a name (composed of first name and last name), a gender, and a date of birth. 
 *  
 * @author sergut
 *
 */
public class User {

    private String username;

    private String firstname;

    private String lastname;

    private Gender gender;

    private Date birthDate;

    public User(String username, String firstname, String lastname, Gender gender, Date birthDate) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public User(String username, String firstname, String lastname, Gender gender) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthDate = new Date(1972, 2, 26);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the initials for this user.
     * 
     * For example, John Smith's initials are "JS".
     *  
     * @return the initials for this user.
     */
    public String getInitials() {
        return "" + this.firstname.charAt(0) + this.lastname.charAt(0);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) return false;
        User other = (User) object;
        return this.username.equals(other.username);
    }

    @Override
    public String toString() {
        return username;
    }
}

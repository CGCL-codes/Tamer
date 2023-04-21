package fhj.itm05.seminarswe.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Refactored by Gert Demol
 * @author susi1306
 * @version 1.0
 */
public class UserData {

    private int id;

    private String userName;

    private String password1;

    private String password2;

    private String title;

    private String firstName;

    private String lastName;

    private String sex;

    private String mail;

    /**
	 * Set the default values for the following parameter
	 * 
	 * @param userName own chosen synonym
	 * @param password own set password, more than 5 characters
	 * @param title is optional (mr., mrs., mag., dr., etc.)
	 * @param firstName first name
	 * @param lastName surname, family name
	 * @param sex male or female
	 * @param mail the mail address has to be valid
	 */
    public UserData() {
        this.id = -1;
        this.userName = "";
        this.password1 = "";
        this.password2 = "";
        this.title = "";
        this.firstName = "";
        this.lastName = "";
        this.sex = "";
        this.mail = "";
    }

    public UserData(String userName, String password1, String password2, String title, String firstName, String lastName, String sex, String mail) {
        this.id = -1;
        setUserName(userName);
        setPassword1(password1);
        setPassword2(password2);
        setTitle(title);
        setFirstName(firstName);
        setLastName(lastName);
        setSex(sex);
        setMail(mail);
    }

    public UserData(int id, String userName, String password1, String password2, String title, String firstName, String lastName, String sex, String mail) {
        setId(id);
        setUserName(userName);
        setPassword1(password1);
        setPassword2(password2);
        setTitle(title);
        setFirstName(firstName);
        setLastName(lastName);
        setSex(sex);
        setMail(mail);
    }

    /**
	 * getters and setters of the parameter
	 */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid ID.");
        } else this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid User Name.");
        } else this.userName = userName;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        if (password1 == null || password1.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Password.");
        } else if (password1.length() < 6) {
            throw new IllegalArgumentException("Password is too short, at least 6 characters.");
        } else this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        if (password2 == null || password2.isEmpty()) {
            throw new IllegalArgumentException("Please enter your password a second time.");
        } else if (!password1.equals(password2)) {
            throw new IllegalArgumentException("Please retype your passwords, they are not equal.");
        } else this.password2 = password2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid title.");
        } else this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid firstName.");
        } else this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid lastName.");
        } else this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex == null || sex.isEmpty() || (!sex.equals("female") && !sex.equals("male"))) {
            throw new IllegalArgumentException("Please select your Sex.");
        } else this.sex = sex;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("Please enter your E-Mail Address.");
        } else {
            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(mail);
            if (!m.matches()) {
                throw new IllegalArgumentException("Your E-Mail Address is not valid.");
            } else {
                this.mail = mail;
            }
        }
    }

    @Override
    public String toString() {
        return ("Title : " + title + "\r\n" + "Username : " + userName + "\r\n" + "Password1 : " + password1 + "\r\n" + "Password2 : " + password2 + "\r\n" + "First Name : " + firstName + "\r\n" + "Last Name : " + lastName + "\r\n" + "Sex : " + sex + "\r\n" + "Mail : " + mail);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            UserData other = (UserData) obj;
            if (this.id == other.id) return true; else return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
}

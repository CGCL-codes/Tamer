package audictiv.shared;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class RegistrationInfo implements Serializable {

    private AccountType type;

    private String login;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String gender;

    private Date birthdate;

    private String address;

    private String zipCode;

    private String city;

    private String country;

    private String phoneNumber;

    private String name;

    private String number;

    public RegistrationInfo() {
    }

    public RegistrationInfo(String login, String password, String email) {
        this.type = AccountType.MEMBER;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public RegistrationInfo(String login, String password, String email, String firstName, String lastName, String gender, Date birthdate, String address, String zipCode, String city, String country, String phoneNumber) {
        this.type = AccountType.ARTIST;
        this.login = login;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public RegistrationInfo(String login, String password, String email, String name, String number) {
        this.type = AccountType.BAND;
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
        this.number = number;
    }

    public AccountType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}

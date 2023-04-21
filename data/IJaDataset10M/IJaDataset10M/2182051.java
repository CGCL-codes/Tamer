package examples.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {

    String firstName;

    String lastName;

    int age;

    Date birthdate;

    String job;

    public Person() {
    }

    public Person(String firstName, String lastName, int age, String birthdateStr, String job) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        try {
            this.birthdate = new SimpleDateFormat("yyyy/MM/dd").parse(birthdateStr);
        } catch (ParseException e) {
            this.birthdate = new Date();
        }
        this.job = job;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}

package oopex.toplink11.nat.usecases.model;

public class Person {

    public Person() {
    }

    private long id;

    private String firstName;

    private String lastName;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public String toString() {
        return "Person(" + id + "): " + lastName + ", " + firstName;
    }
}

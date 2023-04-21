package net.dharwin.common.tools.cli.sample.client.datastore;

/**
 * A simple representation of a user.
 * Very creative stuff here.
 * @author Sean
 *
 */
public class User {

    private String _firstName;

    private String _lastName;

    private int _age;

    public User(String firstName, String lastName, int age) {
        _firstName = firstName;
        _lastName = lastName;
        _age = age;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public int getAge() {
        return _age;
    }
}

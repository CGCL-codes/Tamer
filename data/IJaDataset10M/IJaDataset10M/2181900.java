package de.gstpl.data;

/**
 * A Person only has subjects.
 *
 * @author Peter Karich
 */
public class Person extends Timetable {

    public Person() {
        this(null);
    }

    Person(String name) {
        super();
        setName(name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Person && super.equals(obj)) return true;
        return false;
    }

    protected static final String LOGIN_PROPERTY = "login";

    public void setLogin(String login) {
        writeProperty(LOGIN_PROPERTY, login);
    }

    public String getLogin() {
        return (String) readProperty(LOGIN_PROPERTY);
    }
}

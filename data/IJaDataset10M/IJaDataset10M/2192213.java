package de.tum.in.eist.poll.shared;

/**
 * Representation of a student.
 */
public class Student extends User {

    /**
	 * @see User#User()
	 */
    public Student() {
        super();
    }

    /**
	 * @see User#User(String, String)
	 */
    public Student(String name, String password) {
        super(name, password);
    }
}

package org.exos.tasks.exchange;

public class PersonNotFoundException extends Exception {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(Exception e) {
        super(e.getMessage());
    }

    public PersonNotFoundException(String s) {
        super(s);
    }
}

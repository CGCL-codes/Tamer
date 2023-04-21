package com.orientechnologies.jdo.exceptions;

import javax.jdo.JDOUserException;

public class oObjectNotReleasedException extends JDOUserException {

    public oObjectNotReleasedException() {
        super("Object not released");
    }
}

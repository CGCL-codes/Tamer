package de.robowars.ui.config;

import javax.xml.bind.IllegalEnumerationValueException;

public final class RobotEnumeration {

    private String _RobotEnumeration;

    public static final RobotEnumeration FIRST = new RobotEnumeration("first");

    public static final RobotEnumeration SECOND = new RobotEnumeration("second");

    public static final RobotEnumeration THIRD = new RobotEnumeration("third");

    public static final RobotEnumeration FOURTH = new RobotEnumeration("fourth");

    public static final RobotEnumeration FIFTH = new RobotEnumeration("fifth");

    public static final RobotEnumeration SIXTH = new RobotEnumeration("sixth");

    public static final RobotEnumeration SEVENTH = new RobotEnumeration("seventh");

    public static final RobotEnumeration EIGHTH = new RobotEnumeration("eighth");

    private RobotEnumeration(String s) {
        this._RobotEnumeration = s;
    }

    public static RobotEnumeration parse(String s) {
        if (s.equals("eighth")) {
            return EIGHTH;
        }
        if (s.equals("fifth")) {
            return FIFTH;
        }
        if (s.equals("first")) {
            return FIRST;
        }
        if (s.equals("fourth")) {
            return FOURTH;
        }
        if (s.equals("second")) {
            return SECOND;
        }
        if (s.equals("seventh")) {
            return SEVENTH;
        }
        if (s.equals("sixth")) {
            return SIXTH;
        }
        if (s.equals("third")) {
            return THIRD;
        }
        throw new IllegalEnumerationValueException(s);
    }

    public String toString() {
        return _RobotEnumeration;
    }
}

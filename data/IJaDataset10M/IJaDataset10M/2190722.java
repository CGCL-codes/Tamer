package org.ucdetector.example;

import java.io.Serializable;

/**
 * No markers in overridden methods of Object, 
 * methods of Object should be skipped during search!
 */
public class ObjectClass implements Serializable {

    static final long serialVersionUID = -629941578936022146L;

    public void unused() {
    }

    public int hashCode() {
        return 0;
    }

    public Object clone() {
        return new Object();
    }

    public String toString() {
        return "hello";
    }

    public void finalize() {
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public int hashCode(String s) {
        return 0;
    }

    public Object clone(String s) {
        return new Object();
    }

    public String toString(String s) {
        return "hello";
    }

    public void finalize(String s) {
    }

    public boolean equals(String s, Object o) {
        return super.equals(o);
    }
}

package net.sf.jelly.apt.samplesource;

/**
 * @author Ryan Heaton
 */
public class ExtendedFamily extends Family {

    public int getId() {
        return super.getId();
    }

    public Person getPaternalGrandfather() {
        return null;
    }

    public Person getPaternalGrandmother() {
        return null;
    }

    public Person getMaternalGrandfather() {
        return null;
    }

    public Person getMaternalGrandmother() {
        return null;
    }
}

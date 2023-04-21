package edu.iastate.cs.designlab.utilities;

/**
 * Represents an abstract Command line option.
 * Options have both a flag and a value. For example "-d /home/sean" might represent
 * that the directory used should be "/home/sean".
 * 
 * @author sean mooney
 *
 * @param <E> the type of the value
 */
public abstract class Option<E> {

    private final Flag flag;

    private E value;

    /**
     * Create an option with the desired flag.
     * @param flag
     */
    public Option(Flag flag) {
        this.flag = flag;
    }

    /**
     * 
     * @param value
     */
    public void setValue(E value) {
        this.value = value;
    }

    public E getValue() {
        return this.value;
    }

    /**
     * Flag used to represent the argument on the command line.
     * @return
     */
    public Flag getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return getFlag().toString() + ": " + getValue().toString();
    }

    /**
     * Delegate method make match a flag a little easier.
     * @param s
     * @return
     */
    public boolean matches(String s) {
        return getFlag().matches(s);
    }

    private String infoMsg;

    /**
     * Set the information displayed for the option
     * @param info
     * @return
     */
    public void setInfoMsg(String info) {
        this.infoMsg = info;
    }

    /**
     * Return a basic information msg about the option
     * @return
     */
    public String getInformation() {
        return getFlag() + " " + infoMsg;
    }
}

package proguard.classfile;

/**
 * This interface is a base interface for visitor accepters. It allows
 * visitors to set and get any temporary information they desire on the
 * objects they are visiting. Note that every visitor accepter has only one
 * such property, so visitors will have to take care not to overwrite each
 * other's information, if it is still required.
 *
 * @author Eric Lafortune
 */
public interface VisitorAccepter {

    /**
     * Gets the visitor information of the visitor accepter.
     */
    public Object getVisitorInfo();

    /**
     * Sets the visitor information of the visitor accepter.
     */
    public void setVisitorInfo(Object visitorInfo);
}

package jp.go.aist.six.util.persist;

/**
 * A dependent object.
 * The lifetime of a dependent object depends on the master object.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Dependent.java 300 2011-03-02 05:45:46Z nakamura5akihito $
 */
public interface Dependent<T> {

    /**
     * Sets the master object of this dependent object.
     *
     * @param   master
     *  the master object.
     */
    public void setMasterObject(T master);

    /**
     * Returns the master object of this dependent object.
     *
     * @return
     *  the master object.
     */
    public T getMasterObject();
}
